package console;

import java.io.IOException;
import java.util.StringTokenizer;

import bdd.GlobalManager;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class IG extends GridPane {
	private Label label;
	private TextField entrer;
	private static final TextArea result = new TextArea();
	private Button valider;
	
	public IG(){
		valider = new Button("Envoi");
		label = new Label("Enter command or type help");
		entrer = new TextField();
		
		result.setEditable(false);
		this.add(label, 1, 1);
		this.add(entrer, 1, 2);
		this.add(valider, 2, 2);
		this.add(result, 1, 3);
		
		valider.setOnAction((event)->{

			result.clear();
			StringTokenizer st = new StringTokenizer(entrer.getText());
			String[] rep = new String[st.countTokens()];
			entrer.clear();
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
				result.setText("\n\n\ncommand : \thelp\n"+"List and describ each command."+"\n\n\ncommand : \texit\n"
			+"End the program."	+"\n\n\ncommand : \tdisplay\n"+"Displays all relations."+"\n\n\ncommand : \tclean\n"
			+"Delete all files of database and clear database."+"\n\n\ncommand : \tcreate RelName NbCol TypeCol[1] TypeCol[2] ... TypeCol[NbCol]\n"
			+"Create a relation nammed RelName with NbCol columns and each columns has a type of int, float or stringT, T being the string length."
			+"\n\n\ncommand : \tinsert RelName Val[1] Val[2] ... Val[NbCol]\n"
			+"Insert a record of RelName relation with NbCol values of the relation."
	        +"\n\n\ncommand : \tfill RelName FileName.cvs\n"
			+"Add each record of file into relation."
			+"\n\n\ncommand : \tselectall RelName\n"
			+"Select each record of relation RelNam."
			+"\n\n\ncommand : \tselect RelName IndexColumn V\n"
			+"Select each record of relation RelNam with value V at column IndexColumn."
			+"\n\n\ncommand : \tjoin RelName1 RelName2 IndexColumnR1 IndexColumnR2\n"
			+"Select each record of relation RelName1 and RelName2 with common value between column IndexColumnR1 and IndexColumnR2."
			+"\n\n\ncommand : \tcreateindex RelName IndexColumn ValOrder\n"
			+"Create an index on indexColumn column of RelName of order ValOrder."
			+"\n\n\ncommand : \tselectindex RelName IndexColumn V\n"
			+"Select value V with index on indexColumn column of relation RelName."
			+"\n\n\ncommand : \tjoinindex RelName1 RelName2 IndexColumnR1 IndexColumnR2\n"
			+"Select each record of relation RelName1 and RelName2 with common value between column IndexColumnR1 and IndexColumnR2, this method use an index on RelName2 .");
				break;
				
			case "display":
				GlobalManager.displayRelSchema();
				break;
			case "create":
				try {
					GlobalManager.createRelation(rep);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				break;
			case "insert":
				try {
					GlobalManager.insert(rep);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case "exit":
				try {
					GlobalManager.finish();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				result.setText("Goodbye");
				break;
			case "fill":
				try {
					GlobalManager.fill(rep);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "selectall":
				try {
					GlobalManager.selectAll(rep[1]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "select":
				try {
					GlobalManager.select(rep);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "clean":
				try {
					GlobalManager.clean();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "join":
				try {
					GlobalManager.join(rep);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "joinindex":
				try {
					GlobalManager.joinindex(rep);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "createindex":
				try {
					GlobalManager.createIndex(rep);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "selectindex":
				try {
					GlobalManager.selectIndex(rep);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				result.setText(("Choice not existing"));
				break;
			}
			
		});
		
		
	}
	
	public static void println(String s){
	   
	           result.setText(result.getText()+s+"\n");
	            System.out.println(s);//for echo if you want
	    
	}
	
	
}
