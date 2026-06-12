package io.github.relichunter.screens;

import com.badlogic.gdx.Screen;
import io.github.relichunter.Main;

public class GameScreen implements Screen {
    private static final int LARGURA_VIRTUAL = 480;
    private static final int ALTURA_VIRTUAL = 320;

    private final Main game;
    private MapaTeste mapa;

    public GameScreen(Main game) {
        this.game = game;
        this.mapa = new MapaTeste();
    }

    @Override public void show() {}
    @Override public void render(float delta) {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { mapa.dispose(); }
}
