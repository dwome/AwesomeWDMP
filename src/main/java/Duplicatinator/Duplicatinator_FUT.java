package Duplicatinator;

import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.ClubXMLReader;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.PlayerXMLReader;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class Duplicatinator_FUT {

	public static void main(String[] args) throws Exception {
		// Load the Data into FusibleDataSet
		HashedDataSet<Player, Attribute> dataFifa17 = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/fut17.xml"),
				"/stadiums/stadium/clubs/club/players/player", dataFifa17);

		List<Player> fifa17list = new ArrayList<Player>(dataFifa17.get());
		List<Player> playernames = new ArrayList<Player>();
		List<String> playernamesString = new ArrayList<String>();
		List<String> duplicates = new ArrayList<String>();
		List<String> duplicateIDs = new ArrayList<String>();

		int count = 0;

		for (Player player : fifa17list) {
			if (playernamesString.contains(player.getName())) {
				for (Player addedPlayer : playernames) {
					if (addedPlayer.getName().equals(player.getName())) {
						if (player.getBirthdate().equals(addedPlayer.getBirthdate())) {
							//Really duplicate
							count++;
							System.out.println(player.getId());
							duplicateIDs.add(player.getId());
						}
					}
				}
			} else {
				playernames.add(player);
				playernamesString.add(player.getName());

			}

		}
		
		//DuplicatesWorstEnemy.punishDataset(duplicateIDs);
		System.out.println("Duplicates: " + count);

	}
}