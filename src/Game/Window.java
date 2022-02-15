package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * This class is used for ...
 * @autor Paola-J Rodriguez-C paola.rodriguez@correounivalle.edu.co
 * @version v.1.0.0 date:21/11/2021
 */
public class Window extends JFrame {
    //Private:
    Timer memorizingTimer;
    Timer rememberingTimer;
    MemorizingTimerListener memorizingTimerListener;
    AnswerListener answerListener;
    StartButtonListener StartButtonListener;
    Random random;
    JTextField textLineField;
    FileManager fileManager;

    JLabel infoOutputLabel;
    JPanel centerPanel;
    CardLayout centerCL;
    JButton[] levelButtons;

    JLabel currentWordLabel;
    JButton yesButton;
    JButton noButton;
    JButton skipButton;

    LevelSelectorListener levelSelectorListener;

    LineInputListener lineInputListener;

    //GameState
    int selectedLevel;
    ArrayList<String> usersFileLines;
    int currentUserIndex = 0;
    int currentMaxLevel = 0;
    int totalWordsToRemember = 0;
    int currentMinimumSuccessRate = 0;
    int currentPoints = 0;
    boolean memorizingRound = true;

    String[] completeWordsArray;
    ArrayList<String> availableWordsList;
    ArrayList<String> wordsToRemember;
    ArrayList<String> levelWords;
    ArrayList<String> auxArrayList;

    // constants
    final String USERS_FILE_NAME = "users.txt";
    final String WORDS_FILE_NAME = "words.txt";
    final int TOTAL_LEVELS = 8;
    // center cl strings
    final String LEVEL_SELECTOR = "levelselector";
    final String LOGIN_PANEL = "loginpanel";
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
//        setSize(1360, 755);
    }

    /**
     * This method is used to sekt up the default JComponent Configuration,
     * create Listener and control Objects used for the GUI class
     */
    private void initWindow() {
        // EXIT BUTTON
        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener(new ExitListener());
        exitButton.setFont(new Font(Font.DIALOG,Font.BOLD,30));
        add(exitButton, BorderLayout.SOUTH);

        // Initializing
        answerListener = new AnswerListener();
        memorizingTimerListener = new MemorizingTimerListener();
        memorizingTimer = new Timer(5000, memorizingTimerListener);
        rememberingTimer = new Timer(7000, answerListener);
        random = new Random();
        fileManager = new FileManager();
        infoOutputLabel = new JLabel();
        infoOutputLabel.setText("What's your Username");
        infoOutputLabel.setFont(new Font(Font.DIALOG,Font.BOLD,30));
        infoOutputLabel.setHorizontalAlignment(JLabel.CENTER);

        add(infoOutputLabel, BorderLayout.NORTH);

        // LineField Input
        JPanel loginPanel = new JPanel();
        textLineField = new JTextField();
        lineInputListener = new LineInputListener();
        textLineField.addActionListener(lineInputListener);
        textLineField.setFocusable(true);
        textLineField.setPreferredSize(new Dimension(700, 40));
        textLineField.setFont(new Font(Font.DIALOG,Font.BOLD,36));
        textLineField.setHorizontalAlignment(0);
        loginPanel.add(textLineField, BorderLayout.CENTER);

        // Setting up level selector
        JPanel levelSelectorPanel = new JPanel();
        levelSelectorPanel.setLayout(new GridLayout(2, 4));
        levelSelectorListener = new LevelSelectorListener();
        levelButtons = new JButton[TOTAL_LEVELS];
        for (int i = 0;
             i < TOTAL_LEVELS;
             i++)
        { // Creating level selector buttons
            levelButtons[i] = new JButton("Level " + (i + 1));
            levelButtons[i].addActionListener(levelSelectorListener);
            levelButtons[i].setEnabled(false);
            levelButtons[i].setFont(new Font(Font.DIALOG,Font.BOLD,36));
            levelSelectorPanel.add(levelButtons[i]);
        }

        // Setting up Game Panels
        JButton gameStartButton = new JButton("Start");
        StartButtonListener = new StartButtonListener();
        gameStartButton.addActionListener(StartButtonListener);
        gameStartButton.setFont(new Font(Font.DIALOG,Font.BOLD,36));

        JPanel memorizingPanel = new JPanel(new GridLayout(1, 3));

        yesButton = new JButton("I KNOW THAT WORD");
        yesButton.addActionListener(answerListener);
        yesButton.setFont(new Font(Font.DIALOG,Font.BOLD,36));
        memorizingPanel.add(yesButton);

        currentWordLabel = new JLabel();
        currentWordLabel.setHorizontalAlignment(JLabel.CENTER);
        currentWordLabel.setFont(new Font(Font.DIALOG,Font.BOLD,36));
        JPanel wordPanel = new JPanel(new BorderLayout());
        wordPanel.add(currentWordLabel, BorderLayout.CENTER);

        skipButton = new JButton("Skip Word");
        skipButton.addActionListener(memorizingTimerListener);
        skipButton.setFont(new Font(Font.DIALOG,Font.BOLD,30));
        wordPanel.add(skipButton, BorderLayout.SOUTH);

        memorizingPanel.add(wordPanel, BorderLayout.CENTER);

        noButton = new JButton("I DON'T KNOW THAT WORD");
        noButton.setFont(new Font(Font.DIALOG,Font.BOLD,36));
        noButton.addActionListener(answerListener);
        memorizingPanel.add(noButton);

        // Setting up main panel
        centerPanel = new JPanel();
        centerPanel.setLayout(new CardLayout());
        centerCL = (CardLayout) centerPanel.getLayout();
        add(centerPanel, BorderLayout.CENTER);

        // Adding panels to main panel
        centerPanel.add(loginPanel, LOGIN_PANEL);
        centerPanel.add(levelSelectorPanel, LEVEL_SELECTOR);
        centerPanel.add(gameStartButton, START_BUTTON_PANEL);
        centerPanel.add(memorizingPanel, MEMORIZING_PANEL);
//        centerCL.show(centerPanel, LEVEL_SELECTOR);

        // pass words from array to the arraylist
        completeWordsArray = fileManager.readFileInString(WORDS_FILE_NAME).split("\n");
        availableWordsList = new ArrayList<String>(Arrays.asList(completeWordsArray));
        wordsToRemember = new ArrayList<String>();
        levelWords = new ArrayList<String>();
        auxArrayList = new ArrayList<String>();

        // end
        textLineField.grabFocus();
    }


    /**
     * inner class that extends an Adapter Class or implements Listeners used by GUI class
     */
    private class LineInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            usersFileLines = (fileManager.readFileInArray(USERS_FILE_NAME));
            String lineInput = textLineField.getText();
            textLineField.setText("");
            for (int lineIndex = 0;
                 lineIndex < usersFileLines.size();
                 lineIndex++) {
                String[] userArray = usersFileLines.get(lineIndex).split(";");
                if (lineInput.equals(userArray[0])) {
                    currentUserIndex = lineIndex;
                    currentMaxLevel = Integer.parseInt(userArray[1]);
                    for(int i = 0; i < currentMaxLevel; i++)
                    {
                        levelButtons[i].setEnabled(true);
                    }
                    infoOutputLabel.setText("Select a Level");
                    centerCL.show(centerPanel, LEVEL_SELECTOR);
                    return;
                }
            }
            currentUserIndex = usersFileLines.size();
            usersFileLines.add(lineInput + ";1");
            currentMaxLevel = 1;
            for(int i = 0; i < currentMaxLevel; i++)
            {
                levelButtons[i].setEnabled(true);
            }
            infoOutputLabel.setText("Select a Level");

            centerCL.show(centerPanel, LEVEL_SELECTOR);
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
                    totalWordsToRemember = 10;
                    currentMinimumSuccessRate = 70;
                    break;
                case 2:
                    totalWordsToRemember = 20;
                    currentMinimumSuccessRate = 70;
                    break;
                case 3:
                    totalWordsToRemember = 25;
                    currentMinimumSuccessRate = 75;
                    break;
                case 4:
                    totalWordsToRemember = 30;
                    currentMinimumSuccessRate = 80;
                    break;
                case 5:
                    totalWordsToRemember = 35;
                    currentMinimumSuccessRate = 80;
                    break;
                case 6:
                    totalWordsToRemember = 40;
                    currentMinimumSuccessRate = 85;
                    break;
                case 7:
                    totalWordsToRemember = 50;
                    currentMinimumSuccessRate = 90;
                    break;
                case 8:
                default:
                    totalWordsToRemember = 60;
                    currentMinimumSuccessRate = 90;
                    break;
            }
            memorizingRound = true;
            skipButton.setEnabled(true);
            levelWords.clear();
            wordsToRemember.clear();
            while(levelWords.size() < totalWordsToRemember*2)
            {
                levelWords.add(availableWordsList.remove(random.nextInt(availableWordsList.size())));
            }
            ArrayList<String> tempArray = (ArrayList<String>)levelWords.clone();
            while(wordsToRemember.size() < totalWordsToRemember)
            {
                wordsToRemember.add(tempArray.remove(random.nextInt(tempArray.size())));
            }
            auxArrayList = (ArrayList<String>) wordsToRemember.clone();

            infoOutputLabel.setText("Get Ready To Start Memorizing");
            centerCL.show(centerPanel, START_BUTTON_PANEL);
        }
    }

    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(memorizingRound)
            {
                memorizingTimer.start();
                centerCL.show(centerPanel, MEMORIZING_PANEL);
                yesButton.setEnabled(false);
                noButton.setEnabled(false);
                memorizingTimerListener.actionPerformed(new ActionEvent(memorizingTimer, 0, null));

                infoOutputLabel.setText("Words To Memorize: "+ totalWordsToRemember);
            }else{
                rememberingTimer.start();
                yesButton.setEnabled(true);
                noButton.setEnabled(true);
                answerListener.actionPerformed(new ActionEvent(rememberingTimer, 0, null));
                String infoString = "LeftWords: "+ totalWordsToRemember*2 +", Points: 0";
                infoOutputLabel.setText(infoString);
                centerCL.show(centerPanel, MEMORIZING_PANEL);
            }
        }
    }

    /**
     * inner class that extends an Adapter Class or implements Listeners used by GUI class
     */
    private class MemorizingTimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (memorizingRound)
            {
                if (wordsToRemember.size() > 0) {
                    memorizingTimer.restart();
                    String word = wordsToRemember.remove(random.nextInt(wordsToRemember.size()));
                    currentWordLabel.setText(word);
                    infoOutputLabel.setText("Words to memorize: "+ (wordsToRemember.size()+1));
                } else {
                    memorizingTimer.stop();
                    memorizingRound = false;
                    wordsToRemember = (ArrayList<String>) auxArrayList.clone();
                    infoOutputLabel.setText("Get ready to Start remembering");
                    skipButton.setEnabled(false);
                    centerCL.show(centerPanel, START_BUTTON_PANEL);
                }
            }
        }
    }

    private class AnswerListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {

            String word = currentWordLabel.getText();
            if( (e.getSource()) == yesButton && wordsToRemember.contains(word))
            {
                currentPoints++;
            }else if((e.getSource()) == noButton &&
                    !wordsToRemember.contains(word))
            {
                currentPoints++;
            }
            if (levelWords.size() > 0)
            {
                rememberingTimer.restart();
                word = levelWords.remove(random.nextInt(levelWords.size()));
                currentWordLabel.setText(word);
                String infoString = "LeftWords: "+ (levelWords.size()+1) +", Points: "+ currentPoints;
                infoOutputLabel.setText(infoString);
            }else{ // finish level
                if((currentPoints*100)/(totalWordsToRemember*2) >= currentMinimumSuccessRate)
                {
                    if(selectedLevel == currentMaxLevel && currentMaxLevel<8){
                        currentMaxLevel++;
                        String newUserLine = usersFileLines.get(currentUserIndex);
                        newUserLine = newUserLine.substring(0, newUserLine.length()-1);
                        newUserLine += currentMaxLevel;
                        usersFileLines.set(currentUserIndex, newUserLine);
                        fileManager.writeEntireFile(USERS_FILE_NAME, usersFileLines);
                    }
                }
                infoOutputLabel.setText("Last Score: "+ currentPoints +", Select a Level");
                currentPoints = 0;
                rememberingTimer.stop();
                memorizingRound = true;
                availableWordsList = new ArrayList<String>(Arrays.asList(completeWordsArray));
                centerCL.show(centerPanel, LEVEL_SELECTOR);
                for(int i=0; i<currentMaxLevel; i++)
                {
                    levelButtons[i].setEnabled(true);
                }
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
