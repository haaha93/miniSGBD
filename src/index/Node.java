package index;

import java.util.Arrays;

public class Node {
	private Entry[] entries;
	private Node[] sons;
	private Node leftBrother;
	private Node rightBrother;
	private Node father;

	public Node(int d, Entry[] entries, Node[] sons, Node leftBrother, Node rightBrother, Node father) {
		this.entries = entries;
		this.sons = sons;
		this.leftBrother = leftBrother;
		this.rightBrother = rightBrother;
		this.father = father;
		if (this.entries == null)
			this.entries = new Entry[2 * d];
		if (this.sons == null)
			this.sons = new Node[2 * d + 1];
		for (int i = 0; i < 2 * d; i++)
			entries[i] = new Entry();
	}

	public Node(int d) {
		this.entries = new Entry[2 * d];
		this.sons = new Node[2 * d + 1];
		for (int i = 0; i < 2 * d; i++) {
			entries[i] = new Entry();
			sons[i] = null;
		}
		sons[2*d]=null;
	}

	public Entry[] getEntries() {
		return entries;
	}

	public Entry getEntry(int i) {
		return entries[i];
	}

	public void setEntries(Entry[] entries) {
		this.entries = entries;
	}

	public void setEntry(int i, Entry entry) {
		this.entries[i] = entry;
	}

	public Node[] getSons() {
		return sons;
	}

	public Node getSon(int i) {
		return sons[i];
	}

	public void setSons(Node[] sons) {
		this.sons = sons;
	}

	public void setSon(int i, Node son) {
		this.sons[i] = son;
	}

	public Node getLeftBrother() {
		return leftBrother;
	}

	public void setLeftBrother(Node leftBrother) {
		this.leftBrother = leftBrother;
	}

	public Node getRightBrother() {
		return rightBrother;
	}

	public void setRightBrother(Node rightBrother) {
		this.rightBrother = rightBrother;
	}

	public Node getFather() {
		return father;
	}

	public void setFather(Node father) {
		this.father = father;
	}

	public boolean isFull() {
		for (Entry e : entries)
			if (e.getValue() == null)
				return false;
		return true;
	}

	public boolean isEmpty() {
		for (Entry e : entries)
			if (e.getValue() != null)
				return false;
		return true;
	}

	public boolean isLeaf() {
		if (sons==null)
			return true;
		for (Node n : sons)
			if (n != null)
				return false;
		return true;
	}

	public int compareAt(int i, String value) {
		Entry e = this.getEntry(i);
		try {
			Float fThis = Float.parseFloat(e.getValue());
			Float fValue = Float.parseFloat(value);
			return fThis.compareTo(fValue);
		}
		catch (NumberFormatException nfe) {
			return e.getValue().trim().compareTo(value.trim());
		}
	}
	
	public String spe() {
		return this.disp(0)+"\t"+this.disp(1)+"\t"+this.disp(2)+"\t"+this.disp(3)+"\t"+this.disp(4);
	}
	
	public String disp(int i) {
		return sons[i]==null?"null":Arrays.toString(sons[i].getEntries());
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer(Arrays.toString(entries));
		s.append("\n"+spe());
		//System.out.println(s);
		s.append("\n"+(sons[0]==null?"null":sons[0].spe())+"\t"+(sons[1]==null?"null":sons[1].spe())+"\t"+(sons[2]==null?"null":sons[2].spe())+"\t"+(sons[3]==null?"null":sons[3].spe())+"\t"+(sons[4]==null?"null":sons[4].spe()));
		
//		for (int i = 0 ; i < entries.length ; i++)
//			s.append(entries[i].toString()+"\t");
//		s.append("\n");	
//		for (int i = 0 ; i < sons.length ; i++ )
//			s.append((sons[i]==null)?"null\t":sons[i].toString()+"\t");
		return s.toString();
	}

	
	
	


}
