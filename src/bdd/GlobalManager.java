package bdd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;

import manager.HeapFile;

public class GlobalManager {

	private static Dbdef db;
	private static ArrayList<HeapFile> heapFiles;

	public void init() {
		db = new Dbdef();
		heapFiles = new ArrayList<HeapFile>();
		refreshHeapFiles();
	}

	public void createRelation(String[] userInput) {
		db.addRelationToDB(userInput);
	}

	public void finish() throws IOException {
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
}
