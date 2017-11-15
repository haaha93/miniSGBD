package manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import constant.Constant;

public class DiskManager {
	public static void createFile(int fileID) { // cree un fichier si non
												// existant

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

	public static PageId addPage(PageId page) throws IOException { // ajoute une
																	// page au
																	// nombre
																	// total de
																	// pages

		File file = new File("/BDD/Data_" + page.getFileId() + ".rf");
		RandomAccessFile raf = new RandomAccessFile(file, "wb");
		int idx = (int) (file.length()/Constant.PAGESIZE);
		raf.seek(file.length());
		
		for (int i = 0; i < Constant.PAGESIZE; i++) {
			raf.writeByte((byte) 0);
		}

		raf.close();
		
		return (new PageId(page.getFileId(), idx));

	}

	public static void readPage(PageId page, ByteBuffer buffer) throws IOException {

		File file = new File("/BDD/Data_" + page.getFileId() + ".rf");
		RandomAccessFile raf = new RandomAccessFile(file, "rb");
		raf.seek(Constant.PAGESIZE * page.getIdx());
		
		for (int i = 0; i < Constant.PAGESIZE; i++)
			buffer.put(raf.readByte());
		
		raf.close();
	}

	public static void writePage(PageId page, ByteBuffer buffer) throws IOException {

		File file = new File("/BDD/Data_" + page.getIdx() + ".rf");
		RandomAccessFile raf = new RandomAccessFile(file, "wb");
		raf.seek(Constant.PAGESIZE * page.getIdx());
		
		for (int i = 0 ; i < Constant.PAGESIZE ; i++){
			raf.writeByte(buffer.get(i));
		}
		
		raf.close();
	}
}
