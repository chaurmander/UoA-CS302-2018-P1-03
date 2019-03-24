package pacman.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EntityCreator {
	public ImageView createEntity(int entityX, int entityY, int entityPixelsWidthHeight, Image imgCharacter) {

		ImageView imgViewCharacter = new ImageView(imgCharacter);
		imgViewCharacter.setFitWidth(entityPixelsWidthHeight);
		imgViewCharacter.setFitHeight(entityPixelsWidthHeight);
		imgViewCharacter.setTranslateX(entityX);
		imgViewCharacter.setTranslateY(entityY);
		imgViewCharacter.getProperties().put("alive", true);
		
		return imgViewCharacter;
	}
//	public 
}