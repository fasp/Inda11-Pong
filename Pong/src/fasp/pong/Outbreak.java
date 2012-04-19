package fasp.pong;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;
import java.awt.Font;
import java.awt.Color;
import java.util.Random;


public class Outbreak extends BasicGame {

	private Rectangle player;
	private Circle ball;
	private Image playingField;
	private Image playerColor;
	private Image ballColor;
	private Vector2f ballVelocity;
	private int playerScore;
	private UnicodeFont scoreFont;
	private boolean hasIntersected;
	private Random rand = new Random();
	//We need to access the input from
	private Input input;

	// Constants such as screen height, bat size, etc.
	static boolean FULLSCREEN = false;
	static int SCREEN_HEIGHT = 600;
	static int SCREEN_WIDTH = 800;
	static int BAT_WIDTH = 50;
	static int BAT_HEIGHT = 10;
	static int BAT_MOVEMENT_SPEED = 10;
	static int BALL_RADIUS = 10;

	public Outbreak() {
		super("Outbreak");
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		// Draw the player bats and the ball.
		playingField.draw(0, 0);
		g.texture(player, playerColor);
		g.texture(ball, ballColor);
		scoreFont.drawString(SCREEN_WIDTH / 4, SCREEN_HEIGHT / 30, "Score: "
				+ playerScore);
	}

	public void init(GameContainer container) throws SlickException {
		ballVelocity = new Vector2f(1, 10);
		// Set up the bats and the ball at the correct positions.
		player = new Rectangle(SCREEN_WIDTH / 2, SCREEN_HEIGHT - BAT_HEIGHT,
				BAT_WIDTH, BAT_HEIGHT);

		ball = new Circle(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, BALL_RADIUS);
		// Load textures.
		playingField = new Image("data/tennis_court.jpg");
		playerColor = new Image("data/bat_color.jpg");
		ballColor = new Image("data/tennis_ball_color.jpg");
		playerScore = 0;

		// Create a font to draw scores on the screen.
		scoreFont = new UnicodeFont(new Font("Tw Cen MT", 3, 20));
		scoreFont.addAsciiGlyphs();
		scoreFont.getEffects().add(new ColorEffect(java.awt.Color.DARK_GRAY));
		scoreFont.loadGlyphs();
	}

	public void update(GameContainer container, int delta)
			throws SlickException {
		input = container.getInput();
		updatePlayer(input);
		updateBall();

	}

	public void updateBall() {
		// Adjust the y-coordinate of the ball. If the ball hits the edge of the
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
			ball.setX(SCREEN_WIDTH - 2*BALL_RADIUS);
		}

		// Adjust the y-coordinate of the ball. Check for bat-ball/roof
		// collision. If
		// the ball reaches the bottom of the screen before colliding with the
		// bat, set the ball position to the middle of the field. If the ball
		// travels outside the screen, move it just inside of the screen again.
		ball.setY(ball.getY() + ballVelocity.y);
		if (ball.intersects(player) && !hasIntersected) {
			ballVelocity.y *= -1;
			//Boolean variable that disallows the ball from bouncing multiple times inside the player bat.
			hasIntersected = true; 
			//Adjust the angle of the speed vector depending on which side of the bat it hits.
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
			hasIntersected = false;
			ball.setX(SCREEN_WIDTH / 2);
			ball.setY(SCREEN_HEIGHT / 2);
		}
	}
	/* Determine the angle that we should adjust the velocity by. 
	 * The angle varies between +45 and -45 degrees, depending on distance to bat centerpoint. 
	 */
	public void adjustSpeedVector(){
		double distanceToBatCenter = ball.getX() - player.getCenterX();
		//Divide by half bat length.
		distanceToBatCenter /= BAT_WIDTH/2;
		//Multiply by 45 (degrees).
		distanceToBatCenter *= 35;
		ballVelocity.add(distanceToBatCenter);
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

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Outbreak(),
				SCREEN_WIDTH, SCREEN_HEIGHT, FULLSCREEN);
		app.setTargetFrameRate(60);
		app.start();
	}
}
