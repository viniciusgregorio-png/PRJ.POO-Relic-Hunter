package io.github.relichunter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.relichunter.Main;
import io.github.relichunter.entidades.Bau;
import io.github.relichunter.entidades.PedraEmpurravel;
import io.github.relichunter.entidades.PedraQueCai;
import io.github.relichunter.entidades.Rubi;

public class TelaTeste implements Screen {

    private PedraQueCai pedraQueCai;
    private PedraEmpurravel pedraEmpurravel;
    private Rubi[] rubis;
    private Bau bau;

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
        // 3 rubis em posições diferentes do mapa
        rubis = new Rubi[3];
        rubis[0] = new Rubi(32, 256, 32, 32, personaje);
        rubis[1] = new Rubi(96, 256, 32, 32, personaje);
        rubis[2] = new Rubi(160, 256, 32, 32, personaje);

// baú
        bau = new Bau(256, 256, 32, 32, personaje, rubis);

// pedras
        pedraQueCai = new PedraQueCai(64, 288, 32, 32, personaje, mapa);
        pedraEmpurravel = new PedraEmpurravel(192, 224, 32, 32, mapa, personaje);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.15f, 1f);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        personaje.atualizar(mapa, delta);
        pedraQueCai.update(delta);
        pedraEmpurravel.update(delta);
        for (Rubi rubi : rubis) { rubi.update(delta); }
        bau.update(delta);

        batch.begin();
        mapa.desenhar(batch, ALTURA_VIRTUAL);
        personaje.desenhar(batch, ALTURA_VIRTUAL);
        batch.end();

        pedraQueCai.render();
        pedraEmpurravel.render();
        for (Rubi rubi : rubis) { rubi.render(); }
        bau.render();


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
        pedraQueCai.dispose();
        pedraEmpurravel.dispose();
        for (Rubi rubi : rubis) { rubi.dispose(); }
        bau.dispose();
    }
}
