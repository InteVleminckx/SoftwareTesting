package jpacman.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class offers a test suite for the Move class hierarchy
 * from Pacman. It contains test cases that should pass for
 * <em>all</em> subclasses of Move, and builds a parallel class
 * hierarchy for testing purposes.
 *
 * (For those familiar with Binder: this class follows the
 * <em>Polymorphic Server Test</em> design pattern).
 *
 * @author Arie van Deursen; Aug 2, 2003
 * @version $Id: MoveTest.java,v 1.6 2008/02/04 10:26:57 arie Exp $
 */
public abstract class MoveTest extends GameTestCase {

    /**
     * The move we make.
     */
    private Move aMove;

    /**
     * A simple test case that should hold for all moves:
     * If the move is possible, the guest should indeed
     * have been moved.
     * Note that the creation of the Move object itself
     * is deferred to subclasses via the createMove factory method.
     */
    @Test public void testApply() {
        // create move, mover, and cell to be moved to.
        aMove = createMove(emptyCell);
        MovingGuest mover = aMove.getMovingGuest();
        Cell location1 = mover.getLocation();
        assertNotNull(mover);
        assertNotNull(location1);

        // status before.
        assertTrue(aMove.movePossible());
        assertEquals(location1, mover.getLocation());
        assertEquals(mover, location1.getInhabitant());

        // do the move.
        aMove.apply();
        Cell location2 = mover.getLocation();
        assertNotNull(location2);
        assertFalse(location1.isOccupied());
        assertEquals(emptyCell, location2);
        assertEquals(mover, location2.getInhabitant());
        assertTrue(aMove.moveDone());
        assertFalse(aMove.movePossible());

    }

    /**
     * Create a move object.
     * The actual guest to be moved (to the target Cell)
     * is determined in the subclasses, who also know how to create
     * the specific Move subclass for that type of guest.
     * (See the Gang-of-Four (Gamma et al) "Factory Method" design pattern)
     * @param target Cell to be moved to
     * @return Instantiated Move subclass object.
     */
    protected abstract Move createMove(Cell target);


    @Test
    public void testUndoEmptyCell() {
        Move move = new PlayerMove(thePlayer, emptyCell);
        move.apply();
        move.undo();
        assertEquals(playerCell, thePlayer.getLocation());
        assertNull(emptyCell.getInhabitant());

        move = new MonsterMove(theMonster, emptyCell);
        move.apply();
        move.undo();
        assertEquals(monsterCell, theMonster.getLocation());
        assertNull(emptyCell.getInhabitant());
    }

    @Test
    public void testUndoFoodCell() {
        Move move = new PlayerMove(thePlayer, foodCell);
        int current_points = thePlayer.getPointsEaten();
        move.apply();
        assertEquals(current_points + theFood.getPoints(), thePlayer.getPointsEaten());
        move.undo();
        assertEquals(current_points, thePlayer.getPointsEaten());
        assertEquals(playerCell, thePlayer.getLocation());
        assertEquals(theFood, foodCell.getInhabitant());

        move = new MonsterMove(theMonster, foodCell);
        move.apply();
        move.undo();
        assertEquals(monsterCell, theMonster.getLocation());
        assertEquals(theFood, foodCell.getInhabitant());
    }

}
