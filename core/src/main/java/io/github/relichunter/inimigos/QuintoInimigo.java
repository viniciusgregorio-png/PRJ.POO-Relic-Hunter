package io.github.relichunter.inimigos;

import io.github.relichunter.screens.MapaTeste;

public class QuintoInimigo extends InimigoBase {

    public QuintoInimigo(int forca, int vida, float x, float y, float limiteW, float limiteH, MapaTeste mapa) {
        super(forca, vida, x, y, limiteW, limiteH, mapa);
        direcaoX = 0; direcaoY = 1;
    }

    @Override
    protected void novaDirecao() {
        if (contadorDirecao == 0) {
            direcaoY = -1; contadorDirecao = 1;
        } else {
            direcaoY =  1; contadorDirecao = 0;
        }
        direcaoX = 0;
    }

    @Override
    public void update(float delta) {
        float novoY = y + direcaoY * speed * delta;

        if (colideComMapa(x, novoY)) {
            novaDirecao();
        } else {
            y = novoY;
        }

        if (y < 0)            { y = 0;            novaDirecao(); }
        if (y + 32 > limiteH) { y = limiteH - 32; novaDirecao(); }

        atualizarAnimacao(delta);
    }
}
