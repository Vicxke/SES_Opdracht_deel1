package be.kuleuven.candycrush.model;

import be.kuleuven.CheckNeighboursInGrid;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static be.kuleuven.CheckNeighboursInGrid.*;

public class CandyCrushModel {
    private int circleRadius;
    private int score;

    private String playerName;

    private ArrayList<Integer> grid;

    private BoardSize board;

    public CandyCrushModel(int veldBreedte, int veldHooghte) {
        playerName = "Default Player";
        this.circleRadius = 30;
        this.score = 0;
        board = new BoardSize(veldBreedte, veldHooghte);
        this.grid = new ArrayList<>();
        GenerateGrid();
    }

    public record BoardSize(int breedte, int hoogte){
        public BoardSize{
            if (breedte <= 0) throw new IllegalArgumentException("breedte must be non-negative");
            if (hoogte <= 0) throw new IllegalArgumentException("hoogte must be non-negative");
        }

        public Iterable<Position> positions(){
            ArrayList<Position> posities = new ArrayList<>();
            for (int i = 0; i < breedte * hoogte; i++) {
                posities.add(Position.fromIndex(i, this));
            }
            return posities;
        }
    }

    public record Position(int row,int col, BoardSize bord){
        public Position {
            if(row < 0 || row >= bord.hoogte()){
                throw new IllegalArgumentException("row is out of bounds");
            }
            if(col < 0 || col >= bord.breedte()){
                throw new IllegalArgumentException("hoogte is out of bounds");
            }
        }
        int toIndex(){
            return this.row * bord.breedte() + this.col;
        }

        static Position fromIndex(int index, BoardSize size){
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

        public boolean isLastColumn(){
            //die aangeeft of de positie de laatste is in een rij.
            return this.col == bord.breedte() - 1;
        }
    }

    public sealed interface Candy permits NormalCandy, Stheefje, bolleke, petoterke, haaariebooo{}

    //NormalCandy, met een attribuut color (een int met mogelijke waarden 0, 1, 2, of 3)
    public record NormalCandy(int color) implements Candy{
        public NormalCandy{
            if(color <= 0 || color >= 3){
                throw new IllegalArgumentException("color moet tussen 0 en 3 liggen");
            }
        }
    }
    public record Stheefje() implements Candy{}
    public record bolleke() implements Candy{}
    public record petoterke() implements Candy{}
    public record haaariebooo() implements Candy{}


    public void GenerateGrid(){
        Random rndGen = new Random();
        for (int i = 0; i < board.breedte()*board.hoogte(); i++) {
            int randomNum = rndGen.nextInt(1,6);
            grid.add(randomNum);
        }
    }

    public void onCircleClick(double x, double y){
        //index to check wordt hieronder uigerekend
        double xCoordInArray = (x - (double) (circleRadius /2)) / circleRadius;
        double yCoordInArray = (y - (double) (circleRadius /2)) / circleRadius;
        int indexValue = (int)(xCoordInArray + (yCoordInArray*board.hoogte()));
        System.out.println("index value to Check: " + indexValue);
        CheckAlleBuren(indexValue);
    }

    public void CheckAlleBuren(int index){
        //eerst alle buren eruit halen en dan elke buur vergelijken.

        CheckNeighboursInGrid gridChecker = new CheckNeighboursInGrid();
        //int valueToCheck = grid.get(index);

        //er klopt nog iets niet aan deze functie denk ik aangezien de teruggegeven index voor geen hol klopt
        Iterable<Integer> buren = getSameNeighboursIds(grid, board.breedte(), board.hoogte(), index);//geeft een lijst met buren terug die hetzelfde zijn
        Iterator iterator = buren.iterator();
        //System.out.println(buren);

        int count = 1;
        for (Object i : buren){
            count++;
        }

        if(count >= 3) {
            regenerateValue(index);
            while (iterator.hasNext()) {
                int indexBuur = (int) iterator.next();
                regenerateValue(indexBuur);
            }
            addScore(count);
        }
    }

    public void regenerateValue(int index){
        Random rndGen = new Random();
        grid.set(index, rndGen.nextInt(1,6));
    }
    public void addScore(int waarde){
        score+= waarde;
    }

    public void resetGame(){
        score = 0;
        grid.clear();
        GenerateGrid();
    }

    public BoardSize getBoard() {
        return board;
    }

    public ArrayList<Integer> getGrid() {
        return grid;
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public int getScore() {
        return score;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
