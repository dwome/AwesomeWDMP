package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.solutions;

import java.util.HashSet;
import java.util.Set;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Actor;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Movie;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.query.Q;

public class MovieActorComparator  implements Comparator<Movie, Attribute> {

	private static final long serialVersionUID = 1L;


	@Override
	public double compare(
			Movie record1,
			Movie record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
		
		Set<String> actors1 = new HashSet<>();
		Set<String> actors2 = new HashSet<>();
		
		for(Actor a : record1.getActors()) {
			actors1.add(a.getName());
		}
		for(Actor a : record2.getActors()) {
			actors2.add(a.getName());
		}
		
		double similarity = Q.intersection(actors1, actors2).size() / (double)Math.max(actors1.size(), actors2.size());

		return similarity;
	}

}
