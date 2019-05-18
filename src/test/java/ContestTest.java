import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ContestTest {

    @Test
    void getVoteDistributions() {
        List<String> candidates;
        Collection<List<String>> rawBallots;
        ArrayList<HashMap<String, Integer>> expectedVoteDistributions;

        candidates = List.of("A", "B", "C");
        rawBallots = List.of(
                List.of("A", "B", "C")
        );
        expectedVoteDistributions = new ArrayList<>(
                List.of(
                        new HashMap<>(Map.of("A", 1, "B", 0, "C", 0)),
                        new HashMap<>(Map.of("A", 1))
                )
        );
        getVDTest(candidates, rawBallots, expectedVoteDistributions);

        candidates = List.of("A", "B", "C");
        rawBallots = List.of(
                List.of("A", "B", "C"),
                List.of("A", "B", "C")
        );
        expectedVoteDistributions = new ArrayList<>(
                List.of(
                        new HashMap<>(Map.of("A", 2, "B", 0, "C", 0)),
                        new HashMap<>(Map.of("A", 2))
                )
        );
        getVDTest(candidates, rawBallots, expectedVoteDistributions);

        candidates = List.of("A", "B", "C");
        rawBallots = List.of(
                List.of("A", "B", "C"),
                List.of("B", "A", "C")
        );
        expectedVoteDistributions = new ArrayList<>(
                List.of(
                        new HashMap<>(Map.of("A", 1, "B", 1, "C", 0)),
                        new HashMap<>(Map.of("A", 1, "B", 1))
                )
        );
        getVDTest(candidates, rawBallots, expectedVoteDistributions);

        candidates = List.of("A", "B", "C");
        rawBallots = List.of(
                List.of("A", "B", "C"),
                List.of("A", "B", "C"),
                List.of("A", "B", "C"),
                List.of("B", "A", "C"),
                List.of("B", "A", "C"),
                List.of("B", "A", "C"),
                List.of("C", "B", "A")
        );
        expectedVoteDistributions = new ArrayList<>(
                List.of(
                        new HashMap<>(Map.of("A", 3, "B", 3, "C", 1)),
                        new HashMap<>(Map.of("A", 3, "B", 4)),
                        new HashMap<>(Map.of("B", 7))
                )
        );
        getVDTest(candidates, rawBallots, expectedVoteDistributions);

        candidates = List.of("A", "B", "C", "D");
        rawBallots = List.of(
                List.of("A", "B", "C", "D"),
                List.of("A", "B", "C", "D"),
                List.of("A", "B", "C", "D"),
                List.of("B", "A", "C", "D"),
                List.of("B", "A", "C", "D"),
                List.of("B", "A", "C", "D"),
                List.of("B", "A", "C", "D"),
                List.of("C", "B", "A", "D"),
                List.of("C", "A", "B", "D"),
                List.of("D", "A", "B", "C")
        );
        expectedVoteDistributions = new ArrayList<>(
                List.of(
                        new HashMap<>(Map.of("A", 3, "B", 4, "C", 2, "D", 1)),
                        new HashMap<>(Map.of("A", 4, "B", 4, "C", 2)),
                        new HashMap<>(Map.of("A",5,"B", 5))
                )
        );
        getVDTest(candidates, rawBallots, expectedVoteDistributions);

        candidates = List.of("A", "B", "C");
        rawBallots = List.of(
                List.of("A", "B", "C"),
                List.of("A", "B", "C"),
                List.of("B", "C", "A")
        );
        expectedVoteDistributions = new ArrayList<>(
                List.of(
                        new HashMap<>(Map.of("A", 2, "B", 1, "C", 0)),
                        new HashMap<>(Map.of("A", 2, "B", 1)),
                        new HashMap<>(Map.of("A", 3))
                )
        );
        getVDTest(candidates, rawBallots, expectedVoteDistributions);

        candidates = List.of("A", "B", "C");
        rawBallots = List.of(
                List.of("A", "B", "C"),
                List.of("B", "C"),
                List.of("C")
        );
        expectedVoteDistributions = new ArrayList<>(
                List.of(
                        new HashMap<>(Map.of("A", 1, "B", 1, "C", 1))
                )
        );
        getVDTest(candidates, rawBallots, expectedVoteDistributions);

        candidates = List.of("A", "B", "C");
        rawBallots = List.of(
                List.of("A", "B", "C"),
                List.of("A", "B", "C"),
                List.of("A", "B", "C"),
                List.of("B", "C"),
                List.of("B", "A"),
                List.of("C")
        );
        expectedVoteDistributions = new ArrayList<>(
                List.of(
                        new HashMap<>(Map.of("A", 3, "B", 2, "C", 1)),
                        new HashMap<>(Map.of("A", 3, "B", 2)),
                        new HashMap<>(Map.of("A", 4))
                )
        );
        getVDTest(candidates, rawBallots, expectedVoteDistributions);
    }

    private void getVDTest(Collection<String> candidates, Collection<List<String>> rawBallots, ArrayList<HashMap<String, Integer>> expectedVoteDistributions) {
        Contest contest = new Contest(candidates, rawBallots);
        ArrayList<HashMap<String, Integer>> voteDistributions = contest.getVoteDistributions();
        assertEquals(expectedVoteDistributions, voteDistributions);
    }

    @Test
    void getWinner() {
        List<String> candidates;
        Collection<List<String>> rawBallots;
        HashSet<String> expectedWinners;

        candidates = List.of("A", "B", "C");
        rawBallots = List.of(
                List.of("A", "B", "C")
        );
        expectedWinners = new HashSet<>(Set.of("A"));
        getWTest(candidates, rawBallots, expectedWinners);

        candidates = List.of("A", "B", "C");
        rawBallots = List.of(
                List.of("A", "B", "C"),
                List.of("A", "B", "C")
        );
        expectedWinners = new HashSet<>(Set.of("A"));
        getWTest(candidates, rawBallots, expectedWinners);

        candidates = List.of("A", "B", "C");
        rawBallots = List.of(
                List.of("A", "B", "C"),
                List.of("B", "A", "C")
        );
        expectedWinners = new HashSet<>(Set.of("A", "B"));
        getWTest(candidates, rawBallots, expectedWinners);

        candidates = List.of("A", "B", "C");
        rawBallots = List.of(
                List.of("A", "B", "C"),
                List.of("A", "B", "C"),
                List.of("A", "B", "C"),
                List.of("B", "A", "C"),
                List.of("B", "A", "C"),
                List.of("B", "A", "C"),
                List.of("C", "B", "A")
        );
        expectedWinners = new HashSet<>(Set.of("B"));
        getWTest(candidates, rawBallots, expectedWinners);

        candidates = List.of("A", "B", "C", "D");
        rawBallots = List.of(
                List.of("A", "B", "C", "D"),
                List.of("A", "B", "C", "D"),
                List.of("A", "B", "C", "D"),
                List.of("B", "A", "C", "D"),
                List.of("B", "A", "C", "D"),
                List.of("B", "A", "C", "D"),
                List.of("B", "A", "C", "D"),
                List.of("C", "B", "A", "D"),
                List.of("C", "A", "B", "D"),
                List.of("D", "A", "B", "C")
        );
        expectedWinners = new HashSet<>(Set.of("B"));
        getWTest(candidates, rawBallots, expectedWinners);

        candidates = List.of("A", "B", "C");
        rawBallots = List.of(
                List.of("A", "B", "C"),
                List.of("A", "B", "C"),
                List.of("B", "C", "A")
        );
        expectedWinners = new HashSet<>(Set.of("A"));
        getWTest(candidates, rawBallots, expectedWinners);

        candidates = List.of("A", "B", "C");
        rawBallots = List.of(
                List.of("A", "B", "C"),
                List.of("B", "C"),
                List.of("C")
        );
        expectedWinners = new HashSet<>(Set.of("A", "B", "C"));
        getWTest(candidates, rawBallots, expectedWinners);

        candidates = List.of("A", "B", "C");
        rawBallots = List.of(
                List.of("A", "B", "C"),
                List.of("A", "B", "C"),
                List.of("B", "C"),
                List.of("B", "A"),
                List.of("C")
        );
        expectedWinners = new HashSet<>(Set.of("A", "B"));
        getWTest(candidates, rawBallots, expectedWinners);
    }

    private void getWTest(Collection<String> candidates, Collection<List<String>> rawBallots, HashSet<String> expectedWinners) {
        Contest contest = new Contest(candidates, rawBallots);
        HashSet<String> winners = contest.getWinner();
        assertEquals(expectedWinners, winners);
    }
}