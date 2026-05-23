package io.github.relichunter.entidades;

public abstract class ObjetoJogo {
    protected float y;
    protected float x;
    protected float largura;
    protected float altura;

    public abstract void update(float delta);
    public abstract void render();

}
