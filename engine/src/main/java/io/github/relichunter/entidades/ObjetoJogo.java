package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.OrthographicCamera;

public abstract class ObjetoJogo {
    protected float y;
    protected float x;
    protected float largura;
    protected float altura;

    public abstract void update(float delta);
    public abstract void render(OrthographicCamera camera);
}
