package auto;

import auto.modes.AutoModeBase;
import auto.modes.DoNothingMode;
import auto.modes.MiddleGearMode;
import config.AutoConfig;

public class AutoModeExecutor {
	private AutoModeBase autoMode;
	private Thread thread = null;
	
	public void setModeFromDash() {
		int autoId = AutoChooser.getAutoId();

		switch (autoId) {
			case AutoConfig.doNothingId:
				autoMode = new DoNothingMode();
				break;

			case AutoConfig.middleGearId:
				autoMode = new MiddleGearMode();
				break;
		}
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					if (autoMode != null) {
						autoMode.run();
					}
				}
			});
			thread.start();
		}

	}

	public void stop() {
		if (autoMode != null) {
			autoMode.stop();
		}
		thread = null;
	}
}
