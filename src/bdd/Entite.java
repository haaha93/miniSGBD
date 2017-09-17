package bdd;

import java.util.ArrayList;

public class Entite {
	
	private ArrayList <String> valeursAttributs;	

	public Entite() {
		super();
		valeursAttributs = new ArrayList <String>();
}

	public Entite(int nombreAttributs) {
		super();
		valeursAttributs = new ArrayList <String>(nombreAttributs);
	}
	
	public void supprimerAttribut(int indexAttribut){
		valeursAttributs.remove(indexAttribut);
	}
	
	public void modifierValeurAttribut(int indexAttribut, String valeur){
		valeursAttributs.set(indexAttribut, valeur);
	}
	
	public void ajouterAttributs(){
		valeursAttributs.add(null);
	}
	
	

}
