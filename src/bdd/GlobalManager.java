package bdd;

public class GlobalManager {
	
	private Dbdef db;
	
	public void init(){
		db = new Dbdef();
	}
	
	
	public void createRelation(String[] userInput){
		db.addRelationToDB(userInput);
	}
}
