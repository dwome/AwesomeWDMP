package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import java.io.File;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MovieBlockingKeyByDecadeGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MovieBlockingKeyByYearGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDateComparator10Years;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieDateComparator2Years;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.MovieTitleComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Movie;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.MovieXMLReader;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.solutions.MovieActorComparator;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class IR_using_machine_learning {
    public static void main( String[] args ) throws Exception
    {
    	// loading data
		HashedDataSet<Movie, Attribute> dataAcademyAwards = new HashedDataSet<>();
		new MovieXMLReader().loadFromXML(new File("data/input/academy_awards.xml"), "/movies/movie", dataAcademyAwards);
		HashedDataSet<Movie, Attribute> dataActors = new HashedDataSet<>();
		new MovieXMLReader().loadFromXML(new File("data/input/actors.xml"), "/movies/movie", dataActors);

		// create a matching rule
		String options[] = new String[1];
		options[0] = "";
		String modelType = "SimpleLogistic"; // use a logistic regression
		WekaMatchingRule<Movie, Attribute> matchingRule = new WekaMatchingRule<>(0.6, modelType, options);
		// add comparators
		matchingRule.addComparator(new MovieDateComparator10Years());
		matchingRule.addComparator(new MovieTitleComparatorLevenshtein());
		
		// load the training set
		MatchingGoldStandard gsTraining = new MatchingGoldStandard();
		gsTraining.loadFromCSVFile(new File("data/goldstandard/gs_academy_awards_2_actors.csv"));

		// train the matching rule's model
		RuleLearner<Movie, Attribute> learner = new RuleLearner<>();
		learner.learnMatchingRule(dataAcademyAwards, dataActors, null, matchingRule, gsTraining);
		
		// create a blocker (blocking strategy)
		StandardRecordBlocker<Movie, Attribute> blocker = new StandardRecordBlocker<Movie, Attribute>(new MovieBlockingKeyByDecadeGenerator());
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByDecadeGenerator(), 1);
		
		// Initialize Matching Engine
		MatchingEngine<Movie, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		Processable<Correspondence<Movie, Attribute>> correspondences = engine.runIdentityResolution(
				dataAcademyAwards, dataActors, null, matchingRule,
				blocker);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/academy_awards_2_actors_correspondences.csv"), correspondences);

		// load the gold standard (test set)
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File(
				"data/goldstandard/gs_academy_awards_2_actors_v2.csv"));

		// evaluate your result
		MatchingEvaluator<Movie, Attribute> evaluator = new MatchingEvaluator<Movie, Attribute>(true);
		Performance perfTest = evaluator.evaluateMatching(correspondences.get(),
				gsTest);
		
		// print the evaluation result
		System.out.println("Academy Awards <-> Actors");
		System.out
				.println(String.format(
						"Precision: %.4f\nRecall: %.4f\nF1: %.4f",
						perfTest.getPrecision(), perfTest.getRecall(),
						perfTest.getF1()));
		
    }
}
