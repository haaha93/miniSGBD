package bdd;

public class BddException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BddException(String message) {
		super(message);
		System.out.println(message);
	}


}
