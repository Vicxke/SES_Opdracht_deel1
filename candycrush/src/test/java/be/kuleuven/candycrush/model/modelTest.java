package be.kuleuven.candycrush.model;

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
        assertEquals(100, game.getGrid().size());
        assertEquals(10, game.getBoard().breedte());
        assertEquals(10, game.getBoard().hoogte());
    }

    @Test
    public void CheckScoreIsZeroWhenCreated(){
        assertEquals(0, game.getScore());
    }

    @Test
    public void checkRegenerateValueInRange() {
        CandyCrushModel.Candy candy = game.generateRandomCandy();
        game.getGrid().set(5, candy);
        assertTrue(candy.equals(game.getGrid().get(5)));
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
        assertEquals(100, game.getGrid().size());
    }

    @Test
    public void ScoreNeedsToBe4WhenInTopLeftCorner() {
        game.resetGame();
        CandyCrushModel.Candy candy = game.generateRandomCandy();
        CandyCrushModel.Position pos = new CandyCrushModel.Position(0, 0, game.getBoard());
        game.getGrid().set(0,candy);
        game.getGrid().set(1,candy);
        game.getGrid().set(10,candy);
        game.getGrid().set(11,candy);
        game.CheckAlleBuren(pos);
        assertEquals(4, game.getScore());
    }

    @Test
    public void CheckScoreOnMiddleRightSide() {
        CandyCrushModel.Candy candy = game.generateRandomCandy();
        CandyCrushModel.Position pos = new CandyCrushModel.Position(4, 9, game.getBoard());
        for (int i = 0; i < game.getGrid().size(); i++) {
            game.getGrid().set(i, candy);
        }
        game.CheckAlleBuren(pos); //rechtse muur
        assertEquals(6, game.getScore());
    }

    @Test
    public void CheckScoreOnMiddleLeftSide() {
        CandyCrushModel.Candy candy = game.generateRandomCandy();
        CandyCrushModel.Position pos = new CandyCrushModel.Position(5, 0, game.getBoard());
        for (int i = 0; i < game.getGrid().size(); i++) {
            game.getGrid().set(i, candy);
        }
        game.CheckAlleBuren(pos); //linkse muur
        assertEquals(6, game.getScore());
    }

    @Test
    public void SetPLayerNameAndCheckIfItChanged() {
        game.setPlayerName("Vic");
        assertEquals("Vic", game.getPlayerName());
    }

    @Test
    public void testDefaultName(){
        assertEquals("Default Player", game.getPlayerName());
    }

}
