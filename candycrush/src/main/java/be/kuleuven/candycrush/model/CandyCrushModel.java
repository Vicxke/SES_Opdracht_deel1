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
            Swap swap = new Swap(previousClicked, pos);
            doSwap(swap, grid);
            updateBoard(grid);
            previousClicked = null;
        }
    }


    //opdracht 12 functies
    public boolean firstTwoHaveCandy(Candy candy, Stream<Position> positions, Board<Candy> bord) {
        //neemt de eerste 2 elementen van de lijst
        List<Position> positionList = positions.limit(2).toList();
        if(positionList.size() < 2) return false;

        //geeft alle matches terug met die candy
        return positionList.stream()
                .allMatch(pos -> bord.getCellAt(pos) != null && bord.getCellAt(pos).equals(candy));
    }


    //geen idee of dit een probleem is maar momenteel als je een lengte langer als 2 hebt dan heb je meerdere posities voor dezelfde candy groep
    public Stream<Position> horizontalStartingPositions(Board<Candy> bord) {
        return size.positions().stream().filter(pos -> !firstTwoHaveCandy(bord.getCellAt(pos), pos.walkLeft(), bord)).filter(pos -> bord.getCellAt(pos) != null);
    }

    public Stream<Position> verticalStartingPositions(Board<Candy> bord) {
        return size.positions().stream().filter(pos -> !firstTwoHaveCandy(bord.getCellAt(pos), pos.walkUp(), bord)).filter(pos -> bord.getCellAt(pos) != null);
    }

    public List<Position> longestMatchToRight(Position pos, Board<Candy> bord) {
        Candy candy = bord.getCellAt(pos);
        return pos.walkRight().takeWhile(p -> bord.getCellAt(p) != null && bord.getCellAt(p).equals(candy)).collect(Collectors.toList());
    }

    public List<Position> longestMatchDown(Position pos, Board<Candy> bord) {
        Candy candy = bord.getCellAt(pos);
        return pos.walkDown().takeWhile(p -> bord.getCellAt(p) != null && bord.getCellAt(p).equals(candy)).collect(Collectors.toList());
    }

    public Set<List<Position>> findAllMatches(Board<Candy> bord) {
        Set<List<Position>> matches = new HashSet<>();
        Stream.concat(horizontalStartingPositions(bord), verticalStartingPositions(bord))
            .forEach(pos -> {
                List<Position> matchRight = longestMatchToRight(pos, bord);
                if (matchRight.size() >= 3) {
                    matches.add(matchRight);
                }
                List<Position> matchDown = longestMatchDown(pos, bord);
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

    public void clearMatch(List<Position> match, Board<Candy> bord){
        if (match.isEmpty()) {
            return;
        }
        score++;
        Position pos = match.get(0);
        bord.replaceCellAt(pos, null); // Verwijder het snoepje op de positie

        //ik denk dat dit hier wel mag
        fallDownTo(pos, bord); // Laat snoepjes vallen

        match.remove(0); // Verwijder het eerste element uit de lijst
        clearMatch(match, bord); // Roep de methode opnieuw aan met de bijgewerkte lijst
    }

    public void fallDownTo(Position pos, Board<Candy> bord){

        if (pos.row() == 0) {
            return;
        }
        Position bovenPos = new Position(pos.row() - 1, pos.col(), pos.bord());

        if(bord.getCellAt(pos) == null){
            if (bord.getCellAt(bovenPos) == null) {
                fallDownTo(bovenPos, bord);
            }
            if(bord.getCellAt(bovenPos) != null){
                bord.replaceCellAt(pos, bord.getCellAt(bovenPos));
                bord.replaceCellAt(bovenPos, null);
                fallDownTo(bovenPos, bord);
            }
        }else{
            fallDownTo(bovenPos, bord);
        }
    }

    public boolean updateBoard(Board<Candy> bord){
        Set<List<Position>> matches = findAllMatches(bord);
        if (matches.isEmpty()) {
            return false;
        }

        List<Position> match = matches.iterator().next();
        clearMatch(match, bord);
        updateBoard(bord);
        return true;
    }

    //opdracht 14 functies

    boolean matchAfterSwitch(Swap swap, Board<Candy> bord) {
        //switch match
        simpleSwap(swap, bord);
        Set<List<Position>> matches = findAllMatches(bord);
        simpleSwap(swap, bord);
        return !matches.isEmpty();
    }

    private void doSwap(Swap swap, Board<Candy> bord) {
        if(swap.getPos1() == null || swap.getPos2() == null){
            return;
        }
        if(!matchAfterSwitch(swap, bord)){
            return;
        }
        if (swap.getPos1().isRightNextTo(swap.getPos2())) {
            simpleSwap(swap, bord);
        }
    }

    private void simpleSwap(Swap swap, Board<Candy> bord) {
        if(swap.getPos1() == null || swap.getPos2() == null){
            return;
        }
        Candy temp = bord.getCellAt(swap.getPos1());
        bord.replaceCellAt(swap.getPos1(), bord.getCellAt(swap.getPos2()));
        bord.replaceCellAt(swap.getPos2(), temp);
    }
    
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

    public void maximizeScore() {

        Board<Candy> bord = new Board<>(size);
        grid.copyTo(bord);
        bord.setMaxScore(0);
        List<Swap> bestSequence = bord.getBestSequence();
        GetAllSolutions(new ArrayList<>(), 0, bord);

        System.out.println("Max score: " + bord.getMaxScore());
        System.out.println("amount of moves: " + bord.getBestSequence().size());
        System.out.println("Moves to make: ");
        for (Swap swap : bord.getBestSequence()) {
            System.out.print("move " + swap.getPos1());
            System.out.print(" to " + swap.getPos2() + "\n");
        }

    }

    private void GetAllSolutions(List<Swap> sequence, int score, Board<Candy> bord) {
        //score = countMatches(bord);
        updateBoard(bord);
        //aanpassen als de sequence beter is
        if (score > bord.getMaxScore()) {
            bord.setMaxScore(score);
            bord.setBestSequence(new ArrayList<>(sequence));
        }

        for (Position pos : size.positions()) {
            for (Position neighbor : pos.neighborPositions()) {
                if(pos.isRightNextTo(neighbor)){
                    Swap swap = new Swap(pos, neighbor);
                    if (matchAfterSwitch(swap, bord)){
                        doSwap(swap, bord);
                        int newScore = score + countMatches(bord);
                        sequence.add(swap);
                        GetAllSolutions(sequence, newScore, bord);
                        sequence.remove(swap);
                        doSwap(swap, bord);
                    }
                }
            }
        }
    }

    private int countMatches(Board<Candy> bord) {
        return findAllMatches(bord).stream().mapToInt(List::size).sum();
    }

    public void addScore(int waarde){
        score+= waarde;
    }

    public void resetGame(){
        score = 0;
        generateGrid();
        updateBoard(grid);
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
