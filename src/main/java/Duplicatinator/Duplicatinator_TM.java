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

public class Duplicatinator_TM {

	public static void main(String[] args) throws Exception {
		// Load the Data into FusibleDataSet
		HashedDataSet<Player, Attribute> dataFifa17 = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/transfermarkt.xml"),
				"/stadiums/stadium/clubs/club/players/player", dataFifa17);

		List<Player> fifa17list = new ArrayList<Player>(dataFifa17.get());
		List<Player> playernames = new ArrayList<Player>();
		List<String> playernamesString = new ArrayList<String>();
		List<String> duplicates = new ArrayList<String>();
		int count = 0;

		for (Player player : fifa17list) {
			if (playernamesString.contains(player.getName())) {
				for (Player addedPlayer : playernames) {
					if (addedPlayer.getName().equals(player.getName())) {
						if (player.getBirthdate().equals(addedPlayer.getBirthdate())) {
							count++;
							System.out.println("Player who is in the list: " + player.getName());
							System.out.println("Player who is in the list: " + player.getBirthdate());
							System.out.println("Player who diddnt get in the list: " + addedPlayer.getName());
							System.out.println("Player who diddnt get in the list: " + addedPlayer.getBirthdate());
						}
					}
				}
			} else {
				playernames.add(player);
				playernamesString.add(player.getName());

			}

		}

		System.out.println("Duplicates: " + count);

	}
}