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

                // 2. Aplicação do comportamento seguro com base no lado corrigido
                // Subtraímos/somamos uma folga de segurança (ex: 2.1f) para compensar o alinhamento
                // do sprite (normalmente maior) com a hitbox interna, prevenindo que fiquem colados.
                switch (lado) {
                    case "ESQUERDA":
                        direcao = 1; // Movimento para a direita
                        estaRolando = true;
                        break;
                    case "DIREITA":
                        direcao = -1; // Movimento para a esquerda
                        estaRolando = true;
                        break;
                    case "ACIMA":
                        // Bloqueio superior limpo
                        personagem.setY(y + altura + 0.1f);
                        estaRolando = false;
                        break;
                    case "ABAIXO":
                        // Bloqueio inferior compensando possíveis offsets do eixo Y
                        personagem.setY(y - personagem.getAltura() - 2.1f);
                        estaRolando = false;
                        break;
                }
            }
        } else {
            // Movimentação da pedra após o empurrão (Eixo X)
            float novoX = x + (direcao * velocidade * delta);

            if (colideComMapa(novoX, y)) {
                estaRolando = false;
                direcao = 0;
            } else {
                x = novoX;
                caixaPedra.setX(x);
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
        Rectangle simulaPedra = new Rectangle(px, py, largura, altura);
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
