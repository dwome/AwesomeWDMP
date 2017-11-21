package comparators;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.AbsoluteDifferenceSimilarity;

public class PlayerAgeComparatorFifa2Trans implements Comparator<Player, Attribute> {
	private static final long serialVersionUID = 1L;
	private AbsoluteDifferenceSimilarity sim = new AbsoluteDifferenceSimilarity(1);

	@Override
	public double compare(Player record1, Player record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

		// preprocessing
		Double i1 = (double) record1.getAge();
		Double i2 = (double) record2.getAge();

		// calculate similarity value
		double similarity = sim.calculate(i1, i2);

		return similarity;
	}

}
