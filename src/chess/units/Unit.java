package chess.units;

import chess.board.Cell;

public abstract class Unit {
    private Cell currentCell;

    public abstract boolean move();
}