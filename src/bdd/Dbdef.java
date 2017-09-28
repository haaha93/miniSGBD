package bdd;

import java.util.ArrayList;
import java.util.List;

public class Dbdef {
	private List<RelDef> listRelation;
	private int compteurRel;

	public Dbdef(List<RelDef> db) {
		this.listRelation = new ArrayList<RelDef>();
		this.compteurRel = db.size();
	}

	public Dbdef() {
		this.listRelation = new ArrayList<RelDef>();
		this.compteurRel = 0 ;
	}
	
	public void addRelationToDB(String[] userInput) {
		listRelation.add(new RelDef(userInput, listRelation.size()));
		compteurRel=listRelation.size();
	}

}
