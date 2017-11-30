package solution_player;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import blocker.PlayerBlockingFunctionBirthdate;
import comparators.PlayerBirthdateComparatorJaccard;
import comparators.PlayerHeightComparator;
import comparators.PlayerNameComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.PlayerXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class IR_using_linear_combination_fut_transfermarkt {
	public static void main(String[] args) throws Exception {
		// loading data

				HashedDataSet<Player, Attribute> dataTrans = new HashedDataSet<>();
				new PlayerXMLReader().loadFromXML(new File("data/input/transfermarkt.xml"),
						"/stadiums/stadium/clubs/club/players/player", dataTrans);

				HashedDataSet<Player, Attribute> dataFut17 = new HashedDataSet<>();
				new PlayerXMLReader().loadFromXML(new File("data/input/fut17_WD.xml"),
						"/stadiums/stadium/clubs/club/players/player", dataFut17);

				// create a matching rule
				LinearCombinationMatchingRule<Player, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.9);

				// add comparators
				matchingRule.addComparator(new PlayerNameComparatorJaccard(), 0.6);
				matchingRule.addComparator(new PlayerHeightComparator(), 0.2);
				matchingRule.addComparator(new PlayerBirthdateComparatorJaccard(), 0.79);

				StandardRecordBlocker<Player, Attribute> blocker2 = new StandardRecordBlocker<Player, Attribute>(
						new PlayerBlockingFunctionBirthdate());

				// Initialize Matching Engine
				MatchingEngine<Player, Attribute> engine = new MatchingEngine<>();

				// Execute the matching
				Processable<Correspondence<Player, Attribute>> correspondences = engine.runIdentityResolution(dataFut17,
						dataTrans, null, matchingRule, blocker2);

				// write the correspondences to the output file
				new CSVCorrespondenceFormatter().writeCSV(new File("data/output/fut17_2_trans_correspondences.csv"),
						correspondences);

				// load the gold standard (test set)
				MatchingGoldStandard gsTest = new MatchingGoldStandard();
				gsTest.loadFromCSVFile(new File("data/goldstandard/gs_fut17_2_trans.csv"));

				// evaluate your result
				MatchingEvaluator<Player, Attribute> evaluator = new MatchingEvaluator<Player, Attribute>(true);
				Performance perfTest = evaluator.evaluateMatching(correspondences.get(), gsTest);

				// check which errors were made
				new ErrorAnalysis().printFalsePositives(correspondences, gsTest);
				new ErrorAnalysis().printFalseNegatives(dataFut17, dataTrans, correspondences, gsTest);

				// print the evaluation result
				System.out.println("Fut17 <-> Transfermarkt");
				System.out.println(String.format("Precision: %.4f\nRecall: %.4f\nF1: %.4f", perfTest.getPrecision(),
						perfTest.getRecall(), perfTest.getF1()));
	}

}
