/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

/**
 * {@link XMLFormatter} for {@link Movie}s.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class ClubXMLFormatter extends XMLFormatter<Club> {

	ActorXMLFormatter actorFormatter = new ActorXMLFormatter();

	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("clubs");
	}

	@Override
	public Element createElementFromRecord(Club record, Document doc) {
		Element club = doc.createElement("clubs");

		club.appendChild(createTextElement("id", record.getIdentifier(), doc));

		club.appendChild(createTextElement("name",
				record.getName(),
				doc));
		club.appendChild(createTextElement("players",
				record.getPlayers().toString(),
				doc));

		return club;
	}


}
