package io.github.relichunter;

import com.badlogic.gdx.Game;
import io.github.relichunter.screens.SplashScreen;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new SplashScreen(this));
    }
}
