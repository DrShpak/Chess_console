package chess.ui.impl.consoleUI;

import chess.GlobalSettings;
import chess.unit.*;

public enum  UnitSymbols {
    Dummy("?") {
        @Override
        public boolean match(Unit unit) {
            return GlobalSettings.DEBUG_MODE && unit instanceof Dummy;
        }
    },
    WhitePawn("\u2659") {
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Pawn &&
                    unit.getTeam().getTeamTag().equals("White");
        }
    },
    WhiteKnight("\u2658"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Knight &&
                    unit.getTeam().getTeamTag().equals("White");
        }
    },
    WhiteBishop("\u2657"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Bishop &&
                    unit.getTeam().getTeamTag().equals("White");
        }
    },
    WhiteRook("\u2656"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Rook &&
                    unit.getTeam().getTeamTag().equals("White");
        }
    },
    WhiteQueen("\u2655"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Queen &&
                    unit.getTeam().getTeamTag().equals("White");
        }
    },
    WhiteKing("\u2654"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof King &&
                    unit.getTeam().getTeamTag().equals("White");
        }
    },
    BlackPawn("\u265F"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Pawn &&
                    unit.getTeam().getTeamTag().equals("Black");
        }
    },
    BlackKnight("\u265E"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Knight &&
                    unit.getTeam().getTeamTag().equals("Black");
        }
    },
    BlackBishop("\u265D"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Bishop &&
                    unit.getTeam().getTeamTag().equals("Black");
        }
    },
    BlackRook("\u265C"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Rook &&
                    unit.getTeam().getTeamTag().equals("Black");
        }
    },
    BlackQueen("\u265B"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Queen &&
                    unit.getTeam().getTeamTag().equals("Black");
        }
    },
    BlackKing("\u265A"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof King &&
                    unit.getTeam().getTeamTag().equals("Black");
        }
    },
    EvenCell("\u25A1"){
        @Override
        public boolean match(Unit unit) {
            return false;
        }
    },
    OddCell("\u25A0"){
        @Override
        public boolean match(Unit unit) {
            return false;
        }
    };

    private final String symbol;

    UnitSymbols(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public abstract boolean match(Unit unit);
}
