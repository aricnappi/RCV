import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PileTest {

    private Pile pile;

    @BeforeEach
    void setUp() {
        pile = new Pile("red");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Red", "Green", "Blue", "", " ", "!!!!!!"})
    void getName(String name) {
        Pile namePile = new Pile(name);
        assertEquals(name, namePile.getName());
    }

    @Test
    void addBallot() {
        // Add two ballots, but add one of them twice. Then assert that the total number of ballots is 2 (not 3)
        Ballot ballot1 = new Ballot(new ArrayList<>(List.of("red", "green", "blue")));
        Ballot ballot2 = new Ballot(new ArrayList<>(List.of("red", "green", "blue")));
        pile.addBallot(ballot1);
        pile.addBallot(ballot2);
        pile.addBallot(ballot1);
        assertEquals(2, pile.getTotalBallots());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 5, 10})
    void addAllBallots(int numBallots) {
        // Create a collection of Ballots
        ArrayList<Ballot> ballots = new ArrayList<>();
        for (int i=0; i<numBallots; i++) {
            ballots.add(new Ballot(new ArrayList<>(List.of("red", "green", "blue"))));
        }
        // Add all of them
        pile.addAllBallots(ballots);
        // Assert that the number of Ballots that were intended to be added is the same as the reported total number
        // of Ballots
        assertEquals(numBallots, pile.getTotalBallots());
    }

    @Test
    void getTotalBallots() {
        // See addAllBallots (same test)
    }

    @Test
    void iterator() {
        // Get the interfaces of the Object returned by pile.iterator()
        Class<?>[] interfaceClassesArr = pile.iterator().getClass().getInterfaces();
        // Get the names of the interfaces as Strings
        ArrayList<String> interfaceNames = new ArrayList<>(interfaceClassesArr.length);
        for (int i=0; i<interfaceClassesArr.length; i++) {
            interfaceNames.add(interfaceClassesArr[i].getSimpleName());
        }
        // Assert that the string "Iterator" is among the names of the interfaces, i.e. that pile.iterator() implements
        // the Iterator interface
        assertTrue(interfaceNames.contains("Iterator"));
    }
}