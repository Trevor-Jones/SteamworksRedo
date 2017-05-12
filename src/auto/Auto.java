package auto;

import edu.wpi.first.wpilibj.Timer;
import vision.VisionCore;
import config.AutoConfig;
import core.Drive;
import core.Gear;
import core.Shooter;

public class Auto {
	public Drive drive;
	public Gear gear;
	public Shooter shooter;
	public VisionCore vision;
	Timer timer = new Timer();
	public int step = 0;
	
	public Auto(Drive drive, Gear gear, Shooter shooter, VisionCore vision) {
		this.drive = drive;
		this.gear = gear;
		this.shooter = shooter;
		this.vision = vision;
	}
	
	public void init() {
		timer.start();
		int autoId = AutoChooser.getAutoId();
		
		switch(autoId) {
		case AutoConfig.doNothingId:
			step = 1000;
			break;
		case AutoConfig.middleGearId:
			step = 0;	
			break;
		case AutoConfig.shooterId:
			step = 500;
			break;
		}
	
	}
	
	public void run() {
		vision.update();
		drive.motionProfileMode();
		
		switch (step) {
		case 0:
			if(timer.get() > 0.2) {
				drive.setPaths(vision.getVisionStruct().getLeftPath(), vision.getVisionStruct().getRightPath());
				drive.startMotionProfile();
				timer.reset();
				timer.stop();
				step++;
			}
			break;

		case 1:
			if(drive.getSetValue() == 2) {
				//gear.close();
				step++;
			}
			break;
		case 500:
			shooter.startShooter();
			timer.reset();
			timer.start();
			step++;
			break;
		/*case 501:
			if(timer.get() > 7) {
				step++;
				shooter.stopShooter();
			}
			break;
		case 502:
			drive.driveMode();
			drive.move(0.7, 2*Math.PI/3);
			if(timer.get() > 1) {
				step++;
			}
			break;
		case 503:
			drive.move(1, Math.PI/2);
			if(timer.get() > 2) {
				step++;
				drive.move(0,0);
			}
			break;*/
		default:
			break;
		}
		
		
	}
}
