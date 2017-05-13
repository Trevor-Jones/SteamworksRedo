package core;
import core.loops.Loop;
import core.subsystems.Climber;
import core.subsystems.Drive;
import core.subsystems.Gear;
import sensors.MyJoystick;
import vision.VisionCore;
import config.JoyConfig;

/**
 * @author Jessie
 * intergrate joystick input;
 */
public class Teleop {
	MyJoystick joy = MyJoystick.getInstance();
	core.subsystems.Drive drive = Drive.getInstance();
	core.subsystems.Gear gear = Gear.getInstance();
	core.subsystems.Climber climber = Climber.getInstance();
	VisionCore vision = VisionCore.getInstance();

	public Teleop(){

	}
	private final Loop loop = new Loop() {

		@Override
		public void onStart() {

		}

		@Override
		public void onLoop() {
			gearTeleop();
			climberTeleop();
			vision.update();
			joy.update();
		}

		@Override
		public void onStop() {

		}
	};

	public Loop getLoop() {
		return loop;
	}
	
	private void climberTeleop() {
		if(joy.getRawButton(JoyConfig.climbButton)) {
			climber.setClimberState(Climber.ClimberControlStates.FAST_MODE);
		} else if(joy.getRawButton(JoyConfig.climbSlowButton)) {
			climber.setClimberState(Climber.ClimberControlStates.SLOW_MODE);
		} else if(joy.getDpadDown()){
			climber.setClimberState(Climber.ClimberControlStates.NOT_CLIMBING);
		}
	}
	
	private void gearTeleop() {
		if(joy.getRawButton(JoyConfig.gearOpenButton)) {
			gear.setGearControlState(Gear.GearControlStates.OPEN);
		} else if(joy.getRawButton(JoyConfig.gearCloseButton)) {
			gear.setGearControlState(Gear.GearControlStates.CLOSED);
		}
	}
}
