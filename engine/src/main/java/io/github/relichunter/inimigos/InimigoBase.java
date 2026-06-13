package io.github.relichunter.inimigos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.MapaTeste;
import io.github.relichunter.screens.PersonagemTeste;

public class InimigoBase {
    protected int tipoInimigo;
    protected int tipoMovimento;

    protected float x, y, speed = 60f, direcaoX, direcaoY;
    protected Texture spriteSheet;
    protected TextureRegion[] frames;
    protected TextureRegion frameAtual;
    protected float tempoAnimacao = 0f;
    protected final float VELOCIDADE_ANIMACAO = 0.12f;
    protected MapaTeste mapa;
    protected Rectangle caixa = new Rectangle();

    protected float larguraDesenho = 32f;
    protected float alturaDesenho = 32f;



    public InimigoBase(int tipo, int forca, int vida, float x, float y, float limiteW, float limiteH, MapaTeste mapa) {
        this(tipo, forca, vida, x, y, limiteW, limiteH, mapa, (tipo == 2 || tipo == 3 || tipo == 5) ? 2 : 1);
    }

    public InimigoBase(int tipo, int forca, int vida, float x, float y, float limiteW, float limiteH, MapaTeste mapa, int tipoMovimento) {
        this.tipoInimigo = tipo;
        this.tipoMovimento = tipoMovimento;
        this.x = x;
        this.y = y;
        this.mapa = mapa;

        String caminhoTextura;
        int totalFrames = 4;

        switch (tipoInimigo) {
            case 2:
                caminhoTextura = "assets/inimigos/bat_spritesheet.png";
                totalFrames = 4;
                this.larguraDesenho = 32f;
                this.alturaDesenho = 32f;
                break;
            case 3:
                caminhoTextura = "assets/inimigos/spider_spritesheet.png";
                totalFrames = 6;
                this.larguraDesenho = 32f;
                this.alturaDesenho = 32f;
                break;
            case 4:
                caminhoTextura = "assets/inimigos/fire_spritesheet.png";
                totalFrames = 6;
                this.larguraDesenho = 32f;
                this.alturaDesenho = 64f;
                break;
            case 6:
                caminhoTextura = "assets/inimigos/fire_spritesheet.png";
                totalFrames = 6;
                this.larguraDesenho = 32f;
                this.alturaDesenho = 96f;
                break;

            default:
                caminhoTextura = "assets/inimigos/snake_spritesheet.png";
                totalFrames = 4;
                this.larguraDesenho = 32f;
                this.alturaDesenho = 32f;
                break;
        }

        this.spriteSheet = new Texture(caminhoTextura);
        this.frames = new TextureRegion[totalFrames];

        for (int i = 0; i < totalFrames; i++) {
            int frameAlturaOrigem = (tipoInimigo == 4 || tipoInimigo == 6) ? 48 : 32;
            frames[i] = new TextureRegion(spriteSheet, i * 32, 0, 32, frameAlturaOrigem);
        }

        this.frameAtual = frames[0];

        configurarMovimento();
    }

    private void configurarMovimento() {
        switch (tipoMovimento) {
            case 0: // PARADO
                this.direcaoX = 0;
                this.direcaoY = 0;
                break;
            case 2: // VERTICAL
                this.direcaoX = 0;
                this.direcaoY = 1;
                break;
            case 1: // HORIZONTAL
            default:
                this.direcaoX = 1;
                this.direcaoY = 0;
                break;
        }
    }

    public void update(float delta) {
        tempoAnimacao += delta;
        frameAtual = frames[(int)(tempoAnimacao / VELOCIDADE_ANIMACAO) % frames.length];

        if (tipoMovimento != 0) {
            float proxX = x + (speed * delta * direcaoX);
            float proxY = y + (speed * delta * direcaoY);

            if (mapa.isParede(proxX + (larguraDesenho / 2f), proxY + (alturaDesenho / 2f))) {
                direcaoX *= -1;
                direcaoY *= -1;
            } else {
                x = proxX;
                y = proxY;
            }
        }

        float margemX = 4f;
        float margemY = 4f;
        float larguraFisica = larguraDesenho - (margemX * 2);

        float alturaFisica;
        if (tipoInimigo == 4 || tipoInimigo == 6) {
            alturaFisica = alturaDesenho - 8f;
        } else {
            alturaFisica = alturaDesenho - (margemY * 2);
        }

        caixa.set(x + margemX, y + margemY, larguraFisica, alturaFisica);
    }

    public void render(SpriteBatch batch) {
        batch.draw(frameAtual, x, y, larguraDesenho, alturaDesenho);
    }

    public boolean encostouNoPlayer(PersonagemTeste player) {
        return caixa.overlaps(player.getCaixaPersonagem());
    }

    public void dispose() {
        spriteSheet.dispose();
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
