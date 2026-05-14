package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("logoRelic.png");
    }

    @Override
    public void render() {
        // Limpa a tela com preto para vermos as bordas
        ScreenUtils.clear(0, 0, 0, 1f);

        // Pegamos o tamanho atual da janela do jogo
        int larguraJanela = com.badlogic.gdx.Gdx.graphics.getWidth();
        int alturaJanela = com.badlogic.gdx.Gdx.graphics.getHeight();

        batch.begin();

        // O segredo está aqui: passamos 5 parâmetros para o draw
        // image, x, y, largura, altura
        batch.draw(image, 0, 0, larguraJanela, alturaJanela);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
