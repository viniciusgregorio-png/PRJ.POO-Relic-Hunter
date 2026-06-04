package io.github.relichunter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class PersonagemTeste {

    private final Texture spriteSheet;
    private TextureRegion[][] frames;
    private TextureRegion frameAtual;

    private float posX;
    private float posY;

    private final float VELOCIDADE = 100f;

    private float tempoAnimacao = 0;
    private final float VELOCIDADE_ANIMACAO = 0.15f;
    private int linhaAnimacaoAtual = 0;
    private boolean estaSeMovendo = false;

    private final Rectangle caixaPersonagem = new Rectangle();
    private final Rectangle caixaBloco = new Rectangle();

    public PersonagemTeste() {
        spriteSheet = new Texture("AnimationSheet_Character.png");
        frames = TextureRegion.split(spriteSheet, MapaTeste.TAMANHO_BLOCO, MapaTeste.TAMANHO_BLOCO);
        frameAtual = frames[0][0];

        // Define a coluna 1 e a linha 1 da matriz em pixels exatos na tela
        this.posX = 1 * MapaTeste.TAMANHO_BLOCO;
        this.posY = 320 - ((1 + 1) * MapaTeste.TAMANHO_BLOCO);

        // Caixa de colisão ligeiramente menor para não engasgar nos cantos
        caixaPersonagem.setSize(28, 28);
        caixaPersonagem.setPosition(posX + 2, posY + 2);
    }

    public void atualizar(MapaTeste mapa, float delta) {
        estaSeMovendo = false;
        float movimentoX = 0;
        float movimentoY = 0;

        // Captura as intenções de movimento e define as linhas de animação
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            linhaAnimacaoAtual = 1; // Olhando para trás
            movimentoY = VELOCIDADE * delta;
            estaSeMovendo = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            linhaAnimacaoAtual = 0; // Olhando para frente
            movimentoY = -VELOCIDADE * delta;
            estaSeMovendo = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            linhaAnimacaoAtual = 3;
            movimentoX = -VELOCIDADE * delta;
            estaSeMovendo = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            linhaAnimacaoAtual = 3;
            movimentoX = VELOCIDADE * delta;
            estaSeMovendo = true;
        }

        // Se houver movimento, aplica a checagem de colisão por eixos (AABB)
        if (estaSeMovendo) {
            tempoAnimacao += delta;

            // Tenta mover no eixo X e checa colisão
            float novaPosX = posX + movimentoX;
            if (!detectarColisao(novaPosX, posY, mapa)) {
                posX = novaPosX;
            }

            // Tenta mover no eixo Y e checa colisão
            float novaPosY = posY + movimentoY;
            if (!detectarColisao(posX, novaPosY, mapa)) {
                posY = novaPosY;
            }
        }

        // Controle dos frames da animação
        int totalFramesDaLinha = (linhaAnimacaoAtual == 3) ? 8 : 2;
        int frameId = (int) (tempoAnimacao / VELOCIDADE_ANIMACAO) % totalFramesDaLinha;
        frameAtual = frames[linhaAnimacaoAtual][frameId];

        // Espelhamento do sprite para esquerda/direita
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (!frameAtual.isFlipX()) frameAtual.flip(true, false);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (frameAtual.isFlipX()) frameAtual.flip(true, false);
        }

        // Atualiza a caixa SEMPRE no final, com a posição definitiva do personagem
        caixaPersonagem.setPosition(posX + 2, posY + 2);
    }

    /**
     * Simula a nova posição da caixa do personagem e varre o mapa
     * procurando se colidimos com algum bloco de parede (1).
     */
    private boolean detectarColisao(float proximaX, float proximaY, MapaTeste mapa) {
        caixaPersonagem.setPosition(proximaX + 2, proximaY + 2);
        caixaBloco.setSize(MapaTeste.TAMANHO_BLOCO, MapaTeste.TAMANHO_BLOCO);

        for (int linha = 0; linha < mapa.getQuantidadeLinhas(); linha++) {
            for (int coluna = 0; coluna < 15; coluna++) {
                if (!mapa.isEspacoLivre(coluna, linha)) {
                    int xPixelBloco = coluna * MapaTeste.TAMANHO_BLOCO;
                    int yPixelBloco = 320 - ((linha + 1) * MapaTeste.TAMANHO_BLOCO);

                    caixaBloco.setPosition(xPixelBloco, yPixelBloco);

                    if (caixaPersonagem.overlaps(caixaBloco)) {
                        // Restaura a caixa para a posição real antes de retornar
                        caixaPersonagem.setPosition(posX + 2, posY + 2);
                        return true;
                    }
                }
            }
        }

        // Restaura a caixa para a posição real antes de retornar
        caixaPersonagem.setPosition(posX + 2, posY + 2);
        return false;
    }

    public void desenhar(SpriteBatch batch, int alturaJanela) {
        batch.draw(frameAtual, posX, posY, MapaTeste.TAMANHO_BLOCO, MapaTeste.TAMANHO_BLOCO);
    }

    public Rectangle getCaixaPersonagem() {
        return caixaPersonagem;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    // ✅ Atualiza posX e reposiciona a caixa de colisão
    public void setPosX(float v) {
        posX = v;
        caixaPersonagem.setPosition(posX + 2, posY + 2);
    }

    // ✅ Atualiza posY e reposiciona a caixa de colisão
    public void setPosY(float v) {
        posY = v;
        caixaPersonagem.setPosition(posX + 2, posY + 2);
    }

    // ✅ Necessário para calcular o centro do personagem na pedra
    public float getLargura() {
        return 28f; // mesmo tamanho da caixaPersonagem
    }

    public void dispose() {
        spriteSheet.dispose();
    }

    public float getAltura() {
        return 32f; // <-- Substitua o 32f pela altura real da sua caixaPersonagem
    }
}
