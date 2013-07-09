package libgdxloadingscreen.screen;

import libgdxloadingscreen.SomeCoolGame;

import com.badlogic.gdx.Screen;

/**
 * @author Mats Svensson
 */
public abstract class AbstractScreen implements Screen {

    protected SomeCoolGame game;

    public AbstractScreen(SomeCoolGame game) {
        this.game = game;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
