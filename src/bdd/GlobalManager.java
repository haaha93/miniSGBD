package bdd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

public class GlobalManager {
	
	private Dbdef db;
	
	public void init(){
		db = new Dbdef();
	}
	
	
	public void createRelation(String[] userInput){
		db.addRelationToDB(userInput);
	}
	
	public void finish() throws IOException{
		Iterator it = db.getListRelation().iterator();
		File file = new File("Catalog.def");
		RandomAccessFile enrengistrerDb = new RandomAccessFile(file, "wb");
		
		enrengistrerDb.writeBytes(db.toString());
		
	}
}
