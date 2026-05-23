package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.relichunter.screens.MapaTeste;
import io.github.relichunter.screens.PersonagemTeste;

public class PedraQueCai extends Obstaculo{
    private ShapeRenderer shapeRenderer;
    private PersonagemTeste personagem;
    private int blocoAnterior;
    private boolean estaCaindo;
    private int intervaloDaPedraQueCai;
    public PedraQueCai(float x, float y, float largura, float altura, PersonagemTeste personagem) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.estaCaindo = false;
        this.intervaloDaPedraQueCai = 0;
        this.personagem = personagem;
        this.blocoAnterior = (int) (personagem.getPosX() / MapaTeste.TAMANHO_BLOCO);
        this.shapeRenderer = new ShapeRenderer();
    }
    @Override
    public void update(float delta){
        int blocoAtual = (int) (personagem.getPosX() / MapaTeste.TAMANHO_BLOCO);
        if (blocoAtual != blocoAnterior){
            intervaloDaPedraQueCai = intervaloDaPedraQueCai + 1;
            blocoAnterior = blocoAtual;
        }
        if (intervaloDaPedraQueCai >= 4) {
            estaCaindo = true;
            intervaloDaPedraQueCai = 0;
        }
        if (estaCaindo == true){
            y -= 150 * delta;
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

