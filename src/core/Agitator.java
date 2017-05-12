package core;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import config.AgitatorConfig;

public class Agitator {
	
	CANTalon agitatorTalon = new CANTalon(AgitatorConfig.agitatorTalonChn);
	
	public Agitator() {
		agitatorTalon.changeControlMode(TalonControlMode.PercentVbus);
	}
	
	public void startAgitator() {
		agitatorTalon.set(AgitatorConfig.agitatorSpeed);
	}
	
	public void stopAgitator() {
		agitatorTalon.set(0);
	}
	
	
}
