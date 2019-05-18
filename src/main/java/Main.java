import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("resources/rawBallots.csv"))) {
            ArrayList<List<String>> rawBallots = new ArrayList<>();
            // First line is list of candidate names
            String line = br.readLine();
            String[] candidatesArr = line.split(",");
            List<String> candidates = Arrays.asList(candidatesArr);
            br.readLine();  // skip blank line
            // read in first raw ballot line
            line = br.readLine();
            while (line != null) {
                // Split line along commas
                String[] values = line.split(",");
                // Create list of ranked choices/raw ballot
                List<String> rawBallot = Arrays.asList(values);
                // add raw ballot to list of raw ballots
                rawBallots.add(rawBallot);
                // read next raw ballot line in
                line = br.readLine();
            }
            // Initialize a new contest
            Contest contest = new Contest(candidates, rawBallots);
            // Report the winner(s)
            System.out.println(contest.getWinner());
            System.out.println();
            // Report the distributions of the votes over the rounds of the contest
            System.out.println(contest.getVoteDistributions());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
