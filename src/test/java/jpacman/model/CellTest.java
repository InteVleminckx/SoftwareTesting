package jpacman.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import jpacman.model.Cell.*;

import static org.junit.Assert.*;

/**
 * Test suite for methods working directly on Cells.
 *
 * @author Arie van Deursen; Jul 29, 2003
 * @version $Id: CellTest.java,v 1.16 2008/02/10 12:51:55 arie Exp $
 */
public class CellTest {

    /**
     * Width & heigth of board to be used.
     */
    private final int width = 4, height = 5;

    /**
     * The board the cells occur on.
     */
    private Board aBoard;

    /**
     * The "Cell Under Test".
     */
    private Cell aCell;

    /**
     * Actually create the board and the cell. *
     */
    @Before
    public void setUpBoard() {
        aBoard = new Board(width, height);
        // put the cell on an invariant boundary value.
        aCell = new Cell(0, height - 1, aBoard);
    }



    /**
     * Test obtaining a cell at a given offset. Ensure both postconditions
     * (null value if beyond border, value with board) are executed.
     */
    @Test
    public void testCellAtOffset() {
        assertEquals(height - 2, aCell.cellAtOffset(0, -1).getY());
        assertEquals(0, aCell.cellAtOffset(0, -1).getX());
        // assertNull(aCell.cellAtOffset(-1, 0));

        Cell cell11 = aBoard.getCell(1, 1);
        Cell cell12 = aBoard.getCell(1, 2);
        assertEquals(cell12, cell11.cellAtOffset(0, 1));
    }

    @Test
    public void testAdjacent() {

        //##### HORIZONTAL ADJACENCY #####//
        // horizontal adjacent
        aCell = aBoard.getCell(2, 2);
        assertTrue(aCell.adjacent(aBoard.getCell(3, 2))); // right
        assertTrue(aCell.adjacent(aBoard.getCell(1, 2))); // left
        // horizontal non-adjacent, close
        aCell = aBoard.getCell(1, 1);
        assertFalse(aCell.adjacent(aBoard.getCell(3, 1))); // close right
        aCell = aBoard.getCell(3, 1);
        assertFalse(aCell.adjacent(aBoard.getCell(0, 1))); // close left
        // horizontal non-adjacent, far
        aCell = aBoard.getCell(0, 0);
        assertFalse(aCell.adjacent(aBoard.getCell(3, 0))); // far right
        aCell = aBoard.getCell(3, 3);
        assertFalse(aCell.adjacent(aBoard.getCell(0, 3))); // far left


        //##### VERTICAL ADJACENCY #####//
        // vertical adjacent
        aCell = aBoard.getCell(2, 1);
        assertTrue(aCell.adjacent(aBoard.getCell(2, 2))); // up
        assertTrue(aCell.adjacent(aBoard.getCell(2, 0))); // dow
        // vertical non-adjacent, close
        assertFalse(aCell.adjacent(aBoard.getCell(2, 3))); // close up
        aCell = aBoard.getCell(2, 2);
        assertFalse(aCell.adjacent(aBoard.getCell(2, 0))); // close down
        // vertical non-adjacent, far
        aCell = aBoard.getCell(0, 0);
        assertFalse(aCell.adjacent(aBoard.getCell(0, 3))); // far up
        aCell = aBoard.getCell(3, 3);
        assertFalse(aCell.adjacent(aBoard.getCell(3, 0))); // far down


        //##### DIAGONAL ADJACENCY #####//
        // diagonal adjacent
        aCell = aBoard.getCell(1, 1);
        assertFalse(aCell.adjacent(aBoard.getCell(2, 2))); // up-right
        assertFalse(aCell.adjacent(aBoard.getCell(0, 2))); // up-left
        assertFalse(aCell.adjacent(aBoard.getCell(2, 0))); // down-right
        assertFalse(aCell.adjacent(aBoard.getCell(0, 0))); // down-left
        // diagonal non-adjacent, close
        assertFalse(aCell.adjacent(aBoard.getCell(3, 3))); // close up-right
        aCell = aBoard.getCell(2, 2);
        assertFalse(aCell.adjacent(aBoard.getCell(0, 4))); // close up-left
        assertFalse(aCell.adjacent(aBoard.getCell(0, 0))); // close down-left
        aCell = aBoard.getCell(1, 2);
        assertFalse(aCell.adjacent(aBoard.getCell(3, 0))); // close down-right
        // diagonal non-adjacent, far
        aCell = aBoard.getCell(0, 0);
        assertFalse(aCell.adjacent(aBoard.getCell(3, 3))); // far up-right
        aCell = aBoard.getCell(3, 0);
        assertFalse(aCell.adjacent(aBoard.getCell(0, 3))); // far up-left
        aCell = aBoard.getCell(3, 3);
        assertFalse(aCell.adjacent(aBoard.getCell(0, 0))); // far down-left
        aCell = aBoard.getCell(0, 3);
        assertFalse(aCell.adjacent(aBoard.getCell(3, 0))); // far down-right


        //##### SPECIAL CASES #####//
        // same cell
        aCell = aBoard.getCell(3, 3);
        assertFalse(aCell.adjacent(aBoard.getCell(3, 3)));
        // null cell
        assertFalse(aCell.adjacent(null));
        // different board
        assertFalse(aCell.adjacent(new Board(5, 4).getCell(4, 3)));

    }

}
