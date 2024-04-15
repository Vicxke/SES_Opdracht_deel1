package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.model.CandyCrushModel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    @Test
    public void fillboard(){
        Board<NormalCandy> board = new Board<>(new BoardSize(10, 10));
        board.fill(position -> new NormalCandy(1));
        for (NormalCandy candy : board.getCells()) {
            assertEquals(1, candy.color());
        }
    }

    @Test
    public void copyTo(){
        Board<NormalCandy> board = new Board<>(new BoardSize(10, 10));
        board.fill(position -> new NormalCandy(1));
        Board<NormalCandy> otherBoard = new Board<>(new BoardSize(10, 10));
        board.copyTo(otherBoard);
        for (int i = 0; i < board.getCells().size(); i++) {
            assertEquals(board.getCells().get(i).color(), otherBoard.getCells().get(i).color());
        }
    }
}
