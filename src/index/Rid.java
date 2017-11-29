package index;

public class Rid {
	private int idxPage;
	private int offset;	
	
	public Rid(int idxPage, int offset) {
		super();
		this.idxPage = idxPage;
		this.offset = offset;
	}	
	
	public Rid() {
		super();
	}

	public int getIdxPage() {
		return idxPage;
	}
	public void setIdxPage(int idxPage) {
		this.idxPage = idxPage;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "(" + idxPage + "," + offset + ")";
	}	
	
}
