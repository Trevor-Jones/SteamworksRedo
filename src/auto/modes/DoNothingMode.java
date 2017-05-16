package auto.modes;

import auto.AutoModeEndedException;

/**
 * Created by Trevor on 5/16/17.
 */
public class DoNothingMode extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        System.out.println("Do nothing mode completed");
    }
}
