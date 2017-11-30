package blocker;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Club;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Player;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class ClubBlockingFunction extends
			RecordBlockingKeyGenerator<Club, Attribute> {

		private static final long serialVersionUID = 1L;


		@Override
		public void generateBlockingKeys(Club record, Processable<Correspondence<Attribute, Matchable>> correspondences,
				DataIterator<Pair<String, Club>> resultCollector) {
			resultCollector.next(new Pair<>(record.getName(), record));
			
		}

	}

