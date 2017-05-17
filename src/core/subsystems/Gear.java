package core.subsystems;

import config.GearConfig;
import util.loops.Loop;
import  edu.wpi.first.wpilibj.Servo;

public class Gear {
	private Servo servoLeft = new Servo(GearConfig.leftChn);
	private Servo servoRight = new Servo(GearConfig.rightChn);

	// Singleton
	private static Gear instance = new Gear();
	public static Gear getInstance() {
		return  instance;
	}

	private Gear() {
		open();
	}

	public enum GearControlStates {
		OPEN, CLOSED
	}

	private GearControlStates gearControlState = GearControlStates.CLOSED;

	private final Loop loop = new Loop() {

		@Override
		public void onStart() {

		}

		@Override
		public void onLoop() {
			switch (gearControlState) {
				case OPEN:
					open();
					break;
				case CLOSED:
					close();
					break;
			}
		}

		@Override
		public void onStop() {

		}
	};

	public Loop getLoop() {
		return loop;
	}

	public void setGearControlState(GearControlStates state) {
		gearControlState = state;
	}

	private void close(){
		servoLeft.setAngle(GearConfig.gearServoAngleLeftOpen);
		servoRight.setAngle(GearConfig.gearServoAngleRightOpen);
	}
	private void open(){
		servoLeft.setAngle(GearConfig.gearServoAngleLeftClosed);
		servoRight.setAngle(GearConfig.gearServoAngleRightClosed);
	}
}
