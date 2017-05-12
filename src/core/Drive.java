package core;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import config.DriveConfig;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {
	CANTalon leftDrive1 = new CANTalon(DriveConfig.leftTalonChn1);
	CANTalon leftDrive2 = new CANTalon(DriveConfig.leftTalonChn2);
	CANTalon rightDrive1 = new CANTalon(DriveConfig.rightTalonChn1);
	CANTalon rightDrive2 = new CANTalon(DriveConfig.rightTalonChn2);
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	
	MotionProfileFollower leftFollower = new MotionProfileFollower(leftDrive1);
	MotionProfileFollower rightFollower = new MotionProfileFollower(rightDrive1);
	
	double xPos, yPos, x, y;
	
	public Drive() {
		leftDrive1.changeControlMode(TalonControlMode.PercentVbus);
		rightDrive1.changeControlMode(TalonControlMode.PercentVbus);
		leftDrive1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		rightDrive1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		leftDrive1.configEncoderCodesPerRev(DriveConfig.codesPerRev);
		rightDrive1.configEncoderCodesPerRev(DriveConfig.codesPerRev);
		leftDrive1.reverseSensor(false);
		leftDrive1.reverseOutput(true);
		rightDrive1.reverseOutput(false);
		rightDrive1.reverseSensor(true);
		leftDrive1.setP(DriveConfig.kPLeft);
		leftDrive1.setI(DriveConfig.kILeft);
		leftDrive1.setD(DriveConfig.kDLeft);
		leftDrive1.setF(DriveConfig.kFLeft);
		rightDrive1.setP(DriveConfig.kPRight);
		rightDrive1.setI(DriveConfig.kIRight);
		rightDrive1.setD(DriveConfig.kDRight);
		rightDrive1.setF(DriveConfig.kFRight);
		
		leftDrive2.changeControlMode(TalonControlMode.Follower);
		rightDrive2.changeControlMode(TalonControlMode.Follower);
		leftDrive2.set(DriveConfig.leftTalonChn1);
		rightDrive2.set(DriveConfig.rightTalonChn1);
	}
	
	public void move(double r, double theta) {
		
		xPos = r*Math.cos(theta);
		yPos = r*Math.sin(theta);
		
		x = xPos * Math.abs(xPos);
		y = yPos * Math.abs(yPos);		
		
		double left = y + x;
		double right = y - x;
		
		if(leftDrive1.getControlMode() != TalonControlMode.MotionProfile) {
			ramp(left, -right);	  //-l +r on comp bot
		}
	}
	
	public void set(double left, double right) {
		leftDrive1.set(left);
		rightDrive1.set(right);
	}
	
	public int getSetValue() {
		return leftFollower.getSetValue().value;
	}
	
	public void motionProfileMode() {		
		leftDrive1.changeControlMode(TalonControlMode.MotionProfile);
		leftFollower.control();

		rightDrive1.changeControlMode(TalonControlMode.MotionProfile);
		rightFollower.control();
				
		leftDrive1.set(leftFollower.getSetValue().value);
		rightDrive1.set(rightFollower.getSetValue().value);

		SmartDashboard.putNumber("leftDriveOutput", leftFollower.getSetValue().value);
		SmartDashboard.putNumber("rightDriveOutput", rightFollower.getSetValue().value);
	}
	
	public void driveMode() {
		leftDrive1.changeControlMode(TalonControlMode.PercentVbus);
		rightDrive1.changeControlMode(TalonControlMode.PercentVbus);
		
		leftFollower.reset();
		rightFollower.reset();
		
		SmartDashboard.putNumber("Right 1 Current", pdp.getCurrent(3));
		SmartDashboard.putNumber("Right 2 Current", pdp.getCurrent(13));
		SmartDashboard.putNumber("Left 3 Current", pdp.getCurrent(14));
		SmartDashboard.putNumber("Left 4 Current", pdp.getCurrent(15));
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
		if(Math.abs(wantSpeedLeft - leftDrive1.get()) > DriveConfig.rampRate){
			
			if(wantSpeedLeft > leftDrive1.get())
				leftDrive1.set((leftDrive1.get() +  DriveConfig.rampRate));
			
			else
				leftDrive1.set((leftDrive1.get() - DriveConfig.rampRate));
			
		}
		
		else {
			leftDrive1.set(wantSpeedLeft);
		}
		
		if(Math.abs(wantSpeedRight - rightDrive1.get()) > DriveConfig.rampRate){
			
			if(wantSpeedRight > rightDrive1.get())
				rightDrive1.set(rightDrive1.get() +  DriveConfig.rampRate);
			
			else
				rightDrive1.set(rightDrive1.get() - DriveConfig.rampRate);
			
		}
		
		else {
			rightDrive1.set(wantSpeedRight);
		}
		SmartDashboard.putNumber("leftDrive", leftDrive1.get());
		SmartDashboard.putNumber("rightDrive", rightDrive1.get());
	}
}
