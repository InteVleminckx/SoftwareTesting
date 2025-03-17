package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Specialize the general MoveTest test suite to one
 * that is tailored to PlayerMoves.
 * Thanks to inheritance, all test cases from MoveTest
 * are also methods in PlayerMoveTest, thus helping us
 * to test conformance with Liskov's Substitution Principle (LSP)
 * of the Move hierarchy.
 * <p>
 * @author Arie van Deursen; August 21, 2003.
 * @version $Id: PlayerMoveTest.java,v 1.8 2008/02/10 19:51:11 arie Exp $
 */
public class PlayerMoveTest extends MoveTest {

    /**
     * The move the player would like to make.
     */
    private PlayerMove aPlayerMove;

    /**
     * Simple test of a few getters.
     */
    @Test
    public void testSimpleGetters() {
        PlayerMove playerMove = new PlayerMove(thePlayer, foodCell);
        assertEquals(thePlayer, playerMove.getPlayer());
        assertTrue(playerMove.movePossible());
        assertFalse(playerMove.playerDies());
        assertEquals(1, playerMove.getFoodEaten());
        assertTrue(playerMove.invariant());
    }

    /**
     * Create a move object that will be tested.
     *  @see jpacman.model.MoveTest#createMove(jpacman.model.Cell)
     *  @param target The cell to be occupied by the move.
     *  @return The move to be tested.
     */
    @Override
    protected PlayerMove createMove(Cell target) {
        aPlayerMove = new PlayerMove(thePlayer, target);
        return aPlayerMove;
    }


    @Test
    public void testMoveToEmptyCell() {
        PlayerMove playerMove = createMove(emptyCell);
        assertTrue(playerMove.movePossible());
        assertFalse(playerMove.playerDies());
        assertEquals(0, playerMove.getFoodEaten());
    }

    @Test
    public void testMoveToFoodCell() {
        PlayerMove playerMove = createMove(foodCell);

        assertTrue(foodCell.getInhabitant() instanceof Food);
        assertEquals(Guest.FOOD_TYPE, foodCell.getInhabitant().guestType());

        assertTrue(playerMove.movePossible());
        assertFalse(playerMove.playerDies());
        assertEquals(1, playerMove.getFoodEaten());
    }

    @Test
    public void testMoveToMonster() {
        PlayerMove playerMove = createMove(monsterCell);
        assertFalse(playerMove.movePossible()); // Player can't (actually) move to monster, but dies
        assertTrue(playerMove.playerDies());
        assertEquals(0, playerMove.getFoodEaten());
    }

    @Test
    public void testMoveToWall() {
        PlayerMove playerMove = createMove(wallCell);
        assertFalse(playerMove.movePossible());
        assertFalse(playerMove.playerDies());
        assertEquals(0, playerMove.getFoodEaten());
    }

    @Test
    public void testMoveToPlayer() {
        PlayerMove playerMove = createMove(playerCell);
        assertFalse(playerMove.movePossible());
        assertFalse(playerMove.playerDies());
        assertEquals(0, playerMove.getFoodEaten());
    }

}
