package pacman;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import pacman.view.Handler;

public class Main extends Application {

	private Stage primaryStage;
	private GameMenu gameMenu;
	private Handler handle = new Handler(this);
	//Game starts on Main Menu
	//Then from there players can choose from options
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		gameMenu = new GameMenu(this);
		initMenu();
	}

	public void initMenu() {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,1024,768);

			//Add background image from picture file
			Image img = new Image("resources/BackgroundCave.png");

			//Image properties
			ImageView imgView = new ImageView(img);
			imgView.setFitWidth(1024);
			imgView.setFitHeight(768);
			//Add game title
			Image title = new Image("resources/PAC-MANtitle.png");
			ImageView imgViewTitle = new ImageView(title);
			imgViewTitle.setX(0);
			imgViewTitle.setY(0);

			//Add buttons to Main Menu			
			root.getChildren().addAll(imgView, gameMenu, imgViewTitle);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			this.primaryStage.setScene(scene);
			this.primaryStage.setResizable(false);
			this.primaryStage.sizeToScene();
			this.primaryStage.setTitle("Pacman Dungeoneer");
			this.primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Main Menu buttons and their respective functionality
	private class GameMenu extends Parent {
		public GameMenu(Main main) {
			VBox menu0 = new VBox(20);

			//Initialise font
			Font fontStormBlade = Font.loadFont(getClass()
					.getResourceAsStream("/fonts/stormblade.ttf"), 30);

			//Location of First button - all following buttons are based on this
			menu0.setTranslateX(287);
			menu0.setTranslateY(300);

			//Create and Name Buttons
			Button btnSnglPlyr = new Button("SINGLE PLAYER");
			Button btnMltyPlyr2 = new Button("2    PERSON      MULTIPLAYER");
			Button btnMltyPlyr3 = new Button("3    PERSON      MULTIPLAYER");
			Button btnExit = new Button("EXIT");

			//Button customisation
			btnSnglPlyr.setMinSize(450, 90);
			btnSnglPlyr.setMaxSize(450, 90);
			btnSnglPlyr.setFont(fontStormBlade);
			btnSnglPlyr.setOpacity(0.8);

			btnMltyPlyr2.setMinSize(450, 90);
			btnMltyPlyr2.setMaxSize(450, 90);
			btnMltyPlyr2.setFont(fontStormBlade);
			btnMltyPlyr2.setOpacity(0.8);

			btnMltyPlyr3.setMinSize(450, 90);
			btnMltyPlyr3.setMaxSize(450, 90);
			btnMltyPlyr3.setFont(fontStormBlade);
			btnMltyPlyr3.setOpacity(0.8);

			btnExit.setMinSize(450, 90);
			btnExit.setMaxSize(450, 90);
			btnExit.setFont(fontStormBlade);
			btnExit.setOpacity(0.8);

			//Button functionality

			//SinglePlayer
			btnSnglPlyr.setOnKeyPressed((KeyEvent event)->{
				if (event.getCode() == KeyCode.ENTER) {
					handle.run(primaryStage, 1);//1 player
				}
			});

			//MultiPlayer
			btnMltyPlyr2.setOnKeyPressed((KeyEvent event)->{
				if(event.getCode() == KeyCode.ENTER) {
					handle.run(primaryStage, 2);//2 player
				}
			});
			
			//MultiPlayer
			btnMltyPlyr3.setOnKeyPressed((KeyEvent event)->{
				if(event.getCode() == KeyCode.ENTER) {
					handle.run(primaryStage, 3);//3 player
				}
			});

			//Exit
			btnExit.setOnKeyPressed((KeyEvent event)->{
				if(event.getCode() == KeyCode.ENTER) {
					System.exit(0);//quit
				}
			});

			menu0.getChildren().addAll(btnSnglPlyr, btnMltyPlyr2, btnMltyPlyr3, btnExit);

			getChildren().addAll(menu0);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}