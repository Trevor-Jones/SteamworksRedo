package auto.actions;

import core.subsystems.Drive;

/**
 * Created by Trevor on 5/16/17.
 */
public class FollowPathAction implements Action {

    private final double[][] leftPath, rightPath;
    private final Drive drive = Drive.getInstance();

    public FollowPathAction(double[][] leftPath, double[][] rightPath) {
        this.leftPath = leftPath;
        this.rightPath = rightPath;
    }

    @Override
    public boolean isFinished() {
        if(drive.getSetValue() == 2) { // 2 is value when profile is finished executing
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
        drive.openLoopJoyMode();
    }

    @Override
    public void start() {
        drive.setPaths(leftPath, rightPath);
        drive.motionProfileMode();
        drive.startMotionProfile();
    }
}
