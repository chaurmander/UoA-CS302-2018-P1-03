package pacman;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class EndGame {
	public void endGame(String score) { //Game Finished (either died or collected all pellets
		
		Alert gameOver = new Alert(AlertType.INFORMATION);
		gameOver.setTitle("Game Over");
		gameOver.setGraphic(null);
		gameOver.setHeaderText(null);
		gameOver.setContentText("Congratulation! Your score is: " + score);
		gameOver.show();

	}
	public boolean escKeyPressedInGame() { //Confirmation to exit or continue playing
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.setGraphic(null);
		confirm.setHeaderText("Game Paused! Resume or Quit?");
		ButtonType yes = new ButtonType("QUIT");
		ButtonType no = new ButtonType("RESUME");

		confirm.getButtonTypes().setAll(no, yes);
		Optional <ButtonType> result = confirm.showAndWait();
		if (result.get() == yes) {
			return true;
		}
		if (result.get() == no) {
			return false;
		}
		return false;
	}
}
