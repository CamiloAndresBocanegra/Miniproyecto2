/*
TODO:
    - randomly  pick words from the available words list
    - change word each interval of time
    - record points / words answered correctly
    - unlock levels by finishing
*/
package Game;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.TimerTask;

/**
 * This class is used for ...
 * @autor Paola-J Rodriguez-C paola.rodriguez@correounivalle.edu.co
 * @version v.1.0.0 date:21/11/2021
 */
public class Window extends JFrame {
    //Private:
    Header headerProject;
    Canvas canvas;
    CustomTimer timer;
    TimerListener timerListener;
    TimerButtonListener timerButtonListener;
    Random random;
    JTextField textLineField;
    FileManager fileManager;

    JPanel centerPanel;
    CardLayout centerCL;
    JPanel levelSelectorPanel;
    JButton[] levelButtons;

    JButton gameStartButton;
    JPanel memorizingPanel;
    JPanel squareColor;

    LevelSelectorListener levelSelectorListener;

    LineInputListener lineInputListener;

    //GameState
    int selectedLevel; //might not be necesary
    ArrayList<String> usersFileLines;
    int currentUserIndex;
    int currentMaxLevel;
    int currentTotalWords;
    int currentMinimumSuccessRate;
    int currentPoints;
    boolean memorizingRound;

    String[] completeWordsArray;
    ArrayList<String> availableWordsList;
    ArrayList<String> wordsToRemember;

    final int TOTAL_LEVELS = 8;
    // center cl strings
    final String LEVEL_SELECTOR = "levelselector";
    final String LOGIN_PANEL = "loginpanel";
    // game cl strings
    final String START_BUTTON_PANEL = "startbuttonpanel";
    final String MEMORIZING_PANEL = "memopanel";
    //

    /**
     * Constructor of GUI class
     */
    public Window() {
        initWindow();

        setResizable(true);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Window");

//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
        setSize(1360, 755);
    }

    /**
     * This method is used to set up the default JComponent Configuration,
     * create Listener and control Objects used for the GUI class
     */
    private void initWindow() {
        // EXIT BUTTON
        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener(new ExitListener());
        add(exitButton, BorderLayout.NORTH);

        // Initializing
        timerListener = new TimerListener();
        timer = new CustomTimer(1000, timerListener);
        random = new Random();
        fileManager = new FileManager();

        // LineField Input
        JPanel loginPanel = new JPanel();
        textLineField = new JTextField();
        lineInputListener = new LineInputListener();
        textLineField.addActionListener(lineInputListener);
        textLineField.setFocusable(true);
        textLineField.setPreferredSize(new Dimension(700, 40));
        loginPanel.add(textLineField, BorderLayout.CENTER);

        // Setting up level selector
        levelSelectorPanel = new JPanel();
        levelSelectorPanel.setLayout(new GridLayout(2, 4));
        levelSelectorListener = new LevelSelectorListener();
        levelButtons = new JButton[TOTAL_LEVELS];
        for (int i = 0;
             i < TOTAL_LEVELS;
             i++) { // Creating level selector buttons
            levelButtons[i] = new JButton("Level " + (i + 1));
            levelButtons[i].addActionListener(levelSelectorListener);
            levelButtons[i].setEnabled(false);
            levelSelectorPanel.add(levelButtons[i]);
        }

        // Setting up Game Panels
        gameStartButton = new JButton("Start");
        timerButtonListener = new TimerButtonListener();
        gameStartButton.addActionListener(timerButtonListener);
        memorizingPanel = new JPanel(new GridLayout(1, 3));
        AnswerButtons answerListener = new AnswerButtons();
        JButton yesButton = new JButton("Yes");
        yesButton.addActionListener(answerListener);
        memorizingPanel.add(yesButton);
        JLabel currentWord = new JLabel();
        memorizingPanel.add(currentWord);
        JButton noButton = new JButton("No");
        noButton.addActionListener(answerListener);
        memorizingPanel.add(noButton);

        // Setting up main panel
        centerPanel = new JPanel();
        centerPanel.setLayout(new CardLayout());
        centerCL = (CardLayout) centerPanel.getLayout();
        add(centerPanel, BorderLayout.CENTER);

        // Adding panels to main panel
        centerPanel.add(loginPanel, LOGIN_PANEL); //TODO: Make login panel
        centerPanel.add(levelSelectorPanel, LEVEL_SELECTOR);
        centerPanel.add(gameStartButton, START_BUTTON_PANEL);
        centerPanel.add(memorizingPanel, MEMORIZING_PANEL);
//        centerCL.show(centerPanel, LEVEL_SELECTOR);

        // pass words from array to the arraylist
        completeWordsArray = fileManager.readFileInString("words.txt").split("\n");
        availableWordsList = new ArrayList<String>();
        for (int i = 0;
             i < completeWordsArray.length;
             i++) {
            availableWordsList.add(completeWordsArray[i]);
        }

        // end
        textLineField.grabFocus();
    }


    /**
     * inner class that extends an Adapter Class or implements Listeners used by GUI class
     */
    private class LineInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            usersFileLines = (fileManager.readFileInArray("users.txt"));
            String lineInput = textLineField.getText();
            textLineField.setText("");
            for (int lineIndex = 0;
                 lineIndex < usersFileLines.size();
                 lineIndex++) {
                String[] userArray = usersFileLines.get(lineIndex).split(";");
                if (lineInput.equals(userArray[0])) {
                    currentUserIndex = lineIndex;
                    currentMaxLevel = Integer.parseInt(userArray[1]);
                    for(int i = 0; i < currentMaxLevel; i++) //TODO: maybe extract this in a function
                    {
                        levelButtons[i].setEnabled(true);
                    }

                    centerCL.show(centerPanel, LEVEL_SELECTOR);
                    return;
                }
            }
            usersFileLines.add(lineInput + ";1");
            fileManager.writeLine("users.txt", lineInput + ";1");
            currentUserIndex = usersFileLines.size();
            currentMaxLevel = 1;
            for(int i = 0; i < currentMaxLevel; i++) // TODO: maybe extract this in a function
            {
                levelButtons[i].setEnabled(true);
            }

            centerCL.show(centerPanel, LEVEL_SELECTOR);
        }
    }

    /**
     * inner class that extends an Adapter Class or implements Listeners used by GUI class
     */
    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            timer.incrementSecond();
            int secondsPassed = timer.getCurrentSecond();
            if (memorizingRound) {
                int n = 2;
                if ((secondsPassed % n) == 0) {
                    if (secondsPassed > n * currentTotalWords) {
                        timer.stop();
                        memorizingRound = false;
                        centerCL.show(centerPanel, START_BUTTON_PANEL);
                    } else {
                        //TODO: next word
                    }
                }
            } else {
                int n = 1;
                if ((secondsPassed % n) == 0) {
                    if (secondsPassed > n * currentTotalWords * 2) {
                        timer.stop();
                        memorizingRound = true;
                        centerCL.show(centerPanel, START_BUTTON_PANEL);
                    } else {
                        //TODO: show next word
                    }
                } else {

                }
            }
        }
    }

    private class TimerButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            timer.start();
            timer.resetSeconds();
            centerCL.show(centerPanel, MEMORIZING_PANEL);
        }
    }

    private class LevelSelectorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonText = ((JButton) e.getSource()).getText();
            // this gets the last character of the button text, which is the number of the level.
            selectedLevel = Integer.parseInt(buttonText.substring(buttonText.length() - 1));
            switch(selectedLevel)
            {
                case 1:
                    currentTotalWords = 10;
                    currentMinimumSuccessRate = 70;
                    break;
                case 2:
                    currentTotalWords = 20;
                    currentMinimumSuccessRate = 70;
                    break;
                case 3:
                    currentTotalWords = 25;
                currentMinimumSuccessRate = 75;
                break;
                case 4:
                    currentTotalWords = 30;
                currentMinimumSuccessRate = 80;
                break;
                case 5:
                    currentTotalWords = 35;
                    currentMinimumSuccessRate = 80;
                    break;
                case 6:
                    currentTotalWords = 40;
                    currentMinimumSuccessRate = 85;
                    break;
                case 7:
                    currentTotalWords = 50;
                    currentMinimumSuccessRate = 90;
                    break;
                case 8:
                default:
                    currentTotalWords = 60;
                    currentMinimumSuccessRate = 90;
                    break;
            }
            centerCL.show(centerPanel, START_BUTTON_PANEL); //TODO: get random words from words list

        }
    }

    private class AnswerButtons implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(((JButton)e.getSource()).getText() == "Yes")
            {

            }else{ //TODO: handle cases

            }
        }
    }

    /**
     * inner class that extends an Adapter Class or implements Listeners used by GUI class
     */
    private class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Runtime.getRuntime().exit(0);
        }
    }

    /**
     * Main process of the Java program
     * @param args Object used in order to send input data from command line when
     *             the program is execute by console.
     */
    public static void main(String[] args){
        EventQueue.invokeLater(() -> {
            Window miProjectGUI = new Window();
        });
    }
}
