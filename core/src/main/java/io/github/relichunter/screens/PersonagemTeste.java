package io.github.relichunter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class PersonagemTeste {

    private final Texture texDown;
    private final Texture texUp;
    private final Texture texRightDown;
    private final Texture textureRightUp;

    private final Texture texIdleDown;
    private final Texture texIdleUp;
    private final Texture texIdleRightDown;
    private final Texture texIdleRightUp;

    private TextureRegion[] framesDown;
    private TextureRegion[] framesUp;
    private TextureRegion[] framesRightDown;
    private TextureRegion[] framesRightUp;

    private TextureRegion[] framesIdleDown;
    private TextureRegion[] framesIdleUp;
    private TextureRegion[] framesIdleRightDown;
    private TextureRegion[] framesIdleRightUp;

    private TextureRegion frameAtual;

    private float posX;
    private float posY;

    private final float VELOCIDADE = 100f;
    private float tempoAnimacao = 0;
    private final float VELOCIDADE_ANIMACAO = 0.12f;
    private boolean estaSeMovendo = false;

    private enum DirecaoOlhando { FRENTE, COSTAS, LADO_BAIXO, LADO_CIMA }
    private DirecaoOlhando direcaoAtual = DirecaoOlhando.FRENTE;
    private boolean espelharX = false;

    private final Rectangle caixaPersonagem = new Rectangle();
    private final Rectangle caixaBloco = new Rectangle();

    public PersonagemTeste() {
        // 1. Carrega os arquivos de caminhada
        texDown = new Texture("walk_Down.png");
        texUp = new Texture("walk_Up.png");
        texRightDown = new Texture("walk_Right_Down.png");
        textureRightUp = new Texture("walk_Right_Up.png");

        texIdleDown = new Texture("idle_Down.png");
        texIdleUp = new Texture("idle_Up.png");
        texIdleRightDown = new Texture("idle_Right_Down.png");
        texIdleRightUp = new Texture("idle_Right_Up.png");

        // 3. Recorta as tiras de caminhada dinamicamente (8 frames por linha)
        framesDown = TextureRegion.split(texDown, texDown.getWidth() / 8, texDown.getHeight())[0];
        framesUp = TextureRegion.split(texUp, texUp.getWidth() / 8, texUp.getHeight())[0];
        framesRightDown = TextureRegion.split(texRightDown, texRightDown.getWidth() / 8, texRightDown.getHeight())[0];
        framesRightUp = TextureRegion.split(textureRightUp, textureRightUp.getWidth() / 8, textureRightUp.getHeight())[0];

        // 4. Recorta as tiras de Idle (Supondo que também tenham 8 frames ou mude o divisor se tiver menos)
        framesIdleDown = TextureRegion.split(texIdleDown, texIdleDown.getWidth() / 8, texIdleDown.getHeight())[0];
        framesIdleUp = TextureRegion.split(texIdleUp, texIdleUp.getWidth() / 8, texIdleUp.getHeight())[0];
        framesIdleRightDown = TextureRegion.split(texIdleRightDown, texIdleRightDown.getWidth() / 8, texIdleRightDown.getHeight())[0];
        framesIdleRightUp = TextureRegion.split(texIdleRightUp, texIdleRightUp.getWidth() / 8, texIdleRightUp.getHeight())[0];

        frameAtual = framesIdleDown[0];

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
            direcaoAtual = DirecaoOlhando.COSTAS;
            movimientoY = VELOCIDADE * delta;
            estaSeMovendo = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direcaoAtual = DirecaoOlhando.FRENTE;
            movimientoY = -VELOCIDADE * delta;
            estaSeMovendo = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direcaoAtual = (movimientoY > 0) ? DirecaoOlhando.LADO_CIMA : DirecaoOlhando.LADO_BAIXO;
            movimentoX = -VELOCIDADE * delta;
            estaSeMovendo = true;
            espelharX = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direcaoAtual = (movimientoY > 0) ? DirecaoOlhando.LADO_CIMA : DirecaoOlhando.LADO_BAIXO;
            movimentoX = VELOCIDADE * delta;
            estaSeMovendo = true;
            espelharX = false;
        }

        tempoAnimacao += delta;

        if (estaSeMovendo) {
            float novaPosX = posX + movimentoX;
            if (!detectarColisao(novaPosX, posY, mapa)) {
                posX = novaPosX;
            }

            float novaPosY = posY + movimientoY;
            if (!detectarColisao(posX, novaPosY, mapa)) {
                posY = novaPosY;
            }
        }

        int frameId = (int) (tempoAnimacao / VELOCIDADE_ANIMACAO) % 8;
        TextureRegion frameSelecionado;

        if (estaSeMovendo) {
            switch (direcaoAtual) {
                case COSTAS:     frameSelecionado = framesUp[frameId]; break;
                case LADO_CIMA:  frameSelecionado = framesRightUp[frameId]; break;
                case LADO_BAIXO: frameSelecionado = framesRightDown[frameId]; break;
                case FRENTE:
                default:         frameSelecionado = framesDown[frameId]; break;
            }
        } else {
            switch (direcaoAtual) {
                case COSTAS:     frameSelecionado = framesIdleUp[frameId]; break;
                case LADO_CIMA:  frameSelecionado = framesIdleRightUp[frameId]; break;
                case LADO_BAIXO: frameSelecionado = framesIdleRightDown[frameId]; break;
                case FRENTE:
                default:         frameSelecionado = framesIdleDown[frameId]; break;
            }
        }

        // 5. CRIAÇÃO DA REGIÃO E APLICAÇÃO DO FLIP
        frameAtual = new TextureRegion(frameSelecionado);
        if (espelharX) {
            frameAtual.flip(true, false);
        }

        caixaPersonagem.setPosition(posX + 4, posY + 4);
    }

    private boolean detectarColisao(float proximaX, float proximaY, MapaTeste mapa) {
        caixaPersonagem.setPosition(proximaX + 4, proximaY + 4);
        caixaBloco.setSize(MapaTeste.TAMANHO_BLOCO, MapaTeste.TAMANHO_BLOCO);

        TiledMapTileLayer camada = (TiledMapTileLayer) mapa.getTiledMap().getLayers().get("paredes");
        if (camada == null) return false;

        int colunaInicio = (int) (caixaPersonagem.x / MapaTeste.TAMANHO_BLOCO);
        int colunaFim    = (int) ((caixaPersonagem.x + caixaPersonagem.width) / MapaTeste.TAMANHO_BLOCO);
        int linhaInicio  = (int) (caixaPersonagem.y / MapaTeste.TAMANHO_BLOCO);
        int linhaFim     = (int) ((caixaPersonagem.y + caixaPersonagem.height) / MapaTeste.TAMANHO_BLOCO);

        for (int linha = linhaInicio; linha <= linhaFim; linha++) {
            for (int coluna = colunaInicio; coluna <= colunaFim; coluna++) {
                if (!mapa.isEspacoLivre(coluna, linha)) {
                    int xPixelBloco = coluna * MapaTeste.TAMANHO_BLOCO;
                    int yPixelBloco = linha * MapaTeste.TAMANHO_BLOCO;

                    caixaBloco.setPosition(xPixelBloco, yPixelBloco);

                    if (caixaPersonagem.overlaps(caixaBloco)) {
                        caixaPersonagem.setPosition(posX + 4, posY + 4);
                        return true;
                    }
                }
            }
        }

        caixaPersonagem.setPosition(posX + 4, posY + 4);
        return false;
    }

    public void desenhar(SpriteBatch batch, int alturaJanela) {
        float larguraOriginal = frameAtual.getRegionWidth();
        float alturaOriginal = frameAtual.getRegionHeight();

        // Preservando as proporções exatas do ajuste fino que fizemos por último
        float escalaX = 1.3f;
        float escalaY = 1.05f;

        float larguraFinal = larguraOriginal * escalaX;
        float alturaFinal = alturaOriginal * escalaY;

        float recuoX = (larguraFinal - 24f) / 2f;
        float recuoY = (alturaFinal - 24f) / 3f;

        batch.draw(
            frameAtual,
            posX - recuoX,
            posY - recuoY,
            larguraFinal,
            alturaFinal
        );
    }

    public Rectangle getCaixaPersonagem() { return caixaPersonagem; }
    public float getPosX() { return posX; }
    public float getPosY() { return posY; }

    public void setX(float x) {
        this.posX = x;
        caixaPersonagem.setPosition(posX + 4, posY + 4);
    }

    public void setY(float y) {
        this.posY = y;
        caixaPersonagem.setPosition(posX + 4, posY + 4);
    }

    public float getLargura() { return 24f; }
    public float getAltura() { return 24f; }

    public void dispose() {
        texDown.dispose();
        texUp.dispose();
        texRightDown.dispose();
        textureRightUp.dispose();
        texIdleDown.dispose();
        texIdleUp.dispose();
        texIdleRightDown.dispose();
        texIdleRightUp.dispose();
    }
}
