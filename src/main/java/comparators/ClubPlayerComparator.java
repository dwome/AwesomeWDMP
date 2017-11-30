 package comparators;

import java.util.HashSet;
import java.util.Set;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.query.Q;
import model.Club;
import model.Player;

public class ClubPlayerComparator implements Comparator<Club, Attribute> {
	
	private static final long serialVersionUID = 1L;


	@Override
	public double compare(
			Club record1,
			Club record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
		
		Set<String> players1 = new HashSet<>();
		Set<String> players2 = new HashSet<>();
		
		for(Player a : record1.getPlayers()) {
			players1.add(a.getName());
		}
		for(Player a : record2.getPlayers()) {
			players2.add(a.getName());
		}
		
		double similarity = Q.intersection(players1, players2).size() / (double)Math.max(players1.size(), players2.size());

		return similarity;
	}

}
