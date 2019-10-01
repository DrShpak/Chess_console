package chess.ui;

import chess.board.ChessBoard;

public abstract class UI {
    protected ChessBoard board;

    public UI(ChessBoard board) {
        this.board = board;
    }

    public abstract void start();
}
