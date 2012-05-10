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

public class MainMenuState extends BasicGameState{

    Image background = null;
    Image newGameOption = null;
    Image exitOption = null;

    int stateID = 0;

    private static int menuX = 200;
    private static int menuY = 100;

    float newGameScale = 1;
    float exitScale = 1;

    Sound fx = null;

    public MainMenuState(int stateID )
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
        newGameOption = menuOptions.getSubImage(60, 30, 240, 60);

        exitOption = menuOptions.getSubImage(60, 100, 225, 65);

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
        newGameOption.draw(menuX, menuY, newGameScale);

        exitOption.draw(menuX, menuY+80, exitScale);
    }

    float scaleStep = 0.0001f;

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gc.getInput();

        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        boolean insideNewGame = false;
        boolean insideExit = false;

        if( ( mouseX >= menuX && mouseX <= menuX + newGameOption.getWidth()) &&
            ( mouseY >= menuY && mouseY <= menuY + newGameOption.getHeight()) )
        {
            insideNewGame = true;
        }else if( ( mouseX >= menuX && mouseX <= menuX+ exitOption.getWidth()) &&
            ( mouseY >= menuY+80 && mouseY <= menuY+80 + exitOption.getHeight()) )
        {
            insideExit = true;
        }

        if(insideNewGame)
        {
            if(newGameScale < 1.05f)
                newGameScale += scaleStep * delta;

            if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
            //    fx.play();
                sbg.enterState(BatRackets.GAMEPLAYSTATE);
            }
        }else{
            if(newGameScale > 1.0f)
                newGameScale -= scaleStep * delta;
        }

        if(insideExit)
        {
            if(exitScale < 1.05f)
                exitScale +=  scaleStep * delta;
            if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) )
                gc.exit();
        }else{
            if(exitScale > 1.0f)
                exitScale -= scaleStep * delta;
        }
    }

}