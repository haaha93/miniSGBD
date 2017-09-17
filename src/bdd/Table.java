package bdd;

import java.util.ArrayList;

public class Table {
	
	private String nomTable;
	private ArrayList <String> listeAttributs;
	private ArrayList <Entite> listeEntitees;
	
	public Table(String nomTable) {
		super();
		this.nomTable = nomTable;
		listeAttributs = new ArrayList <String>();
	}

	public String getNomTable() {
		return nomTable;
	}

	public void setNomTable(String nomTable) {
		this.nomTable = nomTable;
	}

	public ArrayList<String> getListeAttributs() {
		return listeAttributs;
	}

	public void setListeAttributs(ArrayList<String> listeAttributs) {
		this.listeAttributs = listeAttributs;
	}

	public ArrayList<Entite> getListeEntitees() {
		return listeEntitees;
	}

	public void setListeEntitees(ArrayList<Entite> listeEntitees) {
		this.listeEntitees = listeEntitees;
	}
	
	public void ajouterAttribut(String nomAttribut) throws BddException{
		if (listeAttributs.contains(nomAttribut))
			throw new BddException("Attribut deja existant");
		else{
			listeAttributs.add(nomAttribut);
			for (int i = 0 ; i < listeEntitees.size() ; i++)
				listeEntitees.get(i).ajouterAttributs();
		}
	}
	
	public void supprimerAttribut(String nomAttribut) throws BddException{
		if (!listeAttributs.contains(nomAttribut))
			throw new BddException("Attribut non existant");
		else{
			for (int i = 0 ; i < listeEntitees.size() ; i++)
				listeEntitees.get(i).supprimerAttribut(listeAttributs.indexOf(nomAttribut));
			listeAttributs.remove(nomAttribut);
		}
	}

	public void modifierAttribut(String nomAttribut, String nouveauNom) throws BddException{
		if (!listeAttributs.contains(nomAttribut))
			throw new BddException("Attribut non existant");
		else if (listeAttributs.contains(nouveauNom))
			throw new BddException("Attribut deja existant");
		else
			listeAttributs.set(listeAttributs.indexOf(nomAttribut),nouveauNom);
	}
	
	public void creerEntiter(){
		listeEntitees.add(new Entite());
	}
	
	public void modfifierEntitee(int ligne, int indexAttribut, String valeur) throws BddException{
		if (ligne < 0 || ligne >= listeEntitees.size())
			throw new BddException("Ligne inexistante");
		else if (indexAttribut < 0 || indexAttribut >= listeAttributs.size())
			throw new BddException("Attributs inexistant");
		else
			listeEntitees.get(ligne).modifierValeurAttribut(indexAttribut, valeur);
	}
	
	public void supprimerEntitee(int ligne, int indexAttribut, String valeur) throws BddException{
		if (ligne < 0 || ligne >= listeEntitees.size())
			throw new BddException("Ligne inexistante");
		else
			listeEntitees.remove(ligne);
	}	
}
	
	