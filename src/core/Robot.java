package core;

import auto.AutoModeExecutor;
import auto.AutoChooser;
import core.loops.Looper;
import core.subsystems.Climber;
import core.subsystems.Drive;
import core.subsystems.Gear;
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

	Drive drive = Drive.getInstance();
	Gear gear = Gear.getInstance();
	Climber climber = Climber.getInstance();
	VisionCore vision = VisionCore.getInstance();

	Looper looper = new Looper();
	Looper teleopLoop = new Looper();

	AutoModeExecutor autoModeExecutor;
	Teleop teleop = new Teleop();


	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		AutoChooser.init();

		// Register loops for subsystems
		looper.register(drive.getLoop());
		looper.register(gear.getLoop());
		looper.register(climber.getLoop());
		teleopLoop.register(teleop.getLoop());
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the autoModeExecutor name from the text box below the Gyro
	 *
	 * You can add additional autoModeExecutor modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		try {
			looper.start();

			autoModeExecutor = new AutoModeExecutor();
			autoModeExecutor.setModeFromDash();
			autoModeExecutor.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}


	}

	@Override
	public void teleopInit() {
		try {
			looper.start();
			teleopLoop.start();
			drive.openLoopJoyMode();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void disabledInit() {
		try {
			looper.stop();
			teleopLoop.stop();
			autoModeExecutor.stop();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

