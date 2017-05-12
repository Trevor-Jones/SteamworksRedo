package core;

import java.util.ArrayList;

import config.PathConfig;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PathPlanner implements Runnable {
	private double dt, maxVel, maxAcc, robotTrackWidth, offset, distance, robotAng;
	private int numPointsTan, numPointsCircle;
	private boolean gottenLeft, gottenRight, pathCompleted = false;
	private double[][] leftPath, rightPath, leftProfile, rightProfile;
	
	/**
	 * 
	 * @param dt Time for each segment to execute
	 * @param maxVel Max velocity in fps
	 * @param maxAcc Max acceleration in feet/sec^2
	 * @param maxJerk Max jerk
	 * @param robotTrackWidth Width of robot in feet
	 */
	public PathPlanner(double dt, double maxVel, double maxAcc, double robotTrackWidth, int numPointsCircle, int numPointsTan, double offset, double distance, double robotAng) {
		this.dt = dt;
		this.robotTrackWidth = robotTrackWidth;
		this.maxVel = maxVel;
		this.maxAcc = maxAcc;
		this.numPointsCircle = numPointsCircle;
		this.numPointsTan = numPointsTan;
		this.offset = offset;
		this.distance = distance;
		this.robotAng = robotAng;
	}
	
	public double[][] generatePathPoints(int numPointsCircle, int numPointsTan, double offset, double distance, double robotAngle) {
		//double[][] points = new double[numPointsCircle + numPointsTan + numPointsTransition][2];
		ArrayList<double[]> traj = new ArrayList<double[]>();
		double radius = 40;
		
		double startPoint = radius * Math.sin(robotAngle);
		double x = startPoint;
		double startY = -1 * Math.sqrt(Math.pow(radius, 2) - Math.pow(x, 2)) + radius;
		traj.add(new double[] {0, 0});
		double segDistance = 0;
		double totalDistance = 0;
		double lastVel = 0;
		double currentY = 0;
		double currentX = 0;
		double lastY = 0;
		double lastX = 0;
		
		double time = System.currentTimeMillis();
		for(int i = 0; i < numPointsCircle; i++) {
			currentY = -1 * Math.sqrt(Math.pow(radius, 2) - Math.pow(x, 2)) + radius - startY;
			currentX = x - startPoint;
			segDistance = Math.sqrt(Math.pow((currentX - traj.get(traj.size() - 1)[0]),2) + Math.pow((currentY - traj.get(traj.size() - 1)[1]),2));
			totalDistance += segDistance;
						
			if(totalDistance/dt > maxVel || (totalDistance/dt) - lastVel > maxAcc * dt) {
				//SmartDashboard.putString("pointGenerating", "totalDistance/dt = " + (totalDistance/dt) + "       acceleration = " + ((totalDistance/dt) - lastVel) + "      lastVel = " + lastVel + "\t\tindex: " + i);
				traj.add(new double[] {lastX, lastY});
				//System.out.println("Adding circle: " + lastX + "    |    " + lastY);
				lastVel = (totalDistance - segDistance)/dt;
				totalDistance = 0;
				i--;
			} else {
				if(robotAngle < Math.PI/2) {
					x += (radius - startPoint) / numPointsCircle;
				} else {
					x -= (radius + startPoint) / numPointsCircle;
				}
			}
			
			lastX = currentX;
			lastY = currentY;
			
			
			if(x >= 40) {
				break;
			}
		}
		
		double tanOffset = offset - traj.get(traj.size()-1)[0];
		double tanDistance = distance - traj.get(traj.size()-1)[1];
		x = -0.5 * tanOffset;
		double vertStretch = 0.125 * tanDistance;
		double horzStretch = 1.185 * tanOffset;
		int lastCircleIndex = traj.size()-1;
		
		for(int i = numPointsCircle; i < numPointsTan + numPointsCircle; i++) {
			currentY = vertStretch * Math.tan((Math.PI * x) / horzStretch) + (0.5 * tanDistance) + traj.get(lastCircleIndex)[1];
			currentX = x + (tanOffset/2) + traj.get(lastCircleIndex)[0];
			
			segDistance = Math.sqrt(Math.pow((currentX - traj.get(traj.size() - 1)[0]),2) + Math.pow((currentY - traj.get(traj.size() - 1)[1]),2));
			totalDistance += segDistance;
			
			if(totalDistance/dt > maxVel || (totalDistance/dt) - lastVel > maxAcc * dt) {
				//System.out.println("totalDistance/dt = " + (totalDistance/dt) + "       acceleration = " + ((totalDistance/dt) - lastVel) + "      lastVel = " + lastVel + "\t\tindex: " + i);
				traj.add(new double[] {lastX, lastY});
				//System.out.println("Adding tangent: " + lastX + "    |    " + lastY);
				lastVel = (totalDistance - segDistance)/dt;
				totalDistance = 0;
				i--;
			} else {
				x += ((tanOffset) / (numPointsTan));
			}
			
			lastX = currentX;
			lastY = currentY;
		}

		SmartDashboard.putNumber("arraySize", traj.size());
		return traj.toArray(new double[traj.size()][2]);
	}
	
	public double[][] removePoints(double[][] path, double currentVel) {
		double distance = 0;
		double lastVel = currentVel;
		double segDistance = 0;
		ArrayList<double[]> traj = new ArrayList<double[]>();
		traj.add(new double[] {0,0});
		
		for(int i = 0; i < path.length-1; i++) {
			segDistance = Math.sqrt(Math.pow((path[i+1][0] - path[i][0]),2) + Math.pow((path[i+1][1] - path[i][1]),2));
			distance += segDistance;
						
			if(distance/dt > maxVel || (distance/dt) - lastVel > maxAcc * dt) {
				//SmartDashboard.putString("pointGenerating", "distance/dt = " + (distance/dt) + "       acceleration = " + ((distance/dt) - lastVel) + "      lastVel = " + lastVel + "\t\tindex: " + i);
				traj.add(new double[] {path[i][0], path[i][1]});
				lastVel = (distance - segDistance)/dt;
				distance = 0;
				i--;
			}
		}
		
		return traj.toArray(new double[traj.size()][2]);
	}
	
	/**
	 * Uses leftPath and rightPath arrays to determine motion profile
	 * @return path 2d array with left pos then velocity, then right. Example: {LeftPos, LeftVel, RightPos, RightVel}
	 */
	private double[][] generateMotionProfile() {
		double[][] leftRightPosVel = new double[leftPath.length][4];
		double leftPos = 0;
		double rightPos = 0;
		double leftPosChange = 0;
		double rightPosChange = 0;
		
		for(int i = 0; i < leftPath.length-1; i++) {
			leftPosChange = Math.sqrt(Math.pow((leftPath[i+1][0] - leftPath[i][0]),2) + Math.pow((leftPath[i+1][1] - leftPath[i][1]),2));
			leftPos += leftPosChange;
			leftRightPosVel[i][0] = leftPos;
			leftRightPosVel[i][1] = leftPosChange/dt * 60; // Talon is in rpm

			rightPosChange = Math.sqrt(Math.pow((rightPath[i+1][0] - rightPath[i][0]),2) + Math.pow((rightPath[i+1][1] - rightPath[i][1]),2));
			rightPos += rightPosChange;
			leftRightPosVel[i][2] = rightPos;
			leftRightPosVel[i][3] = rightPosChange/dt * 60; // Talon is in rpm
		}
		
		leftRightPosVel[leftRightPosVel.length-1][0] = leftPos;
		leftRightPosVel[leftRightPosVel.length-1][1] = 0;
		leftRightPosVel[leftRightPosVel.length-1][2] = rightPos;
		leftRightPosVel[leftRightPosVel.length-1][3] = 0;

		return leftRightPosVel;
	}
	
	private void leftRight(double[][] smoothPath) {
		double[][] leftPath = new double[smoothPath.length][2];
		double[][] rightPath = new double[smoothPath.length][2];

		double[][] gradient = new double[smoothPath.length][2];

		for(int i = 0; i<smoothPath.length-1; i++) {
			gradient[i][1] = Math.atan2(smoothPath[i+1][1] - smoothPath[i][1],smoothPath[i+1][0] - smoothPath[i][0]);	
		}

		gradient[gradient.length-1][1] = gradient[gradient.length-2][1];


		for (int i=0; i<gradient.length; i++) {
			leftPath[i][0] = (robotTrackWidth/2 * Math.cos(gradient[i][1] + Math.PI/2)) + smoothPath[i][0];
			leftPath[i][1] = (robotTrackWidth/2 * Math.sin(gradient[i][1] + Math.PI/2)) + smoothPath[i][1];

			rightPath[i][0] = robotTrackWidth/2 * Math.cos(gradient[i][1] - Math.PI/2) + smoothPath[i][0];
			rightPath[i][1] = robotTrackWidth/2 * Math.sin(gradient[i][1] - Math.PI/2) + smoothPath[i][1];

			//convert to degrees 0 to 360 where 0 degrees is +X - axis, accumulated to aline with WPI sensor
			double deg = Math.toDegrees(gradient[i][1]);

			gradient[i][1] = deg;

			if(i>0)
			{
				if((deg-gradient[i-1][1])>180)
					gradient[i][1] = -360+deg;

				if((deg-gradient[i-1][1])<-180)
					gradient[i][1] = 360+deg;
			}
		}

		this.rightPath = rightPath;
		this.leftPath = leftPath;
	}
	
	private void generateProfileArray(double[][] leftRightPosVel) {
		double time = System.currentTimeMillis();
		leftProfile = new double[leftRightPosVel.length][3];
		rightProfile = new double[leftRightPosVel.length][3];
		
		String left = "";
		String right = "";
		
		for(int i = 0; i < leftRightPosVel.length; i++) {
			leftProfile[i][0] = leftRightPosVel[i][0];
			leftProfile[i][1] = leftRightPosVel[i][1];
			leftProfile[i][2] = dt * 1000;
			
			left += leftProfile[i][0] + "    ";
			
			rightProfile[i][0] = leftRightPosVel[i][2];
			rightProfile[i][1] = leftRightPosVel[i][3];
			rightProfile[i][2] = dt * 1000;

			right += rightProfile[i][0] + "    ";
		}
		
		SmartDashboard.putNumber("timeForCircle", System.currentTimeMillis() - time);
		SmartDashboard.putString("leftProfileDist", left);
		SmartDashboard.putString("rightProfileDist", right);
	}
	
	public void generateProfileFromDistances(int numPointsCircle, int numPointsTan, double offset, double distance, double robotAng) {
		SmartDashboard.putBoolean("pathCompleted", pathCompleted);
		leftRight(generatePathPoints(numPointsCircle, numPointsTan, offset, distance, Math.toRadians(robotAng)));
		double time = System.currentTimeMillis();
		generateProfileArray(generateMotionProfile());
		pathCompleted = true;
		SmartDashboard.putBoolean("pathCompleted", pathCompleted);
	}
	
	public double[][] getLeftProfile() {
		gottenLeft = true;
		return leftProfile;
	}
	
	public double[][] getRightProfile() {
		gottenRight = true;
		return rightProfile;
	}
	
	public boolean getStatus() {
		return pathCompleted;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		generateProfileFromDistances(numPointsCircle, numPointsTan, offset, distance, robotAng);
		while(!gottenLeft || !gottenRight) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
