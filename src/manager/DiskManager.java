package manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DiskManager {
	public static void createFile(int fileID) { //cree un fichier si non existant

		File file = new File("/BDD/Data_" + fileID + ".rf");

		try {
			if (file.createNewFile()) {
				System.out.println("File created");
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static PageId addPage(PageId page) throws IOException { //ajoute une page au nombre total de pages

		File file = new File("/BDD/Data_" + page.getFileId() + ".rf");
		RandomAccessFile raf = new RandomAccessFile(file, "wb");
		raf.seek(page.getPageSize()*page.getIdx());
		
		for(int i = 0; i < 65536; i++){
			raf.write('0');
		}
		
		return (new PageId(page.getFileId(),page.getIdx()+1));
		
	}

	public static void readPage(int pageID) { //a faire

	}

	public static void writePage(int fileID) {// a faire
	}
}
