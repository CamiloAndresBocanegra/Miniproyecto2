package Game;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class is used for ...
 * @autor Paola-J Rodriguez-C paola.rodriguez@correounivalle.edu.co
 * @version v.1.0.0 date:21/11/2021
 */
public class Window extends JFrame
{
    //Private:
    Header headerProject;
    Canvas canvas;
    JButton initTimer;
    CustomTimer timer;
    JPanel squareColor;
    JTextField textLineField;
    JTextArea textOutput;
    FileManager fileManager;

    boolean isFirstPass;
    JPanel centerPanel;
    CardLayout cl;
    JPanel levelSelectorPanel;

    LevelSelectorListener levelSelectorListener;
    TimerListener timerListener;
    InitTimerListener initTimerListener;
    Random random;

    LineInputListener lineInputListener;

    //GameState
    int selectedLevel; //might not be necesary
    ArrayList<String> usersFileLines;
    int currentUserIndex;
    int currentMaxLevel;
    int currentTotalWords;

    final int TOTAL_LEVELS = 8;
    final String LEVEL_SELECTOR = "levelselector";
    final String TEXT_OUTPUT = "textoutput";
    //
    /**
     * Constructor of GUI class
     */
    public Window(){
        initWindow();

        setResizable(true);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Window");

//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
        setSize(1360,755);
    }

    /**
     * This method is used to set up the default JComponent Configuration,
     * create Listener and control Objects used for the GUI class
     */
    private void initWindow() {
        fileManager = new FileManager();
        textLineField = new JTextField();
        lineInputListener = new LineInputListener();
        textLineField.addActionListener(lineInputListener);
        textLineField.setFocusable(true);
        add(textLineField, BorderLayout.SOUTH);

        textOutput = new JTextArea();
        textOutput.setFocusable(false);
        // Setting up level selector
        levelSelectorPanel = new JPanel();
        levelSelectorPanel.setLayout(new GridLayout(2,4));
        levelSelectorListener = new LevelSelectorListener();
        for(int i = 0;
            i < TOTAL_LEVELS;
            i++)
        { // Creating level selector buttons
            JButton button = new JButton("Level "+ (i+1));
            button.addActionListener(levelSelectorListener);
            levelSelectorPanel.add(button);
        }
        // Setting up main panel
        centerPanel = new JPanel();
        centerPanel.setLayout(new CardLayout());
        cl = (CardLayout) centerPanel.getLayout();
        add(centerPanel, BorderLayout.CENTER);

        centerPanel.add(levelSelectorPanel, LEVEL_SELECTOR);
        centerPanel.add(textOutput, TEXT_OUTPUT);

        cl.show(centerPanel, TEXT_OUTPUT);

        // end
        textLineField.grabFocus();

//        timerListener = new TimerListener();
//        timer = new CustomTimer(100, timerListener);
//        timer.start();
//        random = new Random();
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
            for(int lineIndex = 0;
                lineIndex < usersFileLines.size();
                lineIndex++)
            {
                String[] userArray = usersFileLines.get(lineIndex).split(";");
                if(lineInput.equals(userArray[0]))
                {
                    textOutput.append("name found\n");
                    currentUserIndex = lineIndex;
                    currentMaxLevel = Integer.parseInt(userArray[1]);

                    cl.show(centerPanel, LEVEL_SELECTOR);
                    return;
                }
            }
            usersFileLines.add(lineInput+ ";1");
            fileManager.writeLine("users.txt", lineInput+";1");
            textOutput.append("not found, name added\n");
            cl.show(centerPanel, LEVEL_SELECTOR);
        }
    }

    /**
     * inner class that extends an Adapter Class or implements Listeners used by GUI class
     */
    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(isFirstPass)
            {
                isFirstPass = false;
                timer.stop();
                return;
            }
//            squareColor.setBackground(new Color(
//                    random.nextInt(256),
//                    random.nextInt(256),
//                    random.nextInt(256)));
//
//            timer.incrementSecond();
//            if (timer.getCurrentSecond() >= 7) {
//                timer.stop();
//                //initTimer.setVisible(true);
//                initTimer.setEnabled(true);
//                initTimer.addActionListener(timerListener);
        }
    }

    private class InitTimerListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            timer.start();
            timer.resetSeconds();
            //initTimer.setVisible(false);
            initTimer.setEnabled(false);
            initTimer.removeActionListener(timerListener);
        }
    }

    private class LevelSelectorListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonText = ((JButton)e.getSource()).getText();
            // this gets the last character of the button text, which is the number of the level.
            selectedLevel = Integer.parseInt(buttonText.substring(buttonText.length() - 1));
            switch(selectedLevel)
            {
                case 1: currentTotalWords = 10;break;
                case 2: currentTotalWords = 20;break;
                case 3: currentTotalWords = 25;break;
                case 4: currentTotalWords = 30;break;
                case 5: currentTotalWords = 35;break;
                case 6: currentTotalWords = 40;break;
                case 7: currentTotalWords = 50;break;
                case 8:
                default: currentTotalWords = 60;break;
            }
            cl.show(centerPanel, TEXT_OUTPUT);
        }
    }

    /**
     * inner class that extends an Adapter Class or implements Listeners used by GUI class
     */
    private class TestListener implements ActionListener {
        int counter = 0;
        @Override
        public void actionPerformed(ActionEvent e)
        {
//            CardLayout cl = (CardLayout)cardsTest.getLayout();
//            String aaa = BUTTONPANEL;
//            if(counter%3 == 0)
//            {
//                cl.show(cardsTest, aaa);
//            }else if(counter%3 == 1)
//            {
//                cl.show(cardsTest, TEXTPANEL);
//            }else{
//                cl.show(cardsTest, SCROLLPANEL);
//            }
//            counter++;
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
