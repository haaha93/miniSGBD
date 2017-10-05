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
	public void createHeader(HeapFile heapFile) throws IOException {
		
		
		
		DiskManager.addPage(heapFile.relDef.getPage());
		BufferManager.getPage(heapFile.relDef.getPage());
		DiskManager.writePage(heapFile.relDef.getPage(), "0");
		BufferManager.freePAge(heapFile.relDef.getPage(), true);
		

	}

}
