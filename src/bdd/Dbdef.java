package bdd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dbdef implements Serializable {
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
	
	public void addRelationToDB(RelSchema relSchema, int recordSize, int slotCount) {
		listRelation.add(new RelDef(relSchema, listRelation.size(),recordSize, slotCount));
		compteurRel = listRelation.size();
	}
	
	public void addRelationToDBAtIndex(RelDef relDef, int index) {
		listRelation.add(index,relDef);
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

	public RelSchema getRelSchemaByName(String name) {

		for (RelDef rd : listRelation)
			if (rd.getRelSchema().getName().equals(name))
				return rd.getRelSchema();

		return null;
	}
	
	public int getIndexOfRelSchemaByName(String name) {

		for (int i = 0 ; i < listRelation.size() ; i++)
			if (listRelation.get(i).getRelSchema().getName().equals(name))
				return i;

		return -1;
	}

}
