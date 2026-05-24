package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.relichunter.screens.MapaTeste;
import io.github.relichunter.screens.PersonagemTeste;

public class PedraQueCai extends Obstaculo{
    private ShapeRenderer shapeRenderer;
    private PersonagemTeste personagem;
    private boolean estaCaindo;
    private MapaTeste mapa;
    public PedraQueCai(float x, float y, float largura, float altura, PersonagemTeste personagem, MapaTeste mapa) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.estaCaindo = false;
        this.personagem = personagem;
        this.shapeRenderer = new ShapeRenderer();
        this.mapa = mapa;
    }
    @Override
    public void update(float delta){
        int colunaPedra = (int) (x / MapaTeste.TAMANHO_BLOCO);
        int colunaPersonagem = (int) (personagem.getPosX() / MapaTeste.TAMANHO_BLOCO);
        if (colunaPedra == colunaPersonagem) {
            estaCaindo = true;
        }
        if (estaCaindo) {
            y -= 150 * delta;

            int coluna = (int) (x / MapaTeste.TAMANHO_BLOCO);
            int linhaAbaixo = (int) ((y - 1) / MapaTeste.TAMANHO_BLOCO);

            if (!mapa.isEspacoLivre(coluna, linhaAbaixo)) {
                estaCaindo = false;
            }
        }
    }


    @Override
    public void render() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, largura, altura);
        shapeRenderer.end();

    }
    public void dispose(){
        shapeRenderer.dispose();
    }

}

