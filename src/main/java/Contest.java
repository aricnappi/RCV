
// TODO: write tests for this class

import java.util.*;

/**
 * A class representing a ranked-choice voting (i.e. instant runoff) contest.
 */
public class Contest {
    /**
     * All of the Rounds of the Contest.
     */
    private final ArrayList<Round> rounds = new ArrayList<>();

    /**
     * All of the Piles corresponding to each of the candidates in the Contest.
     * key = candidate name, value = corresponding pile
     */
    private final HashMap<String, Pile> piles;

    /**
     * The names of the candidates that have not been eliminated yet.
     */
    private HashSet<String> activeCandidates;

    /**
     * The names of all of the candidates in the Contest.
     */
    private final HashSet<String> allCandidates;

    /**
     * Whether the runContest method has already been called once.
     */
    private boolean contestRun = false;

    /**
     * Create and initialize a new RCV Contest.
     * @param candidates The names of all of the candidates to be included in the Contest.
     * @param rawBallots A collection of lists of candidate names in the order they
     *                   were ranked.
     */
    public Contest(Collection<String> candidates, Collection<List<String>> rawBallots) {
        // Store the set of candidates
        allCandidates = new HashSet<>(candidates);
        // Activate all of the candidates
        activeCandidates = new HashSet<>(candidates);
        // Create a Pile for each candidate
        piles = new HashMap<>(candidates.size());
        Pile pile;
        for (String candidate : candidates) {
            // Create the Pile
            pile = new Pile(candidate);
            // Add it to the collection of Piles
            piles.put(candidate, pile);
        }
        // Create the Pile initially containing all of the Ballots
        Pile initialPile = new Pile("__initialPile");
        Ballot ballot;
        // For each raw ballot
        for (List<String> rawBallot : rawBallots) {
            // Make a corresponding Ballot
            ballot = new Ballot(rawBallot);
            // Add it to the initial Pile
            initialPile.addBallot(ballot);
        }
        // Add the initial Pile to the collection of Piles
        piles.put("__initialPile", initialPile);
        // Add __initalPile to the list of candidates (but NOT the list of active candidates)
        allCandidates.add("__initialPile");
    }

    /**
     * Runs the RCV algorithm with the Ballots and candidates given at
     * construction.
     */
    private void runContest() {
        contestRun = true;
        // Distribute the initial Pile of Ballots
        eliminateCandidates();
        Round round;
        do {
            // Start a new Round
            round = createNewRound();
            // Add it to the list of rounds
            rounds.add(round);
            // Update the list of active candidates
            activeCandidates = round.getNonEliminatedCandidates();
            // Eliminate candidates that are no longer active
            eliminateCandidates();
        // Repeat until all candidates have been eliminated
        } while (!activeCandidates.isEmpty());
    }

    /**
     * Creates a new Round of the Contest with the Piles of the active candidates.
     * @return A new Round with the Piles of the active candidates.
     */
    private Round createNewRound() {
        // Get the Piles of the active candidates
        HashSet<Pile> activeCandidatesPiles = new HashSet<>(activeCandidates.size());
        Pile activeCandidatePile;
        // For each active candidate
        for (String activeCandidate : activeCandidates) {
            // Get its Pile
            activeCandidatePile = piles.get(activeCandidate);
            // Add it to the set of Piles of active candidates
            activeCandidatesPiles.add(activeCandidatePile);
        }
        // Create a new Round with these Piles and return it
        return new Round(activeCandidatesPiles);
    }

    /**
     * Redistribute the Ballots of any candidates that are no longer active.
     */
    private void eliminateCandidates() {
        Pile pile;
        // For each candidate
        for (String candidate : allCandidates) {
            // If the candidate has been eliminated, i.e. is no longer active
            if (!activeCandidates.contains(candidate)) {
                // Redistribute the Ballots of its corresponding Pile
                pile = piles.get(candidate);
                redistributeBallots(pile);
            }
        }
    }

    /**
     * Redistributes the Ballots in the given Pile to the Piles of the active
     * candidates.
     * @param pile The Pile whose Ballots are to be redistributed.
     */
    private void redistributeBallots(Pile pile) {
        String vote;
        Pile votePile;
        // For each Ballot in the Pile
        for (Ballot ballot : pile) {
            // Get the candidate for which the Ballot is voting
            vote = ballot.getVote(activeCandidates);
            // If this Ballot is casting a vote
            if (!vote.isEmpty()) {
                // Get the corresponding Pile
                votePile = piles.get(vote);
                // Add the Ballot to the Pile
                votePile.addBallot(ballot);
            }
            // Else the Ballot is ignored
        }
    }

    /**
     * Get the distributions of votes among the candidates for each Round.
     * @return The distributions of votes in each Round ordered by Round.
     */
    public ArrayList<HashMap<String, Integer>> getVoteDistributions() {
        // If the contest has not already been run once
        if (!contestRun) {
            // Run the contest
            runContest();
        }
        ArrayList<HashMap<String, Integer>> voteDistributions = new ArrayList<>(rounds.size());
        HashMap<String, Integer> voteDist;
        // For each Round in the Contest
        for (Round round : rounds) {
            // Get the vote distribution of the Round
            voteDist = round.getVoteDistribution();
            // Add it to the list of vote distributions
            voteDistributions.add(voteDist);
        }
        // Return the list of vote distributions
        return voteDistributions;
    }

    /**
     * Get the winner of the Contest. The winner is the candidate that got the
     * most votes in the last Round. If two or more candidates tie for first in
     * the last Round, the candidate among them that got the most votes in the
     * second-to-last Round is the winner. If there is a tie among them in the
     * second-to-last Round, the process repeats until a single winner is found. If no
     * single winner is found through this process, then they all tie for winner.
     * @return The winner(s) of the Contest. Contains multiple winners in the case of a tie.
     */
    public HashSet<String> getWinner() {
        // If the contest has not already been run once
        if (!contestRun) {
            // Run the contest
            runContest();
        }
        // Initialize the tracker of the top candidates
        HashSet<String> topCandidates = (HashSet<String>) allCandidates.clone();
        // Initialize the Round tracker
        int roundNum = rounds.size() - 1;
        Round currentRound;
        // While there is a tie and there are still Rounds left to check
        while ((topCandidates.size() != 1) && (roundNum >= 0)) {
            // Get the Round currently being analyzed
            currentRound = rounds.get(roundNum);
            // Get the candidates among the top candidates with the most votes
            // in this round; this is the new list of top candidates
            topCandidates = currentRound.findTopCandidates(topCandidates);
            // Set next iteration to consider the next Round
            roundNum--;
        }
        // The set of top candidates has been narrowed down to the winner(s); return them
        return topCandidates;
    }
}
