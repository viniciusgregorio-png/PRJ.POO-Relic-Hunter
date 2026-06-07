package io.github.relichunter.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.relichunter.Main;
import io.github.relichunter.entidades.Bau;
import io.github.relichunter.entidades.PedraEmpurravel;
import io.github.relichunter.entidades.PedraQueCai;
import io.github.relichunter.entidades.Rubi;
import io.github.relichunter.inimigos.*;

public class TelaTeste implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private Texture resetButton;
    private MapaTeste mapa;
    private PersonagemTeste personaje;

    private PedraQueCai pedraQueCai;
    private PedraEmpurravel pedraEmpurravel;
    private com.badlogic.gdx.utils.Array<Rubi> listaRubis;
    private boolean jaLiberouNovosRubis = false;
    private Bau bau;

    // Nossos inimigos dinâmicos
    private PrimeiroInimigo snake;
    private TerceiroInimigo snakeT;
    private QuintoInimigo snakeQuinto;

    private boolean inimigosAtivos = true;

    private OrthographicCamera camera;
    private Viewport viewport;

    // Resolução da janela que foca no player
    private final int LARGURA_VIRTUAL = 480;
    private final int ALTURA_VIRTUAL = 320;

    private static final float RESET_BUTTON_SIZE = 48f;
    private static final float RESET_BUTTON_X = 8f;
    private static final float RESET_BUTTON_Y = 268f;

    public TelaTeste(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        resetButton = new Texture("resetGame.png");
        mapa = new MapaTeste();
        personaje = new PersonagemTeste();

        camera = new OrthographicCamera();
        viewport = new FitViewport(LARGURA_VIRTUAL, ALTURA_VIRTUAL, camera);

        listaRubis = new com.badlogic.gdx.utils.Array<Rubi>();

        // ─── LEITURA AUTOMÁTICA DE OBJETOS DO TILED ───
        MapLayer camadaObjetos = mapa.getTiledMap().getLayers().get("objetos");

        if (camadaObjetos != null) {
            for (MapObject objeto : camadaObjetos.getObjects()) {
                float objX = objeto.getProperties().get("x", Float.class);
                float objY = objeto.getProperties().get("y", Float.class);
                String nome = objeto.getName();

                if (nome == null) continue;

                // Spawna o objeto de acordo com o nome dado na propriedade do Tiled
                if (nome.equalsIgnoreCase("player")) {
                    personaje.setX(objX);
                    personaje.setY(objY);
                }
                else if (nome.equalsIgnoreCase("rubi")) {
                    listaRubis.add(new Rubi(objX, objY, 28.0F, 28.0F, personaje));
                }
                else if (nome.equalsIgnoreCase("snake_horizontal")) {
                    snake = new PrimeiroInimigo(10, 100, objX, objY, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa);
                }
                else if (nome.equalsIgnoreCase("snake_vertical_meio")) {
                    snakeT = new TerceiroInimigo(10, 100, objX, objY, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa);
                }
                else if (nome.equalsIgnoreCase("snake_vertical_direita")) {
                    snakeQuinto = new QuintoInimigo(10, 100, objX, objY, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa);
                }
            }
        }

        // Inicializa os demais elementos vinculados
        Rubi[] arrayParaOBau = listaRubis.toArray(Rubi.class);
        bau = new Bau(352.0F, 64.0F, 28.0F, 28.0F, personaje, arrayParaOBau); // Pode ser mantido fixo ou movido via Tiled

        pedraQueCai = new PedraQueCai(1.0F, 320.0F, 32.0F, 32.0F, personaje, mapa, ALTURA_VIRTUAL);
        pedraEmpurravel = new PedraEmpurravel(129.0F, 129.0F, 32.0F, 32.0F, mapa, personaje, ALTURA_VIRTUAL);
    }

    private void handleResetButton() {

        if (!Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            return;
        }

        Vector3 touchPos = new Vector3(
            Gdx.input.getX(),
            Gdx.input.getY(),
            0
        );

        viewport.unproject(touchPos);

        float hudX = camera.position.x
            - viewport.getWorldWidth() / 2f
            + RESET_BUTTON_X;

        float hudY = camera.position.y
            - viewport.getWorldHeight() / 2f
            + RESET_BUTTON_Y;

        if (touchPos.x >= hudX
            && touchPos.x <= hudX + RESET_BUTTON_SIZE
            && touchPos.y >= hudY
            && touchPos.y <= hudY + RESET_BUTTON_SIZE) {

            game.setScreen(new EndGameScreen(game, getTotalRubisColetados()));
        }
    }

    private void drawHUD() {

        float hudX = camera.position.x
            - viewport.getWorldWidth() / 2f
            + RESET_BUTTON_X;

        float hudY = camera.position.y
            - viewport.getWorldHeight() / 2f
            + RESET_BUTTON_Y;

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(
            resetButton,
            hudX,
            hudY,
            RESET_BUTTON_SIZE,
            RESET_BUTTON_SIZE
        );

        batch.end();
    }

    private void atualizarCameraSeguirPlayer() {
        // Foca o centro da câmera no Personagem
        camera.position.x = personaje.getPosX() + 16f;
        camera.position.y = personaje.getPosY() + 16f;

        // Limita a câmera dentro das bordas do mapa (60 colunas x 32px e 40 linhas x 32px)
        float metadeLarguraCam = viewport.getWorldWidth() / 2f;
        float metadeAlturaCam = viewport.getWorldHeight() / 2f;

        float limiteDireitoMapa = 60 * 32; // 1920 px
        float limiteSuperiorMapa = 40 * 32; // 1280 px

        if (camera.position.x < metadeLarguraCam) camera.position.x = metadeLarguraCam;
        if (camera.position.x > limiteDireitoMapa - metadeLarguraCam) camera.position.x = limiteDireitoMapa - metadeLarguraCam;

        if (camera.position.y < metadeAlturaCam) camera.position.y = metadeAlturaCam;
        if (camera.position.y > limiteSuperiorMapa - metadeAlturaCam) camera.position.y = limiteSuperiorMapa - metadeAlturaCam;

        camera.update();
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.15F, 0.15F, 0.15F, 1.0F);

        // Chave liga/desliga de teste (Teclado I)
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            inimigosAtivos = !inimigosAtivos;
        }

        // Lógica de física e câmera seguidora
        personaje.atualizar(mapa, delta);
        atualizarCameraSeguirPlayer();

        handleResetButton();

        pedraQueCai.update(delta);
        pedraEmpurravel.update(delta);

        for (Rubi rubi : listaRubis) {
            rubi.update(delta);
        }
        bau.update(delta);

        if (inimigosAtivos) {
            if (snake != null) snake.update(delta);
            if (snakeT != null) snakeT.update(delta);
            if (snakeQuinto != null) snakeQuinto.update(delta);
        }

        // Verificação de Game Over com proteção contra null (caso não use todas as cobras no Tiled)
        if (inimigosAtivos) {
            if ((snake != null && snake.encostouNoPlayer(personaje)) ||
                (snakeT != null && snakeT.encostouNoPlayer(personaje)) ||
                (snakeQuinto != null && snakeQuinto.encostouNoPlayer(personaje))) {

                game.setScreen(new GameOverScreen(game));
                return;
            }
        }

        // 1. RENDERIZA O MAPA DO TILED PRIMEIRO (Chão e Paredes automáticos)
        mapa.render(camera);

        // 2. RENDERIZA OS SPRITES POR CIMA DO MAPA
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        personaje.desenhar(batch, ALTURA_VIRTUAL);
        if (snake != null) snake.render(batch);
        if (snakeT != null) snakeT.render(batch);
        if (snakeQuinto != null) snakeQuinto.render(batch);
        batch.end();

        drawHUD();

        // 3. Renderiza os itens e pedras secundárias
        pedraQueCai.render(camera);
        pedraEmpurravel.render(camera);
        for (Rubi rubi : listaRubis) {
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
        resetButton.dispose();
        mapa.dispose();
        personaje.dispose();
        pedraQueCai.dispose();
        pedraEmpurravel.dispose();

        for (Rubi rubi : listaRubis) {
            rubi.dispose();
        }
        bau.dispose();

        if (snake != null) snake.dispose();
        if (snakeT != null) snakeT.dispose();
        if (snakeQuinto != null) snakeQuinto.dispose();
    }

    public int getTotalRubisColetados() {

        int total = 0;

        for (Rubi rubi : listaRubis) {
            if (rubi.isFoiColetado()) {
                total++;
            }
        }

        return total;
    }
}
