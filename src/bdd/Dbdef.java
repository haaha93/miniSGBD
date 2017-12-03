package bdd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dbdef implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<RelDef> listRelation;
	private int compteurRel;
	
	/**
	 * Constructor of the db
	 *@param List RelDef
	 * 
	 */

	public Dbdef(List<RelDef> db) {
		this.listRelation = new ArrayList<RelDef>();
		this.compteurRel = db.size();
	}

	/**
	 * Constructor of the db
	 *@param 
	 * 
	 */
	public Dbdef() {
		this.listRelation = new ArrayList<RelDef>();
		this.compteurRel = 0;
	}

	/**
	 * Add a new relation to the db
	 *@param Array of Strings
	 * 
	 */
	public void addRelationToDB(String[] userInput) {
		listRelation.add(new RelDef(userInput, listRelation.size()));
		compteurRel = listRelation.size();
	}

	/**
	 * Add a new relation to the db
	 *@param the relation's scheme, Int record's size, and Int for slot counts
	 * 
	 */
	public void addRelationToDB(RelSchema relSchema, int recordSize, int slotCount) {
		listRelation.add(new RelDef(relSchema, listRelation.size(), recordSize, slotCount));
		compteurRel = listRelation.size();
	}

	/**
	 * Add a new relation at a specified index
	 *@param Int index, the relation
	 * 
	 */
	public void addRelationToDBAtIndex(int index, RelDef relDef) {
		listRelation.add(index, relDef);
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

	/**
	 * 
	 *@return the String containing all the relations of the DB
	 * 
	 */
	public String toString() {
		StringBuffer dbString = new StringBuffer();

		for (int i = 0; i < listRelation.size(); i++) {
			dbString.append("Relation " + i + " ").append(listRelation.get(i).toString());
		}

		return dbString.toString();
	}
	
	

	public RelSchema getRelSchemaByName(String name) {

		for (int i = 0 ; i < listRelation.size() ; i++)
			if (listRelation.get(i).getRelSchema().getName().equals(name))
				return listRelation.get(i).getRelSchema();

		return null;
	}

	public int getIndexOfRelSchemaByName(String name) {

		for (int i = 0; i < listRelation.size(); i++)
			if (listRelation.get(i).getRelSchema().getName().equals(name))
				return i;

		return -1;
	}

	public void clean() {
		listRelation.clear();
		compteurRel = 0;
	}

}
