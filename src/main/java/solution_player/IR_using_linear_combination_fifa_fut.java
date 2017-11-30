package solution_player;

import java.io.File;

import blocker.PlayerBlockingFunctionBirthdate;
import comparators.PlayerHeightComparator;
import comparators.PlayerNameComparatorJaccard;
import comparators.PlayerBirthdateComparatorJaccard;
import comparators.PlayerPositionComparatorJaccard;
import comparators.PlayerRatingComparator;
import comparators.PlayerWeightComparator;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import model.Player;
import model.PlayerXMLReader;

public class IR_using_linear_combination_fifa_fut {
	public static void main(String[] args) throws Exception {
		// loading data
		HashedDataSet<Player, Attribute> dataFifa17 = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/fifa17.xml"),
				"/stadiums/stadium/clubs/club/players/player", dataFifa17);

		HashedDataSet<Player, Attribute> dataFut17 = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/fut17_WD.xml"),
				"/stadiums/stadium/clubs/club/players/player", dataFut17);

		// create a matching rule
		LinearCombinationMatchingRule<Player, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.8);

		// add comparators
		matchingRule.addComparator(new PlayerNameComparatorJaccard(), 0.7);
		matchingRule.addComparator(new PlayerBirthdateComparatorJaccard(), 2.1);
		matchingRule.addComparator(new PlayerHeightComparator(), 0.5);
		matchingRule.addComparator(new PlayerWeightComparator(), 0.1);
		matchingRule.addComparator(new PlayerRatingComparator(), 0.2);

		// create a blocker (blocking strategy)
		NoBlocker<Player, Attribute> blocker = new NoBlocker<Player, Attribute>();

		StandardRecordBlocker<Player, Attribute> blocker2 = new StandardRecordBlocker<Player, Attribute>(
				new PlayerBlockingFunctionBirthdate());
		

		// Initialize Matching Engine
		MatchingEngine<Player, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		Processable<Correspondence<Player, Attribute>> correspondences = engine.runIdentityResolution(
				dataFifa17, dataFut17, null, matchingRule,
				blocker2);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/fifa17_2_fut17_correspondences.csv"),
				correspondences);
		
		// load the gold standard (test set)
				MatchingGoldStandard gsTest = new MatchingGoldStandard();
				gsTest.loadFromCSVFile(new File(
						"data/goldstandard/gs_fifa17_2_fut17.csv"));

				// evaluate your result
				MatchingEvaluator<Player, Attribute> evaluator = new MatchingEvaluator<Player, Attribute>(true);
				Performance perfTest = evaluator.evaluateMatching(correspondences.get(),
						gsTest);

				// check which errors were made
				new ErrorAnalysis().printFalsePositives(correspondences, gsTest);
				new ErrorAnalysis().printFalseNegatives(dataFifa17, dataFut17, correspondences, gsTest);
				
				// print the evaluation result
				System.out.println("Fifa17 <-> Fut17");
				System.out
						.println(String.format(
								"Precision: %.4f\nRecall: %.4f\nF1: %.4f",
								perfTest.getPrecision(), perfTest.getRecall(),
								perfTest.getF1()));
		
	}
}
