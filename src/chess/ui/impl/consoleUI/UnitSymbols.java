package chess.ui.impl.consoleUI;

import chess.units.*;

public enum  UnitSymbols {
    WhitePawn("\u2659") {
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Pawn &&
                    unit.team.getTeamTag().equals("White");
        }
    },
    WhiteKnight("\u2658"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Knight &&
                    unit.team.getTeamTag().equals("White");
        }
    },
    WhiteBishop("\u2657"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Bishop &&
                    unit.team.getTeamTag().equals("White");
        }
    },
    WhiteRook("\u2656"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Rook &&
                    unit.team.getTeamTag().equals("White");
        }
    },
    WhiteQueen("\u2655"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Queen &&
                    unit.team.getTeamTag().equals("White");
        }
    },
    WhiteKing("\u2654"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof King &&
                    unit.team.getTeamTag().equals("White");
        }
    },
    BlackPawn("\u265F"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Pawn &&
                    unit.team.getTeamTag().equals("Black");
        }
    },
    BlackKnight("\u265E"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Knight &&
                    unit.team.getTeamTag().equals("Black");
        }
    },
    BlackBishop("\u265D"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Bishop &&
                    unit.team.getTeamTag().equals("Black");
        }
    },
    BlackRook("\u265C"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Rook &&
                    unit.team.getTeamTag().equals("Black");
        }
    },
    BlackQueen("\u265B"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof Queen &&
                    unit.team.getTeamTag().equals("Black");
        }
    },
    BlackKing("\u265A"){
        @Override
        public boolean match(Unit unit) {
            return unit instanceof King &&
                    unit.team.getTeamTag().equals("Black");
        }
    },
    EvenCell("\u25EF"){
        @Override
        public boolean match(Unit unit) {
            return false;
        }
    },
    OddCell("\u26AB"){
        @Override
        public boolean match(Unit unit) {
            return false;
        }
    };

    private String symbol;

    UnitSymbols(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public abstract boolean match(Unit unit);
}
