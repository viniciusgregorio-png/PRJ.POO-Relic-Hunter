package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.PersonagemTeste;

public class Chave extends Item {
    private float x;
    private float y;
    private float largura;
    private float altura;

    private boolean foiColetado;
    private boolean estaVisivel;
    private SpriteBatch spriteBatch;
    private Texture spriteSheet;
    private TextureRegion frameChave;
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
        this.frameChave = new TextureRegion(spriteSheet, 96, 0, 32, 32);
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
