package solution_clubs;

import java.io.File;

import comparators.ClubNameComparatorJaccardNGrams;
import comparators.PlayerHeightComparator;
import comparators.PlayerNameComparatorJaccard;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MovieBlockingKeyByDecadeGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDateComparator10Years;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieTitleComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.ClubXMLReader;
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

public class IR_using_linear_combination_stadium_fifa {
	public static void main(String[] args) throws Exception {
		// loading data
		HashedDataSet<Club, Attribute> dataFifa17 = new HashedDataSet<>();
		new ClubXMLReader().loadFromXML(new File("data/input/fifa17.xml"), "/stadiums/stadium/clubs/club", dataFifa17);

		HashedDataSet<Club, Attribute> dataStadium = new HashedDataSet<>();
		new ClubXMLReader().loadFromXML(new File("data/input/stadium.xml"), "/stadiums/stadium/clubs/club",
				dataStadium);

		// create a matching rule
		LinearCombinationMatchingRule<Club, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0);

		// add comparators
		matchingRule.addComparator(new ClubNameComparatorJaccardNGrams(), 1.0);

		// create a blocker (blocking strategy)
		NoBlocker<Club, Attribute> noblocker = new NoBlocker<Club, Attribute>();
		StandardRecordBlocker<Club, Attribute> blocker2 = new StandardRecordBlocker<Club, Attribute>(
				new ClubBlockingFunction());

		// Initialize Matching Engine
		MatchingEngine<Club, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		Processable<Correspondence<Club, Attribute>> correspondences = engine.runIdentityResolution(dataStadium,
				dataFifa17, null, matchingRule, noblocker);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/stadium_2_fifa17_correspondences.csv"),
				correspondences);

		// load the gold standard (test set)
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File("data/goldstandard/gs_club_stadium_2_fifa17.csv"));

		// evaluate your result
		MatchingEvaluator<Club, Attribute> evaluator = new MatchingEvaluator<Club, Attribute>(true);
		Performance perfTest = evaluator.evaluateMatching(correspondences.get(), gsTest);

		// check which errors were made
		new ErrorAnalysis().printFalsePositives(correspondences, gsTest);
		new ErrorAnalysis().printFalseNegatives(dataStadium, dataFifa17, correspondences, gsTest);

		// print the evaluation result
		System.out.println("Stadium <-> Fifa17");
		System.out.println(String.format("Precision: %.4f\nRecall: %.4f\nF1: %.4f", perfTest.getPrecision(),
				perfTest.getRecall(), perfTest.getF1()));

	}
}
