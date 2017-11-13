package bdd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import constant.Constant;
import manager.BufferManager;
import manager.HeapFile;
import manager.PageId;

public class GlobalManager {

	private static Dbdef db;
	private static ArrayList<HeapFile> heapFiles;

	public static void init() throws IOException {
		try {
			File file = new File("Catalog.def");
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			db = (Dbdef) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			db = new Dbdef();
		} finally {
			BufferManager.bufferManager();
			heapFiles = new ArrayList<HeapFile>();
			refreshHeapFiles();
		}
	}

	/**
	 * 
	 * @param userInput
	 *            , tab of String containing user's values. Add a relation in the
	 *            database.
	 */
	public static void createRelation(String[] userInput) throws IOException {
		RelSchema relSchema = new RelSchema(userInput);
		int sizeRecord = calculRecordSize(relSchema);
		int slotCount = (int) (Constant.PAGESIZE / (sizeRecord + 1));
		int index = db.getCompteurRel();
		RelDef relDef = new RelDef(relSchema,index,sizeRecord,slotCount);
		
		db.addRelationToDBAtIndex(relDef, index);
		HeapFile heapFile = new HeapFile(relDef);
		heapFiles.add(index, heapFile);
		heapFiles.get(index).createHeader();
	}

	/**
	 * Save the DataBase db into a file Catalog.def
	 * 
	 * @throws IOException
	 */
	public static void finish() throws IOException {
		File file = new File("Catalog.def");
		try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos);) {
			oos.writeObject(db);
		}
	}

	public static void refreshHeapFiles() throws IOException {
		RelDef relDef;
		RelSchema relSchema;
		int sizeRecord;
		
		for (int i = 0; i < db.getListRelation().size(); i++) 
			if (!heapFiles.contains(db.getListRelation().get(i))) {
				relSchema = db.getListRelation().get(i).getRelSchema();
				sizeRecord = calculRecordSize(relSchema);
				relDef = new RelDef(relSchema,i,sizeRecord,Constant.PAGESIZE*(sizeRecord+1));
				heapFiles.add(i,new HeapFile(relDef));
				heapFiles.get(i).createHeader();
			}
	}

	public static void insert(String name, String[] userInput) throws IOException {
		Record record = new Record();
		List<String> values = new ArrayList<>(userInput.length - 2);
		int indexOfRelDef;
		for (int i = 0; i < values.size(); i++)
			values.set(i, userInput[i + 2]);

		record.setValues(values);
		
		indexOfRelDef = db.getIndexOfRelSchemaByName(name);
		if (indexOfRelDef!=-1)
			heapFiles.get(indexOfRelDef).insertRecord(record);
		
		//TODO the else
		
	}

	public static int calculRecordSize(RelSchema relSchema) {
		List<String> typeColumns = relSchema.getTypeColumns();
		String type;
		int longueur = 0;
		int recordSize = 0;

		for (int i = 0; i < typeColumns.size(); i++) {
			if (typeColumns.get(i).charAt(0) == 'S') {
				type = typeColumns.get(i).substring(0, 6);
				longueur = Integer.parseInt((typeColumns.get(i).substring(6)));
			} else
				type = typeColumns.get(i);

			switch (type) {
			case "int":
			case "float":
				recordSize += 4;
				break;
			case "String":
				recordSize += 2 * longueur;
				break;
			}
		}

		return recordSize;
	}

	/**
	 * Display of each relation, begins at 0.
	 */
	public static void displayRelSchema() {
		int i = 0;
		for (RelDef r : db.getListRelation()) {
			System.out.print("Relation " + i++ + " : ");
			r.getRelSchema().display();
		}
	}
}
