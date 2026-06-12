package io.github.relichunter.inimigos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.MapaTeste;
import io.github.relichunter.screens.PersonagemTeste;

public class InimigoBase {
    protected int tipoMovimento;
    protected float x, y, speed = 60f, direcaoX, direcaoY;
    protected Texture spriteSheet;
    protected TextureRegion[] frames;
    protected TextureRegion frameAtual;
    protected float tempoAnimacao = 0f;
    protected final float VELOCIDADE_ANIMACAO = 0.15f;
    protected MapaTeste mapa;
    protected Rectangle caixa = new Rectangle();

    public InimigoBase(int tipo, int forca, int vida, float x, float y, float limiteW, float limiteH, MapaTeste mapa) {
        this.tipoMovimento = tipo;
        this.x = x;
        this.y = y;
        this.mapa = mapa;
        this.spriteSheet = new Texture("snake_spritesheet.png");
        this.frames = new TextureRegion[7];
        for (int i = 0; i < 7; i++) frames[i] = new TextureRegion(spriteSheet, i * 32, 0, 32, 32);
        this.frameAtual = frames[0];

        if (tipo == 2 || tipo == 3 || tipo == 5) {
            this.direcaoX = 0;
            this.direcaoY = 1;
        } else {
            this.direcaoX = 1;
            this.direcaoY = 0;
        }
    }

    public void update(float delta) {
        tempoAnimacao += delta;
        frameAtual = frames[(int)(tempoAnimacao / VELOCIDADE_ANIMACAO) % 7];

        float proxX = x + (speed * delta * direcaoX);
        float proxY = y + (speed * delta * direcaoY);

        if (mapa.isParede(proxX + 16, proxY + 16)) {
            direcaoX *= -1;
            direcaoY *= -1;
        } else {
            x = proxX;
            y = proxY;
        }
        caixa.set(x + 4f, y + 4f, 24f, 24f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(frameAtual, x, y, 32, 32);
    }

    public boolean encostouNoPlayer(PersonagemTeste player) {
        return caixa.overlaps(player.getCaixaPersonagem());
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
