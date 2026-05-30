package io.github.relichunter.inimigos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.MapaTeste;

public class InimigosSnake {
    private int forca;
    private int vida;
    private float x, y;
    private float speed = 60f;
    private Texture texture;

    private float direcaoX;
    private float direcaoY;
    private float tempoMudanca = 2f;
    private float temporizador = 0f;
    private int contadorDirecao = 0;

    private float limiteW;
    private float limiteH;
    private MapaTeste mapa;

    private final Rectangle caixaInimigo = new Rectangle();
    private final Rectangle caixaBloco   = new Rectangle();

    public InimigosSnake(int forca, int vida, float x, float y, float limiteW, float limiteH, MapaTeste mapa) {
        this.forca   = forca;
        this.vida    = vida;
        this.x       = x;
        this.y       = y;
        this.limiteW = limiteW;
        this.limiteH = limiteH;
        this.mapa    = mapa;

        Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 1f, 1f);
        pixmap.fill();
        this.texture = new Texture(pixmap);
        pixmap.dispose();

        direcaoX = 1;
        direcaoY = 0;
    }

    private void novaDirecao() {
        contadorDirecao++;
        if (contadorDirecao > 3) contadorDirecao = 0;

        if (contadorDirecao == 0) { direcaoX =  1; direcaoY =  0; }
        if (contadorDirecao == 1) { direcaoX = -1; direcaoY =  0; }
        if (contadorDirecao == 2) { direcaoX =  0; direcaoY =  1; }
        if (contadorDirecao == 3) { direcaoX =  0; direcaoY = -1; }
    }

    private boolean colideComMapa(float px, float py) {
        caixaInimigo.set(px, py, 32, 32);
        caixaBloco.setSize(MapaTeste.TAMANHO_BLOCO, MapaTeste.TAMANHO_BLOCO);

        for (int linha = 0; linha < mapa.getQuantidadeLinhas(); linha++) {
            for (int coluna = 0; coluna < 15; coluna++) {
                if (!mapa.isEspacoLivre(coluna, linha)) {
                    int bx = coluna * MapaTeste.TAMANHO_BLOCO;
                    int by = (int)(limiteH) - ((linha + 1) * MapaTeste.TAMANHO_BLOCO);
                    caixaBloco.setPosition(bx, by);
                    if (caixaInimigo.overlaps(caixaBloco)) return true;
                }
            }
        }
        return false;
    }

    public void update(float delta) {
        float novoX = x + direcaoX * speed * delta;
        float novoY = y + direcaoY * speed * delta;

        // Colisão com paredes do mapa
        if (colideComMapa(novoX, y)) {
            novoX = x;
            novaDirecao();
        }
        if (colideComMapa(novoX, novoY)) {
            novoY = y;
            novaDirecao();
        }

        x = novoX;
        y = novoY;

        // Limites da tela virtual
        if (x < 0)            { x = 0;            novaDirecao(); }
        if (x + 32 > limiteW) { x = limiteW - 32; novaDirecao(); }
        if (y < 0)            { y = 0;            novaDirecao(); }
        if (y + 32 > limiteH) { y = limiteH - 32; novaDirecao(); }

        temporizador += delta;
        if (temporizador >= tempoMudanca) {
            temporizador = 0f;
            novaDirecao();
        }
    }

    public boolean encostouNoPlayer(float playerX, float playerY) {
        float dx = playerX - x;
        float dy = playerY - y;
        if (dx < 0) dx = -dx;
        if (dy < 0) dy = -dy;
        return dx < 32f && dy < 32f;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, 32, 32);
    }

    public void dispose() { texture.dispose(); }

    public int getForca() { return forca; }
    public int getVida()  { return vida;  }
    public float getX()   { return x;    }
    public float getY()   { return y;    }
}
