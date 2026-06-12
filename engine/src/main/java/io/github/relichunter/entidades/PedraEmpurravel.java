package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.MapaTeste;
import io.github.relichunter.screens.PersonagemTeste;

public class PedraEmpurravel {
    private float x, y;
    private float largura, altura;
    private float velocidade = 150f;
    private int direcao = 0; // -1 Esquerda, 1 Direita, 0 Parada
    private boolean estaRolando = false;

    private MapaTeste mapa;
    private PersonagemTeste personagem;
    private float alturaVirtual;

    private final Rectangle caixaPedra = new Rectangle();
    private final Rectangle caixaPlayer = new Rectangle();
    private final Rectangle caixaBloco = new Rectangle();
    private final ShapeRenderer shapeRenderer;

    public PedraEmpurravel(float x, float y, float largura, float altura, MapaTeste mapa, PersonagemTeste personagem, float alturaVirtual) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.mapa = mapa;
        this.personagem = FitsPersonagem(personagem);
        this.alturaVirtual = alturaVirtual;

        this.caixaPedra.set(x, y, largura, altura);
        this.shapeRenderer = new ShapeRenderer();
    }

    private PersonagemTeste FitsPersonagem(PersonagemTeste p) {
        return p;
    }

    public void update(float delta) {
        caixaPlayer.set(personagem.getCaixaPersonagem());

        if (!estaRolando) {
            if (caixaPedra.overlaps(caixaPlayer)) {
                String lado = calcularLadoColisao();

                switch (lado) {
                    case "ESQUERDA":
                        // Só começa a rolar se o caminho à DIREITA no mapa estiver livre
                        if (!colideComMapa(x + 5f, y)) {
                            direcao = 1;
                            estaRolando = true;
                        } else {
                            // Se tiver parede, barra o jogador impedindo clipping
                            personagem.setX(x - personagem.getLargura() - 4.1f);
                        }
                        break;
                    case "DIREITA":
                        // Só começa a rolar se o caminho à ESQUERDA no mapa estiver livre
                        if (!colideComMapa(x - 5f, y)) {
                            direcao = -1;
                            estaRolando = true;
                        } else {
                            // Se tiver parede, barra o jogador impedindo clipping
                            personagem.setX(x + largura + 0.1f);
                        }
                        break;
                    case "ACIMA":
                        personagem.setY(y + altura + 0.1f);
                        estaRolando = false;
                        break;
                    case "ABAIXO":
                        personagem.setY(y - personagem.getAltura() - 4.1f);
                        estaRolando = false;
                        break;
                }
            }
        } else {
            // Movimentação contínua da pedra
            float novoX = x + (direcao * velocidade * delta);

            if (colideComMapa(novoX, y)) {
                estaRolando = false;
                direcao = 0;

                // Ajuste fino: Alinha a pedra perfeitamente ao grid do bloco quando parar
                x = Math.round(x / MapaTeste.TAMANHO_BLOCO) * MapaTeste.TAMANHO_BLOCO;
                caixaPedra.setX(x);
            } else {
                x = novoX;
                caixaPedra.setX(x);
            }

            // Mantém o player colado/empurrando a pedra enquanto ela se move de forma fluida
            if (caixaPedra.overlaps(caixaPlayer)) {
                if (direcao == 1) {
                    personagem.setX(x - personagem.getLargura() - 4.1f);
                } else if (direcao == -1) {
                    personagem.setX(x + largura + 0.1f);
                }
            }
        }
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
        // Reduzimos ligeiramente a caixa de teste para evitar falsos positivos nas quinas do corredor
        Rectangle simulaPedra = new Rectangle(px + 1, py + 1, largura - 2, altura - 2);
        caixaBloco.setSize(MapaTeste.TAMANHO_BLOCO, MapaTeste.TAMANHO_BLOCO);

        int larguraMapa = 60;
        for (int linha = 0; linha < mapa.getQuantidadeLinhas(); linha++) {
            for (int coluna = 0; coluna < larguraMapa; coluna++) {
                if (!mapa.isEspacoLivre(coluna, linha)) {
                    int bx = coluna * MapaTeste.TAMANHO_BLOCO;
                    int by = linha * MapaTeste.TAMANHO_BLOCO;
                    caixaBloco.setPosition(bx, by);

                    if (simulaPedra.overlaps(caixaBloco)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void render(OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.9f, 0.5f, 0.0f, 1.0f); // Laranja para testes
        shapeRenderer.rect(x, y, largura, altura);
        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
