package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.model.CandyCrushModel.*;

public class Swap {
    private Position pos1;
    private Position pos2;

    public Swap(Position pos1, Position pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public void swap(Board<Candy> board) {
        Candy temp = board.getCellAt(pos1);
        board.replaceCellAt(pos1, board.getCellAt(pos2));
        board.replaceCellAt(pos2, temp);
    }

    public Position getPos1() {
        return pos1;
    }

    public Position getPos2() {
        return pos2;
    }
}
