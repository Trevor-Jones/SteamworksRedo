package core;

import config.GearConfig;
import  edu.wpi.first.wpilibj.Servo;

public class Gear {
	private Servo servoLeft = new Servo(GearConfig.leftChn);
	private Servo servoRight = new Servo(GearConfig.rightChn);
	private boolean open = false;
	
	public Gear() {
		open();
	}
	
	public void open(){
		servoLeft.setAngle(GearConfig.gearServoAngleLeftOpen);
		servoRight.setAngle(GearConfig.gearServoAngleRightOpen);
		open = true;
	}
	public void close(){
		servoLeft.setAngle(GearConfig.gearServoAngleLeftClosed);
		servoRight.setAngle(GearConfig.gearServoAngleRightClosed);
		open = false;
	}
	public void toggle(){
		if (!open){
			open();	
		}
		else
			close();
	}
}
