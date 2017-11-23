package index;

import java.util.Arrays;

public class Node {
	private Entry[] entries;
	private Node[] sons;
	private Node leftBrother;
	private Node rightBrother;
	private Node father;
	
	public Node(int d,Entry[] entries, Node[] sons, Node leftBrother, Node rightBrother, Node father) {
		this.entries = entries;
		this.sons = sons;
		this.leftBrother = leftBrother;
		this.rightBrother = rightBrother;
		this.father = father;
		if (this.entries==null)
			entries = new Entry[d];
		if (this.sons==null)
			this.sons= new Node[d];
	}
	
	public Node(int d) {
		new Node(d,null,null,null,null,null);
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
	
	public void setEntries(int i, Entry entry) {
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
	
	public void setSon(int i,Node son) {
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
		if (entries==null)
			return false;
		for (Entry e : entries)
			if (e==null)
				return false;
		return true;
	}
	
	public boolean isEmpty() {
		if (entries==null)
			return true;
		for (Entry e : entries)
			if (e!=null)
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

	public int compareAt(int i , String value) {
		Entry e = this.getEntries()[i];
		return e.getValue().compareTo(value);
	}
	
	@Override
	public String toString() {
		return "Node [entries=" + Arrays.toString(entries) + ", sons=" + Arrays.toString(sons) + ", leftBrother="
				+ leftBrother + ", rightBrother=" + rightBrother + ", father=" + father + "]";
	}
	
	
}
