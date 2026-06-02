package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.PersonagemTeste;

public class Rubi extends Item {
    private boolean foiColetado;
    private ShapeRenderer shapeRenderer;
    private Rectangle caixaRubi = new Rectangle();
    private PersonagemTeste personagem;

    public Rubi(float x, float y, float largura, float altura, PersonagemTeste personagem){
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.foiColetado = false;
        this.personagem = personagem;
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float delta) {
        if (foiColetado) return;

        caixaRubi.set(x + 6, y + 6, 16, 16);
        if (caixaRubi.overlaps(personagem.getCaixaPersonagem())){
            System.out.println("Rubi coletado! posX=" + personagem.getPosX() + " posY=" + personagem.getPosY());
            foiColetado = true;
        }
    }

    @Override
    public void render(OrthographicCamera camera) {
        if (!foiColetado) {
            shapeRenderer.setProjectionMatrix(camera.combined); // ✅ usa a câmera
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.PURPLE);
            shapeRenderer.rect(x, y, largura, altura);
            shapeRenderer.end();
        }
    }

    public boolean isFoiColetado(){
        return foiColetado;
    }

    public void dispose(){
        shapeRenderer.dispose();
    }
}
