package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.MapaTeste;
import io.github.relichunter.screens.PersonagemTeste;

public class Bau extends Item {
    private boolean estaVisivel = false;
    private boolean foiAberto;
    private PersonagemTeste personagem;

    private SpriteBatch spriteBatch;
    private Texture spriteSheet;
    private TextureRegion bauFechado;
    private TextureRegion bauAberto;
    private TextureRegion frameAtual;

    private Rectangle caixaBau;
    private Rubi[] rubisFase1;
    private Chave chave;

    public Bau(float x, float y, float largura, float altura, PersonagemTeste personagem, Rubi[] rubisFase1, Chave chave){
        this.x = Math.round(x / MapaTeste.TAMANHO_BLOCO) * MapaTeste.TAMANHO_BLOCO;
        this.y = Math.round(y / MapaTeste.TAMANHO_BLOCO) * MapaTeste.TAMANHO_BLOCO;
        this.largura = largura;
        this.altura = altura;
        this.personagem = personagem;
        this.rubisFase1 = rubisFase1;
        this.chave = chave;
        this.estaVisivel = false;
        this.foiAberto = false;
        this.caixaBau = new Rectangle();

        this.spriteBatch = new SpriteBatch();
        // Corrigido: nome do arquivo é "chests_byBatuhanK.png" (a textura "BauGR.png"
        // tinha o caminho com letra errada e dimensões que não dividiam certo em frames).
        this.spriteSheet = new Texture("assets/mapa/chests_byBatuhanK.png");

        // Spritesheet de 320x96 -> 8 colunas x 2 linhas de 40x48 cada
        int colunas = 8;
        int linhas = 2;
        int larguraFrame = spriteSheet.getWidth() / colunas;   // 40
        int alturaFrame = spriteSheet.getHeight() / linhas;    // 48

        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, larguraFrame, alturaFrame);

        // Linha 0 = baús fechados, Linha 1 = baús abertos. Usamos a coluna 0 (madeira marrom).
        this.bauFechado = tmp[0][0];
        this.bauAberto  = tmp[1][0];
        this.frameAtual = bauFechado;
    }

    @Override
    public void update(float delta) {
        if (!foiAberto) {
            // O baú só se torna visível quando os 5 rubis da fase 1 forem coletados
            boolean todosRubisFase1Coletados = true;
            for (Rubi rubi : rubisFase1) {
                if (!rubi.isFoiColetado()) {
                    todosRubisFase1Coletados = false;
                    break;
                }
            }

            if (todosRubisFase1Coletados) {
                estaVisivel = true;
                chave.setVisivel(true);
            }

            if (estaVisivel) {
                caixaBau.set(x, y, largura, altura);

                // O baú só abre se o jogador encostar nele E já tiver coletado a chave
                if (caixaBau.overlaps(personagem.getCaixaPersonagem()) && chave.isFoiColetado()) {
                    foiAberto = true;
                    frameAtual = bauAberto;
                }
            }
        } else {
            caixaBau.set(x, y, largura, altura);
            if (frameAtual != bauAberto) {
                frameAtual = bauAberto;
            }
        }
    }

    @Override
    public void render(OrthographicCamera camera) {
        if (estaVisivel) {
            spriteBatch.setProjectionMatrix(camera.combined);
            spriteBatch.begin();
            spriteBatch.draw(frameAtual, x, y, largura, altura);
            spriteBatch.end();
        }
    }

    public void dispose(){
        spriteBatch.dispose();
        spriteSheet.dispose();
    }

    public boolean isFoiAberto() {
        return foiAberto;
    }

    public boolean isEstaVisivel() {
        return estaVisivel;
    }
}
