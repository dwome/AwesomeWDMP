package solution_clubs;

import java.io.File;

import blocker.ClubBlockingFunction;
import blocker.PlayerAgeFunctionRating;
import blocker.PlayerBlockingFunctionBirthdate;
import comparators.ClubPlayersComparator;
import comparators.PlayerBirthdateComparatorJaccard;
import comparators.PlayerHeightComparator;
import comparators.PlayerNameComparatorJaccard;
import comparators.PlayerNationalityComparator;
import comparators.PlayerPositionComparatorJaccard;
import comparators.PlayerRatingComparator;
import comparators.PlayerWeightComparator;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import model.Club;
import model.ClubXMLReader;
import model.Player;
import model.PlayerXMLReader;

public class IR_using_machine_learning_fut_fifa {

	public static void main(String[] args) throws Exception {
		// loading data
		HashedDataSet<Club, Attribute> dataFifa17 = new HashedDataSet<>();
		new ClubXMLReader().loadFromXML(new File("data/input/fifa17.xml"),
				"/stadiums/stadium/clubs/club", dataFifa17);

		HashedDataSet<Club, Attribute> dataFut17 = new HashedDataSet<>();
		new ClubXMLReader().loadFromXML(new File("data/input/fut17_WD.xml"),
				"/stadiums/stadium/clubs/club", dataFut17);

		// create a matching rule
		String options[] = new String[1];
		options[0] = "";
		String modelType = "SimpleLogistic"; // use a logistic regression
		WekaMatchingRule<Club, Attribute> matchingRule = new WekaMatchingRule<>(0.8, modelType, options);
		// add comparators
		matchingRule.addComparator(new ClubPlayersComparator());

		// load the training set
		MatchingGoldStandard gsTraining = new MatchingGoldStandard();
		gsTraining.loadFromCSVFile(new File("data/goldstandard/gs_club_fut17_2_fifa17_v1.csv"));

		// train the matching rule's model
		RuleLearner<Club, Attribute> learner = new RuleLearner<>();
		learner.learnMatchingRule(dataFut17, dataFifa17, null, matchingRule, gsTraining);

		// create a blocker (blocking strategy)
		NoBlocker<Club, Attribute> blocker = new NoBlocker<Club, Attribute>();

		StandardRecordBlocker<Club, Attribute> blocker2 = new StandardRecordBlocker<Club, Attribute>(
				new ClubBlockingFunction());
		// Initialize Matching Engine
		MatchingEngine<Club, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		Processable<Correspondence<Club, Attribute>> correspondences = engine.runIdentityResolution(dataFut17,
				dataFifa17, null, matchingRule, blocker);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/FINAL_club_fut17_2_fifa17_correspondences.csv"),
				correspondences);

		// load the gold standard (test set)
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File("data/goldstandard/gs_club_fut17_2_fifa17_v2.csv"));

		// evaluate your result
		MatchingEvaluator<Club, Attribute> evaluator = new MatchingEvaluator<Club, Attribute>(true);
		Performance perfTest = evaluator.evaluateMatching(correspondences.get(), gsTest);

		// print the evaluation result
		System.out.println("FIFA17 <-> FUT17");
		System.out.println(String.format("Precision: %.4f\nRecall: %.4f\nF1: %.4f", perfTest.getPrecision(),
				perfTest.getRecall(), perfTest.getF1()));

	}
}
