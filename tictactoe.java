import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class tictactoe {
     // Main GUI component
    private JFrame frame;  // Main game window
    private JPanel masterPanel, introPanel, gamePanel, titlePanel, gridPanel, bottomPanel;
    private JButton[] buttons;  // Buttons for the Tic Tac Toe grid
    private JLabel titleLabel, scoreLabel;
    private boolean isXTurn = true; // X starts the game
    private boolean isGameOver = false;
    private String aiDifficulty = "Easy"; // AI difficulty level
    private boolean isPvAI = false; // Game mode
    private int xWins = 0, oWins = 0, draws = 0;
   // Constructor to initialize the game
    public tictactoe() {
        createGUI();
    }
  // Method to create and set up the GUI
    private void createGUI() {
        frame = new JFrame("Tic Tac Toe");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        masterPanel = new JPanel(new CardLayout());
        introPanel = createIntroPanel();
        gamePanel = createGamePanel();

        masterPanel.add(introPanel, "Intro");
        masterPanel.add(gamePanel, "Game");

        frame.add(masterPanel);
        frame.setVisible(true);
    }
   // Create the introduction panel with buttons for game mode selection
    
    private JPanel createIntroPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel welcomeLabel = new JLabel("Welcome to Tic Tac Toe!");
        welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 36));
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20, 0, 20, 0);
        panel.add(welcomeLabel, c);

        JButton pvpButton = new JButton("Player vs Player");
        pvpButton.setFont(new Font("Serif", Font.PLAIN, 24));
        pvpButton.addActionListener(e -> startGame(false));
        c.gridy = 1;
        panel.add(pvpButton, c);

        JLabel aiLabel = new JLabel("Choose AI Difficulty:");
        aiLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        c.gridy = 2;
        panel.add(aiLabel, c);

        JPanel difficultyPanel = new JPanel();
        JButton easyButton = new JButton("Easy");
        JButton hardButton = new JButton("Hard");

        easyButton.setFont(new Font("Serif", Font.PLAIN, 20));
        hardButton.setFont(new Font("Serif", Font.PLAIN, 20));

        easyButton.addActionListener(e -> {
            aiDifficulty = "Easy";
            startGame(true);
        });

        hardButton.addActionListener(e -> {
            aiDifficulty = "Hard";
            startGame(true);
        });

        difficultyPanel.add(easyButton);
        difficultyPanel.add(hardButton);

        c.gridy = 3;
        panel.add(difficultyPanel, c);

        return panel;
    }
   // Create the introduction panel with buttons for game mode selection
    
    private JPanel createGamePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createTitlePanel(), BorderLayout.NORTH);
        panel.add(createGridPanel(), BorderLayout.CENTER);
        panel.add(createBottomPanel(), BorderLayout.SOUTH);
        return panel;
    }
    // Create the Tic Tac Toe grid (3x3 buttons)
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        titleLabel = new JLabel("X's Turn");
        titleLabel.setFont(new Font("Serif", Font.PLAIN, 36));
        scoreLabel = new JLabel("X Wins: 0 | O Wins: 0 | Draws: 0");
        scoreLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        panel.add(titleLabel);
        panel.add(scoreLabel);
        return panel;
    }

    // Bottom panel with a back button to return to the intro screen
    private JPanel createGridPanel() {
        gridPanel = new JPanel(new GridLayout(3, 3));
        buttons = new JButton[9];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Serif", Font.PLAIN, 56));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(new ButtonClickListener(i));
            gridPanel.add(buttons[i]);
        }
        return gridPanel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Serif", Font.PLAIN, 24));
        backButton.addActionListener(e -> switchToIntroPanel());
        panel.add(backButton);
        return panel;
    }
// Switch to the intro panel
    private void switchToIntroPanel() {
        CardLayout cl = (CardLayout) masterPanel.getLayout();
        cl.show(masterPanel, "Intro");
    }
 // Start a new game and switch to the game panel
    private void startGame(boolean isPvAI) {
        this.isPvAI = isPvAI;
        resetGame();
        CardLayout cl = (CardLayout) masterPanel.getLayout();
        cl.show(masterPanel, "Game");
    }
// Reset the game to its initial state
    private void resetGame() {
        for (JButton button : buttons) {
            button.setText("");
            button.setEnabled(true);
        }
        isGameOver = false;
        isXTurn = true;
        titleLabel.setText("X's Turn");
    }
// Button click listener for grid buttons
    private class ButtonClickListener implements ActionListener {
        private final int index;

        public ButtonClickListener(int index) {
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isGameOver) return;

            JButton clickedButton = buttons[index];
            if (clickedButton.getText().isEmpty()) {
                clickedButton.setText(isXTurn ? "X" : "O");
                clickedButton.setEnabled(false);

                if (checkWinner(isXTurn ? "X" : "O")) {
                    handleGameEnd((isXTurn ? "X" : "O") + " Wins!");
                } else if (isDraw()) {
                    handleGameEnd("It's a Draw!");
                } else {
                    isXTurn = !isXTurn;
                    titleLabel.setText((isXTurn ? "X's" : "O's") + " Turn");

                    if (!isXTurn && isPvAI) {
                        aiMove();
                    }
                }
            }
        }
    }

    private boolean checkWinner(String player) {
        int[][] winConditions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6}             // Diagonals
        };

        for (int[] condition : winConditions) {
            if (buttons[condition[0]].getText().equals(player) &&
                buttons[condition[1]].getText().equals(player) &&
                buttons[condition[2]].getText().equals(player)) {
                return true;
            }
        }
        return false;
    }

    private char[][] getGridState() {
        char[][] grid = new char[3][3];
        for (int i = 0; i < 9; i++) {
            grid[i / 3][i % 3] = buttons[i].getText().isEmpty() ? ' ' : buttons[i].getText().charAt(0);
        }
        return grid;
    }

    private boolean isDraw() {
        for (JButton button : buttons) {
            if (button.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }
   // Remaining code includes AI logic, winner checking, and other methods

    private void handleGameEnd(String result) {
        isGameOver = true;
        titleLabel.setText(result);

        for (JButton button : buttons) {
            button.setEnabled(false);
        }

        if (result.contains("X Wins")) xWins++;
        else if (result.contains("O Wins")) oWins++;
        else draws++;

        updateScore();

        Timer timer = new Timer(2000, e -> resetGame());
        timer.setRepeats(false);
        timer.start();
    }

    private void updateScore() {
        scoreLabel.setText("X Wins: " + xWins + " | O Wins: " + oWins + " | Draws: " + draws);
    }

    private void aiMove() {
        int bestMove = findBestMove();
        buttons[bestMove].setText("O");
        buttons[bestMove].setEnabled(false);

        if (checkWinner("O")) {
            handleGameEnd("O Wins!");
        } else if (isDraw()) {
            handleGameEnd("It's a Draw!");
        } else {
            isXTurn = true;
            titleLabel.setText("X's Turn");
        }
    }

    private int findBestMove() {
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().isEmpty()) {
                buttons[i].setText("O");
                if (checkWinner("O")) {
                    buttons[i].setText("");
                    return i;
                }
                buttons[i].setText("");
            }
        }

        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().isEmpty()) {
                buttons[i].setText("X");
                if (checkWinner("X")) {
                    buttons[i].setText("");
                    return i;
                }
                buttons[i].setText("");
            }
        }

        int index;
        do {
            index = (int) (Math.random() * 9);
        } while (!buttons[index].getText().isEmpty());
        return index;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(tictactoe::new);
    }
}
