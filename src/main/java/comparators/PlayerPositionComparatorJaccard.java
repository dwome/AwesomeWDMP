package comparators;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

public class PlayerPositionComparatorJaccard implements Comparator<Player, Attribute> {
	private static final long serialVersionUID = 1L;
	private TokenizingJaccardSimilarity sim = new TokenizingJaccardSimilarity();

	public double compare(Player record1, Player record2, Correspondence<Attribute, Matchable> schemaCorrespondences) {

		double similarity = 0;

        try
        {
    		// preprocessing
    		String s1 = record1.getPosition().toLowerCase();
    		String s2 = record2.getPosition().toLowerCase();

    		// calculate similarity value
    		similarity = sim.calculate(s1, s2);

    		// postprocessing
    		similarity *= similarity;
        }
        catch(NullPointerException e)
        {
            //System.out.println("NullPointerException caught");
            similarity = 0;
        }
        
		return similarity;
	}

}

