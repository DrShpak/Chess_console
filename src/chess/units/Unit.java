package chess.units;

import chess.board.Cell;

public abstract class Unit {

    private Cell currCell;

    public abstract boolean move();

}