package manager;

import java.util.List;

public class HeaderPageInfo {
	
	private int nbPagesDeDonnees;
	private List<Info> infos;
	
	
	public int getNbPagesDeDonnees() {
		return nbPagesDeDonnees;
	}
	public void setNbPagesDeDonnees(int nbPagesDeDonnees) {
		this.nbPagesDeDonnees = nbPagesDeDonnees;
	}
	public List<Info> getInfos() {
		return infos;
	}
	public void setInfos(List<Info> infos) {
		this.infos = infos;
	}
	public HeaderPageInfo(int nbPagesDeDonnees, List<Info> infos) {
		super();
		this.nbPagesDeDonnees = nbPagesDeDonnees;
		this.infos = infos;
	}

	
	
}
