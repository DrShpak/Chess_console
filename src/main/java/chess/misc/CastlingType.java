package chess.misc;

public enum CastlingType {
    SHORT {
        @Override
        public Direction getDirection() {
            return new Direction(0, 1, 2);
        }
    }, // 0-0
    LONG {
        @Override
        public Direction getDirection() {
            return new Direction(0, -1, 2);
        }
    }; //0-0-0

    public static CastlingType parse(String line) {
        if (line.split("-").length == 2) {
            return SHORT;
        }
        return LONG;
    }

    public abstract Direction getDirection();
}
