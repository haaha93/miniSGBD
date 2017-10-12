package bdd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import manager.BufferManager;
import manager.HeapFile;

public class GlobalManager {

	private static Dbdef db;
	private static ArrayList<HeapFile> heapFiles;

	public static void init() {
		db = new Dbdef();
		BufferManager.bufferManager();
		heapFiles = new ArrayList<HeapFile>();
		refreshHeapFiles();
	}

	public static void createRelation(String[] userInput) throws IOException {
		db.addRelationToDB(userInput);
		HeapFile heapFile = new HeapFile(db.getListRelation().get(db.getListRelation().size() - 1));
		heapFiles.set(db.getCompteurRel() - 1, heapFile);
		heapFiles.get(db.getCompteurRel() - 1).createHeader();
	}

	public static void finish() throws IOException {
		Iterator it = db.getListRelation().iterator();
		File file = new File("Catalog.def");
		RandomAccessFile enrengistrerDb = new RandomAccessFile(file, "wb");

		enrengistrerDb.writeBytes(db.toString());

	}

	public static void refreshHeapFiles() {

		for (int i = 0; i < db.getListRelation().size(); i++) {
			if (!heapFiles.contains(db.getListRelation().get(i)))
				heapFiles.add(new HeapFile(db.getListRelation().get(i)));

		}

	}
	

	public static void insert(String name, String[] userInput) {
		Record record = new Record();
		List <String> values = new ArrayList<>(userInput.length-2);
		for (int i = 0 ; i < values.size() ; i++)
			values.set(i, userInput[i+2]);
		
		record.setValues(values);
	}


//	public static void insert(String name, String[] userInput) {
//		Record record = new Record();
//		List <String> typeColumns = db.geRelSchemaByName(name).getTypeColumns();
//		List <String> recordToSave = new ArrayList(typeColumns.size());
//		String type;
//		int longueur;
//		
//		for (int i = 0 ; i < recordToSave.size() ; i++){
//			 if (typeColumns.get(i).charAt(0)=='S'){
//				 type=typeColumns.get(i).substring(0, 6);
//				 longueur = Integer.parseInt((typeColumns.get(i).substring(6)));
//			 }
//			 else
//				 type = typeColumns.get(i);
//			 
//			 switch (type){
//			 case "int" : record.getValues().set(i, userInput[i+2]);
//			 }
//		}
//	}
}
