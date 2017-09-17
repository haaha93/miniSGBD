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
		typeColumns=new String[nbColumns];
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

}
