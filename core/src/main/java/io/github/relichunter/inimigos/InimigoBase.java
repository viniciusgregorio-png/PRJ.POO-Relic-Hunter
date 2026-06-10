package io.github.relichunter.inimigos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.MapaTeste;
import io.github.relichunter.screens.PersonagemTeste;

public abstract class InimigoBase {

    protected int forca, vida;
    protected float x, y;
    protected float speed = 60f;

    protected Texture spriteSheet;
    protected TextureRegion[] frames;
    protected TextureRegion frameAtual;

    protected float tempoAnimacao = 0f;
    protected final float VELOCIDADE_ANIMACAO = 0.15f;

    protected float direcaoX, direcaoY;
    protected int contadorDirecao = 0;

    protected float limiteW, limiteH;
    protected MapaTeste mapa;

    protected final Rectangle cajaInimigo = new Rectangle();
    protected final Rectangle caixaBloco  = new Rectangle();

    public InimigoBase(int forca, int vida, float x, float y,
                       float limiteW, float limiteH, MapaTeste mapa) {
        this.forca   = forca;
        this.vida    = vida;
        this.x       = x;
        this.y       = y;
        this.limiteW = limiteW;
        this.limiteH = limiteH;
        this.mapa    = mapa;

        spriteSheet = new Texture("snake_spritesheet.png");
        frames = new TextureRegion[7];
        for (int i = 0; i < 7; i++) {
            frames[i] = new TextureRegion(spriteSheet, i * 32, 0, 32, 32);
        }
        frameAtual = frames[0];
    }

    protected abstract void novaDirecao();
    public abstract void update(float delta);

    protected boolean colideComMapa(float px, float py) {
        cajaInimigo.set(px + 4f, py + 4f, 24f, 24f);
        caixaBloco.setSize(MapaTeste.TAMANHO_BLOCO, MapaTeste.TAMANHO_BLOCO);

        for (int linha = 0; linha < mapa.getQuantidadeLinhas(); linha++) {
            for (int coluna = 0; coluna < 15; coluna++) {
                if (!mapa.isEspacoLivre(coluna, linha)) {
                    int bx = coluna * MapaTeste.TAMANHO_BLOCO;
                    int by = (int)(limiteH) - ((linha + 1) * MapaTeste.TAMANHO_BLOCO);
                    caixaBloco.setPosition(bx, by);
                    if (cajaInimigo.overlaps(caixaBloco)) return true;
                }
            }
        }
        return false;
    }

    protected void atualizarAnimacao(float delta) {
        tempoAnimacao += delta;
        int frameId = (int)(tempoAnimacao / VELOCIDADE_ANIMACAO) % 7;
        frameAtual = frames[frameId];
    }

    public boolean encostouNoPlayer(PersonagemTeste player) {
        cajaInimigo.set(x + 4f, y + 4f, 24f, 24f);
        return cajaInimigo.overlaps(player.getCaixaPersonagem());
    }

    public void render(SpriteBatch batch) {
        batch.draw(frameAtual, x, y, 32, 32);
    }

    public void dispose() {
        spriteSheet.dispose();
    }

    public int   getForca() { return forca; }
    public int   getVida()  { return vida;  }
    public float getX()     { return x;     }
    public float getY()     { return y;     }
}
