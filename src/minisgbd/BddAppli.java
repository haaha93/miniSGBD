package minisgbd;

import bdd.GlobalManager;
import console.IG;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BddAppli extends Application{

	@Override
	public void start(Stage arg0) throws Exception {

		console.IG pane = new IG();
		Scene scene = new Scene(pane, 600,800);
		arg0.setScene(scene);
		arg0.setTitle("SGBD 2017");
		arg0.show();
		GlobalManager.init();

	}
	
	public static void main(String[] args){
		launch(args);
	}

}
