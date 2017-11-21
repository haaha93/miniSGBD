package manager;

import java.io.Serializable;

public class PageId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int fileId, 
				idx;	

	public PageId(int fileId, int idx) {
		this.fileId = fileId; //numero du fichier
		this.idx = idx; // numero de la page du fichier courant
	}

	
	//getters setters
	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	public boolean equals(PageId pi) {
		return (pi.fileId==this.fileId && pi.idx==this.idx);
	}


	@Override
	public String toString() {
		return "PageId [fileId=" + fileId + ", idx=" + idx + "]";
	}
	
	
}

