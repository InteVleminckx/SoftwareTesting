package jpacman.model;

import org.junit.Test;

import static org.junit.Assert.*;

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
public class MonsterMoveTest extends MoveTest {

    /**
     * The move the monster would like to make.
     */
    private MonsterMove aMonsterMove;

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
     *  @see MoveTest#createMove(Cell)
     *  @param target The cell to be occupied by the move.
     *  @return The move to be tested.
     */
    @Override
    protected MonsterMove createMove(Cell target) {
        aMonsterMove = new MonsterMove(theMonster, target);
        return aMonsterMove;
    }

    @Test
    public void testMoveToEmptyCell() {
        MonsterMove monsterMove = createMove(emptyCell);
        assertTrue(monsterMove.movePossible());
    }

    @Test
    public void testMoveToFoodCell() {
        MonsterMove monsterMove = createMove(foodCell);

        assertTrue(foodCell.getInhabitant() instanceof Food);
        assertEquals(Guest.FOOD_TYPE, foodCell.getInhabitant().guestType());

        assertTrue(monsterMove.movePossible());
    }

    @Test
    public void testMoveToWall() {
        MonsterMove monsterMove = createMove(wallCell);
        assertFalse(monsterMove.movePossible());
    }

    @Test
    public void testMoveToMonster() {
        MonsterMove monsterMove = createMove(monsterCell);
        assertFalse(monsterMove.movePossible());
    }

    @Test
    public void testMoveToPlayer() {
        MonsterMove monsterMove = createMove(playerCell);
        assertFalse(monsterMove.movePossible());  // Monster can't (actually) move to player, but player dies
        assertTrue(monsterMove.playerDies());
    }
}
