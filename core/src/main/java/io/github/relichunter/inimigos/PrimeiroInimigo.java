package io.github.relichunter.inimigos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.MapaTeste;
import io.github.relichunter.screens.PersonagemTeste;

public class PrimeiroInimigo {
    private int forca;
    private int vida;
    private float x, y;
    private float speed = 60f;

    private Texture spriteSheet;
    private TextureRegion[] frames;
    private TextureRegion frameAtual;

    private float tempoAnimacao = 0f;
    private final float VELOCIDADE_ANIMACAO = 0.15f;

    private float direcaoX;
    private float direcaoY;
    private int contadorDirecao = 0; // 0 para Direita, 1 para Esquerda

    private float limiteW;
    private float limiteH;
    private MapaTeste mapa;

    private final Rectangle cajaInimigo = new Rectangle();
    private final Rectangle caixaBloco   = new Rectangle();

    public PrimeiroInimigo(int forca, int vida, float x, float y, float limiteW, float limiteH, MapaTeste mapa) {
        this.forca   = forca;
        this.vida    = vida;
        this.x       = x;
        this.y       = y;
        this.limiteW = limiteW;
        this.limiteH = limiteH;
        this.mapa    = mapa;

        this.spriteSheet = new Texture("snake_spritesheet.png");

        frames = new TextureRegion[7];
        for (int i = 0; i < 7; i++) {
            frames[i] = new TextureRegion(spriteSheet, i * 32, 0, 32, 32);
        }

        this.frameAtual = frames[0];

        // Começa movendo para a DIREITA
        direcaoX = 1;
        direcaoY = 0;
        contadorDirecao = 0;
    }

    // Alterna estritamente entre Esquerda e Direita ao bater na parede
    private void novaDirecao() {
        if (contadorDirecao == 0) {
            direcaoX = -1; // Muda para Esquerda
            direcaoY = 0;
            contadorDirecao = 1;
        } else {
            direcaoX = 1;  // Muda para Direita
            direcaoY = 0;
            contadorDirecao = 0;
        }
    }

    private boolean colideComMapa(float px, float py) {
        cajaInimigo.set(px + 4f, py + 4f, 24f, 24f);
        caixaBloco.setSize(MapaTeste.TAMANHO_BLOCO, MapaTeste.TAMANHO_BLOCO);

        for (int javaLinha = 0; javaLinha < mapa.getQuantidadeLinhas(); javaLinha++) {
            for (int coluna = 0; coluna < 15; coluna++) {
                if (!mapa.isEspacoLivre(coluna, javaLinha)) {
                    int bx = coluna * MapaTeste.TAMANHO_BLOCO;
                    int by = (int)(limiteH) - ((javaLinha + 1) * MapaTeste.TAMANHO_BLOCO);
                    caixaBloco.setPosition(bx, by);
                    if (cajaInimigo.overlaps(caixaBloco)) return true;
                }
            }
        }
        return false;
    }

    public void update(float delta) {
        // Movimento focado no eixo X
        float novoX = x + direcaoX * speed * delta;

        if (colideComMapa(novoX, y)) {
            // Recua um pixel para descolar do bloco colidido e muda de direção
            x = x - (direcaoX * 1f);
            novaDirecao();
        } else {
            x = novoX;
        }

        // Limites horizontais da tela por segurança
        if (x < 0)            { x = 0;            novaDirecao(); }
        if (x + 32 > limiteW) { x = limiteW - 32; novaDirecao(); }

        // Animação dos frames
        tempoAnimacao += delta;
        int frameId = (int)(tempoAnimacao / VELOCIDADE_ANIMACAO) % 7;
        frameAtual = frames[frameId];

        // FLIP AUTOMÁTICO: Inverte o sprite para olhar para onde está andando
        if (direcaoX < 0) {
            if (!frameAtual.isFlipX()) {
                frameAtual.flip(true, false);
            }
        } else if (direcaoX > 0) {
            if (frameAtual.isFlipX()) {
                frameAtual.flip(true, false);
            }
        }
    }

    public boolean encostouNoPlayer(PersonagemTeste player) {
        cajaInimigo.set(this.x + 4f, this.y + 4f, 24f, 24f);
        return cajaInimigo.overlaps(player.getCaixaPersonagem());
    }

    public void render(SpriteBatch batch) {
        batch.draw(frameAtual, x, y, 32, 32);
    }

    public void dispose() {
        spriteSheet.dispose();
    }

    public int getForca() { return forca; }
    public int getVida()  { return vida;  }
    public float getX()   { return x;    }
    public float getY()   { return y;    }
}
