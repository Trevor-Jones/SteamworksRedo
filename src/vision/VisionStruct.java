package vision;

import org.w3c.dom.Element;

import config.PathConfig;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class VisionStruct {
	
	private double distX, distY, ang = 0;
	private double[][] leftPath = new double[0][0];
	private double[][] rightPath = new double[0][0];
	private int timeMs = 0;
	private String tapeStatus = "none";
	
	public double[][] getLeftPath() {
		SmartDashboard.putNumber("leftPathLenght", leftPath.length);
		return leftPath;
	}
	
	public double[][] getRightPath() {
		SmartDashboard.putNumber("rightPathLenght", rightPath.length);
		return rightPath;
	}
	
	public double getDistX() {
		return distX;
	}
	
	public double getDistY() {
		return distY;
	}
	
	public double ang() {
		return ang;
	}
	
	public int timeMs() {
		return timeMs;
	}
	
	public String tapeStatus() {
		return tapeStatus;
	}
	
	public void update(double distX, double distY, double ang, int timeMs, String tapeStatus) {
		this.distX = distX;
		this.distY = distY;
		this.ang = ang;
		this.timeMs = timeMs;
		this.tapeStatus = tapeStatus;
	}
	
	public void setPaths(double[][] left, double right[][]) {
		leftPath = left;
		rightPath = right;
	}
	
	public void update(Element n) {
		try {
			double distX = Double.parseDouble(n.getAttribute("x_offset_to_target"));
			double distY = Double.parseDouble(n.getAttribute("distance_to_target"));
			double ang = Double.parseDouble(n.getAttribute("angle"));
			this.distX = (Math.cos(Math.toRadians(90-ang)) * distY) - PathConfig.offsetFromGear;
			this.distY = Math.sin(Math.toRadians(90-ang)) * distY;
			this.ang = -ang + 90;
			timeMs = Integer.parseInt(n.getAttribute("timestamp"));
			tapeStatus = n.getAttribute("stripes_found");
			SmartDashboard.putNumber("distXVision", this.distX);
			SmartDashboard.putNumber("distYVision", this.distY);
			SmartDashboard.putNumber("angVision", this.ang);
			SmartDashboard.putNumber("timeMsVision", timeMs);
			SmartDashboard.putString("tapeStatus", tapeStatus);
		} catch(NumberFormatException e) {
			//Parsing empty string
			System.out.println("NumberFormatException: string from vision is empty");
		} catch(Exception e) {
			//Do something here
			System.out.println("Exception while parsing xml");
		}
	}
}