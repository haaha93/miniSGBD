package bdd;

import java.util.ArrayList;

public class BDD {

//	private ArrayList<Table> listeTables;
	private ArrayList<RelSchema> listRelSchema;

	public BDD() {
		listRelSchema = new ArrayList<RelSchema>();
	}
	
	/**
	 * Display of each relation, begins at 0.
	 */
	public void displayRelSchema(){
		int i = 0;
		for ( RelSchema r : listRelSchema){
			System.out.print("Relation "+i+++" : ");
			r.display();
		}
	}
	
	/**
	 * @param s , tab of String containing user's values.
	 * Add a relation in the database.
	 */
	public void addRelSchema(String [] s){
		listRelSchema.add(new RelSchema(s));
	}

//	public boolean listTablesContient(String nomTable) {
//		boolean rep = false;
//
//		for (Table t : listeTables)
//			if (t.getNomTable().equals(nomTable))
//				rep = true;
//
//		return rep;
//	}
//
//	public int indexTable(String nomTable) {
//		int i = 0;
//		while (!listeTables.get(i).getNomTable().equals(nomTable))
//			i++;
//		return i;
//	}
//
//	public void ajoutTable(String nomTable) throws BddException {
//		if (listTablesContient(nomTable))
//			throw new BddException("Table deja existante");
//
//		else
//			listeTables.add(new Table(nomTable));
//	}
//
//	public void supprimerTable(String nomTable) throws BddException {
//		if (!listTablesContient(nomTable))
//			throw new BddException("Table non existante");
//
//		else {
//			listeTables.remove(indexTable(nomTable));
//		}
//	}
//
//	public void modifierNomTable(String nomTable, String nouveauNom) throws BddException {
//		if (!listTablesContient(nomTable))
//			throw new BddException("Table non existante");
//
//		else if (listTablesContient(nouveauNom))
//			throw new BddException("Table deja existante");
//
//		else
//			listeTables.get(indexTable(nomTable)).setNomTable(nouveauNom);
//	}
//
//	public void affichageDesTables() {
//		for (Table t : listeTables)
//			System.out.println(t.getNomTable());
//		
//	}
}
