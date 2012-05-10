package fasp.pong;

import java.awt.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class PauseMenuState extends BasicGameState {

	 Image background = null;
	    Image resumeGameOption = null;
	    Image exitOption = null;
	    Image MainMenuOption = null;

	    int stateID = 2;

	    private static int menuX = 200;
	    private static int menuY = 100;

	    float resumeGameScale = 1;
	    float exitScale = 1;
	    float mainMenuScale = 1;

	    Sound fx = null;

	    public PauseMenuState(int stateID )
	    {
	        this.stateID = stateID;
	    }

	    @Override
	    public int getID() {
	        return stateID;
	    }

	    UnicodeFont unicodeFont = null;

	    public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
	        background = new Image("data/menu.jpg");

	        // Load the menu images
	        Image menuOptions = new Image("data/menuoptions.jpg");
	        resumeGameOption = menuOptions.getSubImage(60, 190, 320, 60);

	        exitOption = menuOptions.getSubImage(60, 100, 225, 65);
	        
	        MainMenuOption = menuOptions.getSubImage(60, 290, 235, 60);

	        //--------------------------------------------------

	       // fx = new Sound("test.wav");

	        //--------------------------------------------------

	        Font font = new Font("Verdana", Font.BOLD, 20);
	        unicodeFont = new UnicodeFont(font);
	        
	    }



	    public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
	        // render the background
	        background.draw(0, 0);

	        // Draw menu
	        resumeGameOption.draw(menuX, menuY, resumeGameScale);

	        exitOption.draw(menuX, menuY+160, exitScale);
	        
	        MainMenuOption.draw(menuX, menuY+80, mainMenuScale);
	    }

	    float scaleStep = 0.0001f;

	    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
	        Input input = gc.getInput();

	        int mouseX = input.getMouseX();
	        int mouseY = input.getMouseY();

	        boolean insideResumeGame = false;
	        boolean insideExit = false;
	        boolean insideMainMenu = false;

	        if( ( mouseX >= menuX && mouseX <= menuX + resumeGameOption.getWidth()) &&
	            ( mouseY >= menuY && mouseY <= menuY + resumeGameOption.getHeight()) )
	        {
	            insideResumeGame = true;
	        }
	        
	        if( ( mouseX >= menuX && mouseX <= menuX+ exitOption.getWidth()) &&
	            ( mouseY >= menuY+160 && mouseY <= menuY+160 + exitOption.getHeight()) )
	        {
	            insideExit = true;
	        }
	        if( ( mouseX >= menuX && mouseX <= menuX + MainMenuOption.getWidth()) &&
		        ( mouseY >= menuY+80 && mouseY <= menuY+80 + MainMenuOption.getHeight()) )
		        {
		            insideMainMenu = true;
		        }

	        if(insideResumeGame)
	        {
	            if(resumeGameScale < 1.05f)
	                resumeGameScale += scaleStep * delta;

	            if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
	            //    fx.play();
	                sbg.enterState(BatRackets.GAMEPLAYSTATE);
	            }
	        }else{
	            if(resumeGameScale > 1.0f)
	                resumeGameScale -= scaleStep * delta;
	        }
	        if(insideMainMenu)
	        {
	            if(mainMenuScale < 1.05f)
	                mainMenuScale += scaleStep * delta;

	            if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
	            //    fx.play();
	                sbg.enterState(BatRackets.MAINMENUSTATE);
	            }
	        }else{
	            if(mainMenuScale > 1.0f)
	            mainMenuScale -= scaleStep * delta;
	        }

	        if(insideExit)
	        {
	            if(exitScale < 1.05f)
	                exitScale +=  scaleStep * delta;
	            if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
	                gc.exit();
	        }else{
	            if(exitScale > 1.0f)
	                exitScale -= scaleStep * delta;
	        }
	    }
}
