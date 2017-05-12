package core;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import config.ShooterConfig;

public class Shooter {
	
	CANTalon shooterTalon = new CANTalon(ShooterConfig.shooterTalonChn);
	
	public Shooter() {
		/*
		shooterTalon.changeControlMode(TalonControlMode.Speed);
		shooterTalon.setPID(ShooterConfig.kP, ShooterConfig.kI, ShooterConfig.kD, ShooterConfig.kD, 0, ShooterConfig.rampRate, 0);
		shooterTalon.enableControl();
		*/
		shooterTalon.changeControlMode(TalonControlMode.PercentVbus);
	}
	
	public void startShooter() {
		shooterTalon.set(ShooterConfig.shooterSpeed);
	}
	
	public void stopShooter() {
		shooterTalon.set(0);
	}
}
