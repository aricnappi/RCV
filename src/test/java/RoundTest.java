import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    @ParameterizedTest
    @MethodSource("getRawVoteDists")
    void getVoteDistribution(Map<String, Integer> rawVoteDist) {
        // Create a Round
        Round round = createRound(rawVoteDist);
        // Get its vote distribution
        HashMap<String, Integer> voteDist = round.getVoteDistribution();
        // Assert that that reported vote distribution is the same as the given one
        assertEquals(rawVoteDist, voteDist);
//        // Reporting
//        TreeMap<String, Integer> rawVoteDistSorted = new TreeMap<>(rawVoteDist);
//        System.out.println("rawVoteDist: " + rawVoteDistSorted);
//        System.out.println("voteDist   : " + voteDist);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void getVotes(int numVotes) {
        Round round = createRound(Map.of("A", numVotes));
        assertEquals(numVotes, round.getVotes("A"));
    }

    @Test
    void getNonEliminatedCandidates() {
        Map<String, Integer> rawVoteDist;
        HashSet<String> expectedOutcome;

        rawVoteDist = Map.of("A", 0, "B", 1, "C", 2, "D", 3, "E", 4);  // One winner, one loser
        expectedOutcome = new HashSet<>(Set.of("B", "C", "D", "E"));
        getNECTest(rawVoteDist, expectedOutcome);

        rawVoteDist = Map.of("A", 1, "B", 1, "C", 2, "D", 3, "E", 4);  // One winner, two losers
        expectedOutcome = new HashSet<>(Set.of("C", "D", "E"));
        getNECTest(rawVoteDist, expectedOutcome);

        rawVoteDist = Map.of("A", 0, "B", 1, "C", 2, "D", 3, "E", 3);  // Two winners, one loser
        expectedOutcome = new HashSet<>(Set.of("B", "C", "D", "E"));
        getNECTest(rawVoteDist, expectedOutcome);

        rawVoteDist = Map.of("A", 2, "B", 2, "C", 2, "D", 2, "E", 2);  // All tied
        expectedOutcome = new HashSet<>(Set.of());
        getNECTest(rawVoteDist, expectedOutcome);
    }

    @Test
    void findTopCandidates() {
        Map<String, Integer> rawVoteDist;
        Set<String> restriction;
        HashSet<String> expectedOutcome;

        restriction = Set.of("A", "B", "C", "D", "E");  // No restriction

        rawVoteDist = Map.of("A", 0, "B", 1, "C", 2, "D", 3, "E", 4);  // One winner, one loser
        expectedOutcome = new HashSet<>(Set.of("E"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);

        rawVoteDist = Map.of("A", 1, "B", 1, "C", 2, "D", 3, "E", 4);  // One winner, two losers
        expectedOutcome = new HashSet<>(Set.of("E"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);

        rawVoteDist = Map.of("A", 0, "B", 1, "C", 2, "D", 3, "E", 3);  // Two winners, one loser
        expectedOutcome = new HashSet<>(Set.of("D", "E"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);

        rawVoteDist = Map.of("A", 2, "B", 2, "C", 2, "D", 2, "E", 2);  // All tied
        expectedOutcome = new HashSet<>(Set.of("A", "B", "C", "D", "E"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);


        restriction = Set.of("A", "B", "C", "D");

        rawVoteDist = Map.of("A", 0, "B", 1, "C", 2, "D", 3, "E", 4);  // One winner, one loser
        expectedOutcome = new HashSet<>(Set.of("D"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);

        rawVoteDist = Map.of("A", 1, "B", 1, "C", 2, "D", 3, "E", 4);  // One winner, two losers
        expectedOutcome = new HashSet<>(Set.of("D"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);

        rawVoteDist = Map.of("A", 0, "B", 1, "C", 2, "D", 3, "E", 3);  // Two winners, one loser
        expectedOutcome = new HashSet<>(Set.of("D"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);

        rawVoteDist = Map.of("A", 2, "B", 2, "C", 2, "D", 2, "E", 2);  // All tied
        expectedOutcome = new HashSet<>(Set.of("A", "B", "C", "D"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);


        restriction = Set.of("C", "D", "E");

        rawVoteDist = Map.of("A", 0, "B", 1, "C", 2, "D", 3, "E", 4);  // One winner, one loser
        expectedOutcome = new HashSet<>(Set.of("E"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);

        rawVoteDist = Map.of("A", 1, "B", 1, "C", 2, "D", 3, "E", 4);  // One winner, two losers
        expectedOutcome = new HashSet<>(Set.of("E"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);

        rawVoteDist = Map.of("A", 0, "B", 1, "C", 2, "D", 3, "E", 3);  // Two winners, one loser
        expectedOutcome = new HashSet<>(Set.of("D", "E"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);

        rawVoteDist = Map.of("A", 2, "B", 2, "C", 2, "D", 2, "E", 2);  // All tied
        expectedOutcome = new HashSet<>(Set.of("C", "D", "E"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);


        restriction = Set.of("A", "B", "C");

        rawVoteDist = Map.of("A", 0, "B", 1, "C", 2, "D", 3, "E", 4);  // One winner, one loser
        expectedOutcome = new HashSet<>(Set.of("C"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);

        rawVoteDist = Map.of("A", 1, "B", 1, "C", 2, "D", 3, "E", 4);  // One winner, two losers
        expectedOutcome = new HashSet<>(Set.of("C"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);

        rawVoteDist = Map.of("A", 0, "B", 1, "C", 2, "D", 3, "E", 3);  // Two winners, one loser
        expectedOutcome = new HashSet<>(Set.of("C"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);

        rawVoteDist = Map.of("A", 2, "B", 2, "C", 2, "D", 2, "E", 2);  // All tied
        expectedOutcome = new HashSet<>(Set.of("A", "B", "C"));
        findTCTest(rawVoteDist, restriction, expectedOutcome);


        restriction = Set.of();

        rawVoteDist = Map.of("A", 0, "B", 1, "C", 2, "D", 3, "E", 4);  // One winner, one loser
        expectedOutcome = new HashSet<>(Set.of());
        findTCTest(rawVoteDist, restriction, expectedOutcome);

        rawVoteDist = Map.of("A", 1, "B", 1, "C", 2, "D", 3, "E", 4);  // One winner, two losers
        expectedOutcome = new HashSet<>(Set.of());
        findTCTest(rawVoteDist, restriction, expectedOutcome);

        rawVoteDist = Map.of("A", 0, "B", 1, "C", 2, "D", 3, "E", 3);  // Two winners, one loser
        expectedOutcome = new HashSet<>(Set.of());
        findTCTest(rawVoteDist, restriction, expectedOutcome);

        rawVoteDist = Map.of("A", 2, "B", 2, "C", 2, "D", 2, "E", 2);  // All tied
        expectedOutcome = new HashSet<>(Set.of());
        findTCTest(rawVoteDist, restriction, expectedOutcome);
    }

    /* Non-test Methods */

    private void findTCTest(Map<String, Integer> rawVoteDist, Set<String> restriction, Set<String> expectedOutcome) {
        Round round = createRound(rawVoteDist);
        HashSet<String> topCands = round.findTopCandidates(restriction);
        assertEquals(expectedOutcome, topCands);
    }

    private void getNECTest(Map<String, Integer> rawVoteDist, Set<String> expectedOutcome) {
        Round round = createRound(rawVoteDist);
        HashSet<String> nec = round.getNonEliminatedCandidates();
        assertEquals(expectedOutcome, nec);
    }

    private static Stream<Map<String, Integer>> getRawVoteDists() {
        return Stream.of(
                Map.of("A", 0, "B", 1, "C", 2, "D", 3, "E", 4),  // One winner, one loser
                Map.of("A", 1, "B", 1, "C", 2, "D", 3, "E", 4),  // One winner, two losers
                Map.of("A", 0, "B", 1, "C", 2, "D", 3, "E", 3),  // Two winners, one loser
                Map.of("A", 2, "B", 2, "C", 2, "D", 2, "E", 2)   // All tied
        );
    }


    private Pile createPile(int numBallots, String name) {
        Pile pile = new Pile(name);
        for (int i = 0; i < numBallots; i++) {
            pile.addBallot(new Ballot(new ArrayList<>()));
        }
        return pile;
    }

    private Round createRound(Map<String, Integer> rawVoteDist) {
        // Get Pile names
        Set<String> pileNames = rawVoteDist.keySet();
        // Create list of Piles
        ArrayList<Pile> piles = new ArrayList<>(rawVoteDist.size());
        Pile pile;
        int numBallots;
        // For each candidate
        for (String name : pileNames) {
            // Get their number of Ballots/votes
            numBallots = rawVoteDist.get(name);
            // Create a Pile with that many Ballots
            pile = createPile(numBallots, name);
            // Add it to the list of Piles
            piles.add(pile);
        }
        // Return a Round using the Piles generated
        return new Round(piles);
    }
}