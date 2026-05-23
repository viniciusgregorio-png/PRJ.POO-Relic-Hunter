package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.Color;
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
    public PedraEmpurravel (float x, float y, float largura, float altura, MapaTeste mapa, PersonagemTeste personagem){
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.estaRolando = false;
        this.direcao = 0;
        this.mapa = mapa;
        this.personagem = personagem;
        this.shapeRenderer = new ShapeRenderer();

    }

    @Override
    public void update(float delta) {
        caixaPedra.set(x, y, largura, altura);
        if (caixaPedra.overlaps(personagem.getCaixaPersonagem())) {
            if (personagem.getPosX() < x) {
                direcao = 1;
            } else {
                direcao = -1;
            }
            estaRolando = true;
        }
        if (estaRolando) {
            x += direcao *150 * delta;
            int coluna = (int) (x / MapaTeste.TAMANHO_BLOCO);
            int linha = (int) (y / MapaTeste.TAMANHO_BLOCO);
                if (!mapa.isEspacoLivre(coluna, linha)){
                     estaRolando = false;
            }
        }
    }
    @Override
    public void render() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.ORANGE);
        shapeRenderer.rect(x, y, largura, altura);
        shapeRenderer.end();

    }
    public void dispose() {
        shapeRenderer.dispose();
    }
}
