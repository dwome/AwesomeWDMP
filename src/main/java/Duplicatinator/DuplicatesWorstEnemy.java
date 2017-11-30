package Duplicatinator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.PlayerXMLReader;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class DuplicatesWorstEnemy {

	public static void main(String[] args) throws Exception {
		punishDataset("fut17_11168");
	}

	public static void punishDataset(String delete_id) throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		File file = new File("data/input/fut17.xml");
		Document d = db.parse(file);
		/*        XPath xp = XPathFactory.newInstance().newXPath();
		        NodeList nl = (NodeList)xp.compile("//appender[@name = 'Testlogging']").evaluate(d,XPathConstants.NODESET);
		        for (int i = nl.getLength() - 1; i >= 0; i--) 
		        {
		           nl.item(i).getParentNode().removeChild(nl.item(i));
		        }*/
		
		
		
		/*
	
		try {

			NodeList playerlist = doc.getElementsByTagName("player");

			if (playerlist != null && playerlist.getLength() > 0) {
				for (int i = 0; i < playerlist.getLength(); i++) {

					Node node = playerlist.item(i);
					Element e = (Element) node;
					NodeList nodeList = e.getElementsByTagName("id");
					String id = nodeList.item(0).getChildNodes().item(0).getNodeValue();

					// System.out.println(id + " Player.id");

					if (delete_id.equals(id)) {
						doc.removeChild(node);

					}

				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}*/

}
}
