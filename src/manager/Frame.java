package manager;

public class Frame {
	
	private String buffer;
	private PageId pageId;
	private int pinCount;
	private boolean dirty,	
				refBit;
	
	public Frame() {
		buffer = "";
		pageId = null;
		pinCount = 0;
		dirty = false;
		refBit = false;
	}

	public String getBuffer() {
		return buffer;
	}

	public void setBuffer(String buffer) {
		this.buffer = buffer;
	}

	public PageId getPageId() {
		return pageId;
	}

	public void setPageId(PageId pageId) {
		this.pageId = pageId;
	}

	public int getPinCount() {
		return pinCount;
	}

	public void setPinCount(int pinCount) {
		this.pinCount = pinCount;
	}

	public boolean getRefBit() {
		return refBit;
	}

	public void setRefBit(boolean refBit) {
		this.refBit = refBit;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}	
	
	

}
