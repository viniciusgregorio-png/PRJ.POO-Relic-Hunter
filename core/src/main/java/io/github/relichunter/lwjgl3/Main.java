package io.github.relichunter.lwjgl3;

import com.badlogic.gdx.Game;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new SplashScreen(this));
    }
}
