package chess.ui;

import chess.base.board.ChessState;

public abstract class UI {
    protected ChessState chessState;

    protected UI(ChessState chessState) {
        this.chessState = chessState;
    }

    public abstract void start();
}
