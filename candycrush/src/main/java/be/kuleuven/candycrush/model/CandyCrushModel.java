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
        this.grid = new Board<>(size);
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
            score += countMatches(grid);
            updateBoard(grid, findAllMatches(grid));
            previousClicked = null;

            ArrayList<Swap> swaps = possibleSwaps(grid);
            System.out.println("possble swaps: " + swaps.size());
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
        Position pos = match.get(0);
        bord.replaceCellAt(pos, null); // Verwijder het snoepje op de positie

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

    public boolean updateBoard(Board<Candy> bord, Set<List<Position>> matches){

        if (matches.isEmpty()) {
            return false;
        }

        Iterator<List<Position>> iterator = matches.iterator();
        List<Position> match = iterator.next(); // Haal de eerste match op
        iterator.remove(); // Verwijder de eerste match

        List<Position> copyMatch = new ArrayList<>(match);
        clearMatch(copyMatch, bord);

        updateBoard(bord, matches);

        //hierna alles matches laten vallen
        for (Position pos : match) {
            fallDownTo(pos, bord);
        }

        Set<List<Position>> newMatches = findAllMatches(bord);
        updateBoard(bord, newMatches);

        return true;
    }


    //opdracht 14 functies

    boolean matchAfterSwitch(Swap swap, Board<Candy> bord) {
        if(swap.getPos1() == null || swap.getPos2() == null){
            return false;
        }
        if(bord.getCellAt(swap.getPos1()) == null || bord.getCellAt(swap.getPos2()) == null){
            return false;
        }
        //switch match
        simpleSwap(swap, bord);
        Set<List<Position>> matches = findAllMatches(bord);
        simpleSwap(swap, bord);
        return !matches.isEmpty();
    }

    private void doSwap(Swap swap, Board<Candy> bord) {
        if(!matchAfterSwitch(swap, bord)){
            return;
        }
        if (swap.getPos1().isRightNextTo(swap.getPos2())) {
            simpleSwap(swap, bord);
        }
    }

    private void simpleSwap(Swap swap, Board<Candy> bord) {
        Candy temp = bord.getCellAt(swap.getPos1());
        bord.replaceCellAt(swap.getPos1(), bord.getCellAt(swap.getPos2()));
        bord.replaceCellAt(swap.getPos2(), temp);
    }


    public ArrayList<Swap> possibleSwaps(Board<Candy> bord){
        ArrayList<Swap> swaps = new ArrayList<>();
        for (Position pos : bord.getSize().positions()) {
            for(Position p : pos.neighborPositions()){
                Swap swap = new Swap(pos, p);
                if(swaps.stream().noneMatch(s -> s.getPos1().equals(p) && s.getPos2().equals(pos))){
                    if(pos.isRightNextTo(p) && matchAfterSwitch(swap, bord)){
                        swaps.add(swap);
                    }
                }

            }
        }
        return swaps;
    }

    public Solution maximizeScore() {


        Board<Candy> bord = new Board<>(grid.getSize());
        grid.copyTo(bord);

        Solution solution = new Solution(0, new ArrayList<>(), bord);
        Solution optimalsolution = GetOptimalSolutions(solution, null);

        return optimalsolution;

    }
    private Solution GetOptimalSolutions(Solution current, Solution bestSoFar){
        Board<Candy> oldboard = current.getBord();
        ArrayList<Swap> swaps = possibleSwaps(oldboard);

        //we zijn aan het einde als deze swaps empty is.
        if(swaps.isEmpty()) {
            if(bestSoFar == null || current.isBetterThan(bestSoFar)){
                return current;
            }else{
                return bestSoFar;
            }
        }

        for(Swap swap : swaps){
            Board<Candy> bord = new Board<>(current.getBord().getSize());
            oldboard.copyTo(bord);

            doSwap(swap, bord);
            int newScore = current.getScore() + countMatches(bord);
            updateBoard(bord, findAllMatches(bord));

            ArrayList<Swap> newSwaps = new ArrayList<>(current.getSwaps());
            newSwaps.add(swap);

            Solution newSolution = new Solution(newScore, newSwaps, bord);
            bestSoFar = GetOptimalSolutions(newSolution, bestSoFar);
        }
        return bestSoFar;
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
        updateBoard(grid, findAllMatches(grid));
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
