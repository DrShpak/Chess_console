package chess.board;

import chess.misc.Team;
import chess.units.Unit;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Streams.mapWithIndex;

public class BoardBuilder {
    private int height = -1, width = -1;
    private Team currentTeam;
    private Set<String> usedTags = new HashSet<>();
    private List<Unit> units = new ArrayList<>();
    private List<Integer> divisionIndices = new ArrayList<>();
    private List<List<Class<? extends Unit>>> groups = new ArrayList<>();

    {
        groups.add(null);
    }

    public BoardBuilder height(int height) {
        this.height = height;
        return this;
    }

    public BoardBuilder width(int width) {
        this.width = width;
        return this;
    }

    public BoardBuilder startGroup() {
        groups.set(groups.size() - 1, new ArrayList<>());
        return this;
    }

    public BoardBuilder endGroup() {
        groups.add(null);
        return this;
    }

    public BoardBuilder withTeam(String teamTag) {
        if (usedTags.contains(teamTag)) {
            throw new IllegalStateException
                    ("Tag '" + teamTag + "' has already used");
        }
        currentTeam = new Team(teamTag);
        usedTags.add(teamTag);
        return this;
    }

    public BoardBuilder withUnitRange(Class<? extends Unit> clazz, int length, Object... param) {
        for (int i = 0; i < length; i++) {
            withUnit(clazz, param);
        }
        return this;
    }

    public BoardBuilder withUnit(Class<? extends Unit> clazz, Object... param) {
        var currentGroup = Iterables.getLast(groups);
        if (currentGroup != null) {
            currentGroup.add(clazz);
            return this;
        }

        var args = ArrayUtils.insert(0, param, currentTeam);
        Unit unit;
        try {
            unit = clazz.getConstructor
            (
                Arrays.stream(args).
                map(Object::getClass).
                toArray(Class[]::new)
            ).
                newInstance(args);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException
                    (clazz + " has no public constructor with " + args.length + " args");
        }
        units.add(unit);

        return this;
    }

    public BoardBuilder withGroup(int index) {
        groups.get(index).forEach(this::withUnit);
        return this;
    }

    public BoardBuilder withGap() {
        var nextIndex = units.size();
        var previousIndex = Iterables.getLast(divisionIndices, -1);
        //noinspection ConstantConditions
        if (previousIndex >= nextIndex) {
            throw new IllegalStateException
                    ("Repeated invocation 'withGap()', index = " + previousIndex);
        }
        divisionIndices.add(nextIndex);
        return this;
    }

    public ChessBoardImpl build() {
        var size = height * width;
        if (units.size() > size) {
            throw new IllegalArgumentException
                    ("unable to instantiate board " + height + "x" + width + " unit`s count(" + units.size() + ") > size(" + size + ")");
        }
        var iterator = new UnitIterator(size);
        return new ChessBoardImpl(fillMatrix(iterator));
    }

    private Unit[][] fillMatrix(UnitIterator iterator) {
        var matrix = new Unit[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = iterator.next();
            }
        }
        if (iterator.hasUnits()) {
            throw new IllegalStateException("Not all units have used");
        }
        return matrix;
    }

    class UnitIterator
    implements Iterator<Unit> {
        private int index;
        private int gapSize;
        private int size;
        private Queue<Unit> units = new ArrayDeque<>();
        private Deque<Integer> divisionIndices = new ArrayDeque<>();

        private UnitIterator(int size) {
            var divsCount = BoardBuilder.this.divisionIndices.size();
            var unitsCount = BoardBuilder.this.units.size();
            gapSize = divsCount > 0
                    ? (size - unitsCount) / divsCount
                    : 0;
            this.size = size;
            units.addAll(BoardBuilder.this.units);
            //noinspection UnstableApiUsage
            divisionIndices.addAll(
                    mapWithIndex(BoardBuilder.this.divisionIndices.stream(),
                        (x, i) -> (int)(x + i * gapSize)
                    ).collect(Collectors.toList())
            );
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Unit next() {
            if (!hasNext())
                throw new NoSuchElementException();
            var currDivisionIndex = divisionIndices.peek();
            if (currDivisionIndex == null || index++ < currDivisionIndex ) {
                return units.poll();
            }
            if (index - currDivisionIndex > gapSize) {
                divisionIndices.pop();
                return units.poll();
            }
            return null;
        }

        boolean hasUnits() {
            return !units.isEmpty();
        }
    }
}
