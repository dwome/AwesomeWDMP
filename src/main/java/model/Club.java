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

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

/**
 * A {@link AbstractRecord} representing a movie.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class Club implements Matchable {

	
	private String name;
	private List<Player> players;	
	protected String id;
	protected String provenance;


	public Club(String identifier, String provenance) {
		id = identifier;
		this.provenance = provenance;
		players = new LinkedList<>();
	}

	

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public List<Player> getPlayers() {
		return players;
	}



	public void setPlayers(List<Player> players) {
		this.players = players;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return getIdentifier().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Club){
			return this.getIdentifier().equals(((Club) obj).getIdentifier());
		}else
			return false;
	}



	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return id;
	}



	@Override
	public String getProvenance() {
		// TODO Auto-generated method stub
		return provenance;
	}

	
	
}
