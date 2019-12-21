package chess;

import chess.base.board.ChessState;
import chess.ui.UI;
import chess.ui.fxUI.application.ChessApplication;
import chess.ui.impl.consoleUI.ConsoleUI;


class Main {
    private final static boolean CONSOLE = false;

    public static void main(String[] args) {
        if (CONSOLE) {
            var board = InitialPosition.get();
            var state = new ChessState(board);
            UI ui = new ConsoleUI(state);
            ui.start();
        } else {
            ChessApplication.startFX();
        }
    }
}
