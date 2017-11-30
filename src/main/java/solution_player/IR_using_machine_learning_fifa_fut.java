package solution_player;

import java.io.File;

import blocker.PlayerAgeFunctionRating;
import blocker.PlayerBlockingFunctionBirthdate;
import comparators.PlayerBirthdateComparatorJaccard;
import comparators.PlayerHeightComparator;
import comparators.PlayerNameComparatorJaccard;
import comparators.PlayerNationalityComparator;
import comparators.PlayerPositionComparatorJaccard;
import comparators.PlayerRatingComparator;
import comparators.PlayerWeightComparator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.PlayerXMLReader;
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

public class IR_using_machine_learning_fifa_fut {

	public static void main(String[] args) throws Exception {
		// loading data
		HashedDataSet<Player, Attribute> datafifa17 = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/fifa17.xml"),
				"/stadiums/stadium/clubs/club/players/player", datafifa17);
		HashedDataSet<Player, Attribute> datafut17 = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/fut17.xml"),
				"/stadiums/stadium/clubs/club/players/player", datafut17);

		// create a matching rule
		String options[] = new String[1];
		options[0] = "";
		String modelType = "SimpleLogistic"; // use a logistic regression
		WekaMatchingRule<Player, Attribute> matchingRule = new WekaMatchingRule<>(0.6, modelType, options);
		// add comparators
		matchingRule.addComparator(new PlayerNameComparatorJaccard());
		matchingRule.addComparator(new PlayerBirthdateComparatorJaccard());
		matchingRule.addComparator(new PlayerPositionComparatorJaccard());
		matchingRule.addComparator(new PlayerHeightComparator());
		matchingRule.addComparator(new PlayerRatingComparator());
		matchingRule.addComparator(new PlayerWeightComparator());
		matchingRule.addComparator(new PlayerNationalityComparator());

		// load the training set
		MatchingGoldStandard gsTraining = new MatchingGoldStandard();
		gsTraining.loadFromCSVFile(new File("data/goldstandard/gs_fifa17_2_fut17_v1.csv"));

		// train the matching rule's model
		RuleLearner<Player, Attribute> learner = new RuleLearner<>();
		learner.learnMatchingRule(datafifa17, datafut17, null, matchingRule, gsTraining);

		// create a blocker (blocking strategy)
		NoBlocker<Player, Attribute> blocker = new NoBlocker<Player, Attribute>();

		StandardRecordBlocker<Player, Attribute> blocker2 = new StandardRecordBlocker<Player, Attribute>(
				new PlayerBlockingFunctionBirthdate());
		// Initialize Matching Engine
		MatchingEngine<Player, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		Processable<Correspondence<Player, Attribute>> correspondences = engine.runIdentityResolution(datafifa17,
				datafut17, null, matchingRule, blocker2);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/ML_fifa17_2_fut17_correspondences.csv"),
				correspondences);

		// load the gold standard (test set)
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File("data/goldstandard/gs_fifa17_2_fut17_v2.csv"));

		// evaluate your result
		MatchingEvaluator<Player, Attribute> evaluator = new MatchingEvaluator<Player, Attribute>(true);
		Performance perfTest = evaluator.evaluateMatching(correspondences.get(), gsTest);

		// print the evaluation result
		System.out.println("FIFA17 <-> FUT17");
		System.out.println(String.format("Precision: %.4f\nRecall: %.4f\nF1: %.4f", perfTest.getPrecision(),
				perfTest.getRecall(), perfTest.getF1()));

	}
}
