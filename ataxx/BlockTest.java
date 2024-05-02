package ataxx;

import ataxx.Board;
import ataxx.PieceState;
import org.junit.Test;

import static ataxx.PieceState.*;
import static org.junit.Assert.assertEquals;

public class BlockTest {
    private static final char[] COLS = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
    private static final char[] ROWS = {'1', '2', '3', '4', '5', '6', '7'};


    void checkBoard(Board b, PieceState[][] expectedColors) {
        assertEquals(Board.ONESIDE, expectedColors.length);
        assertEquals(Board.ONESIDE, expectedColors[0].length);
        for (int r = 0; r < expectedColors.length; r++) {
            for (int c = 0; c < expectedColors[0].length; c++) {
                assertEquals("incorrect color at "
                                + COLS[c] + ROWS[ROWS.length - 1 - r],
                        expectedColors[r][c],
                        b.getContent(COLS[c], ROWS[ROWS.length - 1 - r]));
            }
        }
    }




    @Test
    public void testSettingBlockNormal() {
        // 3 reflections
        Board b = new Board();
        b.setBlock('a', '2');
        final PieceState[][] BLOCK = {
                {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
                {BLOCKED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLOCKED},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {BLOCKED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLOCKED},
                {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
        };
        checkBoard(b, BLOCK);

        assertEquals(45, b.unblockedNum());
    }


    @Test
    public void testSettingBlocksOnColMiddle() {
        Board b1 = new Board();
//        b1.setBlock('h','1');  ///不知道这个情况要不要算上
        b1.setBlock('c', '4');
        b1.setBlock('d', '6');
        final PieceState[][] BLOCK1 = {
                {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
                {EMPTY, EMPTY, EMPTY, BLOCKED, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, BLOCKED, EMPTY, BLOCKED, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, BLOCKED, EMPTY, EMPTY, EMPTY},
                {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
        };
        checkBoard(b1, BLOCK1);

        assertEquals(45, b1.unblockedNum());
    }

    @Test
    public void testSettingBlockOnCentre() {
        Board b2 = new Board();
        b2.setBlock('d', '4');
        final PieceState[][] BLOCK2 = {
                {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, BLOCKED, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
        };
        checkBoard(b2, BLOCK2);
        assertEquals(48, b2.unblockedNum());
    }

    @Test(expected = GameException.class)
    public void testSettingBlockThrowError() {
        Board b2 = new Board();
        b2.setBlock('a', '1');
    }

}
