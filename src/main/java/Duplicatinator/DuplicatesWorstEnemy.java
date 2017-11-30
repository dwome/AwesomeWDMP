package Duplicatinator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
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

	public static void punishDataset(List<String> delete) throws ParserConfigurationException, SAXException,
			IOException, XPathExpressionException, TransformerException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		File file = new File("data/input/fut17.xml");
		Document d = db.parse(file);

		for (String s : delete) {
			System.out.println("DELETE: "+s);
			
			XPath xp = XPathFactory.newInstance().newXPath();
			NodeList nl = (NodeList) xp.compile("//stadiums/stadium/clubs/club/players/player[id = '" + s + "']")
					.evaluate(d, XPathConstants.NODESET);
			for (int i = nl.getLength() - 1; i >= 0; i--) {
				Node temp = nl.item(i).getParentNode();
				temp.removeChild(nl.item(i));
			}

		}
		System.out.println("Finished Deletion");

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(d);
		StreamResult result = new StreamResult(new File("data/output/fut17_without_duplicates.xml"));
		transformer.transform(source, result);
	}
}
