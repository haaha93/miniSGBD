package bdd;

import java.util.ArrayList;
import java.util.List;

public class Record {
	private List<String> values;

	public Record(List<String> values) {
		super();
		this.values = values;
	}

	public Record() {
		super();
		this.values = new ArrayList<String>();
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> record) {
		this.values = record;

	}

}
