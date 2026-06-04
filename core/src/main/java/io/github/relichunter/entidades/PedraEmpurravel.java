package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.MapaTeste;
import io.github.relichunter.screens.PersonagemTeste;

public class PedraEmpurravel extends Obstaculo {
    private ShapeRenderer shapeRenderer;
    private boolean estaRolando;
    private float direcao;
    private PersonagemTeste personagem;
    private final Rectangle caixaPedra = new Rectangle();
    private final GerenciadorColisao colisao;

    public PedraEmpurravel(float x, float y, float largura, float altura, MapaTeste mapa, PersonagemTeste personagem, int alturaVirtual) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.estaRolando = false;
        this.direcao = 0;
        this.personagem = personagem;
        this.shapeRenderer = new ShapeRenderer();
        this.colisao = new GerenciadorColisao(mapa, alturaVirtual);
    }

    @Override
    public void update(float delta) {
        caixaPedra.set(x, y, largura, altura);

        // Obtenção direta da caixa de colisão do jogador para precisão absoluta
        Rectangle caixaPlayer = personagem.getCaixaPersonagem();

        if (caixaPedra.overlaps(caixaPlayer)) {
            // 1. Identificação do lado de colisão inicial pelo gerenciador
            String lado = colisao.ladoColisao(caixaPlayer, caixaPedra);

            // 🛡️ FILTRO DE SEGURANÇA (Resolução de conflito de colisão diagonal)
            float centroPlayerX = caixaPlayer.x + caixaPlayer.width / 2f;
            float centroStoneX = caixaPedra.x + caixaPedra.width / 2f;
            float centroPlayerY = caixaPlayer.y + caixaPlayer.height / 2f;
            float centroStoneY = caixaPedra.y + caixaPedra.height / 2f;

            float diffX = Math.abs(centroPlayerX - centroStoneX);
            float diffY = Math.abs(centroPlayerY - centroStoneY);

            if (diffX > diffY) {
                if (centroPlayerX < centroStoneX) {
                    lado = "ESQUERDA";
                } else {
                    lado = "DIREITA";
                }
            } else {
                if (centroPlayerY < centroStoneY) {
                    lado = "ABAIXO";
                } else {
                    lado = "ACIMA";
                }
            }

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
                    personagem.setPosY(y + altura + 0.1f);
                    estaRolando = false;
                    break;
                case "ABAIXO":
                    // Bloqueio inferior compensando possíveis offsets do eixo Y
                    personagem.setPosY(y - caixaPlayer.height - 2.1f);
                    estaRolando = false;
                    break;
            }

            // 3. Execução do movimento físico da pedra
            if (estaRolando) {
                float novaX = x + direcao * 150 * delta;

                if (colisao.podeMoverX(caixaPedra, novaX)) {
                    x = novaX;

                    // O jogador acompanha a pedra mantendo a distância exata da sua hitbox
                    if (lado.equals("ESQUERDA")) {
                        personagem.setPosX(x - caixaPlayer.width - 2.1f);
                    } else if (lado.equals("DIREITA")) {
                        personagem.setPosX(x + largura - 1.9f);
                    }
                } else {
                    estaRolando = false;

                    // Se a pedra colidir com a parede, o jogador é bloqueado sem atravessar
                    if (lado.equals("ESQUERDA")) {
                        personagem.setPosX(x - caixaPlayer.width - 2.1f);
                    } else if (lado.equals("DIREITA")) {
                        personagem.setPosX(x + largura - 1.9f);
                    }
                }
            }
        } else {
            estaRolando = false;
        }
    }

    @Override
    public void render(OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.ORANGE);
        shapeRenderer.rect(x, y, largura, altura);
        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
