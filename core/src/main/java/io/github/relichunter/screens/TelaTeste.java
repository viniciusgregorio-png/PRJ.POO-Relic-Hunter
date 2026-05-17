package io.github.relichunter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.relichunter.Main;

public class TelaTeste implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private MapaTeste mapa;
    private PersonagemTeste personaje;

    private OrthographicCamera camera;
    private Viewport viewport;

    private final int LARGURA_VIRTUAL = 480;
    private final int ALTURA_VIRTUAL = 320;

    public TelaTeste(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        mapa = new MapaTeste();
        personaje = new PersonagemTeste();

        camera = new OrthographicCamera();
        viewport = new FitViewport(LARGURA_VIRTUAL, ALTURA_VIRTUAL, camera);

        camera.position.set(LARGURA_VIRTUAL / 2f, ALTURA_VIRTUAL / 2f, 0);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.15f, 1f);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        personaje.atualizar(mapa, delta);

        batch.begin();
        mapa.desenhar(batch, ALTURA_VIRTUAL);
        personaje.desenhar(batch, ALTURA_VIRTUAL);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        mapa.dispose();
        personaje.dispose();
    }
}
