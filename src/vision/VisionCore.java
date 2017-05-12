package vision;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionCore {
	private XMLParser xmlParser = new XMLParser();
	private VisionStruct vs = new VisionStruct();
	private SocketCore socket = new SocketCore();

	public VisionCore() {	
		new Thread(socket).start();		
	}
	
	public VisionStruct getVisionStruct() {
		return vs;
	}
	
	public void update() {
		try{
			vs = xmlParser.parseString(socket.getXML());
		} catch(Exception e) {
			
		}
	}
	
}
