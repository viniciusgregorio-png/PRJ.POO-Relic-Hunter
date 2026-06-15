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
    protected float offsetX = 0f;
    protected float offsetY = 0f;

    // Variáveis da Hitbox
    protected float hitboxWidth = 24f;
    protected float hitboxHeight = 24f;
    protected float hitboxOffsetX = 0f;
    protected float hitboxOffsetY = 4f;

    public InimigoBase(int tipo, int forca, int vida, float x, float y, float limiteW, float limiteH, MapaTeste mapa) {
        this(tipo, forca, vida, x, y, limiteW, limiteH, mapa, (tipo == 2 || tipo == 3 || tipo == 5) ? 2 : 1);
    }

    public InimigoBase(int tipo, int forca, int vida, float x, float y, float limiteW, float limiteH, MapaTeste mapa, int tipoMovimento) {
        this.tipoInimigo = tipo;
        this.tipoMovimento = tipoMovimento;
        this.x = x;
        this.y = y;
        this.mapa = mapa;

        int totalFrames = 4;
        String caminhoTextura;

        switch (tipoInimigo) {
            case 2:
                caminhoTextura = "assets/inimigos/bat_spritesheet.png";
                this.larguraDesenho = 48f; this.alturaDesenho = 48f;
                this.offsetX = -8f; this.offsetY = 4f;
                this.hitboxOffsetY = 15f;
                break;
            case 3:
                caminhoTextura = "assets/inimigos/spider_spritesheet.png";
                totalFrames = 5;

                this.larguraDesenho = 48f;
                this.alturaDesenho = 48f;

                this.offsetX = -8f;
                this.offsetY = -8f;

                this.hitboxWidth = 24f;
                this.hitboxHeight = 16f;
                this.hitboxOffsetX = 0f;
                this.hitboxOffsetY = 0f;
                break;
            case 4: case 6:
                caminhoTextura = "assets/inimigos/fire_spritesheet.png";
                this.larguraDesenho = 32f; this.alturaDesenho = (tipoInimigo == 4) ? 64f : 96f;
                this.hitboxHeight = this.alturaDesenho - 8f;
                break;
            default:
                caminhoTextura = "assets/inimigos/snake_spritesheet.png";
                this.larguraDesenho = 48f; this.alturaDesenho = 48f;
                this.offsetX = -4f; this.offsetY = 4f;
                this.hitboxOffsetY = 0f;
                this.hitboxHeight = 20f;
                break;
        }

        this.spriteSheet = new Texture(caminhoTextura);
        this.frames = new TextureRegion[totalFrames];
        int h = (tipoInimigo == 4 || tipoInimigo == 6) ? 48 : 32;
        for (int i = 0; i < totalFrames; i++) frames[i] = new TextureRegion(spriteSheet, i * 32, 0, 32, h);

        this.frameAtual = frames[0];
        configurarMovimento();
    }

    private void configurarMovimento() {
        if (tipoMovimento == 2) { direcaoX = 0; direcaoY = 1; }
        else if (tipoMovimento == 1) { direcaoX = 1; direcaoY = 0; }
    }

    public void update(float delta) {
        tempoAnimacao += delta;
        frameAtual = new TextureRegion(frames[(int)(tempoAnimacao / VELOCIDADE_ANIMACAO) % frames.length]);
        if (direcaoX < 0) frameAtual.flip(true, false);

        if (tipoMovimento != 0) {
            float proxX = x + (speed * delta * direcaoX);
            float proxY = y + (speed * delta * direcaoY);

            float margemX = ((larguraDesenho - hitboxWidth) / 2f) + hitboxOffsetX;
            float proxHitboxX = proxX + offsetX + margemX;
            float proxHitboxY = proxY + offsetY + hitboxOffsetY;

            boolean colidiu = false;
            if (direcaoX > 0) colidiu = mapa.isParede(proxHitboxX + hitboxWidth, proxHitboxY + (hitboxHeight / 2f));
            else if (direcaoX < 0) colidiu = mapa.isParede(proxHitboxX, proxHitboxY + (hitboxHeight / 2f));
            else if (direcaoY > 0) colidiu = mapa.isParede(proxHitboxX + (hitboxWidth / 2f), proxHitboxY + hitboxHeight);
            else if (direcaoY < 0) colidiu = mapa.isParede(proxHitboxX + (hitboxWidth / 2f), proxHitboxY);

            if (colidiu) { direcaoX *= -1; direcaoY *= -1; }
            else { x = proxX; y = proxY; }
        }

        caixa.set(x + offsetX + ((larguraDesenho - hitboxWidth) / 2f) + hitboxOffsetX,
            y + offsetY + hitboxOffsetY, hitboxWidth, hitboxHeight);
    }

    public void render(SpriteBatch batch) {
        batch.draw(frameAtual, x + offsetX, y + offsetY, larguraDesenho, alturaDesenho);
    }

    public boolean encostouNoPlayer(PersonagemTeste player) { return caixa.overlaps(player.getCaixaPersonagem()); }
    public void dispose() { spriteSheet.dispose(); }
    public Rectangle getCaixa() { return caixa; }
}
