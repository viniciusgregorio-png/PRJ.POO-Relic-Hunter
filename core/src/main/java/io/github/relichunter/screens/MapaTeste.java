package io.github.relichunter.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MapaTeste {

    private final Texture chao;
    private final Texture parede;

    public static final int TAMANHO_BLOCO = 32;

    private final int[][] matriz = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1},
        {1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1},
        {1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1},
        {1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public MapaTeste() {
        chao = new Texture("chao.png");
        parede = new Texture("parede.png");
    }

    public void desenhar(SpriteBatch batch, int alturaJanela) {
        for (int linha = 0; linha < matriz.length; linha++) {
            for (int coluna = 0; coluna < matriz[linha].length; coluna++) {

                int xPixel = coluna * TAMANHO_BLOCO;
                int yPixel = alturaJanela - ((linha + 1) * TAMANHO_BLOCO);

                if (matriz[linha][coluna] == 1) {
                    batch.draw(parede, xPixel, yPixel, TAMANHO_BLOCO, TAMANHO_BLOCO);
                } else {
                    batch.draw(chao, xPixel, yPixel, TAMANHO_BLOCO, TAMANHO_BLOCO);
                }
            }
        }
    }

    public boolean isEspacoLivre(int coluna, int linha) {
        if (linha < 0 || linha >= matriz.length || coluna < 0 || coluna >= matriz[0].length) {
            return false;
        }
        return matriz[linha][coluna] == 0;
    }

    public int getQuantidadeLinhas() {
        return matriz.length;
    }

    public void dispose() {
        chao.dispose();
        parede.dispose();
    }
}
