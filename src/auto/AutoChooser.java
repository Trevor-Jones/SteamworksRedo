package auto;

import auto.modes.AutoModeBase;
import auto.modes.DoNothingMode;
import auto.modes.MiddleGearMode;
import config.AutoConfig;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoChooser {
	private static SendableChooser autoChooser = new SendableChooser();
	
	public static void init() {
		autoChooser.addDefault("Do Nothing", AutoConfig.doNothingId);
		autoChooser.addObject("MiddleGear", AutoConfig.middleGearId);
		
		SmartDashboard.putData("AutoModeExecutor Chooser", autoChooser);
	}
	
	private static int getAutoId() {
		return (int) autoChooser.getSelected();
	}

	public static AutoModeBase getAutoMode() {
		switch (getAutoId()) {
			case AutoConfig.doNothingId:
				return new DoNothingMode();

			case AutoConfig.middleGearId:
				return new MiddleGearMode();

			default:
				return new DoNothingMode();
		}
	}
}
