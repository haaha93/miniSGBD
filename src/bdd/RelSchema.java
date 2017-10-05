package bdd;

public class RelSchema {
	
	private String name;
	private int nbColumns;
	private String [] typeColumns;

	/**
	 * 
	 * @param userInput , tab containing the name of the relation, the number of columns and the type of each columns enter by user.
	 * put the informations of userInput in the instance. 
	 */
	RelSchema(String[] userInput){
		name=userInput[1];
		nbColumns=Integer.parseInt(userInput[2]);
		typeColumns=new String[nbColumns]; //selection du type à faire
		for (int i = 3 ; i < userInput.length ; i++)
			typeColumns[i-3]=userInput[i];
	}
	
	/**
	 * Display of the relation.
	 */
	public void display(){
		System.out.print("name : "+name+", number of columns : "+nbColumns+", type of columns : ");
		for (String s : typeColumns)
			System.out.print(s+" ");
		System.out.println();
	}
	
	public String toString(){
		StringBuffer relString = new StringBuffer();
		relString.append("Nom Rel:").append(name).append(",").append(" nbColumn: ").append(nbColumns).append(" type columns : ");
		
		for(int i=0; i< nbColumns; i++){
			relString.append(typeColumns[i]).append(" ");
		}
		
		return relString.toString();
	}

}
