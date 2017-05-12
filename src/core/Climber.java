package core;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import config.ClimberConfig;

public class Climber {
	
	CANTalon climberTalonOne = new CANTalon(ClimberConfig.climberChnOne);
	CANTalon climberTalonTwo = new CANTalon(ClimberConfig.climberChnTwo);
	
	public Climber() {
		climberTalonOne.changeControlMode(TalonControlMode.PercentVbus);
		climberTalonTwo.changeControlMode(TalonControlMode.PercentVbus);
	}
	
	public void startClimber() {
		climberTalonOne.set(ClimberConfig.climbSpeed);
		climberTalonTwo.set(ClimberConfig.climbSpeed);
	}
	
	public void startClimberSlow() {
		climberTalonOne.set(0.3);
		climberTalonTwo.set(0.3);
	}
	
	public void stopClimber() {
		climberTalonOne.set(0);
		climberTalonTwo.set(0);
	}
	
	
}
