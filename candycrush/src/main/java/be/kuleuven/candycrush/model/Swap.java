package be.kuleuven.candycrush.model;

public class Swap {
    private Position pos1;
    private Position pos2;

    public Swap(Position pos1, Position pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public Position getPos1() {
        return pos1;
    }

    public Position getPos2() {
        return pos2;
    }
}