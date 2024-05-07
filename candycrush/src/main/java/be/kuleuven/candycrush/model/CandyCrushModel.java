package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.model.candys.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.util.*;


public class CandyCrushModel {
    private int circleRadius;
    private int score;

    private String playerName;

    private Board<Candy> grid;

    private BoardSize size;

    private Position previousClicked;


    public CandyCrushModel(int veldBreedte, int veldHooghte) {
        playerName = "Default Player";
        this.circleRadius = 30;
        this.score = 0;
        size = new BoardSize(veldBreedte, veldHooghte);
        //this.grid = new ArrayList<Candy>();
        this.grid = new Board<>(size);
        //generateGrid();
        resetGame();
    }


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
        Position pos = new Position(row, col, size);
        if(previousClicked == null){
            previousClicked = pos;
        }

        if(!pos.equals(previousClicked)){
            //swap these positions
            doSwap(previousClicked, pos);
            updateBoard();
            previousClicked = null;
        }
    }


    //opdracht 12 functies
    public boolean firstTwoHaveCandy(Candy candy, Stream<Position> positions) {
        //neemt de eerste 2 elementen van de lijst
        List<Position> positionList = positions.limit(2).toList();
        if(positionList.size() < 2) return false;

        //geeft alle matches terug met die candy
        return positionList.stream()
                .allMatch(pos -> grid.getCellAt(pos) != null && grid.getCellAt(pos).equals(candy));
    }


    //geen idee of dit een probleem is maar momenteel als je een lengte langer als 2 hebt dan heb je meerdere posities voor dezelfde candy groep
    public Stream<Position> horizontalStartingPositions() {
        return size.positions().stream().filter(pos -> !firstTwoHaveCandy(grid.getCellAt(pos), pos.walkLeft())).filter(pos -> grid.getCellAt(pos) != null);
    }

    public Stream<Position> verticalStartingPositions() {
        return size.positions().stream().filter(pos -> !firstTwoHaveCandy(grid.getCellAt(pos), pos.walkUp())).filter(pos -> grid.getCellAt(pos) != null);
    }

    public List<Position> longestMatchToRight(Position pos) {
        Candy candy = grid.getCellAt(pos);
        return pos.walkRight().takeWhile(p -> grid.getCellAt(p) != null && grid.getCellAt(p).equals(candy)).collect(Collectors.toList());
    }

    public List<Position> longestMatchDown(Position pos) {
        Candy candy = grid.getCellAt(pos);
        return pos.walkDown().takeWhile(p -> grid.getCellAt(p) != null && grid.getCellAt(p).equals(candy)).collect(Collectors.toList());
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

        //sorteer zodat de langste match als eerste staat
        return matches.stream().filter(match -> matches.stream()
                        .noneMatch(longerMatch -> longerMatch.size() > match.size() && longerMatch.containsAll(match)))
                .collect(Collectors.toSet());
    }

    //opdracht 13 functies

    public void clearMatch(List<Position> match){
        if (match.isEmpty()) {
            return;
        }
        score++;
        Position pos = match.get(0);
        grid.replaceCellAt(pos, null); // Verwijder het snoepje op de positie

        //ik denk dat dit hier wel mag
        fallDownTo(pos); // Laat snoepjes vallen

        match.remove(0); // Verwijder het eerste element uit de lijst
        clearMatch(match); // Roep de methode opnieuw aan met de bijgewerkte lijst
    }

    public void fallDownTo(Position pos){

        if (pos.row() == 0) {
            return;
        }
        Position bovenPos = new Position(pos.row() - 1, pos.col(), pos.bord());

        if(grid.getCellAt(pos) == null){
            if (grid.getCellAt(bovenPos) == null) {
                fallDownTo(bovenPos);
            }
            if(grid.getCellAt(bovenPos) != null){
                grid.replaceCellAt(pos, grid.getCellAt(bovenPos));
                grid.replaceCellAt(bovenPos, null);
                fallDownTo(bovenPos);
            }
        }else{
            fallDownTo(bovenPos);
        }
    }

    public boolean updateBoard(){
        Set<List<Position>> matches = findAllMatches();
        if (matches.isEmpty()) {
            return false;
        }

        List<Position> match = matches.iterator().next();
        clearMatch(match);
        updateBoard();
        return true;
    }

    //opdracht 14 functies
    
    /*

    //eerst proberen de beste optie te vinden voor dat deze na een actie terug kijkt
    //dus gewoon het bord bekijken zoals het is en alle swaps uitbroperen.
    public void maximumScore(int score, int swaps, Board<Candy> bord){

        for (Position pos : size.positions()) {
            for(Position p : pos.neighborPositions()){
                doSwap(pos, p);
                if(matchAfterSwitch()){
                    //score is gelijk aan het aantal snoepjes in de matches
                    score+=findAllMatches().stream().mapToInt(List::size).sum();
                    swaps++;


                    //als er een match is dan moet je kijken of je nog een match kan maken
                    maximumScore(score, swaps, bord);
                }else{
                    undoSwap(pos, p);
                }
            }
        }

    }*/

    public void maxScore() {
        grid.setMaxScore(0);
        List<Swap> bestSequence = grid.getBestSequence();
        maximizeScore(new ArrayList<>(), 0);

        System.out.println("Max score: " + grid.getMaxScore());
        System.out.println("amount of moves: " + grid.getBestSequence().size());
    }

    private void maximizeScore(List<Swap> sequence, int score) {
        if (score > grid.getMaxScore()) {
            grid.setMaxScore(score);
            grid.setBestSequence(new ArrayList<>(sequence));
        }

        for (Position pos : size.positions()) {
            for (Position neighbor : pos.neighborPositions()) {
                Swap swap = new Swap(pos, neighbor);
                if (isValidSwap(swap)) {
                    swap.swap(grid);
                    int newScore = score + countMatches();
                    sequence.add(swap);
                    maximizeScore(sequence, newScore);
                    sequence.remove(swap);
                    swap.swap(grid);
                }
            }
        }
    }

    private boolean isValidSwap(Swap swap) {
        if(swap == null){
            return false;
        }
        if(grid.getCellAt(swap.getPos1()) == null || grid.getCellAt(swap.getPos2()) == null){
            return false;
        }
        return true;
    }

    private void doSwap(Position pos1, Position pos2) {
        if(pos1 == null || pos2 == null){
            return;
        }
        Candy temp = grid.getCellAt(pos1);
        grid.replaceCellAt(pos1, grid.getCellAt(pos2));
        grid.replaceCellAt(pos2, temp);
    }


    private int countMatches() {
        return findAllMatches().stream().mapToInt(List::size).sum();
    }

    boolean matchAfterSwitch() {
        Set<List<Position>> matches = findAllMatches();
        if (matches.isEmpty()) {
            return false;
        }
        return true;
    }

    public void addScore(int waarde){
        score+= waarde;
    }

    public void resetGame(){
        score = 0;
        generateGrid();
        updateBoard();
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
