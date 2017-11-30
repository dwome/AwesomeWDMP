package solution_player;


import java.util.HashSet;
import java.util.Set;

import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import model.Player;

public class ErrorAnalysis {
	

	public void printFalsePositives(Processable<Correspondence<Player, Attribute>> correspondences, MatchingGoldStandard gs) {
		
		// go through the correspondences and check if they are incorrect
		for(Correspondence<Player, Attribute> c : correspondences.get()) {
			
			// is the match incorrect?
			if(gs.containsNegative(c.getFirstRecord(), c.getSecondRecord())) {
				
				// if yes, print the records to the console
				Player m1 = c.getFirstRecord();
				Player m2 = c.getSecondRecord();
				
				// print both records to the console
				System.out.println(String.format("[Incorrect Correspondence]" + m1.getId() +" "+m2.getId()));	
			}
		}		
	}
	
	public void printFalseNegatives(DataSet<Player, Attribute> ds1, DataSet<Player, Attribute> ds2, Processable<Correspondence<Player, Attribute>> correspondences, MatchingGoldStandard gs) {
		
		// first generate a set of all correct correspondences in the gold standard
		// (if a pair is not in the gold standard, we cannot say if its correct or not)
		Set<Pair<String,String>> allPairs = new HashSet<>();
		allPairs.addAll(gs.getPositiveExamples());
		
		// then go through the correspondences and remove all correct matches from the set
		for(Correspondence<Player, Attribute> c : correspondences.get()) {
			
			// create a pair of both record ids
			Pair<String, String> p1 = new Pair<>(c.getFirstRecord().getIdentifier(), c.getSecondRecord().getIdentifier());
			
			// create a second pair where record1 and record2 are switched 
			// (we don't know in which direction the ids were entered in the gold standard 
			Pair<String, String> p2 = new Pair<>(c.getFirstRecord().getIdentifier(), c.getSecondRecord().getIdentifier());
			
			// check if one of the pairs is in the set of correct matches
			if(allPairs.contains(p1) || allPairs.contains(p2)) {
				// if so, remove it
				allPairs.remove(p1);
				allPairs.remove(p2);
			}
		}
		
		// now, the remaining pairs in the set are those that were not found by the matching rule
		// we go through them and print them to the console
		for(Pair<String, String> p : allPairs) {
			
			// get the first record
			Player m1 = ds1.getRecord(p.getFirst());
			if(m1==null) {
				m1 = ds2.getRecord(p.getFirst());
			}
			
			// get the second record
			Player m2 = ds1.getRecord(p.getSecond());
			if(m2==null) {
				m2 = ds2.getRecord(p.getSecond());
			}
			
			// print both records to the console
			if(m1 != null && m2 != null)
			{
			System.out.println("[Missing Correspondence]");
			}
		}
	}
}