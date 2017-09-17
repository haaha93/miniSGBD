package bdd;

import java.util.ArrayList;

public class BDD {

	private ArrayList<Table> listeTables;

	public BDD() {
		listeTables = new ArrayList<Table>();
	}

	public boolean listTablesContient(String nomTable) {
		boolean rep = false;

		for (Table t : listeTables)
			if (t.getNomTable().equals(nomTable))
				rep = true;

		return rep;
	}

	public int indexTable(String nomTable) {
		int i = 0;
		while (!listeTables.get(i).getNomTable().equals(nomTable))
			i++;
		return i;
	}

	public void ajoutTable(String nomTable) throws BddException {
		if (listTablesContient(nomTable))
			throw new BddException("Table deja existante");

		else
			listeTables.add(new Table(nomTable));
	}

	public void supprimerTable(String nomTable) throws BddException {
		if (!listTablesContient(nomTable))
			throw new BddException("Table non existante");

		else {
			listeTables.remove(indexTable(nomTable));
		}
	}

	public void modifierNomTable(String nomTable, String nouveauNom) throws BddException {
		if (!listTablesContient(nomTable))
			throw new BddException("Table non existante");

		else if (listTablesContient(nouveauNom))
			throw new BddException("Table deja existante");

		else
			listeTables.get(indexTable(nomTable)).setNomTable(nouveauNom);
	}

	public void affichageDesTables() {
		for (Table t : listeTables)
			System.out.println(t.getNomTable());
		
	}
}
