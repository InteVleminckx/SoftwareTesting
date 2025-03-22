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
        Monster firstMonster = (Monster) theGame.getBoard().getCell(1, 2).getInhabitant();
        Monster secondMonster = (Monster) theGame.getBoard().getCell(2, 3).getInhabitant();
        // TEST: undo monster move before a player moved
        assertTrue(theGame.getMoveStack().isEmpty());
        theGame.moveMonster(secondMonster, 0, -1);
        assertEquals(1, theGame.getMoveStack().size());
        theGame.undoMove();
        assertTrue(theGame.getMoveStack().isEmpty());

        // TEST: move player to food cell left and undo
        assertTrue(theGame.getMoveStack().isEmpty());
        theGame.movePlayer(-1, 0); // move to food cell left
        assertEquals(1, theGame.getMoveStack().size());
        theGame.undoMove();
        assertTrue(theGame.getMoveStack().isEmpty());

        // TEST: monster moves undone that were executed after a player has moved
        theGame.movePlayer(-1, 0); // move to food cell left
        assertEquals(1, theGame.getMoveStack().size());
        theGame.moveMonster(secondMonster, 0, -1);
        assertEquals(2, theGame.getMoveStack().size());
        theGame.movePlayer(0, -1); // move to empty cell up
        assertEquals(3, theGame.getMoveStack().size());
        theGame.moveMonster(firstMonster, 0, -1);
        assertEquals(4, theGame.getMoveStack().size());
        theGame.moveMonster(secondMonster, 0, -1);
        assertEquals(5, theGame.getMoveStack().size());
        theGame.undoMove();
        // 2 monster moves after a player move, so 3 moves should be removed from the stack
        assertEquals(2, theGame.getMoveStack().size());
    }

    @Test
    public void testUndoPlayerDiedByMonster() {
        Monster firstMonster = (Monster) theGame.getBoard().getCell(1, 2).getInhabitant();
        assertTrue(theGame.getMoveStack().isEmpty());
        theGame.movePlayer(1, 0);
        assertEquals(1, theGame.getMoveStack().size());
        theGame.moveMonster(firstMonster, 1, 0);
        assertEquals(2, theGame.getMoveStack().size());
        theGame.moveMonster(firstMonster, 0, -1);
        assertEquals(3, theGame.getMoveStack().size());

        assertTrue(theGame.playerDied());
        theGame.undoMove();

        assertEquals(0, theGame.getMoveStack().size());
        assertFalse(theGame.playerDied());

    }

    @Test
    public void testUndoPlayerDiedByItself() {
        Monster firstMonster = (Monster) theGame.getBoard().getCell(1, 2).getInhabitant();
        assertTrue(theGame.getMoveStack().isEmpty());
        theGame.movePlayer(1, 0);
        assertEquals(1, theGame.getMoveStack().size());
        theGame.moveMonster(firstMonster, 1, 0);
        assertEquals(2, theGame.getMoveStack().size());
        theGame.movePlayer(0, 1);
        assertTrue(theGame.playerDied());
        assertEquals(3, theGame.getMoveStack().size());
        theGame.undoMove();
        assertEquals(2, theGame.getMoveStack().size());
        assertFalse(theGame.playerDied());
    }

}
