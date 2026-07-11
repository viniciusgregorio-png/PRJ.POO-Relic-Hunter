package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.PersonagemTeste;

public class Chave extends ObjetoJogo {
    private float x;
    private float y;
    private float largura;
    private float altura;

    private boolean foiColetado;
    private boolean estaVisivel;
    private SpriteBatch spriteBatch;
    private Texture spriteSheet;
    private Animation<TextureRegion> animacaoChave;
    private TextureRegion frameChave;
    private float stateTime;
    private Rectangle caixaChave = new Rectangle();
    private PersonagemTeste personagem;

    public Chave(float x, float y, float largura, float altura, PersonagemTeste personagem) {

        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;

        this.foiColetado = false;
        this.estaVisivel = false;
        this.personagem = personagem;

        this.spriteBatch = new SpriteBatch();
        this.spriteSheet = new Texture("assets/mapa/Chave.png");

        int totalFrames = 12;
        int larguraFrame = spriteSheet.getWidth() / totalFrames; // 16
        int alturaFrame = spriteSheet.getHeight();               // 35

        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, larguraFrame, alturaFrame);
        TextureRegion[] frames = tmp[0]; // única linha, 12 frames

        this.animacaoChave = new Animation<>(0.08f, frames);
        this.animacaoChave.setPlayMode(Animation.PlayMode.LOOP);

        this.stateTime = 0f;
        this.frameChave = frames[0];
    }

    public void setVisivel(boolean visivel) {
        this.estaVisivel = visivel;
    }

    public boolean isEstaVisivel() {
        return estaVisivel;
    }

    @Override
    public void update(float delta) {
        if (!estaVisivel || foiColetado) return;

        stateTime += delta;
        frameChave = animacaoChave.getKeyFrame(stateTime, true);

        caixaChave.set(this.x + 6, this.y + 6, 16, 16);
        if (caixaChave.overlaps(personagem.getCaixaPersonagem())) {
            System.out.println("Chave coletada!");
            foiColetado = true;
        }
    }

    @Override
    public void render(OrthographicCamera camera) {
        if (estaVisivel && !foiColetado) {
            spriteBatch.setProjectionMatrix(camera.combined);
            spriteBatch.begin();
            spriteBatch.draw(frameChave, this.x, this.y, this.largura, this.altura);
            spriteBatch.end();
        }
    }

    public boolean isFoiColetado() {
        return foiColetado;
    }

    public void dispose() {
        spriteBatch.dispose();
        spriteSheet.dispose();
    }
}
