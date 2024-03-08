package be.kuleuven.candycrush.model;

import be.kuleuven.CheckNeighboursInGrid;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static be.kuleuven.CheckNeighboursInGrid.*;

public class CandyCrushModel {

    private int veldBreedte;
    private int veldHooghte;
    private int circleRadius;
    private int score;

    private ArrayList<Integer> grid;

    public CandyCrushModel(int veldBreedte, int veldHooghte) {
        this.circleRadius = 30;
        this.score = 0;
        this.veldBreedte = veldBreedte;
        this.veldHooghte = veldHooghte;
        this.grid = new ArrayList<>();
        GenerateGrid();
    }

    public void GenerateGrid(){
        Random rndGen = new Random();
        for (int i = 0; i < veldBreedte*veldHooghte; i++) {
            int randomNum = rndGen.nextInt(1,6);
            grid.add(randomNum);
        }
    }

    public void onCircleClick(double x, double y){
        //Circle clickedCircle = (Circle) e.getSource();
        //System.out.println("Circle clicked! Center X: " + clickedCircle.getTranslateX() + ", Center Y: " + clickedCircle.getTranslateY());

        //index to check wordt hieronder uigerekend
        double xCoordInArray = (x - (double) (circleRadius /2)) / circleRadius;
        double yCoordInArray = (y - (double) (circleRadius /2)) / circleRadius;
        int indexValue = (int)(xCoordInArray + (yCoordInArray*veldHooghte));
        System.out.println("index value to Check: " + indexValue);
        CheckAlleBuren(indexValue);
    }

    public void CheckAlleBuren(int index){
        //eerst alle buren eruit halen en dan elke buur vergelijken.

        CheckNeighboursInGrid gridChecker = new CheckNeighboursInGrid();
        //int valueToCheck = grid.get(index);

        //er klopt nog iets niet aan deze functie denk ik aangezien de teruggegeven index voor geen hol klopt
        Iterable<Integer> buren = getSameNeighboursIds(grid, veldBreedte, veldHooghte, index);//geeft een lijst met buren terug die hetzelfde zijn
        Iterator iterator = buren.iterator();
        System.out.println(buren);

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
    private void addScore(int waarde){
        score+= waarde;
    }


    public ArrayList<Integer> getGrid() {
        return grid;
    }

    public int getVeldBreedte() {
        return veldBreedte;
    }

    public int getVeldHooghte() {
        return veldHooghte;
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public int getScore() {
        return score;
    }
}
