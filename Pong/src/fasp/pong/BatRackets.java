package fasp.pong;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
*
* @author Axel
*
*/
public class BatRackets extends StateBasedGame {

    public static final int MAINMENUSTATE          = 0;
    public static final int GAMEPLAYSTATE          = 1;
    public static final int PAUSEMENUSTATE         = 2;
    public static final int WINMENUSTATE           = 3;
    public static final int LOSEMENUSTATE          = 4;

    public BatRackets()
    {
        super("BatRackets");

        this.addState(new MainMenuState(MAINMENUSTATE));
        this.addState(new GameplayState(GAMEPLAYSTATE));
        this.addState(new PauseMenuState(PAUSEMENUSTATE));
        this.addState(new WinMenuState(WINMENUSTATE));
        this.addState(new LoseMenuState(LOSEMENUSTATE));
        this.enterState(MAINMENUSTATE);
    }

    public static void main(String[] args) throws SlickException
    {
         AppGameContainer app = new AppGameContainer(new BatRackets());

         app.setDisplayMode(900, 800, false);
         app.setTargetFrameRate(60);
         app.start();
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {

       this.getState(MAINMENUSTATE).init(gameContainer, this);
       this.getState(GAMEPLAYSTATE).init(gameContainer, this);
       this.getState(PAUSEMENUSTATE).init(gameContainer, this);
       this.getState(WINMENUSTATE).init(gameContainer, this);
       this.getState(LOSEMENUSTATE).init(gameContainer, this);
        
    }
}