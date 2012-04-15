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
import java.lang.Integer;
import java.awt.Color;

public class Outbreak extends BasicGame {

    Rectangle player;
    Circle ball;
    Image playingField;
    Image playerColor;
    Image ballColor;
    Vector2f ballVelocity;
    int playerScore;
    UnicodeFont scoreFont;

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
      scoreFont.drawString(SCREEN_WIDTH / 4, SCREEN_HEIGHT / 30, "Score: " + playerScore);
    }

    
    public void init(GameContainer container) throws SlickException {
      ballVelocity = new Vector2f(5, 5);
      // Set up the bats and the ball at the correct positions.
      player = new Rectangle(SCREEN_WIDTH / 2, SCREEN_HEIGHT - BAT_HEIGHT, BAT_WIDTH, BAT_HEIGHT);

      ball = new Circle(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, BALL_RADIUS);
      // Load textures.
      playingField = new Image("data/tennis_court.jpg");
      playerColor = new Image("data/bat_color.jpg");
      ballColor = new Image("data/tennis_ball_color.jpg");
      playerScore = 0;
      
      //Create a font to draw scores on the screen.
      scoreFont = new UnicodeFont(new Font("Tw Cen MT", 3, 20));
      scoreFont.addAsciiGlyphs();
      scoreFont.getEffects().add(new ColorEffect(java.awt.Color.DARK_GRAY));
      scoreFont.loadGlyphs();
    }

    
    public void update(GameContainer container, int delta)
        throws SlickException {

      Input input = container.getInput();
      // Move the player bats.
      if (input.isKeyDown(Input.KEY_RIGHT)) {
        if (player.getX() + BAT_MOVEMENT_SPEED <= SCREEN_WIDTH - BAT_WIDTH)
          player.setX(player.getX() + BAT_MOVEMENT_SPEED);
      }
      if (input.isKeyDown(Input.KEY_LEFT)) {
        if (player.getX() - BAT_MOVEMENT_SPEED >= 0)
          player.setX(player.getX() - BAT_MOVEMENT_SPEED);
      }
  
      // Adjust the y-coordinate of the ball. If the ball hits the edge of the
      // playing field, make it "bounce" by multiplying the y-component of the
      // velocity vector by -1.
      ball.setX(ball.getX() + ballVelocity.x);
      if (ball.getMinX() <= 0 || ball.getMaxX() >= SCREEN_WIDTH)
        ballVelocity.x = -ballVelocity.x;
  
      // Adjust the y-coordinate of the ball. Check for bat-ball/roof collision. If
      // the ball reaches the bottom of the screen before colliding with the
      // bat, set the ball position to the middle of the field.
      ball.setY(ball.getY() + ballVelocity.y);
      if (ball.intersects(player)||ball.getY() <= 0) {
        ballVelocity.y = -ballVelocity.y;
      }else if(ball.getY() >= SCREEN_WIDTH) { // If the ball reaches the bottom of the screen without colliding with player bat.
        //End of round.
        ball.setX(SCREEN_WIDTH / 2);
        ball.setY(SCREEN_HEIGHT / 2);
      }
    }

    public static void main(String[] args) throws SlickException {
      AppGameContainer app = new AppGameContainer(new Outbreak(), SCREEN_WIDTH, SCREEN_HEIGHT,
          FULLSCREEN);
      app.setTargetFrameRate(60);
      app.start();
    }
}
