package io.github.relichunter.entidades;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.github.relichunter.screens.MapaTeste;
import io.github.relichunter.screens.PersonagemTeste;

public class PedraEmpurravel {

    // Enumeração para controlar de forma limpa as ações da Pedra (Máquina de Estados)
    private enum Estado {
        PARADA,
        EMPURRADA, // Movimento horizontal de exatamente 1 bloco
        CAINDO     // Movimento vertical (gravidade) até encontrar o chão
    }

    private float x, y;
    private final float largura, altura;

    // Ajuste de velocidades
    private final float VELOCIDADE_EMPURRAO = 150f;
    private final float VELOCIDADE_QUEDA = 300f;

    private int direcaoX = 0; // -1 Esquerda, 1 Direita, 0 Parada
    private Estado estadoAtual = Estado.PARADA;

    // Coordenadas que servem de destino (Grid-Lock)
    private float xAlvo;
    private float yAlvo;

    private final MapaTeste mapa;
    private final PersonagemTeste personagem;
    private final float alturaVirtual;

    private final Rectangle caixaPedra = new Rectangle();
    private final Rectangle caixaPlayer = new Rectangle();
    private final ShapeRenderer shapeRenderer;

    public PedraEmpurravel(float x, float y, float largura, float altura, MapaTeste mapa, PersonagemTeste personagem, float alturaVirtual) {
        // Alinha a pedra perfeitamente ao Grid de 32x32 do mapa logo no spawn
        this.x = Math.round(x / MapaTeste.TAMANHO_BLOCO) * MapaTeste.TAMANHO_BLOCO;
        this.y = Math.round(y / MapaTeste.TAMANHO_BLOCO) * MapaTeste.TAMANHO_BLOCO;

        this.largura = largura;
        this.altura = altura;
        this.mapa = mapa;
        this.personagem = FitsPersonagem(personagem);
        this.alturaVirtual = alturaVirtual;

        this.caixaPedra.set(this.x, this.y, largura, altura);
        this.shapeRenderer = new ShapeRenderer();

        this.xAlvo = this.x;
        this.yAlvo = this.y;
    }

    private PersonagemTeste FitsPersonagem(PersonagemTeste p) {
        return p;
    }

    public void update(float delta) {
        // Atualiza a referência da caixa do jogador
        caixaPlayer.set(personagem.getCaixaPersonagem());

        switch (estadoAtual) {
            case PARADA:
                // Se está imóvel, primeiro verifica se há solo firme abaixo
                if (verificarAbaixoLivre()) {
                    iniciarQueda();
                } else {
                    // Se estiver no chão firme, aceita empurrões do jogador
                    checarInteracaoJogador();
                }
                break;

            case EMPURRADA:
                atualizarEmpurrao(delta);
                break;

            case CAINDO:
                atualizarQueda(delta);
                break;
        }

        // Mantém a hitbox da pedra perfeitamente emparelhada com as coordenadas visuais
        caixaPedra.setPosition(x, y);
    }

    /**
     * Gerencia a aproximação e colisão física estática do jogador contra a pedra.
     */
    private void checarInteracaoJogador() {
        if (caixaPedra.overlaps(caixaPlayer)) {
            String lado = calcularLadoColisao();

            switch (lado) {
                case "ESQUERDA":
                    // Jogador empurrando para a DIREITA
                    float alvoDireita = x + MapaTeste.TAMANHO_BLOCO;
                    if (!colideComMapa(alvoDireita, y)) {
                        xAlvo = alvoDireita;
                        direcaoX = 1;
                        estadoAtual = Estado.EMPURRADA;
                    } else {
                        // Se houver parede atrás da pedra, ela barra o jogador (colisão sólida)
                        bloquearJogadorEsquerda();
                    }
                    break;

                case "DIREITA":
                    // Jogador empurrando para a ESQUERDA
                    float alvoEsquerda = x - MapaTeste.TAMANHO_BLOCO;
                    if (!colideComMapa(alvoEsquerda, y)) {
                        xAlvo = alvoEsquerda;
                        direcaoX = -1;
                        estadoAtual = Estado.EMPURRADA;
                    } else {
                        // Se houver parede atrás da pedra, ela barra o jogador (colisão sólida)
                        bloquearJogadorDireita();
                    }
                    break;

                case "ACIMA":
                    // Jogador caminhando em cima da pedra (atua como plataforma sólida)
                    personagem.setY(y + altura - 3.9f);
                    break;

                case "ABAIXO":
                    // Jogador cabeceando a pedra por baixo
                    personagem.setY(y - personagem.getAltura() - 4.1f);
                    break;
            }
        }
    }

    /**
     * Executa o deslocamento lateral e para de forma precisa no próximo slot de bloco.
     */
    private void atualizarEmpurrao(float delta) {
        float passo = direcaoX * VELOCIDADE_EMPURRAO * delta;
        x += passo;

        // Verifica se ultrapassou ou chegou na posição alvo do grid
        if ((direcaoX == 1 && x >= xAlvo) || (direcaoX == -1 && x <= xAlvo)) {
            x = xAlvo;
            estadoAtual = Estado.PARADA;
            direcaoX = 0;
        }

        // Mantém o jogador empurrando visualmente colado à pedra
        if (caixaPedra.overlaps(caixaPlayer)) {
            if (direcaoX == 1) {
                bloquearJogadorEsquerda();
            } else if (direcaoX == -1) {
                bloquearJogadorDireita();
            }
        }
    }

    /**
     * Inicia a queda vertical calculando a altura alvo.
     */
    private void iniciarQueda() {
        yAlvo = y - MapaTeste.TAMANHO_BLOCO;
        estadoAtual = Estado.CAINDO;
    }

    /**
     * Move a pedra para baixo sob gravidade e checa mortes por esmagamento.
     */
    private void atualizarQueda(float delta) {
        y -= VELOCIDADE_QUEDA * delta;
        caixaPedra.setPosition(x, y); // Atualiza temporariamente para o cálculo preciso de colisão

        // VERIFICAÇÃO DE COLLISÃO DURANTE A QUEDA:
        if (caixaPedra.overlaps(caixaPlayer)) {
            // Calcula as caixas de colisão de forma precisa e isolada
            float playerEsquerda = caixaPlayer.x;
            float playerDireita = caixaPlayer.x + caixaPlayer.width;
            float playerTopo = caixaPlayer.y + caixaPlayer.height;

            float pedraEsquerda = x;
            float pedraDireita = x + largura;
            float pedraBase = y;

            // 1. Só há esmagamento real se a base da pedra estiver acima do "meio" do jogador
            // e se o jogador estiver significativamente alinhado abaixo da pedra (com tolerância de 6 pixels)
            float margemX = 6f;
            boolean estaAlinhadoX = (playerDireita - margemX > pedraEsquerda) && (playerEsquerda + margemX < pedraDireita);
            boolean estaAbaixoDaPedra = playerTopo > pedraBase && caixaPlayer.y < pedraBase;

            if (estaAlinhadoX && estaAbaixoDaPedra) {
                // Esmagamento real! O jogador estava exatamente por baixo
                personagem.morrer();
            } else {
                // Colisão estritamente lateral enquanto a pedra cai (evita clipping e mortes injustas)
                float centroPedraX = x + largura / 2f;
                float centroPlayerX = caixaPlayer.x + caixaPlayer.width / 2f;

                if (centroPlayerX < centroPedraX) {
                    // Jogador está na esquerda da pedra, empurra-o ligeiramente para trás
                    bloquearJogadorEsquerda();
                } else {
                    // Jogador está na direita da pedra, empurra-o ligeiramente para a direita
                    bloquearJogadorDireita();
                }
            }
        }

        // Se alcançou ou passou da altura alvo
        if (y <= yAlvo) {
            y = yAlvo;
            estadoAtual = Estado.PARADA; // Volta a ficar parada para verificar se cai mais ou se estabelece
        }
    }

    /**
     * Verifica se o espaço abaixo do bloco está livre de paredes no mapa.
     */
    private boolean verificarAbaixoLivre() {
        float yAbaixo = y - MapaTeste.TAMANHO_BLOCO;
        return !colideComMapa(x, yAbaixo);
    }

    /**
     * Bloqueios físicos milimetricamente calculados de acordo com os offsets do seu PersonagemTeste
     */
    private void bloquearJogadorEsquerda() {
        personagem.setX(x - personagem.getLargura() - 4.1f);
    }

    private void bloquearJogadorDireita() {
        personagem.setX(x + largura - 3.9f);
    }

    /**
     * Define de qual direção o jogador encostou na pedra.
     */
    private String calcularLadoColisao() {
        float centroPedraX = x + largura / 2f;
        float centroPedraY = y + altura / 2f;
        float centroPlayerX = caixaPlayer.x + caixaPlayer.width / 2f;
        float centroPlayerY = caixaPlayer.y + caixaPlayer.height / 2f;

        float dx = centroPlayerX - centroPedraX;
        float dy = centroPlayerY - centroPedraY;

        if (Math.abs(dx) > Math.abs(dy)) {
            return (dx > 0) ? "DIREITA" : "ESQUERDA";
        } else {
            return (dy > 0) ? "ACIMA" : "ABAIXO";
        }
    }

    /**
     * Sistema de Colisão Otimizado (Tile-Based).
     */
    private boolean colideComMapa(float px, float py) {
        float margem = 1f;
        float x1 = px + margem;
        float y1 = py + margem;
        float x2 = px + largura - margem;
        float y2 = py + altura - margem;

        // Converte as extremidades da caixa da pedra em coordenadas de Tile
        int tileX1 = (int) (x1 / MapaTeste.TAMANHO_BLOCO);
        int tileY1 = (int) (y1 / MapaTeste.TAMANHO_BLOCO);
        int tileX2 = (int) (x2 / MapaTeste.TAMANHO_BLOCO);
        int tileY2 = (int) (y2 / MapaTeste.TAMANHO_BLOCO);

        int larguraMapa = mapa.getQuantidadeColunas();
        int alturaMapa = mapa.getQuantidadeLinhas();

        // Varre apenas os blocos que a pedra ocupa fisicamente no momento
        for (int linha = tileY1; linha <= tileY2; linha++) {
            for (int coluna = tileX1; coluna <= tileX2; coluna++) {
                // Proteção contra leitura fora dos limites do mapa (considerado parede)
                if (coluna < 0 || coluna >= larguraMapa || linha < 0 || linha >= alturaMapa) {
                    return true;
                }
                if (!mapa.isEspacoLivre(coluna, linha)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void render(OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.9f, 0.5f, 0.0f, 1.0f); // Cor Laranja de Teste
        shapeRenderer.rect(x, y, largura, altura);
        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
