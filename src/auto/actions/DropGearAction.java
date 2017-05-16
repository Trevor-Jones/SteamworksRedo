package auto.actions;

import config.GearConfig;
import core.subsystems.Gear;
import edu.wpi.first.wpilibj.Timer;


/**
 * Created by Trevor on 5/16/17.
 */
public class DropGearAction implements Action {
    private final Gear gear = Gear.getInstance();
    private Timer timer = new Timer();

    @Override
    public boolean isFinished() {
        if(timer.get() > GearConfig.dropOffTime) {
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

    }

    @Override
    public void start() {
        timer.start();
        gear.setGearControlState(Gear.GearControlStates.OPEN);
    }
}
