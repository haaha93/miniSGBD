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
		this.compteurRel = 0;
	}

	public void addRelationToDB(String[] userInput) {
		listRelation.add(new RelDef(userInput, listRelation.size()));
		compteurRel = listRelation.size();
	}
	
	public void addRelationToDB(RelSchema relSchema, int recordSize) {
		listRelation.add(new RelDef(relSchema, listRelation.size(),recordSize));
		compteurRel = listRelation.size();
	}

	public List<RelDef> getListRelation() {
		return listRelation;
	}

	public void setListRelation(List<RelDef> listRelation) {
		this.listRelation = listRelation;
	}

	public int getCompteurRel() {
		return compteurRel;
	}

	public void setCompteurRel(int compteurRel) {
		this.compteurRel = compteurRel;
	}

	public String toString() {
		StringBuffer dbString = new StringBuffer();

		for (int i = 0; i < listRelation.size(); i++) {
			dbString.append("Relation " + i + " ").append(listRelation.get(i).toString());
		}

		return dbString.toString();
	}

	public RelSchema geRelSchemaByName(String name) {

		for (RelDef rd : listRelation)
			if (rd.getRelSchema().getName().equals(name))
				return rd.getRelSchema();

		return null;
	}

}
