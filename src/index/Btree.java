package index;

public class Btree {
	private Node root;
	private int d;
	private int key;

	public Btree(int d, int key) {
		this.d = d;
		this.key = key;
		root = new Node(d);
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public int getD() {
		return d;
	}

	public void setD(int d) {
		this.d = d;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "Btree [root=" + root + ", d=" + d + ", key=" + key + "]";
	}

	public Entry search(String value, Node node) {
		if (node.isEmpty())
			return null;
		if (node.isLeaf()) {
			for (Entry e : node.getEntries())
				if (e.getValue() != null && e.getValue().equals(value))
					return e;
			return null;
		} else
			for (int i = 0; i < 2 * d; i++)
				if (node.getEntry(i) != null && node.compareAt(i, value) > 0)
					return search(value, node.getSon(i));
		return search(value, node.getSon(2 * d + 1));

	}

	public Entry search(String value) {
		return search(value, root);
	}

}
