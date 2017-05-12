package core;

import auto.Auto;
import auto.AutoChooser;
import config.PathConfig;
import edu.wpi.first.wpilibj.IterativeRobot;
import vision.VisionCore;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	Drive drive = new Drive();
	Intake intake = new Intake();
	Gear gear = new Gear();
	Agitator agitator = new Agitator();
	Shooter shooter = new Shooter();
	VisionCore vision = new VisionCore();
	Climber climber = new Climber();
	Auto auto = new Auto(drive, gear, shooter, vision);
	Test test = new Test(drive, intake, climber, gear);
	Teleop teleop = new Teleop(drive, intake, agitator, gear, shooter, vision, climber);
	

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		AutoChooser.init();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		auto.init();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		auto.run();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		teleop.run();
	}

	public void testInit() {
		test.init();
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		test.run();
	}
}

