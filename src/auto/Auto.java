package auto;

import edu.wpi.first.wpilibj.Timer;
import vision.VisionCore;
import config.AutoConfig;
import core.subsystems.Drive;
import core.subsystems.Gear;

public class Auto {
	private Drive drive;
	private Gear gear;
	private VisionCore vision;
	
	public Auto() {
		drive = Drive.getInstance();
		gear = Gear.getInstance();
		vision = VisionCore.getInstance();
	}
	
	public void init() {
		int autoId = AutoChooser.getAutoId();
	}
	
	public void run() {
		vision.update();
		drive.motionProfileMode();
	}
}
