package bdd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import constant.Constant;
import manager.BufferManager;
import manager.HeapFile;

public class GlobalManager {

	private static Dbdef db;
	private static ArrayList<HeapFile> heapFiles;

	public static void init() throws IOException {
		try {
			File file = new File("Catalog.def");
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			db = (Dbdef) ois.readObject();
			ois.close();
			fis.close();
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
		RelDef relDef = new RelDef(relSchema, index, sizeRecord, slotCount);

		db.addRelationToDBAtIndex(index, relDef);
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
		BufferManager.flushAll();
	}

	public static void refreshHeapFiles() throws IOException {
		RelDef relDef;
		RelSchema relSchema;
		int sizeRecord;

		for (int i = 0; i < db.getListRelation().size(); i++) {
			relSchema = db.getListRelation().get(i).getRelSchema();
			sizeRecord = calculRecordSize(relSchema);
			relDef = new RelDef(relSchema, i, sizeRecord, Constant.PAGESIZE / (sizeRecord + 1));
			heapFiles.add(i, new HeapFile(relDef));
		}
	}

	public static void insert(String[] userInput) throws IOException {
		String name = userInput[1];
		Record record = new Record();
		List<String> values = new ArrayList<>(userInput.length - 2);
		int indexOfRelDef;
		for (int i = 0; i < userInput.length - 2; i++)
			values.add(i, userInput[i + 2]);

		record.setValues(values);

		indexOfRelDef = db.getIndexOfRelSchemaByName(name);
		if (indexOfRelDef != -1)
			heapFiles.get(indexOfRelDef).insertRecord(record);

		else
			System.out.println("Relation does not exist");

	}

	public static int calculRecordSize(RelSchema relSchema) {
		List<String> typeColumns = relSchema.getTypeColumns();
		String type;
		int longueur = 0;
		int recordSize = 0;

		for (int i = 0; i < typeColumns.size(); i++) {
			if (typeColumns.get(i).charAt(0) == 'S' || typeColumns.get(i).charAt(0) == 's') {
				type = typeColumns.get(i).substring(0, 6);
				try {
					longueur = Integer.parseInt(typeColumns.get(i).substring(6));
				} catch (NumberFormatException nfe) {
					longueur = Constant.STRINGSIZE;
				}
			} else
				type = typeColumns.get(i);

			switch (type.toLowerCase()) {
			case "int":
				recordSize += 4;
				break;
			case "float":
				recordSize += 4;
				break;
			case "string":
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
			System.out.print(++i + ". ");
			r.getRelSchema().display();
		}
	}


	public static void fill(String[] userInput) throws IOException {
		String relName = userInput[1];
		int indexOfRel = db.getIndexOfRelSchemaByName(relName);

		if (indexOfRel == -1)
			System.out.println("Relation does not exist");

		else {
			if (userInput[2].substring(userInput[2].length() - 4).equals(".csv")) {
				File file = new File(userInput[2]);
				RandomAccessFile raf = new RandomAccessFile(file, "r");
				HeapFile hf = heapFiles.get(indexOfRel);
				ArrayList<String> values = new ArrayList<>();
				StringTokenizer st = new StringTokenizer("", ",");
				String s = "";

				raf.seek(0);

				for (s = raf.readLine(); s != null; s = raf.readLine()) {
					st = new StringTokenizer(s, ",");
					while (st.hasMoreTokens())
						values.add(st.nextToken());
					hf.insertRecord(new Record(values));
					values.clear();
				}

				raf.close();
			}

			else
				System.out.println("Wrong file extension, must be \".csv\"");
		}
	}

	public static void selectAll(String relName) throws IOException {
		int indexOfRel = db.getIndexOfRelSchemaByName(relName);
		if (indexOfRel == -1)
			System.out.println("Relation does not exist");
		else
			heapFiles.get(db.getIndexOfRelSchemaByName(relName)).printAllRecords();

	}

	public static void select(String[] userInput) throws IOException {

		int indexOfRel = db.getIndexOfRelSchemaByName(userInput[1]);

		if (indexOfRel == -1)
			System.out.println("Relation does not exist");

		else {
			int indexColumn = Integer.parseInt(userInput[2]);
			String value = userInput[3];

			heapFiles.get(indexOfRel).printAllRecordsWithFilter(indexColumn, value);
		}
	}

	public static void clean() {
		File file = new File("Catalog.def");
		file.delete();
		for (int i = 0; i < heapFiles.size(); i++) {
			file = new File("BDD" + File.separator + "Data_" + i + ".rf");
			file.delete();
		}

		db.clean();
		heapFiles.clear();
	}

	public static void join(String[] userInput) throws IOException {
		int indexOfR = db.getIndexOfRelSchemaByName(userInput[1]);
		int indexOfS = db.getIndexOfRelSchemaByName(userInput[2]);

		if (indexOfR == -1 || indexOfS == -1)
			System.out.println("At least one relation does not exist");
		else {
			HeapFile hp1 = heapFiles.get(indexOfR);
			HeapFile hp2 = heapFiles.get(indexOfS);
			hp1.join(hp2, Integer.parseInt(userInput[3])-1, Integer.parseInt(userInput[4])-1);
		}
	}

	public static void createIndex(String[] userInput) throws IOException {
		int index = db.getIndexOfRelSchemaByName(userInput[1]);

		if (index == -1)
			System.out.println("Relation does not exist");
		else {
			int key = Integer.parseInt(userInput[2]);
			int d = Integer.parseInt(userInput[3]);
			heapFiles.get(index).createIndex(d, key - 1);
		}

	}

	// for testing b+tree
	public static void d() {
		heapFiles.get(0).display();
	}

	public static void selectIndex(String[] userInput) throws IOException {
		int index = db.getIndexOfRelSchemaByName(userInput[1]);

		if (index == -1)
			System.out.println("Relation does not exist");
		else {
			int key = Integer.parseInt(userInput[2]);
			String value = userInput[3];
			heapFiles.get(index).selectIndex(key - 1, value);
		}
	}

}
