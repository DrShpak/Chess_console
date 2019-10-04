package chess.misc;

import org.javatuples.Pair;

import java.util.stream.Stream;

public class Direction {
    private int dx, dy;
    private int maxLength = Integer.MAX_VALUE;
    private MovePolicy movePolicy = MovePolicy.BOTH;

    public Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Direction(int dx, int dy, int maxLength) {
        this(dx, dy);
        this.maxLength = maxLength;
    }

    public Direction(int dx, int dy, int maxLength, MovePolicy movePolicy) {
        this(dx, dy);
        this.maxLength = maxLength;
        this.movePolicy = movePolicy;
    }

    public Stream<Point> move(Point from) {
        return Stream.iterate(
                new Pair<>(Point.sum(from, new Point(dx, dy)), 0),
                x -> x.getValue0().validate() && x.getValue1() < maxLength,
                x -> new Pair<>(
                        Point.sum(x.getValue0(), new Point(dx, dy)),
                        x.getValue1() + 1
                )
        ).map(Pair::getValue0);
    }

    public MovePolicy getMovePolicy() {
        return movePolicy;
    }
}
