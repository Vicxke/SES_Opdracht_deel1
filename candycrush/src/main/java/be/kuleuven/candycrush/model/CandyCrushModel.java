package be.kuleuven.candycrush.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.util.*;


public class CandyCrushModel {
    private int circleRadius;
    private int score;

    private String playerName;

    //private ArrayList<Candy> grid;
    private Board<Candy> grid;

    private BoardSize size;

    public CandyCrushModel(int veldBreedte, int veldHooghte) {
        playerName = "Default Player";
        this.circleRadius = 30;
        this.score = 0;
        size = new BoardSize(veldBreedte, veldHooghte);
        //this.grid = new ArrayList<Candy>();
        this.grid = new Board<>(size);
        generateGrid();
    }

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

    public record Position(int row,int col, BoardSize bord){
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
            return this.bord.positions().stream().filter(pos -> pos.row == this.row && pos.col <= this.col);
        }

        public Stream<Position> walkRight() {
            return this.bord.positions().stream().filter(pos -> pos.row == this.row && pos.col >= this.col);
        }

        public Stream<Position> walkUp() {
            return this.bord.positions().stream().filter(pos -> pos.col == this.col && pos.row <= this.row);
        }

        public Stream<Position> walkDown() {
            return this.bord.positions().stream().filter(pos -> pos.col == this.col && pos.row >= this.row);
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
            if(color < 0 || color > 3){
                throw new IllegalArgumentException("color moet tussen 0 en 3 liggen");
            }
        }
    }
    public record Stheefje() implements Candy{}
    public record bolleke() implements Candy{}
    public record petoterke() implements Candy{}
    public record haaariebooo() implements Candy{}


    public Candy generateRandomCandy(){
        Random rndGen = new Random();
        int random = rndGen.nextInt(1,8);
        switch (random){
            case 1: return new NormalCandy(0);
            case 2: return new NormalCandy(1);
            case 3: return new NormalCandy(2);
            case 4: return new NormalCandy(3);
            case 5: return new Stheefje();
            case 6: return new bolleke();
            case 7: return new petoterke();
            case 8: return new haaariebooo();
            default: return null;
        }
    }

    public void generateGrid(){
        for (Position pos : size.positions()) {
            grid.replaceCellAt(pos, generateRandomCandy());
        }
    }

    public void onCircleClick(double x, double y){
        //index to check wordt hieronder uigerekend
        double xCoordInArray = x / circleRadius;
        double yCoordInArray = y / circleRadius;
        int row = (int) Math.floor(yCoordInArray);
        int col = (int) Math.floor(xCoordInArray);

        System.out.println("row: " + row + " col: " + col);
        //position van index ophalen
        CheckAlleBuren(new Position(row, col, size));
    }

    public void CheckAlleBuren(Position coords){

        Iterable<Position> buren  = getSameNeighbourPositions(coords);
        Iterator iterator = buren.iterator();
        //System.out.println(buren);

        //count op 1 zodat waarde zelf wordt meegeteld
        int count = 1;
        for (Object i : buren){
            count++;
        }

        //System.out.println(count);

        if(count >= 3) {
            addScore(count);
            grid.replaceCellAt(coords, generateRandomCandy());
            while (iterator.hasNext()) {
                Position posBuur = (Position) iterator.next();
                grid.replaceCellAt(posBuur, generateRandomCandy());
            }
        }
    }

    Iterable<Position> getSameNeighbourPositions(Position position){
        //gebruik de neighborPositions functie om de posities van de buren te vinden
        //en controleer of de waarde van de buur overeenkomt met de waarde van de positie
        //return een iterable van posities die dezelfde waarde hebben als de positie
        ArrayList<Position> buren = new ArrayList<>();
        for(Position p : position.neighborPositions()){
            //check if the cany is the same
            if(grid.getCellAt(p).equals(grid.getCellAt(position))){
                buren.add(p);
            }
        }
        return buren;
    }

    public boolean firstTwoHaveCandy(Candy candy, Stream<Position> positions) {
        //neemt de eerste 2 elementen van de lijst
        List<Position> positionList = positions.limit(2).toList();
        if(positionList.size() < 2) return false;

        //geeft alle matches terug met die candy
        return positionList.stream().allMatch(pos -> grid.getCellAt(pos).equals(candy));
    }


    //geen idee of dit een probleem is maar momenteel als je een lengte langer als 2 hebt dan heb je meerdere posities voor dezelfde candy groep
    public Stream<Position> horizontalStartingPositions() {
        return size.positions().stream().filter(pos -> !pos.isLastColumn() && pos.col >= 0 && firstTwoHaveCandy(grid.getCellAt(pos), pos.walkRight()));
    }

    public Stream<Position> verticalStartingPositions() {
        return size.positions().stream().filter(pos -> pos.row < pos.bord.hoogte && pos.row >= 0 && firstTwoHaveCandy(grid.getCellAt(pos), pos.walkDown()));
    }

    public List<Position> longestMatchToRight(Position pos) {
        Candy candy = grid.getCellAt(pos);
        return pos.walkRight().takeWhile(p -> grid.getCellAt(p).equals(candy)).collect(Collectors.toList());
    }

    public List<Position> longestMatchDown(Position pos) {
        Candy candy = grid.getCellAt(pos);
        return pos.walkDown().takeWhile(p -> grid.getCellAt(p).equals(candy)).collect(Collectors.toList());
    }

    public Set<List<Position>> findAllMatches() {
        Set<List<Position>> matches = new HashSet<>();
        Stream.concat(horizontalStartingPositions(), verticalStartingPositions())
            .forEach(pos -> {
                List<Position> matchRight = longestMatchToRight(pos);
                if (matchRight.size() >= 3) {
                    matches.add(matchRight);
                }
                List<Position> matchDown = longestMatchDown(pos);
                if (matchDown.size() >= 3) {
                    matches.add(matchDown);
                }
            });
        return matches;
    }


    public void addScore(int waarde){
        score+= waarde;
    }

    public void resetGame(){
        score = 0;
        generateGrid();
    }

    public BoardSize getSize() {
        return size;
    }

    public Board<Candy> getGrid() {
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
