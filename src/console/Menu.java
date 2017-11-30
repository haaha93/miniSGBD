package console;

import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

import bdd.GlobalManager;

public class Menu {

	/**
	 * Console interface between user and DBMS.
	 * @throws IOException 
	 */
	public static void console() throws IOException {

		GlobalManager.init();
		String[] rep;
		Scanner sc = new Scanner(System.in);
		
		do {

			System.out.println("Enter your command line (help for list of commands)");
			
			StringTokenizer st = new StringTokenizer(sc.nextLine());
			rep = new String[st.countTokens()];
			
			if (rep.length==0) {
				rep = new String[1];
				rep[0] = "";
			}
			int i = 0;

			while (st.hasMoreTokens()) {
				rep[i++] = st.nextToken();

			}

			switch (rep[0].toLowerCase()) {
			case "help":
				help();
				break;
			case "display":
				GlobalManager.displayRelSchema();
				break;
			case "create":
				GlobalManager.createRelation(rep);
				break;
			case "insert":
				GlobalManager.insert(rep);
				break;
			case "exit":
				GlobalManager.finish();
				System.out.println("Goodbye");
				break;
			case "fill":
				GlobalManager.fill(rep);
				break;
			case "selectall":
				GlobalManager.selectAll(rep[1]);
				break;
			case "select":
				GlobalManager.select(rep);
				break;
			case "clean":
				GlobalManager.clean();
				break;
			case "join":
				GlobalManager.join(rep);
				break;
			case "createindex":
				GlobalManager.createIndex(rep);
				break;
			case "selectindex":
				GlobalManager.selectIndex(rep);
				break;
			//for testing b+tree
			case "d":
				GlobalManager.d();
				break;
			default:
				System.out.println("Choice not existing");
				break;
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

		System.out.println("\n\n\ncommand : \tdisplay\n");
		System.out.println("Displays all relations.");
		
		System.out.println("\n\n\ncommand : \tclean\n");
		System.out.println("Delete all files of database and clear database.");

		System.out.println("\n\n\ncommand : \tcreate RelName NbCol TypeCol[1] TypeCol[2] ... TypeCol[NbCol]\n");
		System.out.println("Create a relation nammed RelName with NbCol columns and each columns has a type of int, float or stringT, T being the string length.");
		
		System.out.println("\n\n\ncommand : \tinsert RelName Val[1] Val[2] ... Val[NbCol]\n");
		System.out.println("Insert a record of RelName relation with NbCol values of the relation.");		

		System.out.println("\n\n\ncommand : \tfill RelName FileName.cvs\n");
		System.out.println("Add each record of file into relation.");
		
		System.out.println("\n\n\ncommand : \tselectall RelName\n");
		System.out.println("Select each record of relation RelNam.");
		
		System.out.println("\n\n\ncommand : \tselect RelName IndexColumn V\n");
		System.out.println("Select each record of relation RelNam with value V at column IndexColumn.");
		
		System.out.println("\n\n\ncommand : \tjoin RelName1 RelName2 IndexColumnR1 IndexColumnR2\n");
		System.out.println("Select each record of relation RelNam1 and RelName2 with common value between column IndexColumnR1 and IndexColumnR2 .");
	
		System.out.println("\n\n\ncommand : \tcreateindex RelName IndexColumn ValOrder\n");
		System.out.println("Create an index on indexColumn column of RelName of order ValOrder.");
		
		System.out.println("\n\n\ncommand : \tselectindex RelName IndexColumn V\n");
		System.out.println("Select value V with index on indexColumn column of relation RelName.");
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
