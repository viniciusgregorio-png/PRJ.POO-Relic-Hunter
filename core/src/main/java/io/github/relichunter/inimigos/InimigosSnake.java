package io.github.relichunter.inimigos;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

    public InimigosSnake(int forca, int vida, float x, float y) {
        this.forca = forca;
        this.vida = vida;
        this.x = x;
        this.y = y;
        this.texture = new Texture("inimigo.png");
        // Direção inicial fixa — sem incrementar contadorDirecao
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

    public void update(float delta) {
        x += direcaoX * speed * delta;
        y += direcaoY * speed * delta;

        // Mantém o inimigo dentro dos limites da tela
        float largura = Gdx.graphics.getWidth();
        float altura  = Gdx.graphics.getHeight();

        if (x < 0)              { x = 0;            novaDirecao(); }
        if (x + 32 > largura)   { x = largura - 32; novaDirecao(); }
        if (y < 0)              { y = 0;            novaDirecao(); }
        if (y + 32 > altura)    { y = altura - 32;  novaDirecao(); }

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
