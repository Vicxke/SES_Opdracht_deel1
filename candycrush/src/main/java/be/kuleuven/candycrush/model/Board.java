package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.model.CandyCrushModel.*;

import java.util.ArrayList;
import java.util.function.Function;


//geeft een generisch type mee aan de klasse
public class Board<T> {
    private final BoardSize size;
    private final ArrayList<T> cells;

    public Board(BoardSize size) {
        this.size = size;
        cells = new ArrayList<>();
        //empty the lijst
        for(int i = 0; i < size.breedte() * size.hoogte(); i++) {
            cells.add(null);
        }
    }

    public T getCellAt(Position position) {
        return cells.get(position.toIndex());
    }

    public void replaceCellAt(Position position, T newCell) {
        cells.set(position.toIndex(), newCell);
    }

    public void fill(Function<Position, T> cellCreator) {
        for(int row = 0; row < size.hoogte(); row++) {
            for(int col = 0; col < size.breedte(); col++) {
                Position position = new Position(row, col, size);
                T newCell = cellCreator.apply(position);
                replaceCellAt(position, newCell);
            }
        }
    }

    public void copyTo(Board<T> otherBoard) {
        if(otherBoard.size.breedte() != size.breedte() || otherBoard.size.hoogte() != size.hoogte()) {
            throw new IllegalArgumentException("Het bord is niet van dezelfde grootte");
        }else{
            otherBoard.cells.clear();
            otherBoard.cells.addAll(cells);
        }
    }

    public BoardSize getSize() {
        return size;
    }

    public ArrayList<T> getCells() {
        return cells;
    }
}