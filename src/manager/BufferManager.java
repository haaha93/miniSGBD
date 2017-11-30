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

		System.out.println("avant getPage");
		for (Frame f : frames)
			System.out.println(f.toString()+((f.getBuffer()==null)?"":f.getBuffer().getInt(0)));

		i = indexOfFrame(pageToRead);

		if (i != -1) {
			frames[i].setPinCount(1);
			return frames[i].getBuffer();
		} else
			i = 0;

		for (int j = 0; j < frames.length; j++) {
			if (frames[j].getPageId() == null) {
				frames[j].setPageId(pageToRead);
				DiskManager.readPage(pageToRead, buffer);
				frames[j].setBuffer(buffer);
				frames[j].setPinCount(1);
				return frames[j].getBuffer();
			}
		}

		do

		{
			if (frames[i].getPinCount() == 0 && frames[i].getRefBit())
				frames[i].setRefBit(false);
			else if (frames[i].getPinCount() == 0 && !frames[i].getRefBit())
				chosen = true;
			i = (chosen) ? i : i + 1;
			i = (i == frames.length) ? 0 : i;
		} while (!chosen);

		if (frames[i].isDirty())
			DiskManager.writePage(frames[i].getPageId(), frames[i].getBuffer());

		frames[i].setPageId(pageToRead);
		DiskManager.readPage(pageToRead, buffer);
		frames[i].setBuffer(buffer);
		frames[i].setPinCount(1);
		frames[i].setRefBit(false);
		frames[i].setDirty(false);

		System.out.println("apres getPage");
		for (Frame f : frames)
			System.out.println(f.toString()+((f.getBuffer()==null)?"":f.getBuffer().getInt(0)));

		return buffer;
	}

	public static void freePage(PageId pageToFree, boolean isDirty) {

		System.out.println("avant freePage");
		for (Frame f : frames)
			System.out.println(f.toString()+((f.getBuffer()==null)?"":f.getBuffer().getInt(0)));

		int index = indexOfFrame(pageToFree);

		if (index != -1) {
			if (isDirty)
				frames[index].setDirty(true);
			frames[index].setPinCount(0);
			frames[index].setRefBit(true);
		} else
			System.out.println("The page to free is not in the buffer");

		System.out.println("apres freePage");
		for (Frame f : frames)
			System.out.println(f.toString()+((f.getBuffer()==null)?"":f.getBuffer().getInt(0)));
	}

	public static void flushAll() throws IOException {

		for (int i = 0; i < frames.length; i++) {
			if (frames[i].isDirty())
				DiskManager.writePage(frames[i].getPageId(), frames[i].getBuffer());
		}

		System.out.println("apres flushAll");
		for (Frame f : frames)
			System.out.println(f.toString()+((f.getBuffer()==null)?"":f.getBuffer().getInt(0)));
	}

	public static void writePage(PageId pageToWrite, ByteBuffer bufferToWrite) {
		System.out.println("avant writePage");
		for (Frame f : frames)
			System.out.println(f.toString()+((f.getBuffer()==null)?"":f.getBuffer().getInt(0)));
		
		int index = indexOfFrame(pageToWrite);

		if (index != -1)
			frames[index].setBuffer(bufferToWrite);
		else
			System.out.println("The page to write is not in the buffer");

		System.out.println("apres writePage");
		for (Frame f : frames)
			System.out.println(f.toString()+((f.getBuffer()==null)?"":f.getBuffer().getInt(0)));
	}

	public static int indexOfFrame(PageId pageToFind) {
		int i = 0;
		for (int j = 0; j < frames.length; j++)
			if (frames[j].getPageId() != null && frames[j].getPageId().equals(pageToFind))
				return i;

		return -1;
	}

}
