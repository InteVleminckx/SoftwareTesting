package jpacman.model;

import java.util.Objects;
import java.util.Vector;

import jpacman.TestUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Series of test cases for the game itself.
 * It makes use of the GameTestCase fixture, which
 * contains a simple board.
 * @author Arie van Deursen, 2007
 * @version $Id: GameTest.java,v 1.7 2008/02/10 19:28:20 arie Exp $
 *
 */
public class GameTest extends GameTestCase {

    /**
     * Is each list of monsters a fresh one?
     */
    @Test
    public void testGetMonsters() {
        assertEquals(2, theGame.getMonsters().size());
        // each call to getMonsters should deliver a fresh copy.
        Vector<Monster> ms1 = theGame.getMonsters();
        Vector<Monster> ms2 = theGame.getMonsters();
        assertNotSame(ms1, ms2);
    }

    /**
     * Are the dx/dy in the player correctly set after moving
     * the player around?
     */
    @Test
    public void testDxDyPossibleMove() {
        // start dx/dy should be zero.
        assertEquals(0, theGame.getPlayerLastDx());
        assertEquals(0, theGame.getPlayerLastDy());
        // move to left empty cell -- dx should have beeen adjusted.
        theGame.movePlayer(1, 0);
        assertEquals(1, theGame.getPlayerLastDx());
        assertEquals(0, theGame.getPlayerLastDy());
        // move to up empty cell -- dy should have been adjusted.
        theGame.movePlayer(0, -1);
        assertEquals(0, theGame.getPlayerLastDx());
        assertEquals(-1, theGame.getPlayerLastDy());
    }

    /**
     * Do the player dx/dy remain unaltered if a move fails?
     */
    @Test
    public void testDxDyImpossibleMove() {
        // start dx/dy should be zero.
        assertEquals(0, theGame.getPlayerLastDx());
        assertEquals(0, theGame.getPlayerLastDy());
        // move to a wallcell -- dxdy should have been adjusted.
        theGame.movePlayer(0, -1);
        assertEquals(0, theGame.getPlayerLastDx());
        assertEquals(-1, theGame.getPlayerLastDy());
    }

    /**
     * Test the addGuestFromCode method.
     */
    @Test
    public void testAddGuestFromCode() {

        // map with only known code values
        String[] newMap = {
            "WWWW",
            "WMPW",
            "WF0W",
            "WWWW"
        };
        Game newGame = new Game(newMap);
        // Check if Game object is created
        assertNotNull(newGame);

        char code = 'A';
        // map with an unknown code value 'A'
        newMap = new String[] {
                "WWWW",
                "W" + code + "PW",
                "WF0W",
                "WWWW"
        };

        if (TestUtils.assertionsEnabled()) {
            // catch assertion errors
            boolean failureGenerated;
            try {
                newGame = new Game(newMap);
                failureGenerated = false;
            } catch (AssertionError e) {
                String assertionErrorMsg = "unknown cell type``" + code + "'' in worldmap";
                failureGenerated = Objects.equals(e.getMessage(), assertionErrorMsg);
            }
            assertTrue(failureGenerated);
        }
    }

    @Test
    public void testUndoMove() {
        // move to left empty cell
        theGame.movePlayer(1, 0);
        // move to up empty cell
        theGame.movePlayer(0, -1);
        // undo the last move
        theGame.undoMove();
        assertEquals(1, theGame.getPlayerLastDx());
        assertEquals(0, theGame.getPlayerLastDy());
    }
}
