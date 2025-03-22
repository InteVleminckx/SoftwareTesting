package jpacman.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class EngineSneakPathTest extends GameTestCase {

    private Engine theEngine;

    @Before
    public void setUp() {
        theEngine = new Engine(theGame);
        assertTrue(theEngine.inStartingState());
    }

    @Test
    public void testSneakPathStartingState() {
        // Setup before checking the sneak paths starting from the starting state
        assertTrue(theEngine.inStartingState());

        // Start checking for sneak paths
        theEngine.quit();
        assertTrue(theEngine.inStartingState());
        theEngine.movePlayer(0, -1);
        assertTrue(theEngine.inStartingState());
        Monster monster = theEngine.getMonsters().get(0);
        theEngine.moveMonster(monster, 0, -1);
        assertTrue(theEngine.inStartingState());
        theEngine.undo();
        assertTrue(theEngine.inStartingState());
    }

    @Test
    public void testSneakPathPlayingState() {
        // Setup before checking the sneak paths starting from the playing state
        assertTrue(theEngine.inStartingState());
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());

        // Start checking for sneak paths
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
    }

    @Test
    public void testSneakPathHaltedState() {
        // Setup before checking the sneak paths starting from the halted state
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        theEngine.quit();
        assertTrue(theEngine.inHaltedState());

        // Start checking for sneak paths
        theEngine.quit();
        assertTrue(theEngine.inHaltedState());
        theEngine.movePlayer(1, 0);
        assertTrue(theEngine.inHaltedState());
        Monster monster = theEngine.getMonsters().get(0);
        theEngine.moveMonster(monster, 0, -1);
        assertTrue(theEngine.inHaltedState());
    }

    @Test
    public void testSneakPathPlayerWonState() {
        // Setup before checking the sneak paths starting from the player won state
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        int[][] positions = {{-1, 0}, {0, 1}};

        int i = 0;
        while (!theGame.playerWon()) {
            assertTrue(theEngine.inPlayingState());
            theEngine.movePlayer(positions[i][0], positions[i][1]); // Move right
            i += 1;
        }
        assertTrue(theEngine.inWonState());

        // Start checking for sneak paths
        theEngine.quit();
        assertTrue(theEngine.inWonState());
        theEngine.movePlayer(1, 0);
        assertTrue(theEngine.inWonState());
        Monster monster = theEngine.getMonsters().get(0);
        theEngine.moveMonster(monster, 0, -1);
        assertTrue(theEngine.inWonState());
        theEngine.undo();
        assertTrue(theEngine.inWonState());
    }

    @Test
    public void testSneakPathPlayerDiedState() {
        // Setup before checking the sneak paths starting from the player died state
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertTrue(theEngine.inPlayingState());
        theEngine.movePlayer(0, 1);
        assertTrue(theEngine.inDiedState());

        // Start checking for sneak paths
        theEngine.quit();
        assertTrue(theEngine.inDiedState());
        theEngine.movePlayer(1, 0);
        assertTrue(theEngine.inDiedState());
        Monster monster = theEngine.getMonsters().get(0);
        theEngine.moveMonster(monster, 0, -1);
        assertTrue(theEngine.inDiedState());
    }
}