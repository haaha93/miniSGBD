package manager;

import java.io.IOException;
import java.nio.ByteBuffer;

import constant.Constant;

public class BufferManager {

	private static Frame[] frames;

	public static void bufferManager() {
		frames = new Frame[Constant.F];
		for (int i = 0; i < Constant.F; i++)
			frames[i] = new Frame();

	}

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
		int i;
		boolean chosen = false;

		i = indexOfFrame(pageToRead);

		if (i != -1) {
			frames[i].setPinCount(true);
			return frames[i].getBuffer();
		}

		for (i = 0; i < frames.length; i++) {
			if (frames[i].getPageId() == null) {
				frames[i].setPageId(pageToRead);
				DiskManager.readPage(pageToRead, buffer);
				frames[i].setBuffer(buffer);
				frames[i].setPinCount(true);
				return frames[i].getBuffer();
			}
		}
		
		i=0;

		do

		{
			if (!frames[i].getPinCount() && frames[i].getRefBit())
				frames[i].setRefBit(false);
			else if (!frames[i].getPinCount() && !frames[i].getRefBit())
				chosen = true;
			i = (chosen) ? i : i + 1;
			i = (i == frames.length) ? 0 : i;
		} while (!chosen);

		if (frames[i].isDirty())
			DiskManager.writePage(frames[i].getPageId(), frames[i].getBuffer());
			

		frames[i].setPageId(pageToRead);
		DiskManager.readPage(pageToRead, buffer);
		frames[i].setBuffer(buffer);
		frames[i].setPinCount(true);
		frames[i].setRefBit(false);
		frames[i].setDirty(false);
		
		return frames[i].getBuffer();
	}

	public static void freePage(PageId pageToFree, boolean isDirty) {
		int index = indexOfFrame(pageToFree);

		if (index != -1) {
			if (isDirty)
				frames[index].setDirty(true);
			frames[index].setPinCount(false);
			frames[index].setRefBit(true);
		} else
			System.out.println("The page to free is not in the buffer");
	}

	public static void flushAll() throws IOException {

		for (int i = 0; i < frames.length; i++) {
			if (frames[i].isDirty())
				DiskManager.writePage(frames[i].getPageId(), frames[i].getBuffer());
			if (frames[i].getPinCount())
				System.err.println("A page was not liberated");
		}
		
		bufferManager();
	}

	public static void writePage(PageId pageToWrite, ByteBuffer bufferToWrite) {
		int index = indexOfFrame(pageToWrite);
		
		if (index != -1)
			frames[index].setBuffer(bufferToWrite);
		else
			System.out.println("The page to write is not in the buffer");
	}

	public static int indexOfFrame(PageId pageToFind) {
		int i;
		for (i = 0; i < frames.length; i++)
			if (frames[i].getPageId() != null && frames[i].getPageId().equals(pageToFind))
				return i;

		return -1;
	}
}
