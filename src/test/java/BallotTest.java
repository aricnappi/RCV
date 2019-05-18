import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BallotTest {

    private static Stream<ArrayList<String>> getNumberRankedArgs() {
        return Stream.of(
                new ArrayList<String>(List.of("red", "green", "blue")),
                new ArrayList<String>(List.of("red", "green", "blue", "yellow")),
                new ArrayList<String>(List.of("red", "green")),
                new ArrayList<String>(List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("getNumberRankedArgs")
    void getNumberRanked(ArrayList<String> rankedChoices) {
        Ballot ballot = new Ballot(rankedChoices);
        assertEquals(rankedChoices.size(), ballot.getNumberRanked());
    }

    private static Stream<Arguments> getVoteArgs() {
        return Stream.of(
                Arguments.of(new ArrayList<String>(List.of("red", "green", "blue")), new ArrayList<String>(List.of("green", "blue"))),
                Arguments.of(new ArrayList<String>(List.of("green", "blue", "red")), new ArrayList<String>(List.of("green", "blue"))),
                Arguments.of(new ArrayList<String>(List.of("blue", "red", "green")), new ArrayList<String>(List.of("green"))),
                Arguments.of(new ArrayList<String>(List.of("green", "blue")), new ArrayList<String>(List.of("green", "red")))
        );
    }

    @ParameterizedTest
    @MethodSource("getVoteArgs")
    void getVote(ArrayList<String> rankedChoices, ArrayList<String> activeCandidates) {
        Ballot ballot = new Ballot(rankedChoices);
        String vote = ballot.getVote(activeCandidates);
        assertEquals("green", vote);
    }
}