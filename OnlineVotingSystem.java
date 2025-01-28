# JavaAssignmentimport javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Candidate {
    private final String name;
    private int votes;

    public Candidate(String name) {
        this.name = name;
        this.votes = 0;
    }

    public String getName() {
        return name;
    }

    public int getVotes() {
        return votes;
    }

    public void addVote() {
        votes++;
    }
}

public class OnlineVotingSystem {
    private final List<Candidate> candidates = new ArrayList<>();
    private final Map<String, Boolean> voters = new HashMap<>();

    private JFrame frame;
    private JTextField voterNameField;
    private ButtonGroup candidateGroup;
    private JLabel messageLabel;

    public OnlineVotingSystem() {
        // Add candidates to the list
        candidates.add(new Candidate("LEAH"));
        candidates.add(new Candidate("ELIAH"));
        candidates.add(new Candidate("ELIZABETH"));

        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Online Voting System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 1));

        JLabel welcomeLabel = new JLabel("Welcome to the Online Voting System!", SwingConstants.CENTER);
        topPanel.add(welcomeLabel);

        voterNameField = new JTextField();
        topPanel.add(new JLabel("Enter your name:"));
        topPanel.add(voterNameField);

        frame.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(candidates.size() + 1, 1));
        centerPanel.add(new JLabel("Select a candidate:", SwingConstants.CENTER));

        candidateGroup = new ButtonGroup();
        for (Candidate candidate : candidates) {
            JRadioButton candidateButton = new JRadioButton(candidate.getName());
            candidateButton.setActionCommand(candidate.getName());
            candidateGroup.add(candidateButton);
            centerPanel.add(candidateButton);
        }

        frame.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(3, 1));

        JButton voteButton = new JButton("Vote");
        voteButton.addActionListener(new VoteButtonListener());
        bottomPanel.add(voteButton);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        bottomPanel.add(messageLabel);

        JButton resultsButton = new JButton("Show Results");
        resultsButton.addActionListener(e -> displayResults());
        bottomPanel.add(resultsButton);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private class VoteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String voterName = voterNameField.getText().trim();

            if (voterName.isEmpty()) {
                messageLabel.setText("Please enter your name.");
                return;
            }

            if (voters.containsKey(voterName)) {
                messageLabel.setText("You have already voted! Thank you.");
                return;
            }

            String selectedCandidate = candidateGroup.getSelection() != null ? candidateGroup.getSelection().getActionCommand() : null;

            if (selectedCandidate == null) {
                messageLabel.setText("Please select a candidate.");
                return;
            }

            for (Candidate candidate : candidates) {
                if (candidate.getName().equals(selectedCandidate)) {
                    candidate.addVote();
                    break;
                }
            }

            voters.put(voterName, true);
            messageLabel.setText("Thank you for voting!");
        }
    }

    private void displayResults() {
        StringBuilder results = new StringBuilder("--- Voting Results ---\n");
        for (Candidate candidate : candidates) {
            results.append(candidate.getName()).append(": ").append(candidate.getVotes()).append(" votes\n");
        }

        Candidate winner = candidates.stream().max((c1, c2) -> Integer.compare(c1.getVotes(), c2.getVotes())).orElse(null);
        if (winner != null) {
            results.append("\nWinner: ").append(winner.getName()).append(" with ").append(winner.getVotes()).append(" votes.");
        }

        JOptionPane.showMessageDialog(frame, results.toString(), "Voting Results", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OnlineVotingSystem::new);
    }
}
