package chess.ui;

import chess.base.board.ChessBoardImpl;

public abstract class UI {
    protected final ChessBoardImpl board;

    protected UI(ChessBoardImpl board) {
        this.board = board;
    }

    public abstract void start();
}
