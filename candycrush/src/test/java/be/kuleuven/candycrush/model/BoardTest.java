package be.kuleuven.candycrush.model;

import be.kuleuven.candycrush.model.CandyCrushModel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    @Test
    public void fillboard(){
        Board<NormalCandy> board = new Board<>(new BoardSize(10, 10));
        board.fill(position -> new NormalCandy(1));
        for (NormalCandy candy : board.getCells().values()) {
            assertEquals(1, candy.color());
        }
    }

    @Test
    public void copyTo(){
        BoardSize size = new BoardSize(10, 10);
        Board<NormalCandy> board = new Board<>(size);
        board.fill(position -> new NormalCandy(1));
        Board<NormalCandy> otherBoard = new Board<>(size);
        board.copyTo(otherBoard);
        for (int i = 0; i < board.getCells().size(); i++) {
            Position pos = Position.fromIndex(i, size);
            assertEquals(board.getCells().get(pos).color(), otherBoard.getCells().get(pos).color());
        }
    }

    @Test
    public void testIfgetPositionsWorks(){
        BoardSize size = new BoardSize(10, 10);
        Board<Candy> board = new Board<>(size);
        board.fill(position -> new NormalCandy(1));
        Position pos = new Position(0, 0, size);
        board.replaceCellAt(pos, new NormalCandy(2));
        Position pos2 = new Position(5, 5, size);
        board.replaceCellAt(pos2, new NormalCandy(2));

        List<Position> ls =  board.getPositionsOfElement(new NormalCandy(2));
        assertEquals(pos, ls.get(0));
        assertEquals(pos2, ls.get(1));

    }
}
