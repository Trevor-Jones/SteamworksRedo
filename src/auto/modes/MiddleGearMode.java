package auto.modes;

import auto.AutoModeEndedException;
import config.MiddleGearDefaultPath;
import auto.actions.DropGearAction;
import auto.actions.FollowPathAction;
import auto.actions.TimedMoveAction;

/**
 * Created by Trevor on 5/16/17.
 */
public class MiddleGearMode extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new FollowPathAction(MiddleGearDefaultPath.leftPath, MiddleGearDefaultPath.rightPath));
        runAction(new DropGearAction());
        runAction(new TimedMoveAction(-.3, -.3, 1));
    }
}
