package auto.actions;

import core.subsystems.Drive;
import edu.wpi.first.wpilibj.Timer;

/**
 * Created by Trevor on 5/16/17.
 */
public class TimedMoveAction implements Action {

    private final Drive drive = Drive.getInstance();
    private final Timer timer = new Timer();
    double left, right, time;

    public TimedMoveAction(double left, double right, double time) {
        this.left = left;
        this.right = right;
        this.time = time;
    }

    @Override
    public boolean isFinished() {
        if(timer.get() > time) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void done() {
        drive.openLoopSetMode(0,0);
    }

    @Override
    public void start() {
        timer.start();
        drive.openLoopSetMode(left, right);
    }
}
