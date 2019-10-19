package chess.misc;

public class Point {
    private final int x;
    private final int y;

    @SuppressWarnings("FieldCanBeLocal")
    private final int lx = 0;
    @SuppressWarnings("FieldCanBeLocal")
    private final int ly = 0;
    @SuppressWarnings("FieldCanBeLocal")
    private final int ux = 7;
    @SuppressWarnings("FieldCanBeLocal")
    private final int uy = 7;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(long i, long j) {
        this((int)i, (int)j);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    public static Point parse(String sub) {
        return new Point(
                Integer.parseInt(
                        String.valueOf(sub.charAt(1))
                ) - 1,
                Integer.parseInt(
                        String.valueOf(sub.charAt(0) - 'a')
                )
        );
    }

    public static Point sum(Point rhs, Point lhs) {
        return new Point(rhs.getX() + lhs.getX(), rhs.getY() + lhs.getY());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return (char)((int)'a' + y) + Integer.toString(x + 1);
    }

    boolean validate() {
        return
            lx <= x &&
            ly <= y &&
            ux >= x &&
            uy >= y;
    }
}
