package io.github.relichunter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import io.github.relichunter.inimigos.InimigoBase;

public class TelaTeste implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private Texture resetButton;
    private MapaTeste mapa;
    private PersonagemTeste personaje;
    private Music musicaGame;

    private BitmapFont font;

    private com.badlogic.gdx.utils.Array<PedraQueCai> listaPedrasQueCai;
    private com.badlogic.gdx.utils.Array<Rubi> listaRubis;
    private com.badlogic.gdx.utils.Array<PedraEmpurravel> listaPedrasEmpurraveis;

    private boolean jaLiberouNovosRubis = false;
    private Bau bau;

    private com.badlogic.gdx.utils.Array<InimigoBase> listaInimigos;

    private boolean inimigosAtivos = true;

    private OrthographicCamera camera;
    private Viewport viewport;

    private final int LARGURA_VIRTUAL = 1200;
    private final int ALTURA_VIRTUAL = 900;

    private static final float RESET_BUTTON_SIZE = 80f;
    private static final float RESET_BUTTON_X = 8f;
    private static final float RESET_BUTTON_Y = 268f;

    public TelaTeste(Main game) {
        this.game = game;
    }

    @Override
    public void show() {

        batch = new SpriteBatch();
        resetButton = new Texture("assets/tela/resetGame.png");
        mapa = new MapaTeste();
        personaje = new PersonagemTeste();

        musicaGame = Gdx.audio.newMusic(Gdx.files.internal("assets/musics/musicaTelaDoJogoCortado.mp3"));
        musicaGame.setLooping(true);
        musicaGame.setVolume(0.5f);
        musicaGame.play();

        font = new BitmapFont();
        font.setColor(1, 1, 1, 1);

        camera = new OrthographicCamera();
        viewport = new FitViewport(LARGURA_VIRTUAL, ALTURA_VIRTUAL, camera);

        listaRubis = new com.badlogic.gdx.utils.Array<Rubi>();
        listaInimigos = new com.badlogic.gdx.utils.Array<InimigoBase>();
        listaPedrasEmpurraveis = new com.badlogic.gdx.utils.Array<PedraEmpurravel>();
        listaPedrasQueCai = new com.badlogic.gdx.utils.Array<PedraQueCai>();

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
                } else if (nome.equalsIgnoreCase("snake_horizontal")) {
                    // Tipo 1: Configurado com movimento 1 (Horizontal)
                    listaInimigos.add(new InimigoBase(1, 10, 100, objX, objY, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 1));
                } else if (nome.equalsIgnoreCase("snake_vertical_meio")) {
                    // Tipo 3: Configurado agora com movimento 0 (Parado)
                    listaInimigos.add(new InimigoBase(3, 10, 100, objX, objY, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
                } else if (nome.equalsIgnoreCase("snake_vertical_direita")) {
                    // Tipo 5: Mantido com movimento 2 (Vertical)
                    listaInimigos.add(new InimigoBase(5, 10, 100, objX, objY, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 2));
                } else if (nome.equalsIgnoreCase("snake_parada")) {
                    // Tipo 4: Configurado com movimento 0 (Parado)
                    listaInimigos.add(new InimigoBase(4, 10, 100, objX, objY, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
                }
            }
        }

        if (!objetosCarregadosDoTiled) {
            System.out.println("⚠️ Nenhum objeto carregado do Tiled! Usando posições padrão...");

            personaje.setX(30);
            personaje.setY(30);

            // Inimigos Horizontais (Tipo 1 -> movimento 1)
            listaInimigos.add(new InimigoBase(1, 10, 100, 248f, 30f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 1));
            listaInimigos.add(new InimigoBase(1, 10, 100, 512f, 329f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 1));
            listaInimigos.add(new InimigoBase(1, 10, 100, 642f, 339f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 1));
            listaInimigos.add(new InimigoBase(1, 10, 100, 1073f, 284f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 1));
            listaInimigos.add(new InimigoBase(1, 10, 100, 1506f, 927f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 1));
            listaInimigos.add(new InimigoBase(1, 10, 100, 409f, 700f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 1));
            listaInimigos.add(new InimigoBase(1, 10, 100, 576f, 990f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 1));
            listaInimigos.add(new InimigoBase(1, 10, 100, 992f, 585f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 1));
            listaInimigos.add(new InimigoBase(1, 10, 100, 1395f, 670f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 1));
            listaInimigos.add(new InimigoBase(1, 10, 100, 397f, 509f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 1));
            listaInimigos.add(new InimigoBase(1, 10, 100, 373f, 1218f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 1));

            // Inimigos Verticais (Tipo 2 -> movimento 2)
            listaInimigos.add(new InimigoBase(2, 10, 100, 206f, 547f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 2));
            listaInimigos.add(new InimigoBase(2, 10, 100, 864f, 547f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 2));
            listaInimigos.add(new InimigoBase(2, 10, 100, 1027f, 963f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 2));
            listaInimigos.add(new InimigoBase(2, 10, 100, 285f, 796f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 2));
            listaInimigos.add(new InimigoBase(2, 10, 100, 253f, 1086f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 2));
            listaInimigos.add(new InimigoBase(2, 10, 100, 515f, 1152f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 2));
            listaInimigos.add(new InimigoBase(2, 10, 100, 1276f, 867f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 2));
            listaInimigos.add(new InimigoBase(2, 10, 100, 1526f, 813f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 2));
            listaInimigos.add(new InimigoBase(2, 10, 100, 1827f, 766f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 2));
            listaInimigos.add(new InimigoBase(2, 10, 100, 1429f, 331f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 2));
            listaInimigos.add(new InimigoBase(2, 10, 100, 1809f, 169f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 2));

            // Inimigos Parados (Tipo 3 -> alterado para movimento 0)
            listaInimigos.add(new InimigoBase(3, 10, 100, 609f, 863f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(3, 10, 100, 189f, 934f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(3, 10, 100, 531f, 1023f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(3, 10, 100, 898f, 1023f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(3, 10, 100, 1150f, 321f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(3, 10, 100, 1347f, 384f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(3, 10, 100, 1859f, 963f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(3, 10, 100, 1091f, 1035f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));

            // Inimigos Parados (Tipo 4 -> mantido com movimento 0)
            listaInimigos.add(new InimigoBase(4, 10, 100, 1104f, 28f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(4, 10, 100, 1263f, 29f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(4, 10, 100, 1441f, 28f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(4, 10, 100, 1633f, 28f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(4, 10, 100, 387f, 126f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(4, 10, 100, 352f, 348f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(4, 10, 100, 200f, 348f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(4, 10, 100, 1128f, 414f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(4, 10, 100, 1216f, 542f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(4, 10, 100, 1666f, 765f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));
            listaInimigos.add(new InimigoBase(4, 10, 100, 1123f, 1154f, LARGURA_VIRTUAL, ALTURA_VIRTUAL, mapa, 0));

            listaRubis.add(new Rubi(371.9883f, 508.9478f, 28.0F, 28.0F, personaje));
            listaRubis.add(new Rubi(1571.8608f, 350.97977f, 28.0F, 28.0F, personaje));
            listaRubis.add(new Rubi(1438.9127f, 483.84586f, 28.0F, 28.0F, personaje));
            listaRubis.add(new Rubi(1859.375f, 284.1942f, 28.0F, 28.0F, personaje));
            listaRubis.add(new Rubi(1859.9249f, 1083.5735f, 28.0F, 28.0F, personaje));
            listaRubis.add(new Rubi(1155.463f, 1038.0504f, 28.0F, 28.0F, personaje));
            listaRubis.add(new Rubi(1088.3295f, 1109.3951f, 28.0F, 28.0F, personaje));
            listaRubis.add(new Rubi(284.9394f, 867.3809f, 28.0F, 28.0F, personaje));
            listaRubis.add(new Rubi(35.106544f, 1217.3754f, 28.0F, 28.0F, personaje));
            listaRubis.add(new Rubi(514.6874f, 1127.4712f, 28.0F, 28.0F, personaje));
            listaRubis.add(new Rubi(252.12126f, 1117.4712f, 28.0F, 28.0F, personaje));
            listaRubis.add(new Rubi(50f, 50f, 28.0F, 28.0F, personaje));
        }

        Rubi[] arrayParaOBau = listaRubis.toArray(Rubi.class);
        bau = new Bau(1800.0F, 200.0F, 80.0F, 50.0F, personaje, arrayParaOBau);

        listaPedrasQueCai.add(new PedraQueCai(994.35f, 478.58f, 32.0F, 32.0F, personaje, mapa, ALTURA_VIRTUAL, 287.52f));
        listaPedrasQueCai.add(new PedraQueCai(1218.65f, 887.79f, 32.0F, 32.0F, personaje, mapa, ALTURA_VIRTUAL, 668.12f));

        listaPedrasEmpurraveis.add(new PedraEmpurravel(1186f, 220f, 32.0F, 32.0F, mapa, personaje, ALTURA_VIRTUAL));
        listaPedrasEmpurraveis.add(new PedraEmpurravel(1349f, 221f, 32.0F, 32.0F, mapa, personaje, ALTURA_VIRTUAL));
        listaPedrasEmpurraveis.add(new PedraEmpurravel(1544f, 221f, 32.0F, 32.0F, mapa, personaje, ALTURA_VIRTUAL));
        listaPedrasEmpurraveis.add(new PedraEmpurravel(1736f, 221f, 32.0F, 32.0F, mapa, personaje, ALTURA_VIRTUAL));
    }

    private void drawInfo() {
        int rubisColetados = getTotalRubisColetados();
        int rubisTotal = listaRubis.size;

        batch.setProjectionMatrix(new com.badlogic.gdx.math.Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        batch.begin();
        font.draw(batch, "Rubis: " + rubisColetados + " / " + rubisTotal, 50, Gdx.graphics.getHeight() - 50);
        batch.end();
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
            if (musicaGame != null) {
                musicaGame.stop();
            }
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
        if (camera.position.x > limiteDireitoMapa - metadeLarguraCam)
            camera.position.x = limiteDireitoMapa - metadeLarguraCam;

        if (camera.position.y < metadeAlturaCam) camera.position.y = metadeAlturaCam;
        if (camera.position.y > limiteSuperiorMapa - metadeAlturaCam)
            camera.position.y = limiteSuperiorMapa - metadeAlturaCam;

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

        for (PedraQueCai pedra : listaPedrasQueCai) {
            pedra.update(delta);
            if (pedra.isColidiuComPlayer()) {
                musicaGame.stop();
                game.setScreen(new GameOverScreen(game));
                return;
            }
        }

        for (PedraEmpurravel pedra : listaPedrasEmpurraveis) {
            pedra.update(delta);
        }

        for (Rubi rubi : listaRubis) {
            rubi.update(delta);
        }
        bau.update(delta);

        if (inimigosAtivos) {
            for (InimigoBase inimigo : listaInimigos) {
                inimigo.update(delta);

                if (inimigo.encostouNoPlayer(personaje)) {
                    musicaGame.stop();
                    game.setScreen(new GameOverScreen(game));
                    return;
                }
            }
        }

        mapa.render(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        personaje.desenhar(batch, ALTURA_VIRTUAL);

        for (InimigoBase inimigo : listaInimigos) {
            inimigo.render(batch);
        }

        batch.end();

        drawInfo();
        drawHUD();

        for (PedraQueCai pedra : listaPedrasQueCai) {
            pedra.render(camera);
        }

        for (PedraEmpurravel pedra : listaPedrasEmpurraveis) {
            pedra.render(camera);
        }

        for (Rubi rubi : listaRubis) {
            rubi.render(camera);
        }
        bau.render(camera);

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            System.out.println("Posição do Personagem -> X: " + personaje.getPosX() + " | Y: " + personaje.getPosY());
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        resetButton.dispose();
        mapa.dispose();
        personaje.dispose();

        for (PedraQueCai pedra : listaPedrasQueCai) {
            pedra.dispose();
        }

        for (PedraEmpurravel pedra : listaPedrasEmpurraveis) {
            pedra.dispose();
        }

        musicaGame.dispose();
        font.dispose();

        for (Rubi rubi : listaRubis) {
            rubi.dispose();
        }
        bau.dispose();

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
