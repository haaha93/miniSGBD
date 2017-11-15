package manager;

import java.io.IOException;
import java.nio.ByteBuffer;

import constant.Constant;

public class BufferManager {

	private static Frame[] frames;
	
	public static void bufferManager() {
		frames = new Frame[Constant.F];
		for (Frame f : frames) 
			f = new Frame();
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

		for (Frame f : frames) {
			if (f.getPageId().equals(pageToRead))
				return f.getBuffer();
		}

		for (Frame f : frames) {
			if (f.getPageId() == null) {
				f.setPageId(pageToRead);
				DiskManager.readPage(pageToRead, buffer);
				f.setBuffer(buffer);
				f.setPinCount(1);
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

		for (Frame f : frames) {
			if (f.getPageId().equals(pageToFree)) {
				f.setDirty(isDirty);
				f.setPinCount(0);
				f.setRefBit(true);
				return pageToFree;
			}
		}
		return null;
	}

	public static void flushAll() throws IOException {

		for (Frame f : frames) {
			if (f != null && f.isDirty())
				DiskManager.writePage(f.getPageId(), f.getBuffer());
		}
	}

}
