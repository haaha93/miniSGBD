package console;

import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import bdd.BDD;
import bdd.GlobalManager;
//import manager.DiskManager;

public class Menu {

	/**
	 * Console interface between user and DBMS.
	 */
	public static void console() {

		BDD bdd = new BDD();
		Scanner sc = new Scanner(System.in);
		StringTokenizer st;
		String[] rep;
		int i;

		do {

			System.out.println("Enter your command line (help for list of commands");
			st = new StringTokenizer(sc.nextLine());
			rep = new String[st.countTokens()];
			i = 0;

			while (st.hasMoreTokens()) {

				rep[i++] = st.nextToken();

			}

			switch (rep[0]) {
			case "help":
				help();
			case "display":
				bdd.displayRelSchema();
				break;
			case "create":
				bdd.addRelSchema(rep);
				break;
			case "insert":
				GlobalManager.insert(rep[1], rep);
			case "exit":
				System.out.println("Goodbye");
			default:
				System.out.println("Choice not existing");
			}

		} while (!rep[0].equals("exit"));
		sc.close();
	}

	/**
	 * Display of console's command.
	 */
	public static void help() {
		System.out.println("\n\n\ncommand : \thelp\n");
		System.out.println("List and describ each command.");

		System.out.println("\n\n\ncommand : \texit\n");
		System.out.println("End the program.");

		System.out.println("\n\n\ncommand : \tcreate RelName NbCol TypeCol[1] TypeCol[2] ... TypeCol[NbCol]\n");
		System.out.println(
				"Create a relation nammed RelName with NbCol columns and each columns has a type of int, float or stringT, T being the string length.");
		
		System.out.println("\n\n\ncommand : \tinsert RelName Val[1] Val[2] ... Val[NbCol]\n");
		System.out.println(
				"Insert a record of RelName relation with NbCol values of the relation");
		
		System.out.println("\n\n\ncommand : \tdisplay\n");
		System.out.println("Displays all relations.");
	}

	/**
	 * First step, ask the password of the data base but actually launch an
	 * exception god knows for what...
	 */
	// public static void displayConnexion() {
	//
	// boolean mdpIsOkay = false;
	// String reponse;
	// Scanner sc = new Scanner(System.in);
	//
	// do {
	// System.out.println("***Please, enter password***(Type \"exit\" to
	// egress)");
	// reponse = (sc.next());
	// mdpIsOkay = reponse.equals("mdp");
	//
	// } while (!reponse.equals("exit") && (mdpIsOkay == false));
	//
	// sc.close();
	//
	// if (reponse.equals("exit")) {
	// System.out.println("Goodbye");
	// System.exit(0);
	// }
	//
	// console();
	//
	// }
}
