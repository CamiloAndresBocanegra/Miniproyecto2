package Game;

import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class CustomTimer extends Timer {
    int currentSeconds;

    public CustomTimer(int timerMiliseconds, ActionListener listener)
    {
        super(timerMiliseconds, listener);
        currentSeconds = 0;
    }

    public int getCurrentSecond()
    {
        return currentSeconds;
    }
    public void incrementSecond()
    {
        currentSeconds++;
    }
    public void resetSeconds()
    {
        currentSeconds = 0;
    }

}
