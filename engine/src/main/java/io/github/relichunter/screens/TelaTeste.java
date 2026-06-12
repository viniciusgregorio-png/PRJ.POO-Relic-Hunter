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

    private com.badlogic.gdx.utils.Array<InimigoBase> listaInimigos;

    private boolean inimigosAtivos = true;

    private OrthographicCamera camera;
    private Viewport viewport;

    private final int LARGURA_VIRTUAL = 1200;
    private final int ALTURA_VIRTUAL = 900;

    private static final float RESET_BUTTON_SIZE = 48f;
    private static final float RESET_BUTTON_X = 8f;
    private static final float RESET_BUTTON_Y = 268f;

    public TelaTeste(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        resetButton = new Texture("Png-Telas/resetGame.png");
        mapa = new MapaTeste();
        personaje = new PersonagemTeste();

        camera = new OrthographicCamera();
        viewport = new FitViewport(LARGURA_VIRTUAL, ALTURA_VIRTUAL, camera);

        listaRubis = new com.badlogic.gdx.utils.Array<Rubi>();

        listaInimigos = new com.badlogic.gdx.utils.Array<InimigoBase>();

        MapLayer camadaObjetos = mapa.getTiledMap().getLayers().get("objetos");

        boolean objetosCarregadosDoTiled = false;

        if (camadaObjetos != null) {
            for (MapObject objeto : camadaObjetos.getObjects()) {
                float objX = objeto.getProperties().get("x", Float.class);
                float objY = objeto.getProperties().get("y", Float.class);
                String nome = objeto.getName();

                if (nome == null) continue;

                if (nome.equalsIgnoreCase("player")) {
                    personaje.setX(objX);
                    personaje.setY(objY);
                    objetosCarregadosDoTiled = true;
                }
                else if (nome.equalsIgnoreCase("snake_horizontal")) {
                    listaInimigos.add(new InimigoBase(1, 10, 100, objX, objY, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
                }
                else if (nome.equalsIgnoreCase("snake_vertical_meio")) {
                    listaInimigos.add(new InimigoBase(3, 10, 100, objX, objY, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
                }
                else if (nome.equalsIgnoreCase("snake_vertical_direita")) {
                    listaInimigos.add(new InimigoBase(5, 10, 100, objX, objY, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
                }
            }
        }

        // SE NÃO CARREGOU DO TILED, CRIAR MANUALMENTE
        if (!objetosCarregadosDoTiled) {
            System.out.println("⚠️ Nenhum objeto carregado do Tiled! Usando posições padrão...");

            // Posiçao padrão do jogador
            personaje.setX(30);
            personaje.setY(30);

// Lista de 42 inimigos (sem uso de ciclos), organizados por tipo

// Tipo 1: 11 unidades cobra fechada
            listaInimigos.add(new InimigoBase(1, 10, 100, 248f, 30f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(1, 10, 100, 512f, 329f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(1, 10, 100, 642f, 339f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(1, 10, 100, 1073f, 284f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(1, 10, 100, 1506f, 927f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(1, 10, 100, 409f, 700f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(1, 10, 100, 576f, 990f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(1, 10, 100, 992f, 585f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(1, 10, 100, 1395f, 670f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(1, 10, 100, 397f, 509f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(1, 10, 100, 373f, 1218f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));

//* Tipo 2: 11 unidades morcego
           listaInimigos.add(new InimigoBase(2, 10, 100, 206f, 547f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
           listaInimigos.add(new InimigoBase(2, 10, 100, 864f, 547f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
           listaInimigos.add(new InimigoBase(2, 10, 100, 1027f, 963f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
           listaInimigos.add(new InimigoBase(2, 10, 100, 285f, 796f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
           listaInimigos.add(new InimigoBase(2, 10, 100, 253f, 1086f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
           listaInimigos.add(new InimigoBase(2, 10, 100, 515f, 1152f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
           listaInimigos.add(new InimigoBase(2, 10, 100, 1276f, 867f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
           listaInimigos.add(new InimigoBase(2, 10, 100, 1526f, 813f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
           listaInimigos.add(new InimigoBase(2, 10, 100, 1827f, 766f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
           listaInimigos.add(new InimigoBase(2, 10, 100, 1429f, 331f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
           listaInimigos.add(new InimigoBase(2, 10, 100, 1809f, 169f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));

//* Tipo 3: 8 unidades aranha
            listaInimigos.add(new InimigoBase(3, 10, 100, 609f, 863f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(3, 10, 100, 189f, 934f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(3, 10, 100, 531f, 1023f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(3, 10, 100, 898f, 1023f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(3, 10, 100, 1150f, 321f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(3, 10, 100, 1347f, 384f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(3, 10, 100, 1859f, 963f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(3, 10, 100, 1091f, 1035f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));


//*Tipo 4: 11 unidades fogo
            listaInimigos.add(new InimigoBase(4, 10, 100, 1104f, 28f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(4, 10, 100, 1263f, 29f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(4, 10, 100, 1441f, 28f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(4, 10, 100, 1633f, 28f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(4, 10, 100, 387f, 126f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(4, 10, 100, 352f, 348f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(4, 10, 100, 200f, 348f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(4, 10, 100, 1128f, 414f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(4, 10, 100, 1216f, 542f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(4, 10, 100, 1666f, 765f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));
            listaInimigos.add(new InimigoBase(4, 10, 100, 1123f, 1154f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa));

            // Criar alguns rubis em posiçoes padrao
            listaRubis.add(new Rubi(50f, 50f, 28.0F, 28.0F, personaje));
            listaRubis.add(new Rubi(100f, 100f, 28.0F, 28.0F, personaje));
            listaRubis.add(new Rubi(150f, 150f, 28.0F, 28.0F, personaje));
        }

        Rubi[] arrayParaOBau = listaRubis.toArray(Rubi.class);
        bau = new Bau(352.0F, 64.0F, 28.0F, 28.0F, personaje, arrayParaOBau);

        pedraQueCai = new PedraQueCai(1.0F, 320.0F, 32.0F, 32.0F, personaje, mapa, ALTURA_VIRTUAL);
        pedraEmpurravel = new PedraEmpurravel(129.0F, 129.0F, 32.0F, 32.0F, mapa, personaje, ALTURA_VIRTUAL);
    }

    private void handleResetButton() {
        if (!Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            return;
        }

        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(touchPos);

        float hudX = camera.position.x - viewport.getWorldWidth() / 2f + RESET_BUTTON_X;
        float hudY = camera.position.y - viewport.getWorldHeight() / 2f + RESET_BUTTON_Y;

        if (touchPos.x >= hudX && touchPos.x <= hudX + RESET_BUTTON_SIZE &&
            touchPos.y >= hudY && touchPos.y <= hudY + RESET_BUTTON_SIZE) {
            game.setScreen(new TelaTeste(game));
        }
    }

    private void drawHUD() {
        float hudX = camera.position.x - viewport.getWorldWidth() / 2f + RESET_BUTTON_X;
        float hudY = camera.position.y - viewport.getWorldHeight() / 2f + RESET_BUTTON_Y;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(resetButton, hudX, hudY, RESET_BUTTON_SIZE, RESET_BUTTON_SIZE);
        batch.end();
    }

    private void atualizarCameraSeguirPlayer() {
        camera.position.x = personaje.getPosX() + 16f;
        camera.position.y = personaje.getPosY() + 16f;

        float metadeLarguraCam = viewport.getWorldWidth() / 2f;
        float metadeAlturaCam = viewport.getWorldHeight() / 2f;

        float limiteDireitoMapa = 60 * 32;
        float limiteSuperiorMapa = 40 * 32;

        if (camera.position.x < metadeLarguraCam) camera.position.x = metadeLarguraCam;
        if (camera.position.x > limiteDireitoMapa - metadeLarguraCam) camera.position.x = limiteDireitoMapa - metadeLarguraCam;

        if (camera.position.y < metadeAlturaCam) camera.position.y = metadeAlturaCam;
        if (camera.position.y > limiteSuperiorMapa - metadeAlturaCam) camera.position.y = limiteSuperiorMapa - metadeAlturaCam;

        camera.update();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15F, 0.15F, 0.15F, 1.0F);

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            inimigosAtivos = !inimigosAtivos;
        }

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
            for (InimigoBase inimigo : listaInimigos) {
                inimigo.update(delta);

                // Se o inimigo atual encostar no player, Game Over
                if (inimigo.encostouNoPlayer(personaje)) {
                    game.setScreen(new GameOverScreen(game));
                    return;
                }
            }
        }

        // RENDERIZAR O MAPA COM A CÂMERA
        mapa.render(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        personaje.desenhar(batch, ALTURA_VIRTUAL);

        //  DESENHAR TODOS OS INIMIGOS DA LISTA
        for (InimigoBase inimigo : listaInimigos) {
            inimigo.render(batch);
        }

        batch.end();

        drawHUD();

        pedraQueCai.render(camera);
        pedraEmpurravel.render(camera);
        for (Rubi rubi : listaRubis) {
            rubi.render(camera);
        }
        bau.render(camera);

        // Descobrir a posição exata ao apertar a tecla P
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            System.out.println("📍 Posição do Personagem -> X: " + personaje.getPosX() + " | Y: " + personaje.getPosY());
        }
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

        // ✅ LIMPAR A MEMÓRIA DE TODOS OS INIMIGOS NA LISTA
        for (InimigoBase inimigo : listaInimigos) {
            inimigo.dispose();
        }
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
