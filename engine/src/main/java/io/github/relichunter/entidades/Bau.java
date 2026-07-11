package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.MapaTeste;
import io.github.relichunter.screens.PersonagemTeste;

public class Bau extends ObjetoJogo {
    private boolean estaVisivel = false;
    private boolean foiAberto;
    private PersonagemTeste personagem;

    private SpriteBatch spriteBatch;
    private Texture spriteSheet;
    private TextureRegion bauFechado;
    private Animation<TextureRegion> animacaoAbertura;
    private TextureRegion frameAtual;
    private float stateTime;

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
        this.spriteSheet = new Texture("mapa/BauGR.png");

        // DEBUG - pode remover depois de confirmar as dimensões
        System.out.println("BauGR -> Largura: " + spriteSheet.getWidth() + " | Altura: " + spriteSheet.getHeight());

        int colunas = 3;
        int linhas = 4;
        int larguraFrame = spriteSheet.getWidth() / colunas;
        int alturaFrame = spriteSheet.getHeight() / linhas;

        // DEBUG - pode remover depois de confirmar as dimensões
        System.out.println("Frame -> Largura: " + larguraFrame + " | Altura: " + alturaFrame);

        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, larguraFrame, alturaFrame);

        // Frames de abertura (coluna 0), de cima para baixo: fechado -> abrindo -> aberto
        TextureRegion[] framesAbertura = new TextureRegion[] {
            tmp[0][0], tmp[1][0], tmp[2][0], tmp[3][0]
        };

        this.bauFechado = framesAbertura[0];
        this.animacaoAbertura = new Animation<>(0.15f, framesAbertura);
        this.animacaoAbertura.setPlayMode(Animation.PlayMode.NORMAL); // toca uma vez e fica no último frame

        this.frameAtual = bauFechado;
        this.stateTime = 0f;
    }

    @Override
    public void update(float delta) {
        if (!foiAberto) {
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

                if (caixaBau.overlaps(personagem.getCaixaPersonagem()) && chave.isFoiColetado()) {
                    foiAberto = true;
                    stateTime = 0f; // inicia a animação de abertura do zero
                }
            }

            frameAtual = bauFechado;
        } else {
            caixaBau.set(x, y, largura, altura);
            stateTime += delta;
            frameAtual = animacaoAbertura.getKeyFrame(stateTime, false); // false = não dá loop, fica no último frame
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
