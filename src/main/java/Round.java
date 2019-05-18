import java.util.*;
import java.util.stream.Stream;

/**
 * A class representing the vote distributions of a round of a Contest.
 */
public class Round {
    /**
     * The distribution by candidate name of the votes for this Round
     */
    private final HashMap<String, Integer> voteDistribution;

    /**
     * Create a new Round of the Contest.
     * @param piles A list of the Piles of the candidates in the Contest.
     */
    public Round(Collection<Pile> piles) {
        voteDistribution = new HashMap<>(piles.size());
        String name;
        int votes;
        // For each pile
        for (Pile pile : piles) {
            // Get the name of the candidate
            name = pile.getName();
            // Get the number of ballots/votes in the pile
            votes = pile.getTotalBallots();
            // Store the number of votes with the name of the candidate
            voteDistribution.put(name, votes);
        }
    }

    /**
     * Get the number of votes for each candidate this Round.
     * @return A copy of the distribution of the votes over all candidates.
     */
    public HashMap<String, Integer> getVoteDistribution() {
        return (HashMap<String, Integer>) voteDistribution.clone();
    }

    /**
     * Get the number of votes of a particular candidate in this Round. Any
     * candidate not in this Round will report as having zero votes.
     * @param candidate The name of the candidate.
     * @return The total number of votes in this Round for the candidate.
     */
    public int getVotes(String candidate) {
        int votes = 0;  // If the candidate cannot be found in this Round for whatever reason, they have 0 votes
        // If the candidate is in this Round
        if (voteDistribution.containsKey(candidate)) {
            // Get their total number of votes
            votes = voteDistribution.get(candidate);
        }
        // Return vote total
        return votes;
    }

    /**
     * Get the names of the candidates who were not eliminated this Round, i.e.
     * the candidates who did not have the least number of votes.
     * @return The set of the names of the candidates who were not eliminated.
     */
    public HashSet<String> getNonEliminatedCandidates() {
        Collection<Integer> voteTotals = voteDistribution.values();
        int minVotes = voteTotals.stream().min(Comparator.naturalOrder()).get();
        Set<String> candidates = voteDistribution.keySet();
        HashSet<String> nonEliminatedCandidates = new HashSet<>();
        int votes;
        for (String candidate : candidates) {
            votes = voteDistribution.get(candidate);
            if (votes != minVotes) {
                nonEliminatedCandidates.add(candidate);
            }
        }
        return nonEliminatedCandidates;
    }


    /**
     * Get the candidate(s) among the given candidates with the most votes in
     * this Round.
     * @param restriction A (nonempty) collection of candidates.
     * @return The set of top candidates among the given candidates.
     */
    public HashSet<String> findTopCandidates(Collection<String> restriction) {
        if (restriction.isEmpty()) {
            return new HashSet<>(Set.of());
        }
        HashMap<String, Integer> voteDistRestricted = (HashMap<String, Integer>) voteDistribution.clone();
        for (String candidate : voteDistribution.keySet()) {
            if (!restriction.contains(candidate)) {
                voteDistRestricted.remove(candidate);
            }
        }
        Collection<Integer> voteTotals = voteDistRestricted.values();
        int maxVotes = voteTotals.stream().max(Comparator.naturalOrder()).get();
        Set<String> candidates = voteDistRestricted.keySet();
        HashSet<String> topCandidates = new HashSet<>();
        int votes;
        for (String candidate : candidates) {
            votes = voteDistRestricted.get(candidate);
            if (votes == maxVotes) {
                topCandidates.add(candidate);
            }
        }
        return topCandidates;
    }

}
