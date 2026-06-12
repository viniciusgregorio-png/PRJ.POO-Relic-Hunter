package io.github.relichunter.entidades;

import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.MapaTeste;

public class GerenciadorColisao {

    private final MapaTeste mapa;
    private final int alturaVirtual;

    public GerenciadorColisao(MapaTeste mapa, int alturaVirtual) {
        this.mapa = mapa;
        this.alturaVirtual = alturaVirtual;
    }


    public boolean colideComMapa(Rectangle caixa) {
        int colunaEsq  = (int) (caixa.x / MapaTeste.TAMANHO_BLOCO);
        int colunaDir  = (int) ((caixa.x + caixa.width - 1) / MapaTeste.TAMANHO_BLOCO);
        int linhaBase  = (int) ((alturaVirtual - caixa.y) / MapaTeste.TAMANHO_BLOCO);
        int linhaTopo  = (int) ((alturaVirtual - (caixa.y + caixa.height - 1)) / MapaTeste.TAMANHO_BLOCO);

        for (int linha = linhaTopo; linha <= linhaBase; linha++) {
            for (int coluna = colunaEsq; coluna <= colunaDir; coluna++) {
                if (!mapa.isEspacoLivre(coluna, linha)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean podeMoverX(Rectangle caixa, float novaX) {
        Rectangle teste = new Rectangle(novaX, caixa.y, caixa.width, caixa.height);
        return !colideComMapa(teste);
    }


    public boolean podeMoverY(Rectangle caixa, float novaY) {
        Rectangle teste = new Rectangle(caixa.x, novaY, caixa.width, caixa.height);
        return !colideComMapa(teste);
    }

    public String ladoColisao(Rectangle a, Rectangle b) {
        if (!a.overlaps(b)) return "NENHUM";

        float centroAx = a.x + a.width  / 2f;
        float centroAy = a.y + a.height / 2f;
        float centroBx = b.x + b.width  / 2f;
        float centroBy = b.y + b.height / 2f;

        float dx = centroAx - centroBx;
        float dy = centroAy - centroBy;

        float combinadoX = a.width  / 2f + b.width  / 2f;
        float combinadoY = a.height / 2f + b.height / 2f;

        float overlapX = combinadoX - Math.abs(dx);
        float overlapY = combinadoY - Math.abs(dy);

        if (overlapX < overlapY) {
            return dx < 0 ? "ESQUERDA" : "DIREITA";
        } else {
            return dy > 0 ? "ACIMA" : "ABAIXO";
        }
    }
}
