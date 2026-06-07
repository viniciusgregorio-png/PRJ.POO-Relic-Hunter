package io.github.relichunter.inimigos;

import io.github.relichunter.screens.MapaTeste;

public class PrimeiroInimigo extends InimigoBase {

    public PrimeiroInimigo(int forca, int vida, float x, float y,
                           float limiteW, float limiteH, MapaTeste mapa) {
        super(forca, vida, x, y, limiteW, limiteH, mapa);
        direcaoX = 1; direcaoY = 0;
    }

    @Override
    protected void novaDirecao() {
        if (contadorDirecao == 0) {
            direcaoX = -1; contadorDirecao = 1;
        } else {
            direcaoX =  1; contadorDirecao = 0;
        }
        direcaoY = 0;
    }

    @Override
    public void update(float delta) {
        float novoX = x + direcaoX * speed * delta;

        if (colideComMapa(novoX, y)) {
            x = x - direcaoX;
            novaDirecao();
        } else {
            x = novoX;
        }

        if (x < 0)            { x = 0;            novaDirecao(); }
        if (x + 32 > limiteW) { x = limiteW - 32; novaDirecao(); }

        atualizarAnimacao(delta);

        if (direcaoX < 0 && !frameAtual.isFlipX()) frameAtual.flip(true, false);
        if (direcaoX > 0 &&  frameAtual.isFlipX()) frameAtual.flip(true, false);
    }
}
