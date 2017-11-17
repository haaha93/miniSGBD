package manager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import constant.Constant;

public class DiskManager {
	public static void createFile(int fileID) throws IOException { // cree un
																	// fichier
																	// si non
		// existant
		File file = new File("BDD" + File.separator + "Data_" + fileID + ".rf");
		file.createNewFile();
	}

	public static PageId addPage(int fileID) throws IOException { // ajoute une
																	// page au
																	// nombre
																	// total de
																	// pages

		File file = new File("BDD" + File.separator + "Data_" + fileID + ".rf");
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		int idx = (int) (file.length() / Constant.PAGESIZE);
		raf.seek(file.length());

		for (int i = 0; i < Constant.PAGESIZE; i++) {
			raf.writeByte((byte) 0);
		}

		raf.close();

		return (new PageId(fileID, idx));

	}

	public static void readPage(PageId page, ByteBuffer buffer) throws IOException {

		if (page != null) {

			buffer.position(0);
			File file = new File("BDD" + File.separator + "Data_" + page.getFileId() + ".rf");
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.seek(Constant.PAGESIZE * page.getIdx());

			for (int i = 0; i < Constant.PAGESIZE; i++)
				buffer.put(raf.readByte());

			raf.close();

		}
	}

	public static void writePage(PageId page, ByteBuffer buffer) throws IOException {

		if (page != null) {

			buffer.position(0);
			File file = new File("BDD" + File.separator + "Data_" + page.getFileId() + ".rf");
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			raf.seek(Constant.PAGESIZE * page.getIdx());

			for (int i = 0; i < Constant.PAGESIZE; i++) {
				raf.writeByte(buffer.get(i));
			}

			raf.close();

		}
	}
}
