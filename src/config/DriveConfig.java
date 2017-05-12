package config;

public class DriveConfig {
	public static int leftTalonChn1 = 4;
	public static int leftTalonChn2 = 3;
	public static int rightTalonChn1 = 2; // 2
	public static int rightTalonChn2 = 8; // 1
	public static final double rampRate = 0.2;
	public static final int codesPerRev = 19;
	
	public static final double kPLeft = 0.12;
	public static final double kILeft = 0;
	public static final double kDLeft = 0;
	public static final double kFLeft = .8; //.99
	public static final double kPRight = 0.12;
	public static final double kIRight = 0;
	public static final double kDRight = 0;
	public static final double kFRight = .7;
}
