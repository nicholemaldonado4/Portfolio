// Nichole Maldonado
// CS331 - Lab 5, GameDeleterTest Class

/*
 * Junit tests for GameDeleter's deleteUserSelectedGames method.
 * Test cases covers the correct deletetion of game ids from the
 * deque and correct insertions into the hash set. Also checks other
 * method logic.
 */

// changelog
// [5/01/20] [Nichole Maldonado] added junit tests for deleteUserSelectedGames.
// [5/01/20] [Nichole Maldonado] added junit test cases for possible edge cases
//                               such as invalid ids.

// FOR JUNIT 5 ONLY

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

import utep.cs3331.lab5.chess.GameDeleter;
import utep.cs3331.lab5.chess.GameSelector;
import utep.cs3331.lab5.files.FilePaths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/*
 * Junit tests for GameDeleter's deleteUserSelectedGames method.
 * Test cases covers the correct deletetion of game ids from the
 * deque and correct insertions into the hash set. Also checks other
 * method logic.
 */
class GameDeleterTestJunit5 {
    private Deque<String> idQueue;
    private GameSelector gameSelector;
    private GameDeleter gameDeleter;

    /*
     * Creates Deque and put first String id.
     * Creates GameDeleter and GameSelector.
     * @param: None.
     * @return: None.
     */
    @BeforeEach
    void setup() {
        this.idQueue = new ArrayDeque<String>();
        idQueue.addLast("20200429205546624");
        this.gameSelector = new GameSelector(idQueue, new FilePaths(), "nichole");
        this.gameDeleter = new GameDeleter();
    }

    @Nested
    class GameDeleterNestedTest{


        @BeforeEach
        void setup(){
            idQueue.addLast("20200429205500529");
            idQueue.addLast("20200430200439309");
        }

        /*
         * Tests if user selects to remove the first/oldest game that
         * only the the head of the queue is removed.
         * @param: None.
         * @return: None.
         */
        @Test
        void deleteUserSelectedGamesDequeCheck() {
            Scanner input = new Scanner("1\n2\n2\n");
            gameDeleter.deleteUserSelectedGames(gameSelector, input);

            // Displays the oldest games to remove first.
            assertEquals("20200429205500529", idQueue.peekFirst());
            idQueue.removeFirst();
            assertEquals("20200430200439309", idQueue.peekFirst());
            idQueue.removeFirst();
            assertEquals(0, gameSelector.getIdQueue().size());
        }

        /*
         * Test if the user selects two games to remove then they are
         * in the gamesToDelete hash set.
         * @param: None.
         * @return: None.
         */
        @Test
        void getGamesToDeleteSetCheck() {
            Scanner input = new Scanner("1\n1\n2\n");
            gameDeleter.deleteUserSelectedGames(gameSelector, input);

            // Displays the oldest games to remove first.
            assertTrue(gameDeleter.getGamesToDelete().contains("20200429205546624"));
            assertTrue(gameDeleter.getGamesToDelete().contains("20200429205500529"));
            assertEquals(2, gameDeleter.getGamesToDelete().size());
        }

        /*
         * Test that ensures that the idQueue loses its head item if the
         * user does not select any game to delete. The user must select
         * at least one game to delete so if they do not, then the GameDeleter
         * deletes the oldest game.
         * @param: None.
         * @return: None.
         */
        @Test
        void getGamesToDeleteNoSelection() {
            Scanner input = new Scanner("2\n2\n2\n");
            gameDeleter.deleteUserSelectedGames(gameSelector, input);

            // Game Deleter contains oldest game id.
            assertTrue(gameDeleter.getGamesToDelete().contains("20200429205546624"));
            assertEquals(1, gameDeleter.getGamesToDelete().size());

            // Check queue does not have id ending in 624.
            assertEquals("20200429205500529", idQueue.peekFirst());
            idQueue.removeFirst();
            assertEquals("20200430200439309", idQueue.peekFirst());
            idQueue.removeFirst();
            assertEquals(0, gameSelector.getIdQueue().size());
        }

        /*
         * Test that ensures that if the user decides to delete all their games,
         * then the gamesToDelete has all three ids and the idQueue has zero ids.
         * @param: None.
         * @return: None.
         */
        @Test
        void getGamesToDeleteAll() {
            Scanner input = new Scanner("1\n1\n1\n");
            gameDeleter.deleteUserSelectedGames(gameSelector, input);

            // Check gamesToDelete hash set has all three ids.
            assertTrue(gameDeleter.getGamesToDelete().contains("20200429205546624"));
            assertTrue(gameDeleter.getGamesToDelete().contains("20200429205500529"));
            assertTrue(gameDeleter.getGamesToDelete().contains("20200430200439309"));
            assertEquals(3, gameDeleter.getGamesToDelete().size());

            // Check that idQueue is empty.
            assertEquals(0, gameSelector.getIdQueue().size());
        }
    }

    /*
     * Test that ensures that if the idQueue has a bad id, then the
     * GameDeleter will delete that game id and continue on with its normal operation.
     * @param: None.
     * @return: None.
     */
    @Test
    void getGamesToDeleteBadID() {
        idQueue.addLast("ab45");
        Scanner input = new Scanner("1\n");
        gameDeleter.deleteUserSelectedGames(gameSelector, input);

        // User selected oldest game to delete
        assertTrue(gameDeleter.getGamesToDelete().contains("20200429205546624"));
        assertEquals(1, gameDeleter.getGamesToDelete().size());

        // idQueue does not contain corrupted id.
        assertEquals(0, gameSelector.getIdQueue().size());
    }
}