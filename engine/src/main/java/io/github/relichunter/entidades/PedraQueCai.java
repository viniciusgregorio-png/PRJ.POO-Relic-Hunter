package io.github.relichunter.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.relichunter.screens.MapaTeste;
import io.github.relichunter.screens.PersonagemTeste;

public class PedraQueCai extends ObjetoJogo {
    private PersonagemTeste personagem;
    private boolean estaCaindo;
    private MapaTeste mapa;
    private int alturaVirtual;
    private boolean pedraAtiva = true;
    private float yLimite;
    private boolean colidiuComPlayer = false;

    private Texture texturaPedra;
    private Animation<TextureRegion> animacaoRolando;
    private TextureRegion frameParado;
    private SpriteBatch spriteBatch;
    private float tempoAnimacao;

    public PedraQueCai(float x, float y, float largura, float altura, PersonagemTeste personagem, MapaTeste mapa, int alturaVirtual, float yLimite) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.estaCaindo = false;
        this.personagem = personagem;
        this.mapa = mapa;
        this.alturaVirtual = alturaVirtual;
        this.yLimite = yLimite;

        this.spriteBatch = new SpriteBatch();
        this.texturaPedra = new Texture(Gdx.files.internal("assets/mapa/pedra.png"));

        int colunas = 6;
        int linhas = 1;
        TextureRegion[][] tmp = TextureRegion.split(
            texturaPedra,
            texturaPedra.getWidth() / colunas,
            texturaPedra.getHeight() / linhas
        );

        this.frameParado = tmp[0][0];

        TextureRegion[] framesMovimento = new TextureRegion[4];
        for (int j = 0; j < 4; j++) {
            framesMovimento[j] = tmp[0][j];
        }

        this.animacaoRolando = new Animation<>(0.1f, framesMovimento);
        this.animacaoRolando.setPlayMode(Animation.PlayMode.LOOP);
        this.tempoAnimacao = 0f;
    }

    @Override
    public void update(float delta) {
        if (!pedraAtiva) return;

        if (estaCaindo) {
            tempoAnimacao += delta;
        } else {
            tempoAnimacao = 0f;
        }

        if (!estaCaindo) {
            int colunaPedra = (int) (x / MapaTeste.TAMANHO_BLOCO);
            int colunaPersonagem = (int) (personagem.getPosX() / MapaTeste.TAMANHO_BLOCO);

            if (colunaPedra == colunaPersonagem && personagem.getPosY() < y) {
                float distanciaY = y - personagem.getPosY();
                float distanciaMaximaTrigger = MapaTeste.TAMANHO_BLOCO * 7f;

                if (distanciaY <= distanciaMaximaTrigger) {
                    estaCaindo = true;
                }
            }
        }

        if (estaCaindo) {
            y -= 300 * delta;

            if (x < personagem.getPosX() + personagem.getLargura() &&
                x + largura > personagem.getPosX() &&
                y < personagem.getPosY() + personagem.getAltura() &&
                y + altura > personagem.getPosY()) {
                personagem.morrer();
                colidiuComPlayer = true;
            }

            if (y <= yLimite) {
                pedraAtiva = false;
                estaCaindo = false;
            }
        }
    }

    @Override
    public void render(OrthographicCamera camera) {
        if (!pedraAtiva) return;

        TextureRegion frameAtual;
        if (!estaCaindo) {
            frameAtual = frameParado;
        } else {
            frameAtual = animacaoRolando.getKeyFrame(tempoAnimacao);
        }

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(frameAtual, x, y, largura, altura);
        spriteBatch.end();
    }

    public void dispose() {
        if (texturaPedra != null) {
            texturaPedra.dispose();
        }
        if (spriteBatch != null) {
            spriteBatch.dispose();
        }
    }

    public boolean isColidiuComPlayer() {
        return colidiuComPlayer;
    }
}
