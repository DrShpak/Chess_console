package chess.ui;

import chess.board.ChessBoardImpl;

public abstract class UI {
    protected ChessBoardImpl board;

    public UI(ChessBoardImpl board) {
        this.board = board;
    }

    public abstract void start();
}
