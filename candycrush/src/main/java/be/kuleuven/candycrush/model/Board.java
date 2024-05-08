package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.model.CandyCrushModel.*;

import java.util.*;
import java.util.function.Function;


public class Board<T> {
    private final BoardSize size;
    private final Map<Position, T> cells;
    private final Map<T, Set<Position>> reverseCells;

    private int maxScore;
    private List<Swap> bestSequence;

    public Board(BoardSize size) {
        this.size = size;
        this.cells = new HashMap<>();
        this.reverseCells = new HashMap<>();
        maxScore = 0;
        bestSequence = new ArrayList<>();
    }

    public T getCellAt(Position position) {
        return cells.get(position);
    }

    public void replaceCellAt(Position position, T newCell) {
        T oldCell = cells.put(position, newCell);

        if (oldCell != null) {
            Set<Position> positions = reverseCells.get(oldCell);
            if(positions != null) {
                positions.remove(position);
                if (positions.isEmpty()) {
                    reverseCells.remove(oldCell);
                }
            }
        }
        reverseCells.computeIfAbsent(newCell, k -> new HashSet<>()).add(position);
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
            otherBoard.cells.putAll(cells);
            otherBoard.reverseCells.clear();
            otherBoard.reverseCells.putAll(reverseCells);
        }
    }

    public List<Position> getPositionsOfElement(T candy) {
        Set<Position> positionsSet = reverseCells.getOrDefault(candy, Collections.emptySet());
        return Collections.unmodifiableList(new ArrayList<>(positionsSet));
    }

    public BoardSize getSize() {
        return size;
    }

    public Map<Position, T> getCells() {
        return new HashMap<>(cells);
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
    public int getMaxScore() {
        return maxScore;
    }

    public List<Swap> getBestSequence() {
        return bestSequence;
    }

    public void setBestSequence(List<Swap> bestSequence) {
        this.bestSequence = bestSequence;
    }
}