package vision;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class XMLParser {
	VisionStruct vs = new VisionStruct();
	
	public VisionStruct parseString(String xml) {
		try {
			Document doc = stringToDoc(xml);
			doc.getDocumentElement().normalize();
	
			NodeList pathList = doc.getElementsByTagName("root").item(0).getChildNodes();
			Node leftPath = pathList.item(0);
			Node rightPath = pathList.item(1);
			NodeList leftPoints = leftPath.getChildNodes();
			NodeList rightPoints = rightPath.getChildNodes();
			double[][] leftProfile = new double[leftPoints.getLength()][3];
			double[][] rightProfile = new double[rightPoints.getLength()][3];
			for(int i = 0; i < leftProfile.length; i++) {
				leftProfile[i][0] = Double.parseDouble(leftPoints.item(i).getAttributes().getNamedItem("Distance").getNodeValue());
				leftProfile[i][1] = Double.parseDouble(leftPoints.item(i).getAttributes().getNamedItem("Velocity").getNodeValue());
				leftProfile[i][2] = Double.parseDouble(leftPoints.item(i).getAttributes().getNamedItem("DeltaT").getNodeValue());

				rightProfile[i][0] = Double.parseDouble(rightPoints.item(i).getAttributes().getNamedItem("Distance").getNodeValue());
				rightProfile[i][1] = Double.parseDouble(rightPoints.item(i).getAttributes().getNamedItem("Velocity").getNodeValue());
				rightProfile[i][2] = Double.parseDouble(rightPoints.item(i).getAttributes().getNamedItem("DeltaT").getNodeValue());	
			}
			vs.setPaths(leftProfile, rightProfile);	
	
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("You really fucked up xml parsing");
		}

		return vs;
	}

	public static Document stringToDoc(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		return builder.parse(is);
	}
}
