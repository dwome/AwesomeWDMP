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
package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

/**
 * {@link XMLFormatter} for {@link Actor}s.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class PlayerXMLFormatter extends XMLFormatter<Player> {

	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("player");
	}

	@Override
	public Element createElementFromRecord(Player record, Document doc) {
		Element player = doc.createElement("player");

		player.appendChild(createTextElement("name", record.getName(), doc));
		player.appendChild(createTextElement("brithdate", record.getBirthdate(), doc));
		player.appendChild(createTextElement("nationality", record.getNationality(), doc));
		player.appendChild(createTextElement("position", record.getPosition(), doc));
		player.appendChild(createTextElement("age", Integer.toString(record.getAge()), doc));
		player.appendChild(createTextElement("height", Integer.toString(record.getHeight()), doc));
		player.appendChild(createTextElement("weight", Integer.toString(record.getWeight()), doc));
		player.appendChild(createTextElement("rating", Integer.toString(record.getRating()), doc));

		return player;
	}


}
