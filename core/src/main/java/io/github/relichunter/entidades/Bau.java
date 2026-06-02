package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.PersonagemTeste;


public class Bau extends Item{
    private boolean estaVisivel = false;
    private boolean foiAberto;
    private PersonagemTeste personagem;
    private ShapeRenderer shapeRenderer;
    private Rectangle caixaBau;
    private Rubi[] rubis;

    public Bau(float x, float y, float largura, float altura, PersonagemTeste personagem, Rubi[] rubis){
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.personagem = personagem;
        this.rubis = rubis;
        this.estaVisivel = false;
        this.foiAberto = false;
        this.shapeRenderer = new ShapeRenderer();
        this.caixaBau = new Rectangle();
    }

    @Override
    public void update(float delta) {
        int rubisColetados = 0;
        for (Rubi rubi : rubis) {
            if (rubi.isFoiColetado()){
                rubisColetados++;
            }
        }
        if (rubisColetados == 3){
            estaVisivel = true;
        }
        if (estaVisivel){
            caixaBau.set(x, y, largura, altura);
            System.out.println("Bau em x=" + x + " y=" + y +
                " | Personagem em x=" + personagem.getPosX() +
                " y=" + personagem.getPosY());
            if (caixaBau.overlaps(personagem.getCaixaPersonagem())){
                foiAberto = true;
            }
        }
    }

    @Override
    public void render(OrthographicCamera camera) {
        if (estaVisivel && !foiAberto){
            shapeRenderer.setProjectionMatrix(camera.combined); // ✅ usa a câmera
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.rect(x, y, largura, altura);
            shapeRenderer.end();
        }
    }

    public void dispose(){
        shapeRenderer.dispose();
    }
}
