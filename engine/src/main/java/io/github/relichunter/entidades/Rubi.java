package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.PersonagemTeste;

public class Rubi extends Item {
    private boolean foiColetado;
    private SpriteBatch spriteBatch;
    private Texture spriteSheet;
    private TextureRegion cristalRoxo;
    private Rectangle caixaRubi = new Rectangle();
    private PersonagemTeste personagem;

    public Rubi(float x, float y, float largura, float altura, PersonagemTeste personagem){
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.foiColetado = false;
        this.personagem = personagem;
        this.spriteBatch = new SpriteBatch();
        this.spriteSheet = new Texture("assets/mapa/Crystals.png");
        this.cristalRoxo = new TextureRegion(spriteSheet, 32, 0, 32, 32);
    }

    @Override
    public void update(float delta) {
        if (foiColetado) return;

        caixaRubi.set(x + 6, y + 6, 16, 16);
        if (caixaRubi.overlaps(personagem.getCaixaPersonagem())){
            System.out.println("Rubi coletado! posX=" + personagem.getPosX() + " posY=" + personagem.getPosY());
            foiColetado = true;
        }
    }

    @Override
    public void render(OrthographicCamera camera) {
        if (!foiColetado) {
            spriteBatch.setProjectionMatrix(camera.combined);

            spriteBatch.begin();
            spriteBatch.draw(cristalRoxo, x, y, largura, altura);
            spriteBatch.end();
        }
    }

    public boolean isFoiColetado(){
        return foiColetado;
    }

    public void dispose(){
        spriteBatch.dispose();
        spriteSheet.dispose();
    }
}
