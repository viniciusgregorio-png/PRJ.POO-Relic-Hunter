package io.github.relichunter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.relichunter.Main;

public class SplashScreen implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private Texture logo;
    private Music musica;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private boolean menuAberto = false;
    private boolean draggingVolume = false;

    public SplashScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();

        logo = new Texture("assets/tela/logoRelic.png");

        musica = Gdx.audio.newMusic(Gdx.files.internal("assets/musics/audioInicialTopGear.mp3"));
        musica.setLooping(true);
        musica.setVolume(1.0f);
        musica.play();
    }

    @Override
    public void render(float delta) {
        handleInput();

        ScreenUtils.clear(0, 0, 0, 1f);

        int largura = Gdx.graphics.getWidth();
        int altura = Gdx.graphics.getHeight();

        batch.getProjectionMatrix().setToOrtho2D(0, 0, largura, altura);
        batch.begin();
        batch.draw(logo, 0, 0, largura, altura);
        batch.end();

        if (menuAberto) {
            drawMenu(largura, altura);
        }
    }

    private void handleInput() {
        int largura = Gdx.graphics.getWidth();
        int altura = Gdx.graphics.getHeight();
        float mouseX = Gdx.input.getX();
        float mouseY = altura - Gdx.input.getY();

        float gearX = largura * 0.01f;
        float gearY = altura * 0.90f;
        float gearW = largura * 0.08f;
        float gearH = altura * 0.08f;

        boolean clicouAgora = Gdx.input.isButtonJustPressed(Buttons.LEFT);
        boolean segurando = Gdx.input.isButtonPressed(Buttons.LEFT);

        if (clicouAgora && mouseX >= gearX && mouseX <= gearX + gearW && mouseY >= gearY && mouseY <= gearY + gearH) {
            menuAberto = !menuAberto;
            draggingVolume = false;
            return;
        }

        if (!menuAberto) {
            float playX = largura * 0.40f;
            float playY = altura * 0.15f;
            float playW = largura * 0.20f;
            float playH = altura * 0.12f;

            if (clicouAgora && mouseX >= playX && mouseX <= playX + playW && mouseY >= playY && mouseY <= playY + playH) {
                startGame();
                return;
            }
        }

        if (menuAberto) {
            float sliderX = largura * 0.30f;
            float sliderY = altura * 0.50f;
            float sliderW = largura * 0.40f;
            float sliderH = altura * 0.05f;

            boolean mouseSobreSlider = mouseX >= sliderX && mouseX <= sliderX + sliderW &&
                mouseY >= sliderY && mouseY <= sliderY + sliderH;

            if (clicouAgora && mouseSobreSlider) {
                draggingVolume = true;
            }

            if (draggingVolume && segurando) {
                float volume = (mouseX - sliderX) / sliderW;
                volume = Math.max(0f, Math.min(1f, volume));
                musica.setVolume(volume);
            }

            if (!segurando) {
                draggingVolume = false;
            }
        } else {
            draggingVolume = false;
        }
    }

    private void drawMenu(int largura, int altura) {
        Gdx.gl.glEnable(GL20.GL_BLEND);

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.rect(largura * 0.25f, altura * 0.35f, largura * 0.5f, altura * 0.3f);

        float sliderX = largura * 0.30f;
        float sliderY = altura * 0.50f;
        float sliderW = largura * 0.40f;
        float sliderH = altura * 0.05f;

        shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f);
        shapeRenderer.rect(sliderX, sliderY, sliderW, sliderH);

        shapeRenderer.setColor(0f, 1f, 0f, 1f);
        shapeRenderer.rect(sliderX, sliderY, sliderW * musica.getVolume(), sliderH);

        shapeRenderer.end();

        batch.begin();
        float textX = sliderX + sliderW / 2f;
        float textY = sliderY + sliderH + altura * 0.03f;
        font.getData().setScale(1.5f);
        font.draw(batch, "ÁUDIO", textX - font.getRegion().getRegionWidth() / 8f, textY);
        batch.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void startGame() {
        game.setScreen(new TelaTeste(game));
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        logo.dispose();
        musica.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}
