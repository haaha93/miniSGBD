package bdd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RelSchema implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int nbColumns;
	private List<String> typeColumns;

	/**
	 * 
	 * @param userInput
	 *            , tab containing the name of the relation, the number of
	 *            columns and the type of each columns enter by user. put the
	 *            informations of userInput in the instance.
	 */
	public RelSchema(String[] userInput) {
		name = userInput[1];
		nbColumns = Integer.parseInt(userInput[2]);
		typeColumns = new ArrayList<String>(nbColumns); // selection du type à
														// faire
		for (int i = 3; i < userInput.length; i++)
			typeColumns.add(i-3,userInput[i]);
	}

	/**
	 * Display of the relation.
	 */
	public void display() {
		System.out.print("name : " + name + ", nb of columns : " + nbColumns + ", type of columns : ");
		for (String s : typeColumns)
			System.out.print(s + " ");
		System.out.println();
	}

	public String toString() {
		StringBuffer relString = new StringBuffer();
		relString.append("Nom Rel:").append(name).append(",").append(" nbColumn: ").append(nbColumns)
				.append(" type columns : ");

		for (int i = 0; i < nbColumns; i++) {
			relString.append(typeColumns.get(i)).append(" ");
		}

		return relString.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNbColumns() {
		return nbColumns;
	}

	public void setNbColumns(int nbColumns) {
		this.nbColumns = nbColumns;
	}

	public List<String> getTypeColumns() {
		return typeColumns;
	}

	public void setTypeColumns(List<String> typeColumns) {
		this.typeColumns = typeColumns;
	}
	

	

}
