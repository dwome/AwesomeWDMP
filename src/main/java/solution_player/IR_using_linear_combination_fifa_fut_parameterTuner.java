package solution_player;

import java.io.File;
import java.io.IOException;

import blocker.PlayerBlockingFunctionBirthdate;
import comparators.PlayerHeightComparator;
import comparators.PlayerNameComparatorJaccard;
import comparators.PlayerBirthdateComparatorJaccard;
import comparators.PlayerPositionComparatorJaccard;
import comparators.PlayerRatingComparator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MovieBlockingKeyByDecadeGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDateComparator10Years;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieTitleComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Movie;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.MovieXMLReader;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.PlayerXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class IR_using_linear_combination_fifa_fut_parameterTuner {
	public static void main(String[] args) throws Exception {
		
		double score = 0.0;
		double bestN = 0.0;
		double bestH = 0.0;
		double bestB = 0.0;
		int count = 0;
		for (double j = 0.1; j < 2.0; j += 0.1) {
				for (double k = 0.1; k < 2.0; k += 0.1) {
					for (double l = 0.1; l < 2.0; l += 0.1) {
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
				HashedDataSet<Player, Attribute> dataFifa17 = new HashedDataSet<>();
				new PlayerXMLReader().loadFromXML(new File("data/input/fifa17.xml"),
						"/stadiums/stadium/clubs/club/players/player", dataFifa17);

				HashedDataSet<Player, Attribute> dataFut17 = new HashedDataSet<>();
				new PlayerXMLReader().loadFromXML(new File("data/input/fut17.xml"),
						"/stadiums/stadium/clubs/club/players/player", dataFut17);

				// create a matching rule
				LinearCombinationMatchingRule<Player, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.8);

				// add comparators
				matchingRule.addComparator(new PlayerNameComparatorJaccard(), name);
				matchingRule.addComparator(new PlayerBirthdateComparatorJaccard(), height);
				//matchingRule.addComparator(new PlayerPositionComparatorJaccard(), 0.05);
				matchingRule.addComparator(new PlayerHeightComparator(), bd);
				//matchingRule.addComparator(new PlayerRatingComparator(), 0.2);

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
						System.out.println("Name weight: 0.5");
						//System.out.println("Birthdate weight: 2.0");
						//System.out.println("Position weight: 0.05");
						System.out.println("Height weight: 0.7");
						//System.out.println("Rating weight: 0.2");
						System.out
								.println(String.format(
										"Precision: %.4f\nRecall: %.4f\nF1: %.4f",
										perfTest.getPrecision(), perfTest.getRecall(),
										perfTest.getF1()));
						
						return perfTest.getF1();
				

	}
}
