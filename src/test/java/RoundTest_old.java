import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest_old {

    @BeforeEach
    void setUp() {
    }

    private ArrayList<Pile> generatePiles(ArrayList<String> pileNames, ArrayList<Integer> pileSizes) {
        // Get number of piles
        int numPiles = pileNames.size();
        // Instantiate list of Piles
        ArrayList<Pile> piles = new ArrayList<>(numPiles);
        String name;
        int size;
        Pile pile;
        // For each Pile
        for (int i = 0; i < numPiles; i++) {
            // Get its name and size
            name = pileNames.get(i);
            size = pileSizes.get(i);
            // Create new Pile with designated name
            pile = new Pile(name);
            // Add ballots to Pile according to its designated pile size
            for (int j = 0; j < size; j++) {
                pile.addBallot(new Ballot(new ArrayList<>()));
            }
            // Add Pile to list of Piles
            piles.add(pile);
        }
        // Return list of Piles
        return piles;
    }

    private ArrayList<Integer> generatePileSizes(int numPiles) {
        // Generate sizes of Piles
        ArrayList<Integer> pileSizes = new ArrayList<>(numPiles);
        for (int i = 0; i < numPiles; i++) {
            pileSizes.add((int) (100 * Math.random()));
        }
        return pileSizes;
    }

    private ArrayList<String> generatePileNames(int numPiles) {
        // Generate names of Piles
        ArrayList<String> pileNames = new ArrayList<>(numPiles);
        char nameChar;
        for (int i = 0; i < numPiles; i++) {
            nameChar = (char) (65 + i);  // Uppercase alphabet
            char[] nameCharArr = {nameChar};
            pileNames.add(new String(nameCharArr));
        }
        return pileNames;
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void getVoteDistribution(int numPiles) {
        // Generate Pile names and sizes
        ArrayList<String> pileNames = generatePileNames(numPiles);
        ArrayList<Integer> pileSizes = generatePileSizes(numPiles);
        // Generate Piles
        ArrayList<Pile> piles = generatePiles(pileNames, pileSizes);
        // Create Round
        Round round = new Round(piles);
        // Get its vote distribution
        HashMap<String, Integer> voteDist = round.getVoteDistribution();
        String name;
        int size;
        System.out.println("numPiles: " + numPiles + "\n");
        // For each Pile
        for (int i = 0; i < numPiles; i++) {
            // Get its name and size
            name = pileNames.get(i);
            size = pileSizes.get(i);
            System.out.println("Name: " + name + "\n" + "Size: " + size + "\n");
            // Assert that the vote distribution has the name of the Pile
            assertTrue(voteDist.containsKey(name));
            // If it does have the name of the Pile
            if (voteDist.containsKey(name)) {
                // Assert that the vote distribution reports the correct number of Ballots/votes for that Pile/candidate
                assertEquals(size, voteDist.get(name));
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void getVotes(int numPiles) {
        // Generate Pile names and sizes
        ArrayList<String> pileNames = generatePileNames(numPiles);
        ArrayList<Integer> pileSizes = generatePileSizes(numPiles);
        // Generate Piles
        ArrayList<Pile> piles = generatePiles(pileNames, pileSizes);
        // Create Round
        Round round = new Round(piles);
        for (int i = 0; i < numPiles; i++) {
            assertEquals(pileSizes.get(i), round.getVotes(pileNames.get(i)));
        }
    }

    private static Stream<List<Integer>> getNECArgs() {
        return Stream.of(
                List.of(0, 1, 2, 3, 4, 5),
                List.of(0, 0, 2, 3, 4, 5)
        );
    }

    private static Pile createPileWithNumBallots(int numBallots, String name) {
        Pile pile = new Pile(name);
        for (int i = 0; i < numBallots; i++) {
            pile.addBallot(new Ballot(new ArrayList<>()));
        }
        return pile;
    }

    private Round createRound(List<Integer> rawVoteDist) {
        // Get number of Piles in Round
        int numPiles = rawVoteDist.size();
        // Generate Pile names
        ArrayList<String> pileNames = generatePileNames(numPiles);
        // Create list of Piles
        ArrayList<Pile> piles = new ArrayList<>(numPiles);
        Pile pile;
        String name;
        int numBallots;
        // For each Pile
        for (int i = 0; i < numPiles; i++) {
            // Get its name
            name = pileNames.get(i);
            // Get its number of Ballots
            numBallots = rawVoteDist.get(i);
            // Create a Pile with that name and number of Ballots
            pile = createPileWithNumBallots(numBallots, name);
            // Add it to the list of Piles
            piles.add(pile);
        }
        // Return a Round using the Piles generated
        return new Round(piles);
    }

    @ParameterizedTest
    @MethodSource("getNECArgs")
    void getNonEliminatedCandidates(List<Integer> rawVoteDist) {
        int numPiles = rawVoteDist.size();
        ArrayList<String> names = generatePileNames(numPiles);

    }

    @Test
    void findTopCandidates() {
    }
}