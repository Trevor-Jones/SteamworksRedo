package core.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import config.DriveConfig;
import core.MotionProfileFollower;
import core.loops.Loop;
import sensors.MyJoystick;

public class Drive {

	// Singleton
	private static Drive instance = new Drive();
	public static Drive getInstance() {
		return instance;
	}

	private MyJoystick joy = MyJoystick.getInstance();

	private CANTalon leftDriveMaster = new CANTalon(DriveConfig.leftTalonChn1);
	private CANTalon leftDriveSlave = new CANTalon(DriveConfig.leftTalonChn2);
	private CANTalon rightDriveMaster = new CANTalon(DriveConfig.rightTalonChn1);
	private CANTalon rightDriveSlave = new CANTalon(DriveConfig.rightTalonChn2);

	private MotionProfileFollower leftFollower = new MotionProfileFollower(leftDriveMaster);
	private MotionProfileFollower rightFollower = new MotionProfileFollower(rightDriveMaster);

	public enum DriveControlStates {
		OPEN_LOOP_JOY, MOTION_PROFILE, OPEN_LOOP_SET
	}

	private DriveControlStates driveControlState;

	private double xPos, yPos, x, y;
	private double left, right = 0;


	
	private Drive() {
		// Start in open loop mode
		leftDriveMaster.changeControlMode(TalonControlMode.PercentVbus);
		rightDriveMaster.changeControlMode(TalonControlMode.PercentVbus);
		driveControlState = DriveControlStates.OPEN_LOOP_JOY;

		// Set up encoders
		leftDriveMaster.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		rightDriveMaster.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		leftDriveMaster.configEncoderCodesPerRev(DriveConfig.codesPerRev);
		rightDriveMaster.configEncoderCodesPerRev(DriveConfig.codesPerRev);
		leftDriveMaster.reverseSensor(false);
		leftDriveMaster.reverseOutput(true);
		rightDriveMaster.reverseOutput(false);
		rightDriveMaster.reverseSensor(true);

		// Set up PID values
		leftDriveMaster.setP(DriveConfig.kPLeft);
		leftDriveMaster.setI(DriveConfig.kILeft);
		leftDriveMaster.setD(DriveConfig.kDLeft);
		leftDriveMaster.setF(DriveConfig.kFLeft);
		rightDriveMaster.setP(DriveConfig.kPRight);
		rightDriveMaster.setI(DriveConfig.kIRight);
		rightDriveMaster.setD(DriveConfig.kDRight);
		rightDriveMaster.setF(DriveConfig.kFRight);

		// Set slaves
		leftDriveSlave.changeControlMode(TalonControlMode.Follower);
		rightDriveSlave.changeControlMode(TalonControlMode.Follower);
		leftDriveSlave.set(DriveConfig.leftTalonChn1);
		rightDriveSlave.set(DriveConfig.rightTalonChn1);
	}

	// Main control loop, run at 100hz
	private final Loop loop = new Loop() {

		@Override
		public void onStart() {

		}

		@Override
		public void onLoop() {
			switch (driveControlState) {
				case OPEN_LOOP_JOY:
					move(joy.getRTheta());
					break;

				case MOTION_PROFILE:
					leftFollower.control();
					rightFollower.control();

					leftDriveMaster.set(leftFollower.getSetValue().value);
					rightDriveMaster.set(rightFollower.getSetValue().value);
					break;

				case OPEN_LOOP_SET:
					ramp(-left, right);
					break;
			}
		}

		@Override
		public void onStop() {
			openLoopJoyMode();
			move(0,0);
		}
	};

	public Loop getLoop () {
		return loop;
	}

	public void move(double[] rTheta) {
		move(rTheta[0], rTheta[1]);
	}

	public void move(double r, double theta) {

		xPos = r * Math.cos(theta);
		yPos = r * Math.sin(theta);

		x = xPos * Math.abs(xPos);
		y = yPos * Math.abs(yPos);

		double left = y + x;
		double right = y - x;

		if (leftDriveMaster.getControlMode() != TalonControlMode.MotionProfile) {
			ramp(-left, right);
		}
	}

	public int getSetValue() {
		return leftFollower.getSetValue().value;
	}
	
	public void motionProfileMode() {
		driveControlState = DriveControlStates.MOTION_PROFILE;

		leftDriveMaster.changeControlMode(TalonControlMode.MotionProfile);
		leftFollower.control();

		rightDriveMaster.changeControlMode(TalonControlMode.MotionProfile);
		rightFollower.control();
				
		leftDriveMaster.set(leftFollower.getSetValue().value);
		rightDriveMaster.set(rightFollower.getSetValue().value);
	}
	
	public void openLoopJoyMode() {
		driveControlState = DriveControlStates.OPEN_LOOP_JOY;
		resetToOpenLoop();
	}

	public void openLoopSetMode(double left, double right) {
		this.left = left;
		this.right = right;

		if(driveControlState != DriveControlStates.OPEN_LOOP_SET) {
			resetToOpenLoop();
			driveControlState = DriveControlStates.OPEN_LOOP_SET;
		}
	}

	private void resetToOpenLoop() {
		leftDriveMaster.changeControlMode(TalonControlMode.PercentVbus);
		rightDriveMaster.changeControlMode(TalonControlMode.PercentVbus);

		leftFollower.reset();
		rightFollower.reset();
	}
	
	public void startMotionProfile() {
		leftFollower.startMotionProfile();
		rightFollower.startMotionProfile();
	}
	
	public void setPaths(double[][]left, double[][]right) {
		leftFollower.setPoints(left);
		rightFollower.setPoints(right);
	}
	
	public void ramp(double wantSpeedLeft, double wantSpeedRight){
		if(Math.abs(wantSpeedLeft - leftDriveMaster.get()) > DriveConfig.rampRate){
			
			if(wantSpeedLeft > leftDriveMaster.get())
				leftDriveMaster.set((leftDriveMaster.get() +  DriveConfig.rampRate));
			
			else
				leftDriveMaster.set((leftDriveMaster.get() - DriveConfig.rampRate));
			
		}
		
		else {
			leftDriveMaster.set(wantSpeedLeft);
		}
		
		if(Math.abs(wantSpeedRight - rightDriveMaster.get()) > DriveConfig.rampRate){
			
			if(wantSpeedRight > rightDriveMaster.get())
				rightDriveMaster.set(rightDriveMaster.get() +  DriveConfig.rampRate);
			
			else
				rightDriveMaster.set(rightDriveMaster.get() - DriveConfig.rampRate);
			
		}
		
		else {
			rightDriveMaster.set(wantSpeedRight);
		}
	}
}
