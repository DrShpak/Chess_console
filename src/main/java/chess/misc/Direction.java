package chess.misc;

import org.javatuples.Pair;
import xml.XML;

import java.util.stream.Stream;

@XML
public class Direction {
    @XML
    private final int dx;
    @XML
    private final int dy;
    @XML
    private int maxLength = Integer.MAX_VALUE;
    @XML
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

    public Stream<Point> getPointsAlong(Point from) {
        if (this.isZero()) {
            return Stream.empty();
        }
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

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public static Direction getAvgDistance(Direction direction) {
        return new Direction(direction.dx / 2, direction.dy / 2, direction.maxLength, direction.movePolicy);
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isZero() {
        return dx == 0 && dy == 0;
    }
}
