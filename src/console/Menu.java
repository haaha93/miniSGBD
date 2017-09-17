package console;

import java.util.Scanner;

import javax.swing.JOptionPane;

import bdd.BDD;
import bdd.BddException;

public class Menu {

	public static void affichageMenu() {

		BDD bdd = new BDD();
		int rep = 0;
		Scanner sc = new Scanner(System.in);
		String s;

		do {
			try {
				System.out.println("\n******************   Menu   ******************\n");
				System.out.println("1) Afficher les tables");
				System.out.println("2) Creer une table");
				System.out.println("3) Modifier une table");
				System.out.println("4) Supprimer une table");
				System.out.println("5) Quittez");

				rep = sc.nextInt();

				switch (rep) {
				case 1:
					bdd.affichageDesTables();
					break;
				case 2:
					System.out.println("Nom de la table ? ");
					s=sc.nextLine();
					bdd.ajoutTable(s);
					break;
				case 3:
					System.out.println("Nom de la table ? ");
					s=sc.nextLine();
					modifierTable(bdd,s);
					break;
				case 4:
					System.out.println("Nom de la table ? ");
					s=sc.nextLine();
					bdd.supprimerTable(s);
					break;
				case 5:
					break;
				default:
					System.out.println("Choix impossible");
				}
			} catch (BddException e) {
			}
		} while (rep != 5);
		sc.close();
	}

	private static void modifierTable(BDD bdd, String nextLine) {
		// TODO Auto-generated method stub
		
	}

	public static void affichageConnexion() {

		boolean mdpIsOkay = false;
		String reponse;
		Scanner sc = new Scanner(System.in);

		do {
			System.out.println("***Entrez le mot de passe***(Tapez \"exit\" pour quitter)");
			reponse = (sc.nextLine());
			mdpIsOkay=reponse.equals("mdp");

		} while (!reponse.equals("exit") && (mdpIsOkay == false));

		sc.close();

		if (reponse.equals("exit")) {
			System.exit(0);
		}

		affichageMenu();

	}
}
