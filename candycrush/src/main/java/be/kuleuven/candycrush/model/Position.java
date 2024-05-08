package be.kuleuven.candycrush.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

public record Position(int row, int col, BoardSize bord){
    public Position {
        if(row < 0 || row >= bord.hoogte()){
            throw new IllegalArgumentException("row is out of bounds");
        }
        if(col < 0 || col >= bord.breedte()){
            throw new IllegalArgumentException("hoogte is out of bounds");
        }
    }
    public int toIndex(){
        return this.row * bord.breedte() + this.col;
    }

    public static Position fromIndex(int index, BoardSize size){
        if(index > size.breedte() * size.hoogte() || index < 0){
            throw new IllegalArgumentException("index bestaat niet");
        }else{
            int row = index / size.breedte();
            int col = index % size.hoogte();
            return new Position(row,col, size);
        }
    }

    public Iterable<Position> neighborPositions(){
        //alle bestaande buren ongeacht of ze dezelfde waarde hebben.
        ArrayList<Position> buren = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0){
                    continue;
                }
                int newRow = this.row + i;
                int newCol = this.col + j;
                if(newRow >= 0 && newRow < bord.hoogte() && newCol >= 0 && newCol < bord.breedte()){
                    buren.add(new Position(newRow, newCol, bord));
                }
            }
        }
        return buren;
    }

    public Stream<Position> walkLeft() {
        return this.bord.positions().stream().filter(pos -> pos.row == this.row && pos.col <= this.col).sorted(Comparator.comparingInt(Position::col).reversed());
    }

    public Stream<Position> walkRight() {
        return this.bord.positions().stream().filter(pos -> pos.row == this.row && pos.col >= this.col).sorted(Comparator.comparingInt(Position::col));
    }

    public Stream<Position> walkUp() {
        return this.bord.positions().stream().filter(pos -> pos.col == this.col && pos.row <= this.row).sorted(Comparator.comparingInt(Position::row).reversed());
    }

    public Stream<Position> walkDown() {
        return this.bord.positions().stream().filter(pos -> pos.col == this.col && pos.row >= this.row).sorted(Comparator.comparingInt(Position::row));
    }

    public boolean isRightNextTo(Position pos){
        if(walkDown().limit(2).toList().contains(pos) || walkUp().limit(2).toList().contains(pos)){
            return true;
        }
        if(walkRight().limit(2).toList().contains(pos) || walkLeft().limit(2).toList().contains(pos)){
            return true;
        }
        return false;
    }

    public boolean isLastColumn(){
        //die aangeeft of de positie de laatste is in een rij.
        return this.col == bord.breedte() - 1;
    }
}