package comparators;


import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.GeneralisedStringJaccard;
import de.uni_mannheim.informatik.dws.winter.similarity.string.JaccardOnNGramsSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinEditDistance;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.MaximumOfTokenContainment;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

public class ClubNameComparatorJaccardNGrams implements Comparator<Club, Attribute> {
	private static final long serialVersionUID = 1L;
	private JaccardOnNGramsSimilarity sim = new JaccardOnNGramsSimilarity(5);

	public double compare(Club record1, Club record2, Correspondence<Attribute, Matchable> schemaCorrespondences) {

		// preprocessing
		String s1 = record1.getName().toLowerCase();
		String s2 = record2.getName().toLowerCase();


		// calculate similarity value
		double similarity = sim.calculate(s1, s2);

		// postprocessing
		similarity *= similarity;
		return similarity;
	}
}
