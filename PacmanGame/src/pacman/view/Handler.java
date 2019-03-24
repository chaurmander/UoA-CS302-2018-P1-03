package pacman.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pacman.EndGame;
import pacman.Main;
import pacman.model.Map1;
import pacman.model.EntityCreator;
import pacman.view.PlaySounds;

public class Handler {
	private Main main;
	private boolean running = true;
	private boolean finished = false;
	private boolean restartGame = false;
	private boolean escKeyPressed = false;
	private boolean loseLife = false;
	private int multiplayer;
	private EntityCreator entity = new EntityCreator();
	private EndGame gameOver = new EndGame();
	private Map1 level = new Map1();
	private PlaySounds playsounds = new PlaySounds();

	private long time;
	private long tempTime;
	private long prevTime;
	private int counter;
	private AnimationTimer timer;

	private Pane root = new Pane();
	private Pane gameRoot = new Pane();
	private Pane coinRoot = new Pane();
	private Pane powerupRoot = new Pane();
	private Pane hud = new Pane();
	private Scene scene = new Scene(root);

	private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
	private ArrayList<Node> objects = new ArrayList<Node>();
	private ArrayList<Rectangle> wallBounds = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> coins = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> powerups = new ArrayList<Rectangle>();
	private ArrayList<Node> coinNodes = new ArrayList<Node>();
	private ArrayList<Node> powerupNodes = new ArrayList<Node>();

	private ImageView pacmanCreated;
	private Image pacmanCreatedLeft;
	private Image pacmanCreatedRight;
	private Image pacmanCreatedUp;
	private Image pacmanCreatedDown;
	private Image AI1CreatedLeft;
	private Image AI1CreatedRight;
	private Image AI1CreatedUp;
	private Image AI1CreatedDown;
	private Image AI2CreatedLeft;
	private Image AI2CreatedRight;
	private Image AI2CreatedUp;
	private Image AI2CreatedDown;
	private Image AI3CreatedLeft;
	private Image AI3CreatedRight;
	private Image AI3CreatedUp;
	private Image AI3CreatedDown;
	
	private ImageView AI1Created;
	private ImageView AI2Created;
	private ImageView AI3Created;

	private Text score = new Text();
	private Text scoreTitle = new Text();
	private Text timeLeft = new Text();
	private Text timeLeftTitle = new Text();
	private Text livesLeftTitle = new Text();
	private Text livesLeftDisplay = new Text();
	private Text countDownText = new Text();
	private Circle countDownTextBox = new Circle(525, 350, 80);

	private int abc;
	private int timerMax = 120;
	private int pacmanSpeed = 5;
	private int AISpeed = 4;
	private int livesLeft = 3;
	private int pacmanPixelsWidthHeight = 28;
	private int AIPixelsWidthHeight = 32;
	private int powerupTimeCounter = 0;

	private int pacmanCurrentDirection = 1;
	private int pacmanPrevDirection = 1;
	private int AI1CurrentDirection = 1;
	private int AI1PrevDirection = 1;
	private int AI2CurrentDirection = 1;
	private int AI2PrevDirection = 1;
	private int AI3CurrentDirection = 1;
	private int AI3PrevDirection = 1;

	private int playerX = 1, playerY = 1;
	private int AI1X = 1, AI1Y = 1, AI2X = 1, AI2Y = 1, AI3X = 1, AI3Y = 1;

	public Handler(Main main) {
		this.main = main;
	}

	public void init() { //run once at start of each game
		//Initialise variables - create a clean slate, in case of a reset
		running = true;
		finished = false;
		escKeyPressed = false;
		loseLife = false;
		root.getChildren().clear();
		gameRoot.getChildren().clear();
		coinRoot.getChildren().clear();
		powerupRoot.getChildren().clear();
		hud.getChildren().clear();
		objects.clear();
		wallBounds.clear();
		coins.clear();
		coinNodes.clear();
		powerups.clear();
		powerupNodes.clear();
		abc = 0;
		livesLeft = 3;
		tempTime = time;
		prevTime = tempTime;
		counter = 0;
		countDownText.setText("");
		timeLeft.setText(String.valueOf(timerMax));
		
//		PlaySounds
//		PlaySounds playsounds = new PlaySounds();
		playsounds.init();
		
		
		int[][] map = level.getMap();

		int mapSizeX = map[0].length; 
		int mapSizeY = map.length; 

		Image bkground = new Image("/resources/caveBkground2.png");
		ImageView view = new ImageView(bkground);
		//		Rectangle view = new Rectangle(0, 0, 1024, 768);
		//		view.setFill(Color.ROSYBROWN);

		view.setFitHeight(768);
		view.setFitWidth(1024);

		Font stormBladeFont = Font.loadFont(getClass().getResourceAsStream("/fonts/stormblade.ttf"), 35);		
		Font stormBladeFontLarge = Font.loadFont(getClass().getResourceAsStream("/fonts/stormblade.ttf"), 100);

//		int playerX = 1, playerY = 1;
//		int AI1X = 1, AI1Y = 1, AI2X = 1, AI2Y = 1, AI3X = 1, AI3Y = 1, AI4X = 1, AI4Y = 1;

		//Create/Load map
		for(int yy = 0; yy < mapSizeY; yy++) {
			for(int xx = 0; xx < mapSizeX; xx++) {
				switch (map[yy][xx]) {
				case 0: //Wall
					//Rectangle for collision detection, invisible
					Rectangle wall = new Rectangle(xx*32, yy*32, 32, 32);
					wallBounds.add(wall);
					//Wall images make up map
					Node imgViewWall = entity.createEntity(xx*32, yy*32, 32, new Image("/resources/caveWalls.png"));
					objects.add(imgViewWall);
					gameRoot.getChildren().add(imgViewWall);
					break;
				case 1: //Pellet images
					Rectangle coin = new Rectangle(xx*32 + 5, yy*32 + 5, 22, 22);
					coin.setFill(Color.GOLD);
					coins.add(coin);
					coinRoot.getChildren().add(coin);
					Node imgViewGround = entity.createEntity(xx*32 + 10, yy*32 + 10, 12, new Image("/resources/Coin.png"));
					coinNodes.add((Node) imgViewGround);
					gameRoot.getChildren().add(imgViewGround);
					break;
				case 2: //Nothing - empty walkspace
					break;
				case 3:
					//Player starting position
					playerX = xx;
					playerY = yy;
					break;
				case 4:
					AI1X = xx;
					AI1Y = yy;
					//AI go here - Shortest path
					break;
				case 5:
					AI2X = xx;
					AI2Y = yy;
					//AI go here
					break;
				case 6:
					AI3X = xx;
					AI3Y = yy;
					//AI go here
					break;
				case 8:
					//power-up pellets
					Rectangle powerup = new Rectangle(xx*32, yy*32, 32, 32);
					powerup.setFill(Color.GOLD);
					powerups.add(powerup);
					powerupRoot.getChildren().add(powerup);
					Node imgViewPowerup = entity.createEntity(xx*32, yy*32, 32, new Image("/resources/Powerup.png"));
					powerupNodes.add((Node) imgViewPowerup);
					gameRoot.getChildren().add(imgViewPowerup);
					break;
				}
			}
		}

		//Create player
		pacmanCreatedLeft = new Image("/resources/MinotaurLeft.png");
		pacmanCreatedRight = new Image("/resources/MinotaurRight.png");
		pacmanCreatedUp = new Image("/resources/MinotaurUp.png");
		pacmanCreatedDown = new Image("/resources/MinotaurDown.png");
		
		AI1CreatedLeft = new Image("/resources/GhostRedLeft.png");
		AI1CreatedRight = new Image("/resources/GhostRedRight.png");
		AI1CreatedUp = new Image("/resources/GhostRedUp.png");
		AI1CreatedDown = new Image("/resources/GhostRedDown.png");
		
		AI2CreatedLeft = new Image("/resources/GhostOrangeLeft.png");
		AI2CreatedRight = new Image("/resources/GhostOrangeRight.png");
		AI2CreatedUp = new Image("/resources/GhostOrangeUp.png");
		AI2CreatedDown = new Image("/resources/GhostOrangeDown.png");
		
		AI3CreatedLeft = new Image("/resources/GhostCyanLeft.png");
		AI3CreatedRight = new Image("/resources/GhostCyanRight.png");
		AI3CreatedUp = new Image("/resources/GhostCyanUp.png");
		AI3CreatedDown = new Image("/resources/GhostCyanDown.png");
		
		pacmanCreated = entity.createEntity(playerX*32, playerY*32, pacmanPixelsWidthHeight, pacmanCreatedUp);
		pacmanCreated.setImage(new Image("/resources/MinotaurLeft.png"));
		
		root.getChildren().add(pacmanCreated);

		//Create AI
		AI1Created = entity.createEntity(AI1X*32, AI1Y*32, AIPixelsWidthHeight, AI1CreatedDown);
		root.getChildren().add(AI1Created);
		AI2Created = entity.createEntity(AI2X*32, AI2Y*32, AIPixelsWidthHeight, AI2CreatedDown);
		root.getChildren().add(AI2Created);
		AI3Created = entity.createEntity(AI3X*32, AI3Y*32, AIPixelsWidthHeight, AI3CreatedDown);
		root.getChildren().add(AI3Created);

		scoreTitle.setText("Score: ");
		scoreTitle.setFont(stormBladeFont);
		scoreTitle.setFill(Color.WHITE);
		scoreTitle.setX(32);
		scoreTitle.setY(25);

		score.setText(String.valueOf(abc));
		score.setFont(stormBladeFont);
		score.setFill(Color.WHITE);
		score.setX(160);
		score.setY(25);

		timeLeftTitle.setText("Time Left: ");
		timeLeftTitle.setFont(stormBladeFont);
		timeLeftTitle.setFill(Color.WHITE);
		timeLeftTitle.setX(768);
		timeLeftTitle.setY(25);

		timeLeft.setText(String.valueOf(timerMax));
		timeLeft.setFont(stormBladeFont);
		timeLeft.setFill(Color.WHITE);
		timeLeft.setX(928);
		timeLeft.setY(25);

		livesLeftTitle.setText("Lives Left: ");
		livesLeftTitle.setFont(stormBladeFont);
		livesLeftTitle.setFill(Color.WHITE);
		livesLeftTitle.setX(380);
		livesLeftTitle.setY(25);

		livesLeftDisplay.setText(String.valueOf(livesLeft));
		livesLeftDisplay.setFont(stormBladeFont);
		livesLeftDisplay.setFill(Color.WHITE);
		livesLeftDisplay.setX(570);
		livesLeftDisplay.setY(25);
		
		countDownText.setText("");
		countDownText.setFill(Color.WHITE);
		countDownText.setFont(stormBladeFontLarge);
		countDownText.setX(509);
		countDownText.setY(380);
		countDownText.setOpacity(0.8);

		countDownTextBox.setFill(Color.BLACK);
		countDownTextBox.setOpacity(0.6);

		hud.getChildren().add(scoreTitle);
		hud.getChildren().add(score);
		hud.getChildren().add(timeLeftTitle);
		hud.getChildren().add(timeLeft);
		hud.getChildren().add(livesLeftTitle);
		hud.getChildren().add(livesLeftDisplay);
		hud.getChildren().add(countDownTextBox);
		hud.getChildren().add(countDownText);

		objects.add(pacmanCreated);
		gameRoot.getChildren().add(pacmanCreated);
		objects.add(AI1Created);
		gameRoot.getChildren().add(AI1Created);
		objects.add(AI2Created);
		gameRoot.getChildren().add(AI2Created);
		objects.add(AI3Created);
		gameRoot.getChildren().add(AI3Created);

		root.getChildren().addAll(view, gameRoot, hud);
	}

	private int moveEntityX(int amount, Node entity, int entityPixelsWidthHeight) { //moves entities left and right

		boolean XdirectionRight = amount > 0; //true = Right; false = Left
		int movement = 0;
		for (int i = 0; i < Math.abs(amount); i++) {
			//Collision with walls
			for (int j = 0; j < wallBounds.size(); j++) {
				if(XdirectionRight) {
					if(entity.getTranslateX() + entityPixelsWidthHeight >= wallBounds.get(j).getX() && entity.getTranslateX() < wallBounds.get(j).getX() + 32 &&
							entity.getTranslateY() < wallBounds.get(j).getY() + 32 && entity.getTranslateY() + entityPixelsWidthHeight > wallBounds.get(j).getY()) {
						return -1;//for AI - can't change direction
					}
				} else {
					if(entity.getTranslateX() > wallBounds.get(j).getX() && entity.getTranslateX() <= wallBounds.get(j).getX() + 32 &&
							entity.getTranslateY() < wallBounds.get(j).getY() + 32 && entity.getTranslateY() + entityPixelsWidthHeight > wallBounds.get(j).getY()) {
						return -1;//for AI - can't change direction
					}
				}
			}

			//If no collision detected
			if(XdirectionRight) {
				movement = 1;
			} else {
				movement = -1;
			}
			entity.setTranslateX(entity.getTranslateX() + movement);
		}
		if(movement == 1) {
			return 2;//right movement - for AI
		} else if(movement == -1) {
			return 1;//left movement - for AI
		} else {
			return -1;
		}
	}

	private int moveEntityY(int amount, Node entity, int entityPixelsWidthHeight) { //moves entities up and down
		boolean YdirectionDown = amount > 0; //true = Down; false = Up
		int movement = 0;

		for (int i = 0; i < Math.abs(amount); i++) {
			//collision with walls
			for (int j = 0; j < wallBounds.size(); j++) {
				if(YdirectionDown) {
					if(entity.getTranslateX() + entityPixelsWidthHeight > wallBounds.get(j).getX() && entity.getTranslateX() < wallBounds.get(j).getX() + 32 &&
							entity.getTranslateY() + entityPixelsWidthHeight >= wallBounds.get(j).getY() && entity.getTranslateY() < wallBounds.get(j).getY() + 32) {
						return -1;//for AI - can't change direction
					}
				} else {
					if(entity.getTranslateX() + entityPixelsWidthHeight > wallBounds.get(j).getX() && entity.getTranslateX() < wallBounds.get(j).getX() + 32 &&
							entity.getTranslateY() <= wallBounds.get(j).getY() + 32 && entity.getTranslateY() + entityPixelsWidthHeight > wallBounds.get(j).getY()) {
						return -1;//for AI - can't change direction 
					}
				}
			}

			//if no collision detected
			if(YdirectionDown) {
				movement = 1;
			} else {
				movement = -1;
			}
			entity.setTranslateY(entity.getTranslateY() + movement);
		}
		if(movement == 1) {
			return 4;//down movement - for AI
		} else if(movement == -1) {
			return 3;//up movement - for AI
		} else {
			return -1;
		}
	}

	private boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
	}

	private void update() { //everything happens from here
		if(counter > 3) {
			if(isPressed(KeyCode.RIGHT) && pacmanCreated.getTranslateX() + 32 <= 1024 - 2 && !(isPressed(KeyCode.LEFT))) {
				if(moveEntityX(pacmanSpeed, pacmanCreated, pacmanPixelsWidthHeight) == 2) {
					pacmanCurrentDirection = 2;
				}
			}
			if(isPressed(KeyCode.LEFT) && pacmanCreated.getTranslateX() >= 2 && !(isPressed(KeyCode.RIGHT))) {
				if(moveEntityX(-pacmanSpeed, pacmanCreated, pacmanPixelsWidthHeight) == 1) {
					pacmanCurrentDirection = 1;
				}
			}
			if(isPressed(KeyCode.UP) && pacmanCreated.getTranslateY() > 1 && !(isPressed(KeyCode.DOWN))) {
				if(moveEntityY(-pacmanSpeed, pacmanCreated, pacmanPixelsWidthHeight) == 3) {
					pacmanCurrentDirection = 3;
				}
			}
			if(isPressed(KeyCode.DOWN) && pacmanCreated.getTranslateY() + 32 <= 768 - 2 && !(isPressed(KeyCode.UP))) {
				if(moveEntityY(pacmanSpeed, pacmanCreated, pacmanPixelsWidthHeight) == 4) {
					pacmanCurrentDirection = 4;
				}
			}
			if(multiplayer == 2 || multiplayer ==3) {
				if(isPressed(KeyCode.D) && AI1Created.getTranslateX() + 32 <= 1024 - 2 && !(isPressed(KeyCode.A))) {
					if(moveEntityX(AISpeed, AI1Created, AIPixelsWidthHeight) == 2) {
						AI1CurrentDirection = 2;
					}
				}
				if(isPressed(KeyCode.A) && AI1Created.getTranslateX() >= 2 && !(isPressed(KeyCode.D))) {
					if(moveEntityX(-AISpeed, AI1Created, AIPixelsWidthHeight) == 1) {
						AI1CurrentDirection = 1;
					}
				}
				if(isPressed(KeyCode.W) && AI1Created.getTranslateY() > 1 && !(isPressed(KeyCode.S))) {
					if(moveEntityY(-AISpeed, AI1Created, AIPixelsWidthHeight) == 3) {
						AI1CurrentDirection = 3;
					}
				}
				if(isPressed(KeyCode.S) && AI1Created.getTranslateY() + 32 <= 768 - 2 && !(isPressed(KeyCode.W))) {
					if(moveEntityY(AISpeed, AI1Created, AIPixelsWidthHeight) == 4) {
						AI1CurrentDirection = 4;
					}
				}
			}
			if(multiplayer ==3) {
				if(isPressed(KeyCode.L) && AI2Created.getTranslateX() + 32 <= 1024 - 2 && !(isPressed(KeyCode.A))) {
					if(moveEntityX(AISpeed, AI2Created, AIPixelsWidthHeight) == 2) {
						AI2CurrentDirection = 2;
					}
				}
				if(isPressed(KeyCode.J) && AI2Created.getTranslateX() >= 2 && !(isPressed(KeyCode.D))) {
					if(moveEntityX(-AISpeed, AI2Created, AIPixelsWidthHeight) == 1) {
						AI2CurrentDirection = 1;
					}
				}
				if(isPressed(KeyCode.I) && AI2Created.getTranslateY() > 1 && !(isPressed(KeyCode.S))) {
					if(moveEntityY(-AISpeed, AI2Created, AIPixelsWidthHeight) == 3) {
						AI2CurrentDirection = 3;
					}
				}
				if(isPressed(KeyCode.K) && AI2Created.getTranslateY() + 32 <= 768 - 2 && !(isPressed(KeyCode.W))) {
					if(moveEntityY(AISpeed, AI2Created, AIPixelsWidthHeight) == 4) {
						AI2CurrentDirection = 4;
					}
				}
			}

			moveAI();

			if(playerHitEnemy()) {
				playsounds.playGameOverSound();
				loseLife = true;
			}
			
			for(int c = 0; c < coinNodes.size(); c++) { //removes pellets if player collides with them and updates score accordingly
				if(pacmanCreated.getTranslateX() + 32 > coinNodes.get(c).getTranslateX() && pacmanCreated.getTranslateX() < coinNodes.get(c).getTranslateX() + 12 &&
						pacmanCreated.getTranslateY() < coinNodes.get(c).getTranslateY() + 12 && pacmanCreated.getTranslateY() + 32 > coinNodes.get(c).getTranslateY()) {
					//				score.increase(1);
					playsounds.playCoinDroppedSound();
					abc+=100;
					score.setText(String.valueOf(abc));

					coinNodes.get(c).getProperties().put("alive", false);
					
				}
			}
			for(int p = 0; p < powerupNodes.size(); p++) { //removes pellets if player collides with them and updates score accordingly
				if(pacmanCreated.getTranslateX() + 32 > powerupNodes.get(p).getTranslateX() && pacmanCreated.getTranslateX() < powerupNodes.get(p).getTranslateX() + 32 &&
						pacmanCreated.getTranslateY() < powerupNodes.get(p).getTranslateY() + 32 && pacmanCreated.getTranslateY() + 32 > powerupNodes.get(p).getTranslateY()) {
					//				score.increase(1);
					playsounds.playCoinClatterSound();
					abc+=500;
					score.setText(String.valueOf(abc));
					powerupTimeCounter = 10;
					AISpeed = 2;
					powerupNodes.get(p).getProperties().put("alive", false);
				}
			}
			for(Iterator<Node> iterator = coinNodes.iterator(); iterator.hasNext();) {
				Node coinToCheck = iterator.next();
				if(!(boolean)coinToCheck.getProperties().get("alive")) {
					iterator.remove();
					gameRoot.getChildren().remove(coinToCheck);
					coins.remove(0);
					return;
				}
			}
			for(Iterator<Node> iterator = powerupNodes.iterator(); iterator.hasNext();) {
				Node powerupToCheck = iterator.next();
				if(!(boolean)powerupToCheck.getProperties().get("alive")) {
					iterator.remove();
					gameRoot.getChildren().remove(powerupToCheck);
					powerups.remove(0);
					return;
				}
			}
			if(coins.size() == 0) { //player won game - all pellets have been collected
				playsounds.playGameWonSound();
				finished = true;
			}
		}
		if(timerMax + 4 - counter <= 0) {
			finished = true;
		}
		tempTime = time;
		if(tempTime >= prevTime+1000000000) {//increment by 1 second
			prevTime = tempTime;
			counter++;
			if(powerupTimeCounter > 0) {
				powerupTimeCounter--;
			} else {
				AISpeed = 4;
			}
		}
		if(counter == 0 || counter == 1) {
			countDownText.setText("3");
			countDownText.setOpacity(0.8);
			countDownTextBox.setOpacity(0.6);
		} else if(counter == 2) {
			countDownText.setText("2");
		} else if(counter == 3) {
			countDownText.setText("1");
		} else {
			countDownText.setOpacity(0);
			countDownTextBox.setOpacity(0);
			timeLeft.setText(String.valueOf(timerMax + 4 - counter));
		}
		updateEntityImageDirection();
	}

	private void updateEntityImageDirection() {
		if(pacmanCurrentDirection != pacmanPrevDirection) {
			switch (pacmanCurrentDirection) {
			case 1:
				pacmanCreated.setImage(pacmanCreatedLeft);
				break;
			case 2:
				pacmanCreated.setImage(pacmanCreatedRight);
				break;
			case 3:
				pacmanCreated.setImage(pacmanCreatedUp);
				break;
			case 4:
				pacmanCreated.setImage(pacmanCreatedDown);
				break;
			}
			pacmanPrevDirection = pacmanCurrentDirection;
		}
		if(AI1CurrentDirection != AI1PrevDirection) {
			switch (AI1CurrentDirection) {
			case 1:
				AI1Created.setImage(AI1CreatedLeft);
				break;
			case 2:
				AI1Created.setImage(AI1CreatedRight);
				break;
			case 3:
				AI1Created.setImage(AI1CreatedUp);
				break;
			case 4:
				AI1Created.setImage(AI1CreatedDown);
				break;
			}
			AI1PrevDirection = AI1CurrentDirection;
		}
		if(AI2CurrentDirection != AI2PrevDirection) {
			switch (AI2CurrentDirection) {
			case 1:
				AI2Created.setImage(AI2CreatedLeft);
				break;
			case 2:
				AI2Created.setImage(AI2CreatedRight);
				break;
			case 3:
				AI2Created.setImage(AI2CreatedUp);
				break;
			case 4:
				AI2Created.setImage(AI2CreatedDown);
				break;
			}
			AI2PrevDirection = AI2CurrentDirection;
		}
		if(AI3CurrentDirection != AI3PrevDirection) {
			switch (AI3CurrentDirection) {
			case 1:
				AI3Created.setImage(AI3CreatedLeft);
				break;
			case 2:
				AI3Created.setImage(AI3CreatedRight);
				break;
			case 3:
				AI3Created.setImage(AI3CreatedUp);
				break;
			case 4:
				AI3Created.setImage(AI3CreatedDown);
				break;
			}
			AI3PrevDirection = AI3CurrentDirection;
		}
	}
	
	private boolean playerHitEnemy() {//Die
		if(pacmanCreated.getTranslateX() + (pacmanPixelsWidthHeight/2) >= AI1Created.getTranslateX()) {
			if(pacmanCreated.getTranslateX() + (pacmanPixelsWidthHeight/2) <= AI1Created.getTranslateX() + AIPixelsWidthHeight) {
				if(pacmanCreated.getTranslateY() + (pacmanPixelsWidthHeight/2) >= AI1Created.getTranslateY()) {
					if(pacmanCreated.getTranslateY() + (pacmanPixelsWidthHeight/2) <= AI1Created.getTranslateY() + AIPixelsWidthHeight) {
						return true;
					}
				}
			}
		}
		if(pacmanCreated.getTranslateX() + (pacmanPixelsWidthHeight/2) >= AI2Created.getTranslateX()) {
			if(pacmanCreated.getTranslateX() + (pacmanPixelsWidthHeight/2) <= AI2Created.getTranslateX() + AIPixelsWidthHeight) {
				if(pacmanCreated.getTranslateY() + (pacmanPixelsWidthHeight/2) >= AI2Created.getTranslateY()) {
					if(pacmanCreated.getTranslateY() + (pacmanPixelsWidthHeight/2) <= AI2Created.getTranslateY() + AIPixelsWidthHeight) {
						return true;
					}
				}
			}
		}
		if(pacmanCreated.getTranslateX() + (pacmanPixelsWidthHeight/2) >= AI3Created.getTranslateX()) {
			if(pacmanCreated.getTranslateX() + (pacmanPixelsWidthHeight/2) <= AI3Created.getTranslateX() + AIPixelsWidthHeight) {
				if(pacmanCreated.getTranslateY() + (pacmanPixelsWidthHeight/2) >= AI3Created.getTranslateY()) {
					if(pacmanCreated.getTranslateY() + (pacmanPixelsWidthHeight/2) <= AI3Created.getTranslateY() + AIPixelsWidthHeight) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void moveAI() { //caller to move AI
		//AI#CurrentDirection: 1=Left, 2=Right, 3=Up, 4=Down
		//AI4
		for(int i = 0; i < AISpeed; i++) {
			moveAI3();
		}
		if(multiplayer == 1) {
			//AI1
			for(int i = 0; i < AISpeed; i++) {
				moveAI1();
			}
		}
		if(multiplayer == 1 || multiplayer == 2) {
			//AI2
			for(int i = 0; i < AISpeed; i++) {
				moveAI2();
			}
		}
	}

	private void moveAI1() { //AI1's movement logic
		//AI#CurrentDirection: 1=Left, 2=Right, 3=Up, 4=Down
		if(AI1CurrentDirection == 1) {//going left, can't turn right
			if(pacmanCreated.getTranslateX() + (pacmanPixelsWidthHeight/2) >= AI1Created.getTranslateX() + (AIPixelsWidthHeight/2)) {//pacman is right of AI
				if(pacmanCreated.getTranslateY() + (pacmanPixelsWidthHeight/2) >= AI1Created.getTranslateY() + (AIPixelsWidthHeight/2)) {//pacman is below AI
					if(moveEntityY(1, AI1Created, AIPixelsWidthHeight) == 4) {//down is successful
						AI1CurrentDirection = 4;
						return;
					} else if(moveEntityY(-1, AI1Created, AIPixelsWidthHeight) == 3) {//up is successful
						AI1CurrentDirection = 3;
						return;
					} else {
						moveEntityX(-1, AI1Created, AIPixelsWidthHeight);//have to continue left, as can't go any other direction (continued direction)
						return;
					}
				} else {//pacman is above AI
					if(moveEntityY(-1, AI1Created, AIPixelsWidthHeight) == 3) {//up is successful
						AI1CurrentDirection = 3;
						return;
					} else if(moveEntityX(-1, AI1Created, AIPixelsWidthHeight) == 1) {//left is successful (continued direction)
						return;
					} else {
						moveEntityY(1, AI1Created, AIPixelsWidthHeight);//have to go down, as can't go any other direction
						AI1CurrentDirection = 4;
						return;
					}
				}
			} else {//pacman is left of AI
				if(pacmanCreated.getTranslateY() + (pacmanPixelsWidthHeight/2) < AI1Created.getTranslateY() + (AIPixelsWidthHeight/2)) {//pacman is above AI
					if(moveEntityX(-1, AI1Created, AIPixelsWidthHeight) == 1) {//left is successful (continued direction)
						return;
					} else if(moveEntityY(-1, AI1Created, AIPixelsWidthHeight) == 3) {//up is successful
						AI1CurrentDirection = 3;
						return;
					} else {
						moveEntityY(1, AI1Created, AIPixelsWidthHeight);//have to go down, as can't go any other direction
						AI1CurrentDirection = 4;
						return;
					}
				} else {//pacman is below AI
					if(moveEntityX(-1, AI1Created, AIPixelsWidthHeight) == 1) {//left is successful (continued direction)
						return;
					} else if(moveEntityY(-1, AI1Created, AIPixelsWidthHeight) == 3) {//up is successful
						AI1CurrentDirection = 3;
						return;
					} else {
						moveEntityY(1, AI1Created, AIPixelsWidthHeight);//have to go down, as can't go any other direction
						AI1CurrentDirection = 4;
						return;
					}
				}
			}

		} else if(AI1CurrentDirection == 2) {//AI going right
			if(pacmanCreated.getTranslateX() + (pacmanPixelsWidthHeight/2) >= AI1Created.getTranslateX() + (AIPixelsWidthHeight/2)) {//pacman is right of AI
				if(pacmanCreated.getTranslateY() + (pacmanPixelsWidthHeight/2) >= AI1Created.getTranslateY() + (AIPixelsWidthHeight/2)) {//pacman is below AI
					if(moveEntityX(1, AI1Created, AIPixelsWidthHeight) == 2) {//go right
						return;
					} else if(moveEntityY(1, AI1Created, AIPixelsWidthHeight) == 4) {//go down
						AI1CurrentDirection = 4;
						return;
					} else {//go up
						moveEntityY(-1, AI1Created, AIPixelsWidthHeight);
						AI1CurrentDirection = 3;
						return;
					}
				} else {//pacman is above AI
					//go right
					if(moveEntityX(1, AI1Created, AIPixelsWidthHeight) == 2) {
						return;
					} else if(moveEntityY(-1, AI1Created, AIPixelsWidthHeight) == 3) {//go up
						AI1CurrentDirection = 3;
						return;
					} else {//go down
						moveEntityY(1, AI1Created, AIPixelsWidthHeight);
						AI1CurrentDirection = 4;
						return;
					}
				}
			} else {//pacman is left of AI
				if(pacmanCreated.getTranslateY() + (pacmanPixelsWidthHeight/2) >= AI1Created.getTranslateY() + (AIPixelsWidthHeight/2)) {//pacman is below AI
					if(moveEntityY(1, AI1Created, AIPixelsWidthHeight) == 4) {//go down
						AI1CurrentDirection = 4;
						return;
					} else if(moveEntityY(-1, AI1Created, AIPixelsWidthHeight) == 3) {//go up
						AI1CurrentDirection = 3;
						return;
					} else {//right
						moveEntityX(1, AI1Created, AIPixelsWidthHeight);
						return;
					}
				} else {//pacman is above AI
					if(moveEntityY(-1, AI1Created, AIPixelsWidthHeight) == 3) {//go up
						AI1CurrentDirection = 3;
						return;
					} else if(moveEntityY(1, AI1Created, AIPixelsWidthHeight) == 4) {//go down
						AI1CurrentDirection = 4;
						
						return;
					} else {//right
						moveEntityX(1, AI1Created, AIPixelsWidthHeight);
						return;
					}
				}
			}

		} else if(AI1CurrentDirection == 3) {//AI going up
			if(pacmanCreated.getTranslateX() + (pacmanPixelsWidthHeight/2) >= AI1Created.getTranslateX() + (AIPixelsWidthHeight/2)) {//pacman is right of AI
				if(moveEntityX(1, AI1Created, AIPixelsWidthHeight) == 2) {//go right
					AI1CurrentDirection = 2;
					return;
				} else if(moveEntityY(-1, AI1Created, AIPixelsWidthHeight) == 3) {//go up
					return;
				} else {//go left
					moveEntityX(-1, AI1Created, AIPixelsWidthHeight);
					AI1CurrentDirection = 1;
					return;
				}
			} else {//pacman is left of AI
				if(moveEntityX(-1, AI1Created, AIPixelsWidthHeight) == 1) {//go left
					AI1CurrentDirection = 1;
					return;
				} else if(moveEntityY(-1, AI1Created, AIPixelsWidthHeight) == 3) {//go up
					return;
				} else {//go right
					moveEntityX(1, AI1Created, AIPixelsWidthHeight);
					AI1CurrentDirection = 2;
					return;
				}
			}

		} else {//AI1CurrentDirection == 4 AI going down
			if(pacmanCreated.getTranslateX() + (pacmanPixelsWidthHeight/2) >= AI1Created.getTranslateX() + (AIPixelsWidthHeight/2)) {//pacman is right of AI
				if(moveEntityX(1, AI1Created, AIPixelsWidthHeight) ==2) {//go right
					AI1CurrentDirection = 2;
					return;
				} else if(moveEntityY(1, AI1Created, AIPixelsWidthHeight) == 4) {//go down
					return;
				} else {//go left
					moveEntityX(-1, AI1Created, AIPixelsWidthHeight);
					AI1CurrentDirection = 1;
					return;
				}
			} else {//pacman is left of AI
				if(moveEntityX(-1, AI1Created, AIPixelsWidthHeight) == 1) {//go left
					AI1CurrentDirection = 1;
					return;
				} else if(moveEntityY(1, AI1Created, AIPixelsWidthHeight) == 4) {//go down
					return;
				} else {//go right
					moveEntityX(1, AI1Created, AIPixelsWidthHeight);
					AI1CurrentDirection = 2;
					return;
				}
			} 
		}	
	}

	private void moveAI2() { //AI2's movement logic
		//AI#CurrentDirection: 1=Left, 2=Right, 3=Up, 4=Down
		double pacmanTargetSpotX;
		double pacmanTargetSpotY;
		if(pacmanCurrentDirection == 1) {
			pacmanTargetSpotX = pacmanCreated.getTranslateX() - (pacmanPixelsWidthHeight * 4); 
		} else if(pacmanCurrentDirection == 2) {
			pacmanTargetSpotX = pacmanCreated.getTranslateX() + (pacmanPixelsWidthHeight * 4); 
		} else {
			pacmanTargetSpotX = pacmanCreated.getTranslateX();
		}
		if(pacmanCurrentDirection == 3) {
			pacmanTargetSpotY = pacmanCreated.getTranslateY() - (pacmanPixelsWidthHeight * 4); 
		} else if(pacmanCurrentDirection == 4) {
			pacmanTargetSpotY = pacmanCreated.getTranslateY() + (pacmanPixelsWidthHeight * 4); 
		} else {
			pacmanTargetSpotY = pacmanCreated.getTranslateY();
		}
		if(AI2CurrentDirection == 1) {//going left, can't turn right
			if(pacmanTargetSpotX >= AI2Created.getTranslateX() + (AIPixelsWidthHeight/2)) {//pacman is right of AI
				if(pacmanTargetSpotY >= AI2Created.getTranslateY() + (AIPixelsWidthHeight/2)) {//pacman is below AI
					if(moveEntityY(1, AI2Created, AIPixelsWidthHeight) == 4) {//down is successful
						AI2CurrentDirection = 4;
						return;
					} else if(moveEntityY(-1, AI2Created, AIPixelsWidthHeight) == 3) {//up is successful
						AI2CurrentDirection = 3;
						return;
					} else {
						moveEntityX(-1, AI2Created, AIPixelsWidthHeight);//have to continue left, as can't go any other direction (continued direction)
						return;
					}
				} else {//pacman is above AI
					if(moveEntityY(-1, AI2Created, AIPixelsWidthHeight) == 3) {//up is successful
						AI2CurrentDirection = 3;
						return;
					} else if(moveEntityX(-1, AI2Created, AIPixelsWidthHeight) == 1) {//left is successful (continued direction)
						return;
					} else {
						moveEntityY(1, AI2Created, AIPixelsWidthHeight);//have to go down, as can't go any other direction
						AI2CurrentDirection = 4;
						return;
					}
				}
			} else {//pacman is left of AI
				if(pacmanTargetSpotY < AI2Created.getTranslateY() + (AIPixelsWidthHeight/2)) {//pacman is above AI
					if(moveEntityX(-1, AI2Created, AIPixelsWidthHeight) == 1) {//left is successful (continued direction)
						return;
					} else if(moveEntityY(-1, AI2Created, AIPixelsWidthHeight) == 3) {//up is successful
						AI2CurrentDirection = 3;
						return;
					} else {
						moveEntityY(1, AI2Created, AIPixelsWidthHeight);//have to go down, as can't go any other direction
						AI2CurrentDirection = 4;
						return;
					}
				} else {//pacman is below AI
					if(moveEntityX(-1, AI2Created, AIPixelsWidthHeight) == 1) {//left is successful (continued direction)
						return;
					} else if(moveEntityY(-1, AI2Created, AIPixelsWidthHeight) == 3) {//up is successful
						AI2CurrentDirection = 3;
						return;
					} else {
						moveEntityY(1, AI2Created, AIPixelsWidthHeight);//have to go down, as can't go any other direction
						AI2CurrentDirection = 4;
						return;
					}
				}
			}

		} else if(AI2CurrentDirection == 2) {//AI going right
			if(pacmanTargetSpotX >= AI2Created.getTranslateX() + (AIPixelsWidthHeight/2)) {//pacman is right of AI
				if(pacmanTargetSpotY >= AI2Created.getTranslateY() + (AIPixelsWidthHeight/2)) {//pacman is below AI
					if(moveEntityX(1, AI2Created, AIPixelsWidthHeight) == 2) {//go right
						return;
					} else if(moveEntityY(1, AI2Created, AIPixelsWidthHeight) == 4) {//go down
						AI2CurrentDirection = 4;
						return;
					} else {//go up
						moveEntityY(-1, AI2Created, AIPixelsWidthHeight);
						AI2CurrentDirection = 3;
						return;
					}
				} else {//pacman is above AI
					//go right
					if(moveEntityX(1, AI2Created, AIPixelsWidthHeight) == 2) {
						return;
					} else if(moveEntityY(-1, AI2Created, AIPixelsWidthHeight) == 3) {//go up
						AI2CurrentDirection = 3;
						return;
					} else {//go down
						moveEntityY(1, AI2Created, AIPixelsWidthHeight);
						AI2CurrentDirection = 4;
						return;
					}
				}
			} else {//pacman is left of AI
				if(pacmanTargetSpotY >= AI2Created.getTranslateY() + (AIPixelsWidthHeight/2)) {//pacman is below AI
					if(moveEntityY(1, AI2Created, AIPixelsWidthHeight) == 4) {//go down
						AI2CurrentDirection = 4;
						return;
					} else if(moveEntityY(-1, AI2Created, AIPixelsWidthHeight) == 3) {//go up
						AI2CurrentDirection = 3;
						return;
					} else {//right
						moveEntityX(1, AI2Created, AIPixelsWidthHeight);
						return;
					}
				} else {//pacman is above AI
					if(moveEntityY(-1, AI2Created, AIPixelsWidthHeight) == 3) {//go up
						AI2CurrentDirection = 3;
						return;
					} else if(moveEntityY(1, AI2Created, AIPixelsWidthHeight) == 4) {//go down
						AI2CurrentDirection = 4;
						return;
					} else {//right
						moveEntityX(1, AI2Created, AIPixelsWidthHeight);
						return;
					}
				}
			}

		} else if(AI2CurrentDirection == 3) {//AI going up
			if(pacmanTargetSpotX >= AI2Created.getTranslateX() + (AIPixelsWidthHeight/2)) {//pacman is right of AI
				if(moveEntityX(1, AI2Created, AIPixelsWidthHeight) == 2) {//go right
					AI2CurrentDirection = 2;
					return;
				} else if(moveEntityY(-1, AI2Created, AIPixelsWidthHeight) == 3) {//go up
					return;
				} else {//go left
					moveEntityX(-1, AI2Created, AIPixelsWidthHeight);
					AI2CurrentDirection = 1;
					return;
				}
			} else {//pacman is left of AI
				if(moveEntityX(-1, AI2Created, AIPixelsWidthHeight) == 1) {//go left
					AI2CurrentDirection = 1;
					return;
				} else if(moveEntityY(-1, AI2Created, AIPixelsWidthHeight) == 3) {//go up
					return;
				} else {//go right
					moveEntityX(1, AI2Created, AIPixelsWidthHeight);
					AI2CurrentDirection = 2;
					return;
				}
			}

		} else {//AI1CurrentDirection == 4 AI going down
			if(pacmanTargetSpotX >= AI2Created.getTranslateX() + (AIPixelsWidthHeight/2)) {//pacman is right of AI
				if(moveEntityX(1, AI2Created, AIPixelsWidthHeight) ==2) {//go right
					AI2CurrentDirection = 2;
					return;
				} else if(moveEntityY(1, AI2Created, AIPixelsWidthHeight) == 4) {//go down
					return;
				} else {//go left
					moveEntityX(-1, AI2Created, AIPixelsWidthHeight);
					AI2CurrentDirection = 1;
					return;
				}
			} else {//pacman is left of AI
				if(moveEntityX(-1, AI2Created, AIPixelsWidthHeight) == 1) {//go left
					AI2CurrentDirection = 1;
					return;
				} else if(moveEntityY(1, AI2Created, AIPixelsWidthHeight) == 4) {//go down
					return;
				} else {//go right
					moveEntityX(1, AI2Created, AIPixelsWidthHeight);
					AI2CurrentDirection = 2;
					return;
				}
			} 
		}
	}

	private void moveAI3() { //AI3's movement logic
		//AI#CurrentDirection: 1=Left, 2=Right, 3=Up, 4=Down
				double pacmanTargetSpotX;
				double pacmanTargetSpotY;

				pacmanTargetSpotX = (2*(pacmanCreated.getTranslateX()) - AI1Created.getTranslateX()); 
				pacmanTargetSpotY = (2*(pacmanCreated.getTranslateY()) - AI1Created.getTranslateY()); 

				if(AI3CurrentDirection == 1) {//going left, can't turn right
					if(pacmanTargetSpotX >= AI3Created.getTranslateX() + (AIPixelsWidthHeight/2)) {//pacman is right of AI
						if(pacmanTargetSpotY >= AI3Created.getTranslateY() + (AIPixelsWidthHeight/2)) {//pacman is below AI
							if(moveEntityY(1, AI3Created, AIPixelsWidthHeight) == 4) {//down is successful
								AI3CurrentDirection = 4;
								return;
							} else if(moveEntityY(-1, AI3Created, AIPixelsWidthHeight) == 3) {//up is successful
								AI3CurrentDirection = 3;
								return;
							} else {
								moveEntityX(-1, AI3Created, AIPixelsWidthHeight);//have to continue left, as can't go any other direction (continued direction)
								return;
							}
						} else {//pacman is above AI
							if(moveEntityY(-1, AI3Created, AIPixelsWidthHeight) == 3) {//up is successful
								AI3CurrentDirection = 3;
								return;
							} else if(moveEntityX(-1, AI3Created, AIPixelsWidthHeight) == 1) {//left is successful (continued direction)
								return;
							} else {
								moveEntityY(1, AI3Created, AIPixelsWidthHeight);//have to go down, as can't go any other direction
								AI3CurrentDirection = 4;
								return;
							}
						}
					} else {//pacman is left of AI
						if(pacmanTargetSpotY < AI3Created.getTranslateY() + (AIPixelsWidthHeight/2)) {//pacman is above AI
							if(moveEntityX(-1, AI3Created, AIPixelsWidthHeight) == 1) {//left is successful (continued direction)
								return;
							} else if(moveEntityY(-1, AI3Created, AIPixelsWidthHeight) == 3) {//up is successful
								AI3CurrentDirection = 3;
								return;
							} else {
								moveEntityY(1, AI3Created, AIPixelsWidthHeight);//have to go down, as can't go any other direction
								AI3CurrentDirection = 4;
								return;
							}
						} else {//pacman is below AI
							if(moveEntityX(-1, AI3Created, AIPixelsWidthHeight) == 1) {//left is successful (continued direction)
								return;
							} else if(moveEntityY(-1, AI3Created, AIPixelsWidthHeight) == 3) {//up is successful
								AI3CurrentDirection = 3;
								return;
							} else {
								moveEntityY(1, AI3Created, AIPixelsWidthHeight);//have to go down, as can't go any other direction
								AI3CurrentDirection = 4;
								return;
							}
						}
					}

				} else if(AI3CurrentDirection == 2) {//AI going right
					if(pacmanTargetSpotX >= AI3Created.getTranslateX() + (AIPixelsWidthHeight/2)) {//pacman is right of AI
						if(pacmanTargetSpotY >= AI3Created.getTranslateY() + (AIPixelsWidthHeight/2)) {//pacman is below AI
							if(moveEntityX(1, AI3Created, AIPixelsWidthHeight) == 2) {//go right
								return;
							} else if(moveEntityY(1, AI3Created, AIPixelsWidthHeight) == 4) {//go down
								AI3CurrentDirection = 4;
								return;
							} else {//go up
								moveEntityY(-1, AI3Created, AIPixelsWidthHeight);
								AI3CurrentDirection = 3;
								return;
							}
						} else {//pacman is above AI
							//go right
							if(moveEntityX(1, AI3Created, AIPixelsWidthHeight) == 2) {
								return;
							} else if(moveEntityY(-1, AI3Created, AIPixelsWidthHeight) == 3) {//go up
								AI3CurrentDirection = 3;
								return;
							} else {//go down
								moveEntityY(1, AI3Created, AIPixelsWidthHeight);
								AI3CurrentDirection = 4;
								return;
							}
						}
					} else {//pacman is left of AI
						if(pacmanTargetSpotY >= AI3Created.getTranslateY() + (AIPixelsWidthHeight/2)) {//pacman is below AI
							if(moveEntityY(1, AI3Created, AIPixelsWidthHeight) == 4) {//go down
								AI3CurrentDirection = 4;
								return;
							} else if(moveEntityY(-1, AI3Created, AIPixelsWidthHeight) == 3) {//go up
								AI3CurrentDirection = 3;
								return;
							} else {//right
								moveEntityX(1, AI3Created, AIPixelsWidthHeight);
								return;
							}
						} else {//pacman is above AI
							if(moveEntityY(-1, AI3Created, AIPixelsWidthHeight) == 3) {//go up
								AI3CurrentDirection = 3;
								return;
							} else if(moveEntityY(1, AI3Created, AIPixelsWidthHeight) == 4) {//go down
								AI3CurrentDirection = 4;
								return;
							} else {//right
								moveEntityX(1, AI3Created, AIPixelsWidthHeight);
								return;
							}
						}
					}

				} else if(AI3CurrentDirection == 3) {//AI going up
					if(pacmanTargetSpotX >= AI3Created.getTranslateX() + (AIPixelsWidthHeight/2)) {//pacman is right of AI
						if(moveEntityX(1, AI3Created, AIPixelsWidthHeight) == 2) {//go right
							AI3CurrentDirection = 2;
							return;
						} else if(moveEntityY(-1, AI3Created, AIPixelsWidthHeight) == 3) {//go up
							return;
						} else {//go left
							moveEntityX(-1, AI3Created, AIPixelsWidthHeight);
							AI3CurrentDirection = 1;
							return;
						}
					} else {//pacman is left of AI
						if(moveEntityX(-1, AI3Created, AIPixelsWidthHeight) == 1) {//go left
							AI3CurrentDirection = 1;
							return;
						} else if(moveEntityY(-1, AI3Created, AIPixelsWidthHeight) == 3) {//go up
							return;
						} else {//go right
							moveEntityX(1, AI3Created, AIPixelsWidthHeight);
							AI3CurrentDirection = 2;
							return;
						}
					}

				} else {//AI1CurrentDirection == 4 AI going down
					if(pacmanTargetSpotX >= AI3Created.getTranslateX() + (AIPixelsWidthHeight/2)) {//pacman is right of AI
						if(moveEntityX(1, AI3Created, AIPixelsWidthHeight) ==2) {//go right
							AI3CurrentDirection = 2;
							return;
						} else if(moveEntityY(1, AI3Created, AIPixelsWidthHeight) == 4) {//go down
							return;
						} else {//go left
							moveEntityX(-1, AI3Created, AIPixelsWidthHeight);
							AI3CurrentDirection = 1;
							return;
						}
					} else {//pacman is left of AI
						if(moveEntityX(-1, AI3Created, AIPixelsWidthHeight) == 1) {//go left
							AI3CurrentDirection = 1;
							return;
						} else if(moveEntityY(1, AI3Created, AIPixelsWidthHeight) == 4) {//go down
							return;
						} else {//go right
							moveEntityX(1, AI3Created, AIPixelsWidthHeight);
							AI3CurrentDirection = 2;
							return;
						}
					} 
				}
			}
	
	public void resetGame() { //Cleanup and/or restart
		root.getChildren().clear();
		gameRoot.getChildren().clear();
		coinRoot.getChildren().clear();
		powerupRoot.getChildren().clear();
		hud.getChildren().clear();
		objects.clear();
		wallBounds.clear();
		coins.clear();
		coinNodes.clear();
		powerups.clear();
		powerupNodes.clear();
		abc = 0;
		tempTime = time;
		prevTime = tempTime;
		counter = 0;
	}

	public void partialReset() { //Died - respawning
		abc -= 2000;
		tempTime = time;
		prevTime = tempTime;
		counter = 0;
		livesLeftDisplay.setText(String.valueOf(livesLeft));
		
		pacmanCreated.setTranslateX(playerX*32);
		pacmanCreated.setTranslateY(playerY*32);
		AI1Created.setTranslateX(AI1X*32);
		AI1Created.setTranslateY(AI1Y*32);
		AI2Created.setTranslateX(AI2X*32);
		AI2Created.setTranslateY(AI2Y*32);
		AI3Created.setTranslateX(AI3X*32);
		AI3Created.setTranslateY(AI3Y*32);
	}
	
	public void run(Stage stage, int multiplayer) { //Animation timer
		this.multiplayer = multiplayer;

		init();

		scene.setOnKeyPressed((KeyEvent event)->{
			if (event.getCode() == KeyCode.ESCAPE) {
				running = false;
				if(gameOver.escKeyPressedInGame()) {
					escKeyPressed = true;
				} else {
					running = true;
				}

			}
			if (event.getCode() == KeyCode.PAGE_DOWN) {
				counter += timerMax + 4;
			}
			if (event.getCode() == KeyCode.P) {
				running = false;
				if(gameOver.escKeyPressedInGame()) {
					escKeyPressed = true;
				} else {
					running = true;
				}
			}
			if (event.getCode() == KeyCode.R) {
				restartGame = true;
			}
			keys.put(event.getCode(), true);
		});
		scene.setOnKeyReleased((KeyEvent event) ->{
			keys.put(event.getCode(), false);
		});

		stage.setScene(scene);
		stage.show();

		timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				if(running) {
					update();
				}
				if(restartGame) {
					resetGame();
					restartGame = false;
					init();
				}
				if(finished) {
					this.stop();
					gameOver.endGame(String.valueOf(abc));
					resetGame();
					main.initMenu();
					timer.stop();
				}
				if(escKeyPressed) {
					this.stop();
					resetGame();
					main.initMenu();
					timer.stop();
				}
				if(loseLife) {
					if(livesLeft > 1) {
						livesLeft -= 1;
						partialReset();
						loseLife = false;
					} else {
						finished = true;
					}
				}
				time = now;
			}
		};
		timer.start();
	}
}
