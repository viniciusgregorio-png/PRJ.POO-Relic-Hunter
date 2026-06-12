package io.github.relichunter.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.MapaTeste;
import io.github.relichunter.screens.PersonagemTeste;

public class PedraEmpurravel {

    private enum Estado {
        PARADA,
        EMPURRADA,
        CAINDO
    }

    private float x, y;
    private final float largura, altura;

    private final float VELOCIDADE_EMPURRAO = 150f;
    private final float VELOCIDADE_QUEDA = 300f;

    private int direcaoX = 0;
    private Estado estadoAtual = Estado.PARADA;

    private float xAlvo;
    private float yAlvo;

    private final MapaTeste mapa;
    private final PersonagemTeste personagem;
    private final float alturaVirtual;

    private final Rectangle caixaPedra = new Rectangle();
    private final Rectangle caixaPlayer = new Rectangle();

    private Texture texturaPedra;
    private Animation<TextureRegion> animacaoRolando;
    private TextureRegion frameParado;
    private SpriteBatch spriteBatch;
    private float tempoAnimacao;

    public PedraEmpurravel(float x, float y, float largura, float altura, MapaTeste mapa, PersonagemTeste personagem, float alturaVirtual) {
        this.x = Math.round(x / MapaTeste.TAMANHO_BLOCO) * MapaTeste.TAMANHO_BLOCO;
        this.y = Math.round(y / MapaTeste.TAMANHO_BLOCO) * MapaTeste.TAMANHO_BLOCO;

        this.largura = largura;
        this.altura = altura;
        this.mapa = mapa;
        this.personagem = FitsPersonagem(personagem);
        this.alturaVirtual = alturaVirtual;

        this.caixaPedra.set(this.x, this.y, largura, altura);

        this.spriteBatch = new SpriteBatch();
        this.texturaPedra = new Texture(Gdx.files.internal("assets/mapa/pedra.png"));

        int colunas = 6;
        int linhas = 1;
        TextureRegion[][] tmp = TextureRegion.split(
            texturaPedra,
            texturaPedra.getWidth() / colunas,
            texturaPedra.getHeight() / linhas
        );

        this.frameParado = tmp[0][0];

        TextureRegion[] framesMovimento = new TextureRegion[4];
        for (int j = 0; j < 4; j++) {
            framesMovimento[j] = tmp[0][j];
        }

        this.animacaoRolando = new Animation<>(0.1f, framesMovimento);
        this.animacaoRolando.setPlayMode(Animation.PlayMode.LOOP);
        this.tempoAnimacao = 0f;

        this.xAlvo = this.x;
        this.yAlvo = this.y;
    }

    private PersonagemTeste FitsPersonagem(PersonagemTeste p) {
        return p;
    }

    public void update(float delta) {
        caixaPlayer.set(personagem.getCaixaPersonagem());

        if (estadoAtual != Estado.PARADA) {
            tempoAnimacao += delta;
        } else {
            tempoAnimacao = 0f;
        }

        switch (estadoAtual) {
            case PARADA:
                if (verificarAbaixoLivre()) {
                    iniciarQueda();
                } else {
                    checarInteracaoJogador();
                }
                break;

            case EMPURRADA:
                atualizarEmpurrao(delta);
                break;

            case CAINDO:
                atualizarQueda(delta);
                break;
        }

        caixaPedra.setPosition(x, y);
    }

    private void checarInteracaoJogador() {
        if (caixaPedra.overlaps(caixaPlayer)) {
            String lado = calcularLadoColisao();

            switch (lado) {
                case "ESQUERDA":
                    float alvoDireita = x + MapaTeste.TAMANHO_BLOCO;
                    if (!colideComMapa(alvoDireita, y)) {
                        xAlvo = alvoDireita;
                        direcaoX = 1;
                        estadoAtual = Estado.EMPURRADA;
                    } else {
                        bloquearJogadorEsquerda();
                    }
                    break;

                case "DIREITA":
                    float alvoEsquerda = x - MapaTeste.TAMANHO_BLOCO;
                    if (!colideComMapa(alvoEsquerda, y)) {
                        xAlvo = alvoEsquerda;
                        direcaoX = -1;
                        estadoAtual = Estado.EMPURRADA;
                    } else {
                        bloquearJogadorDireita();
                    }
                    break;

                case "ACIMA":
                    personagem.setY(y + altura - 3.9f);
                    break;

                case "ABAIXO":
                    personagem.setY(y - personagem.getAltura() - 4.1f);
                    break;
            }
        }
    }

    private void atualizarEmpurrao(float delta) {
        float passo = direcaoX * VELOCIDADE_EMPURRAO * delta;
        x += passo;

        if ((direcaoX == 1 && x >= xAlvo) || (direcaoX == -1 && x <= xAlvo)) {
            x = xAlvo;
            estadoAtual = Estado.PARADA;
            direcaoX = 0;
        }

        if (caixaPedra.overlaps(caixaPlayer)) {
            if (direcaoX == 1) {
                bloquearJogadorEsquerda();
            } else if (direcaoX == -1) {
                bloquearJogadorDireita();
            }
        }
    }

    private void iniciarQueda() {
        yAlvo = y - MapaTeste.TAMANHO_BLOCO;
        estadoAtual = Estado.CAINDO;
    }

    private void atualizarQueda(float delta) {
        y -= VELOCIDADE_QUEDA * delta;
        caixaPedra.setPosition(x, y);

        if (caixaPedra.overlaps(caixaPlayer)) {
            float playerEsquerda = caixaPlayer.x;
            float playerDireita = caixaPlayer.x + caixaPlayer.width;
            float playerTopo = caixaPlayer.y + caixaPlayer.height;

            float pedraEsquerda = x;
            float pedraDireita = x + largura;
            float pedraBase = y;

            float margemX = 6f;
            boolean estaAlinhadoX = (playerDireita - margemX > pedraEsquerda) && (playerEsquerda + margemX < pedraDireita);
            boolean estaAbaixoDaPedra = playerTopo > pedraBase && caixaPlayer.y < pedraBase;

            if (estaAlinhadoX && estaAbaixoDaPedra) {
                personagem.morrer();
            } else {
                float centroPedraX = x + largura / 2f;
                float centroPlayerX = caixaPlayer.x + caixaPlayer.width / 2f;

                if (centroPlayerX < centroPedraX) {
                    bloquearJogadorEsquerda();
                } else {
                    bloquearJogadorDireita();
                }
            }
        }

        if (y <= yAlvo) {
            y = yAlvo;
            estadoAtual = Estado.PARADA;
        }
    }

    private boolean verificarAbaixoLivre() {
        float yAbaixo = y - MapaTeste.TAMANHO_BLOCO;
        return !colideComMapa(x, yAbaixo);
    }

    private void bloquearJogadorEsquerda() {
        personagem.setX(x - personagem.getLargura() - 4.1f);
    }

    private void bloquearJogadorDireita() {
        personagem.setX(x + largura - 3.9f);
    }

    private String calcularLadoColisao() {
        float centroPedraX = x + largura / 2f;
        float centroPedraY = y + altura / 2f;
        float centroPlayerX = caixaPlayer.x + caixaPlayer.width / 2f;
        float centroPlayerY = caixaPlayer.y + caixaPlayer.height / 2f;

        float dx = centroPlayerX - centroPedraX;
        float dy = centroPlayerY - centroPedraY;

        if (Math.abs(dx) > Math.abs(dy)) {
            return (dx > 0) ? "DIREITA" : "ESQUERDA";
        } else {
            return (dy > 0) ? "ACIMA" : "ABAIXO";
        }
    }

    private boolean colideComMapa(float px, float py) {
        float margem = 1f;
        float x1 = px + margem;
        float y1 = py + margem;
        float x2 = px + largura - margem;
        float y2 = py + altura - margem;

        int tileX1 = (int) (x1 / MapaTeste.TAMANHO_BLOCO);
        int tileY1 = (int) (y1 / MapaTeste.TAMANHO_BLOCO);
        int tileX2 = (int) (x2 / MapaTeste.TAMANHO_BLOCO);
        int tileY2 = (int) (y2 / MapaTeste.TAMANHO_BLOCO);

        int larguraMapa = mapa.getQuantidadeColunas();
        int alturaMapa = mapa.getQuantidadeLinhas();

        for (int linha = tileY1; linha <= tileY2; linha++) {
            for (int coluna = tileX1; coluna <= tileX2; coluna++) {
                if (coluna < 0 || coluna >= larguraMapa || linha < 0 || linha >= alturaMapa) {
                    return true;
                }
                if (!mapa.isEspacoLivre(coluna, linha)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void render(OrthographicCamera camera) {
        TextureRegion frameAtual;

        if (estadoAtual == Estado.PARADA) {
            frameAtual = frameParado;
        } else {
            frameAtual = animacaoRolando.getKeyFrame(tempoAnimacao);
        }

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(frameAtual, x, y, largura, altura);
        spriteBatch.end();
    }

    public void dispose() {
        if (texturaPedra != null) {
            texturaPedra.dispose();
        }
        if (spriteBatch != null) {
            spriteBatch.dispose();
        }
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
