package pacman.view;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class PlaySounds {
	private MediaPlayer gameOverSound;
	private MediaPlayer coinClatterSound;
	private MediaPlayer coinDroppedSound;
	private MediaPlayer gameWonSound;
	
	public void init() {
		
		String fileGameOver = "resources/SoundGameOver.wav";
		Media gameOverMedia = new Media(new File(fileGameOver).toURI().toString());
		gameOverSound = new MediaPlayer(gameOverMedia);

		String fileCoinClatter = "resources/SoundCoinClatter.wav";
		Media coinClatterMedia = new Media(new File(fileCoinClatter).toURI().toString());
		coinClatterSound = new MediaPlayer(coinClatterMedia);
		
		String fileCoinDropped = "resources/SoundCoinDropped.wav";
		Media coinDroppedMedia = new Media(new File(fileCoinDropped).toURI().toString());
		coinDroppedSound = new MediaPlayer(coinDroppedMedia);
		
		String fileGameWon = "resources/SoundGameWon.wav";
		Media gameWonMedia = new Media(new File(fileGameWon).toURI().toString());
		gameWonSound = new MediaPlayer(gameWonMedia);
		
//		String file = "resources/SoundDeletedUserGameOver.wav";
//		Media gameOverMedia = new Media(new File(file).toURI().toString());
//		MediaPlayer gameOverSound = new MediaPlayer(gameOverMedia);
	}

	public void playCoinDroppedSound() {
		coinDroppedSound.stop();
		coinDroppedSound.play();
	}
	
	public void playCoinClatterSound() {
		coinClatterSound.stop();
		coinClatterSound.play();
	}
	
	public void playGameOverSound() {
		gameOverSound.stop();
		gameOverSound.play();
	}
	
	public void playGameWonSound() {
		gameWonSound.stop();
		gameWonSound.play();
	}
}
