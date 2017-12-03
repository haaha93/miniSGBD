package bdd;

import java.util.ArrayList;
import java.util.List;

public class Record {
	private List<String> values;

	
	/**
	 * create an instance of a record
	 * @param a List of values for a specified record
	 */
	public Record(List<String> values) {
		this.values = values;
	}

	public Record() {
		this.values = new ArrayList<String>();
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> record) {
		this.values = record;

	}
	
	public String getValueAtIndex(int index) {
		return values.get(index);
	}

	
	/**
	 * return the record as String
	 * 
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("");
		for (String s : values)
			sb.append(s+", ");
		return sb.substring(0, sb.length()-2);
	}

	
}
