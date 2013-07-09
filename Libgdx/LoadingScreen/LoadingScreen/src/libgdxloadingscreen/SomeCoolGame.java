package libgdxloadingscreen;

import libgdxloadingscreen.screen.LoadingScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;


/**
 * @author Mats Svensson
 */
public class SomeCoolGame extends Game {

    /**
     * Holds all our assets
     */
    public AssetManager manager = new AssetManager();

    @Override
    public void create() {
        setScreen(new LoadingScreen(this));
    }
}
