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
    private MapaTeste mapa;
    private PersonagemTeste personagem;
    private final Rectangle caixaPedra = new Rectangle();
    private int alturaVirtual;

    public PedraEmpurravel(float x, float y, float largura, float altura, MapaTeste mapa, PersonagemTeste personagem, int alturaVirtual) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.estaRolando = false;
        this.direcao = 0;
        this.mapa = mapa;
        this.personagem = personagem;
        this.shapeRenderer = new ShapeRenderer();
        this.alturaVirtual = alturaVirtual;
    }

    @Override
    public void update(float delta) {
        caixaPedra.set(x, y, largura, altura);

        if (caixaPedra.overlaps(personagem.getCaixaPersonagem())) {
            // Centro do personagem e da pedra
            float centroPersonX = personagem.getPosX() + personagem.getLargura() / 2f;
            float centroPersonY = personagem.getPosY() + 14f; // metade da caixa (28/2)
            float centroPedraX  = x + largura / 2f;
            float centroPedraY  = y + altura / 2f;

            // Diferença entre centros
            float dx = centroPersonX - centroPedraX;
            float dy = centroPersonY - centroPedraY;

            // Soma das metades (distância necessária para não haver sobreposição)
            float combinadoX = personagem.getLargura() / 2f + largura / 2f;
            float combinadoY = 14f + altura / 2f;

            // Sobreposição real em cada eixo
            float overlapX = combinadoX - Math.abs(dx);
            float overlapY = combinadoY - Math.abs(dy);

            if (overlapX < overlapY) {
                // Colisão lateral → empurra no X e rola a pedra
                if (dx < 0) {
                    System.out.println("ESQUERDA");
                    // Personagem à esquerda → pedra vai pra direita
                    direcao = 1;
                    personagem.setPosX(x - personagem.getLargura());
                } else {
                    System.out.println("DIREITA");
                    // Personagem à direita → pedra vai pra esquerda
                    direcao = -1;
                    personagem.setPosX(x + largura);
                }
                estaRolando = true;
            } else {
                // Colisão vertical → bloqueia sem rolar
                estaRolando = false;
                if (dy > 0) {
                    System.out.println("ACIMA");
                    // Personagem acima da pedra
                    personagem.setPosY(y + altura);
                } else {
                    System.out.println("ABAIXO");
                    // Personagem abaixo da pedra

                    y += altura;
                    personagem.setPosY(y - altura);
                }
                estaRolando = true;
            }
        } else {
            estaRolando = false; // para quando o personagem sai do alcance
        }

        if (estaRolando) {
            float novaX = x + direcao * 150 * delta;

            // Checa a borda da frente da pedra antes de mover
            int colunaFrente = (int) ((novaX + (direcao > 0 ? largura : 0)) / MapaTeste.TAMANHO_BLOCO);
            int linha = (int) ((alturaVirtual - y) / MapaTeste.TAMANHO_BLOCO);

            if (mapa.isEspacoLivre(colunaFrente, linha)) {
                x = novaX; // só move se o espaço for livre
            } else {
                estaRolando = false; // para sem atravessar a parede
            }
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
