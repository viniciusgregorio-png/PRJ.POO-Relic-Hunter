package io.github.relichunter.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MapaTeste {
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapaRenderer;
    private TiledMapTileLayer camadaColisao;

    public static final int TAMANHO_BLOCO = 32;

    public MapaTeste() {
        this.tiledMap = new TmxMapLoader().load("mapa.tmx");
        this.mapaRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        this.camadaColisao = (TiledMapTileLayer) tiledMap.getLayers().get("paredes");
    }

    public void render(OrthographicCamera camera) {
        mapaRenderer.setView(camera);
        mapaRenderer.render();
    }

    public boolean isEspacoLivre(int coluna, int javaLinha) {
        if (camadaColisao == null) return true;

        if (coluna < 0 || coluna >= camadaColisao.getWidth() || javaLinha < 0 || javaLinha >= camadaColisao.getHeight()) {
            return false;
        }

        TiledMapTileLayer.Cell celula = camadaColisao.getCell(coluna, javaLinha);

        return celula == null;
    }

    public int getQuantidadeLinhas() {
        return camadaColisao != null ? camadaColisao.getHeight() : 40;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void dispose() {
        tiledMap.dispose();
        mapaRenderer.dispose();
    }
}
