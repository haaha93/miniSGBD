package minisgbd;

import java.io.IOException;
import bdd.GlobalManager;
import console.Menu;

public class Main {

	public static void main(String[] args) throws IOException {
		
		try {
			Menu.console();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			GlobalManager.finish();
		}

	}

}
