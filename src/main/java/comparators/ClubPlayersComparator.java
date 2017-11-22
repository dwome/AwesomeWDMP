package comparators;

import java.util.HashSet;
import java.util.Set;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;
import de.uni_mannheim.informatik.dws.winter.utils.query.Q;

public class ClubPlayersComparator  implements Comparator<Club, Attribute> {
	
	private static final long serialVersionUID = 1L;
	private TokenizingJaccardSimilarity sim = new TokenizingJaccardSimilarity();
	private int counter;


	@Override
	public double compare(
			Club record1,
			Club record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
		
		counter = 0;
		
		

		for(Player r1 : record1.getPlayers()) {
			for(Player r2: record2.getPlayers()) {
				double similarity = sim.calculate(r1.getName().toLowerCase(), r2.getName().toLowerCase());
				
				if(similarity==1.0){
					counter ++;
					if(counter>10)
						return 1;
				}
					
		
			}
		}
		return 0;
	}

}