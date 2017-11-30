package solution_player;

import java.io.File;

import blocker.PlayerBlockingFunctionBirthdate;
import comparators.PlayerBirthdateComparatorJaccard;
import comparators.PlayerHeightComparator;
import comparators.PlayerWeightComparator;
import comparators.PlayerNameComparatorJaccard;
import comparators.PlayerNationalityComparator;
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

public class IR_using_linear_combination_fifa_transfermarkt_parameterTuning {
	public static void main(String[] args) throws Exception {

		double score = 0.0;
		double bestN = 0.0;
		double bestH = 0.0;
		double bestB = 0.0;
		int count = 0;
		for (double j = 0.2; j < 0.5; j += 0.1) {
				for (double k = 1; k < 1.1; k += 0.1) {
					for (double l = 0.5; l < 1.5; l += 0.1) {
						count++;
						System.out.println("COUNT:   " + count);
						System.out.println("Name: " + l);
						System.out.println("Height: " + j);
						System.out.println("Birthdate: " + k);
						System.out.println("---------------------------------------------" + score
								+ "---------------------------------------------");
						double tempscore = parameterTuner(l, j, k);
						if (tempscore >= score) {
							score = tempscore;
							bestN = l;
							bestH = j;
							bestB = k;
						}
				}
			}
		}
		System.out.println("-----PARAMETER------");
		System.out.println("Name: " + bestN);
		System.out.println("Height: " + bestH);
		System.out.println("Birthdate: " + bestB);
		System.out.println("F1: " + score);
	}

	public static double parameterTuner(double name, double height, double bd) throws Exception {
		// loading data

		HashedDataSet<Player, Attribute> dataTrans = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/transfermarkt.xml"),
				"/stadiums/stadium/clubs/club/players/player", dataTrans);

		HashedDataSet<Player, Attribute> dataFifa17 = new HashedDataSet<>();
		new PlayerXMLReader().loadFromXML(new File("data/input/fifa17.xml"),
				"/stadiums/stadium/clubs/club/players/player", dataFifa17);

		// create a matching rule
		LinearCombinationMatchingRule<Player, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.6);

		// add comparators
		matchingRule.addComparator(new PlayerNameComparatorJaccard(), name);
		// matchingRule.addComparator(new PlayerAgeComparatorFifa2Trans(), 0.2);
		matchingRule.addComparator(new PlayerBirthdateComparatorJaccard(), bd);
		matchingRule.addComparator(new PlayerHeightComparator(), height);
		// matchingRule.addComparator(new PlayerRatingComparator(), 0.2);

		// create a blocker (blocking strategy)
		NoBlocker<Player, Attribute> blocker = new NoBlocker<Player, Attribute>();

		StandardRecordBlocker<Player, Attribute> blocker2 = new StandardRecordBlocker<Player, Attribute>(
				new PlayerBlockingFunctionBirthdate());

		// Initialize Matching Engine
		MatchingEngine<Player, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		Processable<Correspondence<Player, Attribute>> correspondences = engine.runIdentityResolution(dataFifa17,
				dataTrans, null, matchingRule, blocker2);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/fifa17_2_trans_correspondences.csv"),
				correspondences);

		// load the gold standard (test set)
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File("data/goldstandard/gs_fifa17_2_trans.csv"));

		// evaluate your result
		MatchingEvaluator<Player, Attribute> evaluator = new MatchingEvaluator<Player, Attribute>(true);
		Performance perfTest = evaluator.evaluateMatching(correspondences.get(), gsTest);

		// check which errors were made
		new ErrorAnalysis().printFalsePositives(correspondences, gsTest);
		new ErrorAnalysis().printFalseNegatives(dataFifa17, dataTrans, correspondences, gsTest);

		// print the evaluation result

		System.out.println("Name weight: 0.8");
		System.out.println("Birthdate weight: 2.0");
		// System.out.println("Age weight: 0.2");
		// System.out.println("Position weight: 0.5");
		System.out.println("Height weight: 0.3");
		System.out.println("Weight weight: 0.3");
		// System.out.println("Rating weight: 0.2");
		System.out.println("Fifa17 <-> Transfermarkt");
		System.out.println(String.format("Precision: %.4f\nRecall: %.4f\nF1: %.4f", perfTest.getPrecision(),
				perfTest.getRecall(), perfTest.getF1()));
		return perfTest.getF1();

	}
}