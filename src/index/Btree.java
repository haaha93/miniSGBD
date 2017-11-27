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

	public Entry findEntry(String value, Node node) {
		if (node.isEmpty())
			return null;

		if (node.isLeaf()) {
			for (Entry e : node.getEntries())
				if (e.getValue() != null && e.getValue().equals(value))
					return e;
			return null;
		}

		else
			for (int i = 0; i < 2 * d; i++)
				if (node.getEntry(i) != null && node.compareAt(i, value) > 0)
					return findEntry(value, node.getSon(i));
		return findEntry(value, node.getSon(2 * d + 1));

	}

	public Entry findEntry(String value) {
		return findEntry(value, root);
	}

	public void simpleAddEntry(Entry entry, Node node, Node nodeToInsert) {
		int index = -1;

		while (node.getEntry(++index) != null && node.compareAt(index, entry.getValue()) < 0)
			;

		for (int i = 2 * d - 1; i > index; i++) {
			node.setEntry(i, node.getEntry(i - 1));
			node.setSon(i + 1, node.getSon(i));
		}

		node.setEntry(index, entry);
		node.setSon(index + 1, nodeToInsert);
		if (nodeToInsert != null)
			nodeToInsert.setFather(node);
	}

	public Node findNode(String value, Node node) {
		if (node.isLeaf())
			return node;

		else
			for (int i = 0; i < 2 * d; i++)
				if (node.compareAt(i, value) > 0)
					return findNode(value, node.getSon(i));

		return findNode(value, node.getSon(2 * d));
	}

	public void insertEntry(Entry entry) {
		Node node = findNode(entry.getValue(), this.root);

		if (node.isFull())
			split(entry, node, null);

		else
			simpleAddEntry(entry, node, null);

	}

	private Entry addEntryBetweenTwoNodes(Entry entry, Node node1, Node node2, Node nodeToInsert) {
		Entry e = null;
		boolean find = false;

		for (int i = 0; i < d && !find; i++)
			if (node1.compareAt(i, entry.getValue()) > 0)
				find = true;

		if (find) {
			simpleAddEntry(entry, node1, nodeToInsert);
			e = node2.getEntry(1);
			if (!node1.isLeaf()) {
				e = node1.getEntry(d);
				node1.setEntry(d, null);
			}
			return e;
		}

		simpleAddEntry(entry, node2, nodeToInsert);
		e = node2.getEntry(1);
		if (!node2.isLeaf()) {
			node1.setSon(d, node2.getSon(0));
			node1.getSon(d).setFather(node1);
			for (int i = 0; i < d; i++) {
				node2.setEntry(i, node2.getEntry(i + 1));
				node2.setSon(i, node2.getSon(i + 1));
			}
		}
		return e;
	}

	private void split(Entry entry, Node node, Node nodeToInsert) {
		Node newNode = new Node(d);
		Entry e = null;

		if (node.isLeaf()) {
			newNode.setLeftBrother(node);
			newNode.setRightBrother(node.getRightBrother());
			node.setRightBrother(newNode);
		}
		for (int i = 0; i < d; i++) {
			newNode.setEntry(i, node.getEntry(d + i));
			node.setEntry(i, null);
			newNode.setSon(i, node.getSon(d + 1 + i));
			node.setSon(d + 1 + i, null);
		}

		e = addEntryBetweenTwoNodes(entry, node, newNode, nodeToInsert);

		if (node.getFather() == null) {
			Node father = new Node(d);
			node.setFather(father);
			newNode.setFather(father);
			father.setEntry(0, e);
			father.setSon(0, node);
			father.setSon(1, newNode);
			root = father;
		}

		else {
			if (node.getFather().isFull())
				split(e, node.getFather(), newNode);
			else
				simpleAddEntry(e, node.getFather() , newNode);
		}
	}
}