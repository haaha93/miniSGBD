package index;

import java.util.ArrayList;
import java.util.List;

public class Entry {
	private String value;
	private List<Rid> rids;

	public Entry(String value) {
		this.value = value;
		this.rids = new ArrayList<Rid>();
	}
	
	public Entry() {
		this(null);
	}

	public Entry(String value, Rid rid) {
		this(value);
		rids.add(rid);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Rid> getRids() {
		return rids;
	}

	public void setRids(List<Rid> rids) {
		this.rids = rids;
	}

	public void addRid(Rid rid) {
		rids.add(rid);
	}

	@Override
	public String toString() {
		return "{"+ value +"}";
	}


}
