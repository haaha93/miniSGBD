package manager;

public class PageId {
	private int fileId, idx;
	private static long pageSize = 64*1024;

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

	public static long getPageSize() {
		return pageSize;
	}
	
	
	
	
	
	
}
