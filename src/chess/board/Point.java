package chess.board;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
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

    public Point sum(Point rhs, Point lhs) {
        return new Point(rhs.getX() + lhs.getX(), rhs.getY() + lhs.getY());
    }

    public static Point diff(Point rhs, Point lhs) {
        return new Point(rhs.getX() - lhs.getX(), rhs.getY() - lhs.getY());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
