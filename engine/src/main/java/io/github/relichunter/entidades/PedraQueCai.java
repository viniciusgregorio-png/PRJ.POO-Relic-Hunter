package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.relichunter.screens.MapaTeste;
import io.github.relichunter.screens.PersonagemTeste;

public class PedraQueCai extends Obstaculo {
    private ShapeRenderer shapeRenderer;
    private PersonagemTeste personagem;
    private boolean estaCaindo;
    private MapaTeste mapa;
    private int alturaVirtual;
    private boolean pedraAtiva = true;
    private float yLimite;
    private boolean colidiuComPlayer = false;

    public PedraQueCai(float x, float y, float largura, float altura, PersonagemTeste personagem, MapaTeste mapa, int alturaVirtual, float yLimite) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.estaCaindo = false;
        this.personagem = personagem;
        this.shapeRenderer = new ShapeRenderer();
        this.mapa = mapa;
        this.alturaVirtual = alturaVirtual;
        this.yLimite = yLimite;
    }

    @Override
    public void update(float delta) {
        if (!pedraAtiva) return;

        if (!estaCaindo) {
            int colunaPedra = (int) (x / MapaTeste.TAMANHO_BLOCO);
            int colunaPersonagem = (int) (personagem.getPosX() / MapaTeste.TAMANHO_BLOCO);

            if (colunaPedra == colunaPersonagem && personagem.getPosY() < y) {
                estaCaindo = true;
            }
        }

        if (estaCaindo) {
            y -= 300 * delta;

            if (x < personagem.getPosX() + personagem.getLargura() &&
                x + largura > personagem.getPosX() &&
                y < personagem.getPosY() + personagem.getAltura() &&
                y + altura > personagem.getPosY()) {
                personagem.morrer();
                colidiuComPlayer = true; // (Mantendo o personagem.morrer())
            }

            if (y <= yLimite) {
                pedraAtiva = false;
                estaCaindo = false;
            }
        }
    }

    @Override
    public void render(OrthographicCamera camera) {
        if (!pedraAtiva) return;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, largura, altura);
        shapeRenderer.end();
    }

    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }

    public boolean isColidiuComPlayer() {
        return colidiuComPlayer;
    }
}
