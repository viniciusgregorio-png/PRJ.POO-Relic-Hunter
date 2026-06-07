package io.github.relichunter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

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

        this.posX = 1 * MapaTeste.TAMANHO_BLOCO;
        this.posY = 1 * MapaTeste.TAMANHO_BLOCO;

        caixaPersonagem.setSize(24, 24);
        caixaPersonagem.setPosition(posX + 4, posY + 4);
    }

    public void atualizar(MapaTeste mapa, float delta) {
        estaSeMovendo = false;
        float movimentoX = 0;
        float movimientoY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            linhaAnimacaoAtual = 1;
            movimientoY = VELOCIDADE * delta;
            estaSeMovendo = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            linhaAnimacaoAtual = 0;
            movimientoY = -VELOCIDADE * delta;
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

        if (estaSeMovendo) {
            tempoAnimacao += delta;

            float novaPosX = posX + movimentoX;
            if (!detectarColisao(novaPosX, posY, mapa)) {
                posX = novaPosX;
            }

            float novaPosY = posY + movimientoY;
            if (!detectarColisao(posX, novaPosY, mapa)) {
                posY = novaPosY;
            }
        }

        int totalFramesDaLinha = (linhaAnimacaoAtual == 3) ? 8 : 2;
        int frameId = (int) (tempoAnimacao / VELOCIDADE_ANIMACAO) % totalFramesDaLinha;
        frameAtual = frames[linhaAnimacaoAtual][frameId];

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (!frameAtual.isFlipX()) frameAtual.flip(true, false);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (frameAtual.isFlipX()) frameAtual.flip(true, false);
        }

        caixaPersonagem.setPosition(posX + 4, posY + 4);
    }

    private boolean detectarColisao(float proximaX, float proximaY, MapaTeste mapa) {
        // Posiciona a caixa simulada no local para onde o player quer ir
        caixaPersonagem.setPosition(proximaX + 4, proximaY + 4);
        caixaBloco.setSize(MapaTeste.TAMANHO_BLOCO, MapaTeste.TAMANHO_BLOCO);

        TiledMapTileLayer camada = (TiledMapTileLayer) mapa.getTiledMap().getLayers().get("paredes");
        if (camada == null) return false;

        // Em vez de varrer o mapa inteiro (o que deixa o jogo pesado num mapa 60x40),
        // vamos calcular apenas os blocos que estão encostando na caixa do personagem!
        int colunaInicio = (int) (caixaPersonagem.x / MapaTeste.TAMANHO_BLOCO);
        int colunaFim    = (int) ((caixaPersonagem.x + caixaPersonagem.width) / MapaTeste.TAMANHO_BLOCO);
        int linhaInicio  = (int) (caixaPersonagem.y / MapaTeste.TAMANHO_BLOCO);
        int linhaFim     = (int) ((caixaPersonagem.y + caixaPersonagem.height) / MapaTeste.TAMANHO_BLOCO);

        // Varre apenas o quadrado de blocos ao redor do player
        for (int linha = linhaInicio; linha <= linhaFim; linha++) {
            for (int coluna = colunaInicio; coluna <= colunaFim; coluna++) {

                // Se o espaço NÃO estiver livre (ou seja, tem bloco de parede ali)
                if (!mapa.isEspacoLivre(coluna, linha)) {
                    int xPixelBloco = coluna * MapaTeste.TAMANHO_BLOCO;
                    int yPixelBloco = linha * MapaTeste.TAMANHO_BLOCO;

                    caixaBloco.setPosition(xPixelBloco, yPixelBloco);

                    // Se houver overlap, reseta a caixa do player e para o movimento
                    if (caixaPersonagem.overlaps(caixaBloco)) {
                        caixaPersonagem.setPosition(posX + 4, posY + 4);
                        return true;
                    }
                }
            }
        }

        // Reseta a caixa para a posição real se não colidiu
        caixaPersonagem.setPosition(posX + 4, posY + 4);
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

    public void setX(float x) {
        this.posX = x;
        caixaPersonagem.setPosition(posX + 4, posY + 4);
    }

    public void setY(float y) {
        this.posY = y;
        caixaPersonagem.setPosition(posX + 4, posY + 4);
    }

    public float getLargura() {
        return 24f;
    }

    public float getAltura() {
        return 24f;
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
