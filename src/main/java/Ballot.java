import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An object holding the ranked choices submitted by a user.
 */
public class Ballot {
    /**
     * An internal collection that holds the candidate names in ranked order.
     */
    private final ArrayList<String> ballotArr;

    /**
     * Create a new Ballot.
     * @param rankedChoices The choices submitted by the user in the order they were ranked.
     */
    public Ballot(List<String> rankedChoices) {
        ballotArr = new ArrayList<>(rankedChoices);
    }

    /**
     * Get the number of candidates that were ranked by the user when they submitted their ballot.
     * @return The number of candidates ranked on the ballot.
     */
    public int getNumberRanked() {
        return ballotArr.size();
    }

    /**
     * Get the name of the active candidate for which this Ballot is voting.
     * @param activeCandidates All of the currently active candidates.
     * @return The name of the highest-ranked, non-eliminated candidate on this Ballot.
     */
    public String getVote(Collection<String> activeCandidates) {
        String vote = "";  // If all candidates have been eliminated, this ballot does not have a vote
        // Get the candidates on the ballot who are active
        ArrayList<String> activeCandidatesOnThisBallot = (ArrayList<String>) ballotArr.clone();
        activeCandidatesOnThisBallot.retainAll(activeCandidates);  // should preserve order
        // If not all candidates on this Ballot have been eliminated
        if (!activeCandidatesOnThisBallot.isEmpty()) {
            // Return the highest-ranked candidate
            vote = activeCandidatesOnThisBallot.get(0);
        }
        // Return result
        return vote;
    }
}
