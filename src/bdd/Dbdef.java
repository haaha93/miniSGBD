package bdd;

import java.util.ArrayList;
import java.util.List;

public class Dbdef {
	private List<RelDef> db;
	private int compteurRel;

	public Dbdef(List<RelDef> db) {
		this.db = new ArrayList<RelDef>();
		this.compteurRel = db.size();
	}

	public Dbdef() {
		this.db = new ArrayList<RelDef>();
		this.compteurRel = 0;
	}

}
