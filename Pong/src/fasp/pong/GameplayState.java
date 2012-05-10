package fasp.pong;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;
import java.awt.Font;
import java.io.*;

public class GameplayState extends BasicGameState {

	private Rectangle player;
	private Circle ball;
	private Image playingField;
	private Image playerColor;
	private Image ballColor;
	private Image brickColor;
	private Vector2f ballVelocity;
	private int score;
	private UnicodeFont scoreFont;
	private UnicodeFont victoryFont;
	private boolean hasIntersected;
	private boolean hasStarted;
	private boolean endOfRound;
	private boolean beatTheGame;
	private int time;
	private LevelCreator levelCreator;
	private Brick[] currentLevel;
	private int thisLevel;
	private int bricksLeft;
	private int lives;
	private Stopwatch stopwatch;

	// Constants such as screen height, bat size, etc.
	final static boolean FULLSCREEN = false;
	final static int SCREEN_HEIGHT = 800;
	final static int SCREEN_WIDTH = 900;
	final static int BAT_WIDTH = 75;
	final static int BAT_HEIGHT = 10;
	final static int BAT_MOVEMENT_SPEED = 15;
	final static int BALL_RADIUS = 10;
	int stateID = 1;

	GameplayState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public int getID() {
		return stateID;
	}

	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		ballVelocity = new Vector2f(5, 13);
		thisLevel = 1;
		// Set up the bats and the ball.
		player = new Rectangle(SCREEN_WIDTH / 2, SCREEN_HEIGHT - BAT_HEIGHT,
				BAT_WIDTH, BAT_HEIGHT);
		ball = new Circle(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, BALL_RADIUS);
		// Set up the level creator and load the first level.
		try {
			levelCreator = new LevelCreator();
			levelCreator.createLevel("data/1.txt");
			currentLevel = levelCreator.getLevel();
			bricksLeft = currentLevel.length;
		} catch (IOException e) {
			System.exit(1);
		}
		// Load textures.
		playingField = new Image("data/Outbreak_background.jpg");
		playerColor = new Image("data/bat_color.jpg");
		ballColor = new Image("data/tennis_ball_color.jpg");
		brickColor = new Image("data/brick_color.jpg");
		// Create fonts to draw strings on the screen.
		scoreFont = new UnicodeFont(new Font("Tw Cen MT", 3, 20));
		scoreFont.addAsciiGlyphs();
		scoreFont.getEffects().add(new ColorEffect(java.awt.Color.BLACK));
		scoreFont.loadGlyphs();
		victoryFont = new UnicodeFont(new Font("Tw Cen MT", 3, 30));
		victoryFont.addAsciiGlyphs();
		victoryFont.getEffects().add(new ColorEffect(java.awt.Color.ORANGE));
		victoryFont.loadGlyphs();

		// Stopwatch used for typing the end of round message.
		stopwatch = new Stopwatch();

		lives = 5;

	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		// Draw the player bats and the ball.
		playingField.draw(0, 0);
		// Type out the victory message if someone beats the game.
		if (beatTheGame) {
			return;
		}
		g.texture(player, playerColor);
		g.texture(ball, ballColor);
		if (endOfRound) {
			victoryFont.drawString(SCREEN_WIDTH / 5, SCREEN_HEIGHT / 2,
					"Well done. Progressing to next level.");
			scoreFont.drawString(SCREEN_WIDTH * 4 / 6, SCREEN_HEIGHT * 3 / 4,
					"Score: " + score);
			scoreFont.drawString(SCREEN_WIDTH * 4 / 6, SCREEN_HEIGHT * 3 / 4 + 20,
					"Lives: " + lives);
			return;
		}
		// Draw all the bricks. If durability is equal to zero, don't draw the
		// brick. (Brick is destroyed.)
		for (Brick b : currentLevel) {
			if (b.getDurability() != 0)
				g.texture(b.getRectangle(), brickColor);

		}
		scoreFont.drawString(SCREEN_WIDTH * 4 / 6, SCREEN_HEIGHT * 3 / 4,
				"Score: " + score);
		scoreFont.drawString(SCREEN_WIDTH * 4 / 6, SCREEN_HEIGHT * 3 / 4 + 20,
				"Lives: " + lives);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		// If-statements to determine when to stop printing the end of round
		// message.
		if (lives <= 0) {
			restartLevel();
			sbg.enterState(BatRackets.LOSEMENUSTATE);
		}
		if (endOfRound && !stopwatch.isRunning())
			stopwatch.reset().start();
		if (endOfRound && stopwatch.milliseconds() < 3000)
			return;
		if (endOfRound && stopwatch.milliseconds() >= 3000) {
			endOfRound = false;
			stopwatch.stop().reset();
		}
		time += delta;
		if (time < 20)
			return;
		time -= 20;
		endOfRound = false;
		Input input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_ESCAPE))
			sbg.enterState(BatRackets.PAUSEMENUSTATE);
		updatePlayer(input);
		if (input.isKeyPressed(Input.KEY_SPACE))
			hasStarted = true;
		if (!hasStarted) {
			ball.setCenterX(player.getCenterX());
			ball.setCenterY(player.getMinY() - BALL_RADIUS);
			return;
		}
		checkBallCollision();
		if (bricksLeft == 0) {
			endOfRound = true;
			hasStarted = false;
			thisLevel += 1;
			// add a life-point as reward.
			lives++;
			generateNextLevel();
			if (beatTheGame) {
				restartGame();
				sbg.enterState(BatRackets.WINMENUSTATE);
			}
		}
	}

	// Generate the next level.
	public void generateNextLevel() {
		if (thisLevel == 6) {
			beatTheGame = true;
		}
		try {
			levelCreator.createLevel("data/" + thisLevel + ".txt");
			currentLevel = levelCreator.getLevel();
			bricksLeft = currentLevel.length;
		} catch (IOException e) {
			System.exit(1);
		}

	}

	public void checkBallCollision() {
		// Adjust the x-coordinate of the ball. If the ball hits the edge of the
		// playing field, make it "bounce" by multiplying the y-component of the
		// velocity vector by -1. If the ball travels outside the screen, move
		// it just inside of the screen again.
		ball.setX(ball.getX() + ballVelocity.x);
		if (ball.getMinX() <= 0) {
			ballVelocity.x *= -1;
			// Moves the ball just inside the screen.
			ball.setX(0 + BALL_RADIUS);
		} else if (ball.getMaxX() >= SCREEN_WIDTH) {
			ballVelocity.x *= -1;
			ball.setX(SCREEN_WIDTH - 2 * BALL_RADIUS);
		}

		// Adjust the y-coordinate of the ball. Check for bat-ball/roof
		// collision. If
		// the ball reaches the bottom of the screen before colliding with the
		// bat, set the ball position to the middle of the field. If the ball
		// travels outside the screen, move it just inside of the screen again.
		ball.setY(ball.getY() + ballVelocity.y);
		if (ball.intersects(player) && !hasIntersected) {
			ballVelocity.y *= -1;
			// Boolean variable that disallows the ball from bouncing multiple
			// times inside the player bat.
			hasIntersected = true;
			// Adjust the angle of the speed vector depending on which side of
			// the bat it hits.
			adjustSpeedVector();
		} else if (ball.getY() <= 0) {
			ballVelocity.y *= -1;
			ball.setY(0 + BALL_RADIUS);
			hasIntersected = false;
		} else if (ball.getY() >= SCREEN_HEIGHT) { // If the ball reaches the
			// bottom of the screen
			// without colliding with
			// player bat.
			// End of round.
			// loses one life
			hasIntersected = false;
			hasStarted = false;
			lives--;
		}

		for (Brick b : currentLevel) {
			if (ball.intersects(b.getBottomSide()) && b.getDurability() != 0) {
				ballVelocity.y *= -1;
				b.decrementDurability();
				if (b.getDurability() == 0) {
					score += 100;
					bricksLeft -= 1;
				}
				ball.setY(b.getBottomSide().getY1() + BALL_RADIUS);
				hasIntersected = false;
			}
			if (ball.intersects(b.getTopSide()) && b.getDurability() != 0) {
				ballVelocity.y *= -1;
				b.decrementDurability();
				if (b.getDurability() == 0) {
					score += 100;
					bricksLeft -= 1;
				}
				ball.setY(b.getTopSide().getY1() + BALL_RADIUS);
				hasIntersected = false;
			}
			if (ball.intersects(b.getRightSide()) && b.getDurability() != 0) {
				ballVelocity.x *= -1;
				b.decrementDurability();
				if (b.getDurability() == 0) {
					score += 100;
					bricksLeft -= 1;
				}
				hasIntersected = false;
			}
			if (ball.intersects(b.getLeftSide()) && b.getDurability() != 0) {
				ballVelocity.x *= -1;
				b.decrementDurability();
				if (b.getDurability() == 0) {
					score += 100;
					bricksLeft -= 1;
				}
				hasIntersected = false;
			}
		}

	}

	/*
	 * Determine the angle that we should adjust the velocity by. The
	 * adjustion-angle varies between +35 and -35 degrees, depending on distance
	 * to bat centerpoint.
	 */
	public void adjustSpeedVector() {
		// Distance to bat center.
		double x = ball.getX() - player.getCenterX();
		// Divide by half bat length.
		x /= BAT_WIDTH / 2;
		// Multiply by 35 (degrees).
		x *= 35;
		ballVelocity.add(x);
		// Preventing the ball from moving downwards and from having a too slant
		// angle.
		// Here, x is the new angle assigned to ballVelocity.
		x = ballVelocity.getTheta();
		if (90 < x && x < 185)
			ballVelocity.setTheta(185);
		else if (x > 355 || x < 185)
			ballVelocity.setTheta(355);
	}

	public void updatePlayer(Input input) {
		// Move the player bat.
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			if (player.getX() + BAT_MOVEMENT_SPEED <= SCREEN_WIDTH - BAT_WIDTH)
				player.setX(player.getX() + BAT_MOVEMENT_SPEED);
		}
		if (input.isKeyDown(Input.KEY_LEFT)) {
			if (player.getX() - BAT_MOVEMENT_SPEED >= 0)
				player.setX(player.getX() - BAT_MOVEMENT_SPEED);
		}
	}

	public void restartLevel() {
		lives = 5;
		ballVelocity = new Vector2f(5, 13);
		beatTheGame = false;
		endOfRound = false;
		hasStarted = false;
		score = 0;
		try {
			levelCreator.createLevel("data/" + thisLevel + ".txt");
			currentLevel = levelCreator.getLevel();
			bricksLeft = currentLevel.length;
		} catch (IOException e) {
			System.exit(1);
		}

	}

	public void restartGame() {
		thisLevel = 1;
		lives = 5;
		ballVelocity = new Vector2f(5, 13);
		beatTheGame = false;
		endOfRound = false;
		hasStarted = false;
		score = 0;
		try {
			levelCreator.createLevel("data/" + thisLevel + ".txt");
			currentLevel = levelCreator.getLevel();
			bricksLeft = currentLevel.length;
		} catch (IOException e) {
			System.exit(1);
		}
	}
}
