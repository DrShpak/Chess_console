package chess.board;

import chess.misc.Point;
import chess.units.Unit;

import java.util.ArrayList;
import java.util.Collection;

public class MoveHandlers {
    private Collection<IMoveHandler<? super IBoard>> handlers = new ArrayList<>();

    void handle(IBoard feedback, Object handler, Point oldPoint, Point newPoint) {
        handlers.
                stream().
                filter(x -> x == handler).
                forEach(x -> x.handleMove(feedback, oldPoint, newPoint));
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    boolean remove(Object entity) {
        return handlers.remove(entity);
    }

    public void register(IMoveHandler<? super IBoard> entity) {
        handlers.add(entity);
    }
}
