package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.model.candys.*;
import be.kuleuven.candycrush.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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
    public void testBoard(){

        CandyCrushModel model1 = createBoardFromString("""
   @@o#
   o*#o
   @@**
   *#@@""");

        CandyCrushModel model2 = createBoardFromString("""
   #oo##
   #@o@@
   *##o@
   @@*@o
   **#*o""");

        CandyCrushModel model3 = createBoardFromString("""
   #@#oo@
   @**@**
   o##@#o
   @#oo#@
   @*@**@
   *#@##*""");

        //model1.maxScore();
        /*
        assertEquals(16, model1.getScore());
        assertEquals(4, model1.getGrid().getBestSequence().size());

        model2.maxScore();
        assertEquals(24, model2.getScore());
        assertEquals(7, model2.getGrid().getBestSequence().size());

        model2.maxScore();
        assertEquals(35, model3.getScore());
        assertEquals(10, model3.getGrid().getBestSequence().size());
        */
    }


    public static CandyCrushModel createBoardFromString(String configuration) {
        var lines = configuration.toLowerCase().lines().toList();
        BoardSize size = new BoardSize(lines.size(), lines.getFirst().length());
        var model = new CandyCrushModel(size.breedte(), size.hoogte());
        for (int row = 0; row < lines.size(); row++) {
            var line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                model.getGrid().replaceCellAt(new Position(row, col, size), characterToCandy(line.charAt(col)));
            }
        }
        return model;
    }

    private static Candy characterToCandy(char c) {
        return switch(c) {
            case 'o' -> null;
            case '*' -> new NormalCandy(1);
            case '#' -> new NormalCandy(2);
            case '@' -> new NormalCandy(3);
            default -> throw new IllegalArgumentException("Unexpected value: " + c);
        };
    }

}
