package manager;

import java.nio.ByteBuffer;


public class Frame {
	
	private ByteBuffer buffer;
	private PageId pageId;
	private boolean pinCount;
	private boolean dirty;	
	private boolean	refBit;
	
	public Frame() {
		buffer = null;
		pageId = null;
		pinCount = false;
		dirty = false;
		refBit = false;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public PageId getPageId() {
		return pageId;
	}

	public void setPageId(PageId pageId) {
		this.pageId = pageId;
	}

	public boolean getPinCount() {
		return pinCount;
	}

	public void setPinCount(boolean pinCount) {
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

	@Override
	public String toString() {
		return "Frame [pageId=" + pageId + ", pinCount=" + pinCount + ", dirty=" + dirty + ", refBit=" + refBit + "]";
	}	
	
	

}
