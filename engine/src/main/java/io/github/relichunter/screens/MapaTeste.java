package io.github.relichunter.screens;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MapaTeste {
    public static final int TAMANHO_BLOCO = 32;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    public MapaTeste() {
        tiledMap = new TmxMapLoader().load("assets/mapa/mapa.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public void render(OrthographicCamera camera) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public boolean isParede(float x, float y) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("paredes");
        if (layer == null) return false;
        int tileX = (int) (x / TAMANHO_BLOCO);
        int tileY = (int) (y / TAMANHO_BLOCO);
        return layer.getCell(tileX, tileY) != null;
    }

    public boolean isEspacoLivre(int tileX, int tileY) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("paredes");
        return layer == null || layer.getCell(tileX, tileY) == null;
    }

    public int getQuantidadeLinhas() {
        return tiledMap.getProperties().get("height", Integer.class);
    }

    public int getQuantidadeColunas() {
        return tiledMap.getProperties().get("width", Integer.class);
    }

    public TiledMap getTiledMap() { return tiledMap; }
    public void dispose() { tiledMap.dispose(); }
}
