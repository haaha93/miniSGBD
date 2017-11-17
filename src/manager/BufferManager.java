package manager;

import java.io.IOException;
import java.nio.ByteBuffer;

import constant.Constant;

public class BufferManager {

	private static Frame[] frames;
	
	public static void bufferManager() {
		frames = new Frame[Constant.F];
		for (int i = 0 ; i < Constant.F ; i++) 
			frames[i] = new Frame();
	}

	// Algo "clock" tourne a l'infini si pinCount == 1 pour chaque frame
	/**
	 * Fonction getPage puts a page to current frames if the clock algorithm allows
	 * it
	 * 
	 * @param pageToRead
	 * @return
	 * @throws IOException
	 */
	public static ByteBuffer getPage(PageId pageToRead) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate((int) Constant.PAGESIZE);
		int i = 0;
		boolean chosen = false;

		for (int j = 0 ; j < frames.length ; j++ ) {
			if (frames[j].getPageId()!=null && frames[j].getPageId().equals(pageToRead))
				return frames[j].getBuffer();
		}

		for (int j = 0 ; j < frames.length ; j++) {
			if (frames[j].getPageId() == null) {
				frames[j].setPageId(pageToRead);
				DiskManager.readPage(pageToRead, buffer);
				frames[j].setBuffer(buffer);
				frames[j].setPinCount(1);
				return buffer;
			}
		}

		do {
			if (frames[i].getPinCount() == 0 && frames[i].getRefBit())
				frames[i].setRefBit(false);
			else if (frames[i].getPinCount() == 0 && !frames[i].getRefBit())
				chosen = true;
			i = chosen ? i : i + 1;
			i = i == Constant.F ? 0 : i;
		} while (!chosen);

		frames[i].setPageId(pageToRead);
		DiskManager.readPage(pageToRead, buffer);
		frames[i].setBuffer(buffer);
		frames[i].setPinCount(1);

		return buffer;
	}

	public static PageId freePage(PageId pageToFree, boolean isDirty) {

		for (int i = 0 ; i < frames.length ; i++ ) {
			if (frames[i].getPageId().equals(pageToFree)) {
				frames[i].setDirty(isDirty);
				frames[i].setPinCount(0);
				frames[i].setRefBit(true);
				return pageToFree;
			}
		}
		return null;
	}

	public static void flushAll() throws IOException {

		for (int i = 0 ; i < frames.length ; i++) {
			if (frames[i].isDirty())
				DiskManager.writePage(frames[i].getPageId(), frames[i].getBuffer());
		}
	}

}
