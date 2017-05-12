package auto;

import config.AutoConfig;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoChooser {
	private static SendableChooser autoChooser = new SendableChooser();
	
	public static void init() {
		autoChooser.addDefault("Do Nothing", AutoConfig.doNothingId);
		autoChooser.addObject("MiddleGear", AutoConfig.middleGearId);
		autoChooser.addObject("Shooter", AutoConfig.shooterId);
		
		SmartDashboard.putData("Auto Chooser", autoChooser);
	}
	
	public static int getAutoId() {
		return (int) autoChooser.getSelected();
	}
}
