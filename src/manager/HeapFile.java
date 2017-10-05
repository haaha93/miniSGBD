package manager;

import java.io.IOException;

import bdd.RelDef;

public class HeapFile {

	private RelDef relDef;

	public HeapFile(RelDef relDef) {
		this.relDef = relDef;
	}

	// Le Header devra avoir un nom de fichier different des autres noms de
	// fichier relations
	public void createHeader() throws IOException {

		DiskManager.addPage(relDef.getPage());
		BufferManager.getPage(relDef.getPage());
		DiskManager.writePage(relDef.getPage(), "0");
		BufferManager.freePAge(relDef.getPage(), true);

	}

}
