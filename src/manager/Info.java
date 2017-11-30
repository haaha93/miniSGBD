package manager;

public class Info {
	
	private int idxPages;
	private int nbSlotsAvailable;
	
	public Info(int idxPages, int nbSlotsAvailable) {
		super();
		this.idxPages = idxPages;
		this.nbSlotsAvailable = nbSlotsAvailable;
	}

	public int getIdxPages() {
		return idxPages;
	}

	public void setIdxPages(int idxPages) {
		this.idxPages = idxPages;
	}

	public int getNbSlotsAvailable() {
		return nbSlotsAvailable;
	}

	public void setNbSlotsAvailable(int nbSlotsAvailable) {
		this.nbSlotsAvailable = nbSlotsAvailable;
	}
	
	public void incrementerNbslotsAvailable(int arg){
		this.nbSlotsAvailable+=arg;
	}

	@Override
	public String toString() {
		return "Info [idxPages=" + idxPages + ", nbSlotsAvailable=" + nbSlotsAvailable + "]";
	}
	
	

}
