package manager;

import java.io.IOException;

import constant.Constant;

public class BufferManager{
	
	private static Frame [] frames;
	
	public BufferManager() {
		frames = new Frame [Constant.F];
		for (Frame f : frames)
			f = new Frame();
	}
	
	// Algo "clock" tourne a l'infini si pinCount == 1 pour chaque frame
	public static String getPage(PageId pageToRead) throws IOException {
		String s="";
		int i = 0;
		boolean chosen = false;
		
		for (Frame f : frames) {
			if (f.getPageId().equals(pageToRead))
				return f.getBuffer();
		}
		
		for (Frame f : frames) {
			if (f.getPageId()==null) {
				f.setPageId(pageToRead);
				DiskManager.readPage(pageToRead, s);
				f.setBuffer(s);
				f.setPinCount(1);
				return s;
			}
		}
		
		do {
			if (frames[i].getPinCount()==0 && frames[i].getRefBit())
				frames[i].setRefBit(false);
			else if (frames[i].getPinCount()==0 && !frames[i].getRefBit())
				chosen=true;
			i=chosen?i:i+1;
			i=i==Constant.F?0:i;
		} while (!chosen);
		
		frames[i].setPageId(pageToRead);
		DiskManager.readPage(pageToRead, s);
		frames[i].setBuffer(s);
		frames[i].setPinCount(1);
		
		return s;
	}
	
	public static PageId freePAge(PageId pageToFree,boolean isDirty) {
		
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

}
