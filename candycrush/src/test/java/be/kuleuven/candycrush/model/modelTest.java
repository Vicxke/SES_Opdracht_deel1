package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.model.candys.*;
import be.kuleuven.candycrush.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static be.kuleuven.candycrush.CandyCrushGameController.createBoardFromString;
import static org.junit.jupiter.api.Assertions.*;

public class modelTest {
    private CandyCrushModel game;
    @BeforeEach
    public void initializeGame() {
        game = new CandyCrushModel(10, 10);
    }

    @Test
    public void CheckGridSize(){
        assertEquals(100, game.getGrid().getSize().boardSize());
        assertEquals(10, game.getSize().breedte());
        assertEquals(10, game.getSize().hoogte());
    }
    /*
    @Test
    public void CheckScoreIsZeroWhenCreated(){
        assertEquals(0, game.getScore());
    }

    @Test
    public void checkRegenerateValueInRange() {
        Candy candy = game.generateRandomCandy();
        Position pos = new Position(0, 5, game.getSize());
        game.getGrid().replaceCellAt(pos, candy);
        assertTrue(candy.equals(game.getGrid().getCellAt(pos)));
    }

    @Test
    public void checkAddScore() {
        game.addScore(5);
        assertEquals(5, game.getScore());
    }

    @Test
    public void checkResetGame() {
        game.addScore(10);
        game.resetGame();
        assertEquals(0, game.getScore());
        assertEquals(100, game.getGrid().getSize().boardSize());
    }
    /*
    @Test
    public void ScoreNeedsToBe4WhenInTopLeftCorner() {
        game.resetGame();
        CandyCrushModel.Candy candy = game.generateRandomCandy();
        CandyCrushModel.Position pos = new CandyCrushModel.Position(0, 0, game.getSize());
        game.getGrid().replaceCellAt(pos, candy);
        game.getGrid().replaceCellAt(new CandyCrushModel.Position(0, 1, game.getSize()), candy);
        game.getGrid().replaceCellAt(new CandyCrushModel.Position(1, 0, game.getSize()), candy);
        game.getGrid().replaceCellAt(new CandyCrushModel.Position(1, 1, game.getSize()), candy);
        game.CheckAlleBuren(pos);
        assertEquals(4, game.getScore());
    }

    @Test
    public void CheckScoreOnMiddleRightSide() {
        CandyCrushModel.Candy candy = game.generateRandomCandy();
        CandyCrushModel.Position pos = new CandyCrushModel.Position(4, 9, game.getSize());
        for (int i = 0; i < game.getGrid().getSize().boardSize(); i++) {
            game.getGrid().replaceCellAt(CandyCrushModel.Position.fromIndex(i, game.getSize()), candy);
        }
        game.CheckAlleBuren(pos); //rechtse muur
        assertEquals(6, game.getScore());
    }

    @Test
    public void CheckScoreOnMiddleLeftSide() {
        CandyCrushModel.Candy candy = game.generateRandomCandy();
        CandyCrushModel.Position pos = new CandyCrushModel.Position(5, 0, game.getSize());
        for (int i = 0; i < game.getGrid().getSize().boardSize(); i++) {
            game.getGrid().replaceCellAt(CandyCrushModel.Position.fromIndex(i, game.getSize()), candy);
        }
        game.CheckAlleBuren(pos); //linkse muur
        assertEquals(6, game.getScore());
    }
    */
    @Test
    public void SetPLayerNameAndCheckIfItChanged() {
        game.setPlayerName("Vic");
        assertEquals("Vic", game.getPlayerName());
    }

    @Test
    public void testDefaultName(){
        assertEquals("Default Player", game.getPlayerName());
    }




    @Test
    public void testBoardModel1(){

        CandyCrushModel model1 = createBoardFromString("""
   @@o#
   o*#o
   @@**
   *#@@""");

        Solution sol = model1.maximizeScore();

        assertEquals(16, sol.getScore());
        assertEquals(4, sol.getSwaps().size());

    }

    @Test
    public void testBoardModel2(){

        CandyCrushModel model2 = createBoardFromString("""
   #oo##
   #@o@@
   *##o@
   @@*@o
   **#*o""");


        Solution sol = model2.maximizeScore();

        assertEquals(24, sol.getScore());
        assertEquals(7, sol.getSwaps().size());

    }

    @Test
    public void testBoardModel3(){
        CandyCrushModel model3 = createBoardFromString("""
   #@#oo@
   @**@**
   o##@#o
   @#oo#@
   @*@**@
   *#@##*""");


        Solution sol = model3.maximizeScore();

        assertEquals(35, sol.getScore());
        assertEquals(10, sol.getSwaps().size());

    }




}
