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
        game.regenerateValue(5);
        int value = game.getGrid().get(5);
        assertTrue(value >= 1 && value <= 6);
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
        game.getGrid().set(0,2);
        game.getGrid().set(1,2);
        game.getGrid().set(10,2);
        game.getGrid().set(11,2);
        game.CheckAlleBuren(0);
        assertEquals(4, game.getScore());
    }

    @Test
    public void CheckScoreOnMiddleRightSide() {
        for (int i = 0; i < game.getGrid().size(); i++) {
            game.getGrid().set(i, 3);
        }
        game.CheckAlleBuren(49); //rechtse muur
        assertEquals(6, game.getScore());
    }

    @Test
    public void CheckScoreOnMiddleLeftSide() {
        for (int i = 0; i < game.getGrid().size(); i++) {
            game.getGrid().set(i, 3);
        }
        game.CheckAlleBuren(50); //linkse muur
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

    @Test
    public void testIdexToPosition() {
        //game.pos.blabla
        //werkt niet
        CandyCrushModel.Position pos = new CandyCrushModel.Position(5,5, new CandyCrushModel.BoardSize(10,10));
        //System.out.println(pos.neighborPositions());
    }

}
