package core;

import config.IntakeConfig;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

/**
 * 
 * @author Ajay 
 *
 */

public class Intake {
	private CANTalon intakeTalon = new CANTalon(IntakeConfig.chnIntake);
	
	public Intake() {
		intakeTalon.changeControlMode(TalonControlMode.PercentVbus);
	}
	
	/**
	 * sets the intake motor to a constant in IntakeConfig
	 */
	public void intakeStart(){
		intakeTalon.set(IntakeConfig.intakeSpeed);
	}
	
	/**
	 * stops intake motor
	 */
	public void intakeStop(){
		intakeTalon.set(0);
	}

	/**
	 * returns intake speed
	 * @return
	 */
	public double getIntakeSpeed(){
		return intakeTalon.getSpeed();
	}
	
	public void intakeToggle(){
		if(intakeTalon.get() == 0){
			intakeStart();
		}
		
		else{
			intakeStop();
		}
	}
}
