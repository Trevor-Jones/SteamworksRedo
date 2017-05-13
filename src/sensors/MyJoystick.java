package sensors;

import config.JoyConfig;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Adds functions to assist in joystick use
 * 
 * @author Trevor
 *
 */
public class MyJoystick extends Joystick {

	private boolean[] lastButtonState;
	private boolean[] buttonState;

	private static MyJoystick instance = new MyJoystick(JoyConfig.portNum);

	public static MyJoystick getInstance() {
		return instance;
	}

	/**
	 * Creates joystick at given port
	 * 
	 * @param portNumber
	 */
	private MyJoystick(int portNumber) {
		super(portNumber);

		lastButtonState = new boolean[JoyConfig.maxButtons];
		buttonState = new boolean[JoyConfig.maxButtons];

		for (int i = 0; i < JoyConfig.maxButtons; i++) {
			lastButtonState[i] = false;
			buttonState[i] = false;
		}
	}

	/**
	 * Updates the button values for the controller
	 */
	public void update() {
		for (int i = 0; i < JoyConfig.maxButtons; i++) {
			buttonState[i] = !lastButtonState[i] && super.getRawButton(i + 1);
			lastButtonState[i] = super.getRawButton(i + 1);
		}
	}

	/**
	 * Calculates joystick position in polar coordinates
	 * 
	 * @return magnitude and angle of joystick
	 */
	public double[] getRTheta() {
		double x = this.getRawRightX()*-1;
		double y = this.getRawLeftY();
		
//		System.out.println("Right X: " + x + "\tLeftY: " + y);
		
		double r = Math.sqrt((x * x) + (y * y));
		double theta = Math.atan2(y, x);

		double[] rTheta = { r, theta };

//		System.out.println("Y: " + y + "\t\tX: " + x);
		
		return rTheta;
		
	}

	/**
	 * Gives button value
	 * 
	 * @param button the button number on the controller
	 * @return button value
	 */
	public boolean getButton(int button) {
		return buttonState[button- 1];
	}

	/**
	 * Gets the x value of the left joystick
	 * 
	 * @return the x value of the left joystick
	 */
	public double getRawLeftX() {
		return super.getRawAxis(JoyConfig.chnLeftX);
	}

	/**
	 * Gets the y value of the left joystick
	 * 
	 * @return the y value of the left joystick
	 */
	public double getRawLeftY() {
		return super.getRawAxis(JoyConfig.chnLeftY);
	}

	/**
	 * Gets the x value of the right joystick
	 * 
	 * @return the x value of the right joystick
	 */
	public double getRawRightX() {
		return super.getRawAxis(JoyConfig.chnRightX);
	}

	/**
	 * Gets the y value of the right joystick
	 * 
	 * @return the y value of the right joystick
	 */
	public double getRawRightY() {
		return super.getRawAxis(JoyConfig.chnRightY);
	}
	
	public boolean getDpadUp() {
    	return super.getPOV(0) == 0;
    }
    
    /**
     * Checks if the dpad is pressed in the right direction 
     * @return is the dpad is pressed
     */
    public boolean getDpadRight() {
    	return super.getPOV(0) == 90;
    }

    /**
     * Checks if the dpad is pressed in the down direction 
     * @return is the dpad is pressed
     */
    public boolean getDpadDown(){
    	return super.getPOV(0) == 180;
    }
    
    /**
     * Checks if the dpad is pressed in the left direction 
     * @return is the dpad is pressed
     */
    public boolean getDpadLeft(){
    	return super.getPOV(0) == 270;
    }
}