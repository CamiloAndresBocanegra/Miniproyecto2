package Game;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    JTextField textLine;
    JTextArea textArea;
    FileManager fileManager;
    LineInputListener listener;
    TimerListener timerListener;
    InitTimerListener initTimerListener;
    Random random;
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
        //Set up JFrame Container's Layout
        //Create Listener Object and Control Object
        //Set up JComponents
        headerProject = new Header("test", Color.BLACK);
        add(headerProject,BorderLayout.NORTH);

        timerListener = new TimerListener();
        timer = new CustomTimer(1000,timerListener);
        random = new Random();
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

    /**
     * inner class that extends an Adapter Class or implements Listeners used by GUI class
     */
    private class LineInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            fileManager.WriteFile(textLine.getText());
            textArea.setText(fileManager.ReadFileInString());
        }
    }

    /**
     * inner class that extends an Adapter Class or implements Listeners used by GUI class
     */
    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            squareColor.setBackground(new Color(
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256)));

            timer.incrementSecond();
            if (timer.getCurrentSecond() >= 7) {
                timer.stop();
                //initTimer.setVisible(true);
                initTimer.setEnabled(true);
                initTimer.addActionListener(timerListener);
            }
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

    /**
     * inner class that extends an Adapter Class or implements Listeners used by GUI class
     */
    private class KeyListener extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            canvas.dibujarParte();
        }
    }
}
