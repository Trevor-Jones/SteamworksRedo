package core;

import com.ctre.CANTalon.TalonControlMode;

import config.ClimberConfig;
import edu.wpi.first.wpilibj.Timer;

public class Test {
	private Timer timer = new Timer();
	private Drive drive;
	private Intake intake;
	private Climber climber;
	private Gear gear;
	private int counter = 0;
	
	public Test(Drive drive, Intake intake, Climber climber, Gear gear) {
		this.drive = drive;
		this.intake = intake;
		this.climber = climber;
		this.gear = gear;
	}
	
	public void init() {
		timer.reset();
		timer.start();
	}
	
	public void run() {
		drive.driveMode();
		
		switch (counter) {
		case 0:
			drive.leftDrive1.set(.5);
			
			if(timer.get() > 1) {
				timer.reset();
				timer.start();
				counter++;
			}
			break;
		case 1:
			drive.leftDrive1.set(0);
			drive.rightDrive1.set(.5);
			
			if(timer.get() > 1) {
				timer.reset();
				timer.start();
				counter++;
			}
			break;
		case 2:
			gear.open();
			drive.rightDrive1.set(0);
			
			if(timer.get() > 1) {
				timer.reset();
				timer.start();
				counter++;
			}
			break;
		case 3:
			gear.close();
			
			if(timer.get() > 1) {
				timer.reset();
				timer.start();
				counter++;
			}
			break;
		case 4:
			intake.intakeStart();
			
			if(timer.get() > 1) {
				timer.reset();
				timer.start();
				counter++;
			}
			break;
		case 5:
			intake.intakeStop();
			climber.climberTalonOne.set(ClimberConfig.climbSpeed);
			
			if(timer.get() > 1) {
				timer.reset();
				timer.start();
				counter++;
			}
			break;
		case 6:
			climber.climberTalonOne.set(0);
			
			if(timer.get() > 1) {
				timer.reset();
				timer.start();
				counter++;
			}
			break;
		case 7:
			climber.climberTalonTwo.set(1);
			
			if(timer.get() > 1) {
				timer.reset();
				timer.start();
				counter++;
			}
			break;
		case 8:
			climber.climberTalonTwo.set(0);
			
			if(timer.get() > 1) {
				timer.reset();
				timer.start();
				counter++;
			}
			break;
		case 9:
			drive.move(1, Math.PI);
			
			if(timer.get() > 1) {
				timer.reset();
				timer.start();
				counter++;
			}
			break;
		case 10:
			drive.move(0, 0);
			counter++;
			break;
		default:
			break;
		}
	}
	
}
