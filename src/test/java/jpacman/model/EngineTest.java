package jpacman.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;


/**
 * Systematic testing of the game state transitions.
 * The test makes use of the simple map and its containing monsters
 * and players, as defined in the GameTestCase.
 * @author Arie van Deursen; Aug 5, 2003
 * @version $Id: EngineTest.java,v 1.6 2008/02/04 23:00:12 arie Exp $
 */
public class EngineTest extends GameTestCase {

    /**
     * The engine that we'll push along every possible transition.
     */
    private Engine theEngine;


    /**
     * Set up an Engine, making use of the Game object
     * (with a small map containing all sorts of guests)
     * created in the superclass.
     */
    @Before
    public void setUp() {
        theEngine = new Engine(theGame);
        assertTrue(theEngine.inStartingState());
    }

    /**
     * Test transition: Starting → Playing
     */
    @Test
    public void testStartTransition() {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
    }

    /**
     * Test transition: Playing → Halted
     */
    @Test
    public void testQuitTransition() {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        theEngine.quit();
        assertTrue(theEngine.inHaltedState());
    }

    /**
     * Test transition: Halted → Playing
     */
    @Test
    public void testResumeTransition() {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        theEngine.quit();
        assertTrue(theEngine.inHaltedState());
        theEngine.start(); // Resume to Playing state
        assertTrue(theEngine.inPlayingState());
    }

    /**
     * Test transition: Playing → Player Won
     */
    @Test
    public void testPlayerWonTransition() {
        assertTrue(theEngine.inStartingState());
        theEngine.start();

        int[][] positions = {{-1, 0}, {0, 1}};
        int i = 0;

        // Simulate the player winning
        while (!theGame.playerWon()) { // playerWon returns theGame.getPointsEaten() >= totalPoints
            assertTrue(theEngine.inPlayingState());
            theEngine.movePlayer(positions[i][0], positions[i][1]); // Move right
            i += 1;
        }

        assertTrue(theEngine.inWonState());
    }

    /**
     * Test transition: Playing → Player Died (via Player Move)
     */
    @Test
    public void testPlayerDiedTransitionPlayerMove() {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());

        // Simulate the player moving into a monster
        theEngine.movePlayer(0, 1); // Move down into a monster
        assertTrue(theEngine.inDiedState());
    }

    /**
     * Test transition: Playing → Player Died (via Monster Move)
     */
    @Test
    public void testPlayerDiedTransitionMonsterMove() {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());

        // Simulate a monster moving into the player
        Monster monster = theEngine.getMonsters().get(0);
        theEngine.moveMonster(monster, 0, -1); // Move monster into the player
        assertTrue(theEngine.inDiedState());
    }

    /**
     * Test transition: Player Won → Starting
     */
    @Test
    public void testRestartFromWonState() {
        assertTrue(theEngine.inStartingState());
        theEngine.start();

        int[][] positions = {{-1, 0}, {0, 1}};

        int i = 0;
        // Simulate the player winning
        while (!theGame.playerWon()) { // playerWon returns theGame.getPointsEaten() >= totalPoints
            assertTrue(theEngine.inPlayingState());
            theEngine.movePlayer(positions[i][0], positions[i][1]); // Move right
            i += 1;
        }
        assertTrue(theEngine.inWonState());

        // Restart the game
        theEngine.start();
        assertTrue(theEngine.inStartingState());
    }

    /**
     * Test transition: Player Died → Starting
     */
    @Test
    public void testRestartFromDiedState() {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());

        // Simulate the player dying
        theEngine.movePlayer(0, 1); // Move down into a monster
        assertTrue(theEngine.inDiedState());

        // Restart the game
        theEngine.start();
        assertTrue(theEngine.inStartingState());
    }

    /**
     * Test transition: Playing → Playing (Player Move)
     */
    @Test
    public void testPlayingToPlayingPlayerMove() {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());

        // Simulate a valid player move
        theEngine.movePlayer(1, 0); // Move right
        assertTrue(theEngine.inPlayingState());
    }

    /**
     * Test transition: Playing → Playing (Monster Move)
     */
    @Test
    public void testPlayingToPlayingMonsterMove() {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());

        // Simulate a valid monster move
        Monster monster = theEngine.getMonsters().get(0);
        theEngine.moveMonster(monster, 1, 0); // Move monster right
        assertTrue(theEngine.inPlayingState());
    }

    /**
     * Test transition: Playing → Halted (Undo)
     */
    @Test
    public void testUndoTransitionPlaying() {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        theEngine.undo();
        assertTrue(theEngine.inHaltedState());
    }

    /**
     * Test transition: Halted → Halted (Undo)
     */
    @Test
    public void testUndoTransitionHalted() {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        theEngine.quit();
        assertTrue(theEngine.inHaltedState());
        theEngine.undo();
        assertTrue(theEngine.inHaltedState());
    }

    /**
     * Test transition: Player Died → Halted (Undo)
     */
    @Test
    public void testUndoTransitionPlayerDied() {
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        theEngine.movePlayer(0, 1);
        assertTrue(theEngine.inDiedState());
        theEngine.undo();
        assertTrue(theEngine.inHaltedState());
        assertFalse(theGame.playerDied());
    }

}
