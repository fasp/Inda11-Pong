package fasp.pong;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;
import java.awt.Font;

public class Pong extends BasicGame {

	Rectangle player_1;
	Rectangle player_2;
	Circle ball;
	Image playingField;
	Image playerColor;
	Image ballColor;
	Vector2f ballVelocity;
	int player_1_score, player_2_score;

	// Constants such as screen height, bat size, etc.
	static boolean FULLSCREEN = false;
	static int SCREEN_HEIGHT = 600;
	static int SCREEN_WIDTH = 800;
	static int BAT_WIDTH = 20;
	static int BAT_HEIGHT = 50;
	static int BAT_MOVEMENT_SPEED = 10;
	static 

	public Pong() {
		super("Pong");
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		// Draw the player bats and the ball.
		playingField.draw(0, 0);
		g.texture(player_1, playerColor);
		g.texture(player_2, playerColor);
		g.texture(ball, ballColor);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		ballVelocity = new Vector2f(5, 5);
		// Set up the bats and the ball at the correct positions.
		player_1 = new Rectangle(0, SCREEN_HEIGHT / 2, BAT_WIDTH, BAT_HEIGHT);
		player_2 = new Rectangle(SCREEN_WIDTH - BAT_WIDTH, SCREEN_HEIGHT / 2,
				BAT_WIDTH, BAT_HEIGHT);
		ball = new Circle(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, 10);
		// Load textures.
		playingField = new Image("data/tennis_court.jpg");
		playerColor = new Image("data/bat_color.jpg");
		ballColor = new Image("data/tennis_ball_color.jpg");
		player_1_score = 0;
		player_2_score = 0;
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {

		Input input = container.getInput();
		// Move the player bats.
		if (input.isKeyDown(Input.KEY_S)) {
			if (player_1.getY() + BAT_MOVEMENT_SPEED <= 550)
				player_1.setY(player_1.getY() + BAT_MOVEMENT_SPEED);
		}
		if (input.isKeyDown(Input.KEY_W)) {
			if (player_1.getY() - BAT_MOVEMENT_SPEED >= 0)
				player_1.setY(player_1.getY() - BAT_MOVEMENT_SPEED);
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			if (player_2.getY() + BAT_MOVEMENT_SPEED <= 550)
				player_2.setY(player_2.getY() + BAT_MOVEMENT_SPEED);
		}
		if (input.isKeyDown(Input.KEY_UP)) {
			if (player_2.getY() - BAT_MOVEMENT_SPEED >= 0)
				player_2.setY(player_2.getY() - BAT_MOVEMENT_SPEED);
		}

		// Adjust the y-coordinate of the ball. If the ball hits the edge of the
		// playing field, make it "bounce" by multiplying the y-component of the
		// velocity vector by -1.
		ball.setY(ball.getY() + ballVelocity.y);
		if (ball.getMinY() <= 0 || ball.getMaxY() >= 600)
			ballVelocity.y = -ballVelocity.y;
		// Adjust the x-coordinate of the ball. Check for bat-ball collision. If
		// the ball reaches the edge of the screen before colliding with the
		// bat, set the ball position to the middle of the field.
		ball.setX(ball.getX() + ballVelocity.x);
		if (ball.intersects(player_1) || ball.intersects(player_2)) {
			ballVelocity.x = -ballVelocity.x;
		}else if(ball.getX() >= 800 || ball.getX() <= 0){ // If the ball reaches the edge of the screen without colliding with a player bat.
			ball.setX(SCREEN_WIDTH / 2);
			ball.setY(SCREEN_HEIGHT / 2);
			if(ball.getX() >= 800)
				player_1_score++;
			if(ball.getX() <= 0)
				player_2_score++;
		}
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Pong(), SCREEN_WIDTH, SCREEN_HEIGHT,
				FULLSCREEN);
		app.setTargetFrameRate(60);
		app.start();
	}

}
