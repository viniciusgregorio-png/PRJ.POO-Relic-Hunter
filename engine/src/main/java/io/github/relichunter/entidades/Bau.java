package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.PersonagemTeste;

public class Bau extends Item {
    private boolean estaVisivel = false;
    private boolean foiAberto;
    private PersonagemTeste personagem;

    private SpriteBatch spriteBatch;
    private Texture spriteSheet;
    private TextureRegion bauFechado;
    private TextureRegion bauAberto;
    private TextureRegion frameAtual;

    private Rectangle caixaBau;
    private Rubi[] rubis;

    public Bau(float x, float y, float largura, float altura, PersonagemTeste personagem, Rubi[] rubis){
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.personagem = personagem;
        this.rubis = rubis;
        this.estaVisivel = false;
        this.foiAberto = false;
        this.caixaBau = new Rectangle();

        this.spriteBatch = new SpriteBatch();
        this.spriteSheet = new Texture("assets/mapa/chests_byBatuhanK.png");
        System.out.println("Texture do Bau carregada");

        this.bauFechado = new TextureRegion(spriteSheet, 0, 0, 40, 16);
        this.bauAberto  = new TextureRegion(spriteSheet, 40, 0, 40, 16);
        this.frameAtual = bauFechado;
    }

    @Override
    public void update(float delta) {
        if (foiAberto) return;

        int rubisColetados = 0;
        for (Rubi rubi : rubis) {
            if (rubi.isFoiColetado()){
                rubisColetados++;
            }
        }

        if (rubisColetados == 1){
            estaVisivel = true;
        }

        if (estaVisivel){
            caixaBau.set(x, y, largura, altura);

            if (caixaBau.overlaps(personagem.getCaixaPersonagem())){
                foiAberto = true;
                frameAtual = bauAberto;
            }
        }
    }

    @Override
    public void render(OrthographicCamera camera) {
        if (estaVisivel) {
            spriteBatch.setProjectionMatrix(camera.combined);

            spriteBatch.begin();
            spriteBatch.draw(frameAtual, x, y, largura, altura);
            spriteBatch.end();
        }
    }

    public void dispose(){
        spriteBatch.dispose();
        spriteSheet.dispose();
    }

    public boolean isFoiAberto() {
        return foiAberto;
    }

    public boolean isEstaVisivel() {
        return estaVisivel;
    }
}
