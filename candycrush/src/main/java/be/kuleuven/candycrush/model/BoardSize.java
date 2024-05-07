package be.kuleuven.candycrush.model;

import java.util.ArrayList;
import java.util.Collection;

public record BoardSize(int breedte, int hoogte){
    public BoardSize{
        if (breedte <= 0) throw new IllegalArgumentException("breedte must be non-negative");
        if (hoogte <= 0) throw new IllegalArgumentException("hoogte must be non-negative");
    }

    public Collection<Position> positions(){
        Collection<Position> posities = new ArrayList<>();
        for (int i = 0; i < breedte * hoogte; i++) {
            posities.add(Position.fromIndex(i, this));
        }
        return posities;
    }

    public int boardSize(){
        return breedte * hoogte;
    }
}