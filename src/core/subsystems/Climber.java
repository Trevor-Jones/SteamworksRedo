package core.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import config.ClimberConfig;
import core.loops.Loop;

public class Climber {
	
	private CANTalon climberTalonOne = new CANTalon(ClimberConfig.climberChnOne);
	private CANTalon climberTalonTwo = new CANTalon(ClimberConfig.climberChnTwo);

	public enum ClimberControlStates {
		SLOW_MODE, FAST_MODE, NOT_CLIMBING
	}

	private ClimberControlStates climberControlState = ClimberControlStates.NOT_CLIMBING;

	private static Climber instance = new Climber();
	public static Climber getInstance() {
		return instance;
	}

	private final Loop loop = new Loop() {

		@Override
		public void onStart() {

		}

		@Override
		public void onLoop() {
			switch(climberControlState) {
				case NOT_CLIMBING:
					stopClimber();
					break;
				case SLOW_MODE:
					startClimberSlow();
					break;
				case FAST_MODE:
					startClimber();
					break;
			}
		}

		@Override
		public void onStop() {
			stopClimber();
		}
	};

	public Loop getLoop() {
		return loop;
	}

	private Climber() {
		climberTalonOne.changeControlMode(TalonControlMode.PercentVbus);
		climberTalonTwo.changeControlMode(TalonControlMode.PercentVbus);
	}

	public void setClimberState(ClimberControlStates state) {
		climberControlState = state;
	}
	
	private void startClimber() {
		climberTalonOne.set(ClimberConfig.climbSpeed);
		climberTalonTwo.set(ClimberConfig.climbSpeed);
	}
	
	private void startClimberSlow() {
		climberTalonOne.set(0.3);
		climberTalonTwo.set(0.3);
	}
	
	private void stopClimber() {
		climberTalonOne.set(0);
		climberTalonTwo.set(0);
	}
}
