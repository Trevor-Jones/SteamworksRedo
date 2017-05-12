package config;

public class PathConfig {
	public static final double dt = 0.02; // How many seconds each segment lasts for
	public static final double maxVel = 120; // inches/second
	public static final double maxAcc = 100; // (inches/second) / second
	public static final int numPointsCircle = 10000; // More means more time but smoother curves, won't converge if too low
	public static final int numPointsTan = 50000; 
	public static final double circleRad = 40; 
	public static final double offsetFromGear = 5; // How many inches from the tape we want to stop
	public static final double robotTrackWidth = 31; // inches between drivebase wheels, from center of wheels

}
