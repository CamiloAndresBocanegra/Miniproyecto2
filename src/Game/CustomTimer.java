package Game;

import javax.swing.Timer;
import java.awt.event.ActionListener;

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
