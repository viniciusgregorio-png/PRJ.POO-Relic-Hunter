package io.github.relichunter.screens;

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
import io.github.relichunter.inimigos.InimigosSnake;

public class TelaTeste implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private MapaTeste mapa;
    private PersonagemTeste personaje;

    private PedraQueCai pedraQueCai;
    private PedraEmpurravel pedraEmpurravel;
    private Rubi[] rubis;
    private Bau bau;
    private InimigosSnake snake;

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
        camera.position.set(LARGURA_VIRTUAL / 2f, ALTURA_VIRTUAL / 2f, 0f);

        rubis = new Rubi[3];
        rubis[0] = new Rubi(160.0F, 64.0F, 28.0F, 28.0F, personaje);
        rubis[1] = new Rubi(224.0F, 64.0F, 28.0F, 28.0F, personaje);
        rubis[2] = new Rubi(288.0F, 64.0F, 28.0F, 28.0F, personaje);

        bau = new Bau(352.0F, 64.0F, 28.0F, 28.0F, personaje, rubis);
        pedraQueCai = new PedraQueCai(1.0F, 320.0F, 32.0F, 32.0F, personaje, mapa, ALTURA_VIRTUAL);
        pedraEmpurravel = new PedraEmpurravel(160.0F, 224.0F, 32.0F, 32.0F, mapa, personaje, ALTURA_VIRTUAL);
        snake = new InimigosSnake(10, 100, 224.0F, 128.0F, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15F, 0.15F, 0.15F, 1.0F);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        personaje.atualizar(mapa, delta);
        pedraQueCai.update(delta);
        pedraEmpurravel.update(delta);

        for (Rubi rubi : rubis) {
            rubi.update(delta);
        }

        bau.update(delta);
        snake.update(delta);

        if (snake.encostouNoPlayer(personaje)) {
            game.setScreen(new GameOverScreen(game));
            return;
        }

        batch.begin();
        mapa.desenhar(batch, ALTURA_VIRTUAL);
        personaje.desenhar(batch, ALTURA_VIRTUAL);
        snake.render(batch);
        batch.end();

        pedraQueCai.render(camera);
        pedraEmpurravel.render(camera);

        for (Rubi rubi : rubis) {
            rubi.render(camera);
        }
        bau.render(camera);
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

        for (Rubi rubi : rubis) {
            rubi.dispose();
        }
        bau.dispose();
        snake.dispose();
    }
}
