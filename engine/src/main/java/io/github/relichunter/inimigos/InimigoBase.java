package io.github.relichunter.inimigos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.MapaTeste;
import io.github.relichunter.screens.PersonagemTeste;

public class InimigoBase {
    // Separação do Tipo Visual (qual bicho é) do Tipo de Movimento!
    protected int tipoInimigo; // 1 = Cobra, 2 = Morcego, 3 = Aranha, 4 = Fogo
    protected int tipoMovimento; // 0 = Parado, 1 = Horizontal, 2 = Vertical

    protected float x, y, speed = 60f, direcaoX, direcaoY;
    protected Texture spriteSheet;
    protected TextureRegion[] frames;
    protected TextureRegion frameAtual;
    protected float tempoAnimacao = 0f;
    protected final float VELOCIDADE_ANIMACAO = 0.12f;
    protected MapaTeste mapa;
    protected Rectangle caixa = new Rectangle();

    // Dimensões de renderização e colisão individuais do inimigo
    protected float larguraDesenho = 32f;
    protected float alturaDesenho = 32f;

    /**
     * CONSTRUTOR SOBRECARREGADO (Compatibilidade):
     * Aceita as chamadas antigas de 8 parâmetros vindas da sua TelaTeste.java.
     */
    public InimigoBase(int tipo, int forca, int vida, float x, float y, float limiteW, float limiteH, MapaTeste mapa) {
        this(tipo, forca, vida, x, y, limiteW, limiteH, mapa, (tipo == 2 || tipo == 3 || tipo == 5) ? 2 : 1);
    }

    /**
     * Construtor Principal:
     * Inicializa a textura correta do inimigo e recorta os frames conforme o tamanho visual de cada um.
     */
    public InimigoBase(int tipo, int forca, int vida, float x, float y, float limiteW, float limiteH, MapaTeste mapa, int tipoMovimento) {
        this.tipoInimigo = tipo;
        this.tipoMovimento = tipoMovimento;
        this.x = x;
        this.y = y;
        this.mapa = mapa;

        String caminhoTextura;
        int totalFrames = 4;

        // Define dinamicamente a textura, dimensões e número de quadros de animação de cada tipo
        switch (tipoInimigo) {
            case 2:
                caminhoTextura = "assets/inimigos/bat_spritesheet.png"; // Morcego
                totalFrames = 4;
                this.larguraDesenho = 32f;
                this.alturaDesenho = 32f;
                break;
            case 3:
                caminhoTextura = "assets/inimigos/spider_spritesheet.png"; // Aranha
                totalFrames = 6;
                this.larguraDesenho = 32f;
                this.alturaDesenho = 32f;
                break;
            case 4:
                caminhoTextura = "assets/inimigos/fire_spritesheet.png"; // Fogo Alto
                totalFrames = 6;
                this.larguraDesenho = 32f;
                this.alturaDesenho = 64f;
                break;

            default:
                caminhoTextura = "assets/inimigos/snake_spritesheet.png"; // Cobra
                totalFrames = 4;
                this.larguraDesenho = 32f;
                this.alturaDesenho = 32f;
                break;
        }

        this.spriteSheet = new Texture(caminhoTextura);
        this.frames = new TextureRegion[totalFrames];

        // Recorta os frames do spritesheet de acordo com o tamanho original da imagem
        for (int i = 0; i < totalFrames; i++) {
            // Se for o fogo, o corte de origem na imagem (png) é de 48px de altura
            int frameAlturaOrigem = (tipoInimigo == 4) ? 48 : 32;
            frames[i] = new TextureRegion(spriteSheet, i * 32, 0, 32, frameAlturaOrigem);
        }

        this.frameAtual = frames[0];

        // Configura as direções de movimento (Horizontal, Vertical ou Parado)
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

            // Ajuste dinâmico do teste de colisão com o mapa baseado na largura e altura do inimigo
            if (mapa.isParede(proxX + (larguraDesenho / 2f), proxY + (alturaDesenho / 2f))) {
                direcaoX *= -1;
                direcaoY *= -1;
            } else {
                x = proxX;
                y = proxY;
            }
        }

        // Define a caixa de colisão física com o jogador.
        float margemX = 4f;
        float margemY = 4f;
        float larguraFisica = larguraDesenho - (margemX * 2);

        // Se for o fogo, criamos uma caixa de colisão física vertical proporcional à sua nova altura
        float alturaFisica;
        if (tipoInimigo == 4) {
            alturaFisica = alturaDesenho - 8f; // Colisão quase em toda a altura da labareda
        } else {
            alturaFisica = alturaDesenho - (margemY * 2);
        }

        caixa.set(x + margemX, y + margemY, larguraFisica, alturaFisica);
    }

    public void render(SpriteBatch batch) {
        // Renderiza o frame usando as proporções de tamanho de desenho configuradas no construtor
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
