# Documentação do Projeto

# Visão Geral

Relic Hunter é um jogo 2D desenvolvido em Java utilizando a framework LibGDK. O jogador controla um personagem em um mapa baseado em tiles, coletando rubis, evitando inimigos e obstáculos, e interagindo com elementos como baús e pedras empurráveis e uma chave. O objetivo do jogo é dividido em duas fases:

1. Fase 1: Coletar 5 rubis espalhados pelo mapa. Ao coletá-los, uma chave aparece.
2. Fase 2: Coletar a chave e abrir o baú. Após abri-lo, 7 novos rubis surgem. Coletar todos eles completa o jogo.

# Estrutura do Projeto

io.github.relichunter/
├── Main.java                               # Classe principal do jogo
├── screens/
│   ├── SplashScreen.java                   # Tela inicial com menu e controle de áudio
│   ├── TelaTeste.java                      # Tela principal do jogo (gameplay)
│   ├── GameOverScreen.java                 # Tela de fim de jogo (morte)
│   ├── EndGameScreen.java                  # Tela de conclusão (vitória)
│   ├── MapaTeste.java                      # Gerenciamento do mapa Tiled
│   ├── PersonagemTeste.java                # Controla o jogador (movimento, animações, colisão)
│   └── GameScreen.java                     # (Não utilizado ativamente)
├── entidades/
│   ├── ObjetoJogo.java                     # Classe abstrata base para objetos do jogo
│   ├── Item.java                           # Subclasse abstrata para itens coletáveis
│   ├── Obstaculo.java                      # Subclasse abstrata para obstáculos
│   ├── Rubi.java                           # Item coletável (rubi)
│   ├── Chave.java                          # Item coletável (chave)
│   ├── Bau.java                            # Baú que só abre com a chave
│   ├── PedraEmpurravel.java                # Pedra que pode ser empurrada pelo jogador
│   ├── PedraQueCai.java                    # Pedra que cai quando o jogador se aproxima
│   └── GerenciadorColisao.java             # Utilitário para detectar colisões com o mapa
└── inimigos/
└── InimigoBase.java                    # Classe base para todos os tipos de inimigos

io.github.relichunter/
├── Lwjgl3Launcher.java                     # Ponto de entrada do jogo
└── StartupHelper.java                      # Utilitários de compatibilidade entre SOs

# Análise Detalhada das Classes

# 1. Main.java

Extende Game da LibGDX, responsável por iniciar o jogo e trocar de telas.

# 2. SplashScreen.java

Tela inicial com logo, música de fundo e botão de menu para iniciar o jogo e ajustar volume.

Principais imports:

- `com.badlogic.gdx.audio.Music` – para tocar música de fundo.
- `com.badlogic.gdx.graphics.glutils.ShapeRenderer` – para desenhar o slider de volume.
- `com.badlogic.gdx.graphics.g2d.BitmapFont` – para texto.

Métodos importantes:

- `render()`: desenha o logo, verifica cliques e desenha o menu se estiver aberto.
- `handleInput()`: detecta clique no ícone de engrenagem (abrir/fechar menu) e no slider de volume.
- `drawMenu()`: desenha o fundo semi-transparente, o slider e o texto "ÁUDIO".

# 3. TelaTeste.java

A tela principal do jogo. Carrega o mapa, o jogador, todos os inimigos, os rubis divididos em duas fases, a chave, o baú e as pedras. Controla a lógica de progressão e vitória/derrota.

Estrutura:

- `show()`: inicializa todos os recursos, carrega objetos do mapa via Tiled ou usa posições padrão. Cria duas listas de rubis: `listaRubisFase1` e `listaRubisFase2`. Instancia a `Chave` e o `Bau`.
- `render(delta)`: chama `update` de todos os componentes, verifica colisões com inimigos/pedras que caem, atualiza câmera, desenha tudo. Quando o baú foi aberto e todos os rubis da fase 2 foram coletados (`getTotalRubisColetadosFase2() == listaRubisFase2.size`), o jogo chama `EndGameScreen` passando o total geral de rubis coletados.
- `atualizarCameraSeguirPlayer()`: centraliza a câmera no personagem, respeitando os limites do mapa.
- `drawHud`: exibe botão de reset.
- `drawInfo()`: mostra contador de rubis.

Principais imports:

- `com.badlogic.gdx.utils.viewport.StretchViewport` – mantém a resolução virtual (1200x900) independente da janela, esticando a imagem.
- `com.badlogic.gdx.maps.MapLayer` e `MapObject` – para ler objetos do arquivo .tmx do Tiled.
- `com.badlogic.gdx.utils.Array` – uma lista otimizada da LibGDX para evitar overhead do Java padrão.

Métodos auxiliares importantes:

- `getTotalRubisColetadosFase1()` – retorna quantos rubis iniciais foram pegos.
- `getTotalRubisColetadosFase2()` – retorna quantos rubis finais foram pegos.
- `getTotalRubisColetados()` – soma dos dois.

Lógica de progressão e vitória:

1. Fase 1: Jogador coleta os 5 rubis da `listaRubisFase1`.
2. Quando o último rubi da fase 1 é coletado, o baú se torna visível e a chave aparece (`chave.setVisivel(true)`).
3. Jogador deve coletar a chave.
4. Com a chave, ao encostar no baú, ele abre (`bau.isFoiAberto() = true`).
5. Ao abrir o baú, os 7 rubis da `listaRubisFase2` passam a ser atualizados e renderizados.
6. Vitória: quando todos os 7 rubis da fase 2 forem coletados, o jogo chama `EndGameScreen(game, getTotalRubisColetados())`. `getTotalRubisColetados()` soma os coletados da fase 1 e fase 2. A tela de fim exibe de 0 a 3 estrelas com base no total.

# 4. MapaTeste.java

Gerencia o carregamento e renderização do mapa Tiled.

```java
public class MapaTeste {
    public static final int TAMANHO_BLOCO = 32;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
}
```

- `TiledMap`: carrega o arquivo `.tmx`.
- `OrthogonalTiledMapRenderer`: renderiza mapas ortogonais.
- `isParede(float x, float y)` e `isEspacoLivre(int tileX, int tileY)` verificam colisões com a camada "paredes". Isso é usado pelo jogador e pelas pedras.

# 5. PersonagemTeste.java

Controla o herói: movimento, animações, colisão com paredes.

Método `atualizar()`:

- Lê teclas W/A/S/D ou setas.
- Define `direcaoAtual` e `espelharX` para invertê-lo horizontalmente.
- Calcula movimento X e Y.
- Chama `detectarColisao()` para cada eixo separadamente.
- Atualiza animação baseado no estado `estaSeMovendo`.

O método `detectarColisao` implementa um sistema anti-travamento. Se o personagem já estava dentro de um bloco e o novo movimento reduz a sobreposição, o movimento é permitido para que ele possa sair.

`desenhar()`: Aplica escala e recuo centralizado. Isso foi feito para ajustar o tamanho do personagem após a troca de sprites de 32x32 para 64x64.

# 6. InimigoBase.java

Classe genérica para todos os inimigos: Cobra, Morcego, Aranha, Fogo.

Principais atributos:

- `tipoInimigo`: 1=Cobra, 2=Morcego, 3=Aranha, 4=Fogo.
- `tipoMovimento`: 0=Parado, 1=Horizontal, 2=Vertical.
- Velocidade fixa = 60f.

Construtor:

Carrega a spritesheet correta com base no tipo, recorta os frames e define dimensões de desenho.

`update()`:

- Atualiza animação.
- Se o movimento não for parado, tenta mover, se o próximo passo bater na parede (`mapa.isParede`), inverte a direção.
- Define a caixa de colisão.

`encostouNoPlayer()` verifica se o retângulo de colisão do inimigo sobrepõe o do jogador.

# 7 Rubi.java

Item coletável. Herda de `Item`.

- Usa `TextureRegion` para recortar um cristal roxo de uma spritesheet.
- Caixa de colisão é reduzida para dar tolerância.
- Quando coleta, `foiColetado = true` e para de renderizar.

# 8 Chave.java

```java
public class Chave extends Item {
    private boolean foiColetado;
    private boolean estaVisivel;
    private TextureRegion frameChave;
    private Rectangle caixaChave;
    private PersonagemTeste personagem;
}
```
- A chave é um item que não fica visível inicialmente (`estaVisivel = false`).
- O método `setVisivel(boolean)` é chamado pelo baú quando todos os rubis da Fase 1 são coletados, tornando a chave visível no mapa.
- Quando o jogador encosta na chave, `foiColetado = true`.
- A renderização só ocorre se `estaVisivel && !foiColetado`.

# 9 Bau.java

Baú que controla a progressão entre as fases do jogo. Ele só se torna visível após o jogador coletar todos os 5 rubis da Fase 1. Ao ficar visível, ele também torna a chave visível no mapa. O baú só pode ser aberto se o jogador já tiver coletado a chave.

- Carrega spritesheet `chests_byBatuhanK.png`. Frame fechado = `tmp[0][0]`, aberto = `tmp[1][0]`.
- `update(delta)`: Verifica se todos os rubis da Fase 1 foram coletados, percorre o array `rubisFase1` e checa `isFoiColetado()`. Se sim, define `estaVisivel = true` e chama `chave.setVisivel(true)`, a chave passa a ser renderizada e coletável.
- Quando o baú está visível, ele verifica colisão com o personagem.
- Se houver colisão e a chave já tiver sido coletada (`chave.isFoiColetado()`), o baú abre: `foiAberto = true` e o frame é trocado para `bauAberto`.
- Após aberto, o baú permanece aberto para sempre.

* Uso da chave: A abertura do baú depende obrigatoriamente da posse da chave. Isso impede que o jogador pule a etapa de coleta da chave.
* Efeito no jogo: Assim que o baú é aberto, os 7 rubis da Fase 2 tornam-se ativos no mapa, e o jogador pode coletá-los para concluir o jogo.

# 10 PedraEmpurravel.java

Pedra que o jogador pode empurrar para os lados e que também pode cair se não houver chão abaixo.

Estados: `PARADA`, `EMPURRADA`, `CAINDO`.

Lógica de empurrar:

- Quando o jogador colide com a pedra, `calcularLadoColisao()` determina se ele está à esquerda ou direita.
- Se houver espaço livre no chão para o próximo bloco naquela direção, a pedra inicia o estado `EMPURRADA` e desloca-se suavemente para `xAlvo`.
- Se o espaço estiver ocupado, o jogador é bloqueado.

Lógica de queda:

- A cada `update`, se a pedra está parada e o bloco abaixo dela estiver vazio (`verificarAbaixoLivre()`), inicia a queda.
- Durante a queda, se a pedra colidir com o jogador, ele morre (`personagem.morrer()`).
- A lógica de morte é diferente se o jogador estiver alinhado abaixo da pedra ou se for empurrado lateralmente.

`colideComMapa()` usa uma margem de 1px para evitar problemas de arredondamento.

# 11 PedraQueCai.java

Similar à empurrável, mas fixa no lugar até que o jogador se aproxime verticalmente na mesma coluna. Quando a distância vertical for menor que 7 blocos, ela cai.

Ativação: `colunaPedra == colunaPersonagem && personagem.getPosY() < y && distanciaY <= TAMANHO_BLOCO * 7f`.

Efeito: Se atingir o jogador, `GameOverScreen` é chamado.

`isColidiuComPlayer()` usada na `TelaTeste` para verificar se o jogador morreu.

# 12 ObjetoJogo e derivados

Abstração para padronizar `update` e `render` com câmera. `Item` e `Obstaculo` são subclasses apenas para categorização semântica.

## Explicação dos Imports Mais Relevantes

- `com.badlogic.gdx.graphics.OrthographicCamera`	Câmera 2D que define o que é visível na tela.
- `com.badlogic.gdx.graphics.g2d.SpriteBatch`	    Desenha texturas e regiões de textura de forma otimizada.
- `com.badlogic.gdx.graphics.g2d.TextureRegion`	    Representa uma subárea de uma textura.
- `com.badlogic.gdx.graphics.g2d.Animation`	        Gerencia sequências de frames para animação com velocidade configurável.
- `com.badlogic.gdx.math.Rectangle`	                Estrutura para representar caixas de colisão e realizar detecção `overlaps()`.
- `com.badlogic.gdx.maps.tiled.*`	                API para carregar e renderizar mapas no formato Tiled.
- `com.badlogic.gdx.utils.viewport.*`	            Controla como a câmera se adapta a diferentes resoluções de janela.
- `com.badlogic.gdx.Input e Input.Keys`	            Captura entradas do teclado e mouse.

### Relato dos Desenvolvedores

# Renan

Partes que fez:

- Mapa (integração com Tiled)
- Movimentação do player
- Sprites no geral
- Correção de hitboxes
- Correção de aspect ratio do personagem
- Sistema de animação idle/walk cycle
- Revisão de bugs, especialmente na lógica de colisão e no sistema anti-stuck

Maior dificuldade:

Atualização dos sprites do personagem antigo para o novo. A spritesheet nova carregava imagens em 64x64, enquanto o antigo era 32x32. Isso quebrou completamente a escala visual e as caixas de colisão. Foi necessário redimensionar o desenho e ajustar manualmente as coordenadas para que o personagem ainda parecesse natural no mapa.

Como resolveu:

Alterei o método `desenhar()` para aplicar escala e centralização, e mantive a hitbox física em 24x24 (indiferente ao tamanho visual). Também ajustei o alinhamento dos frames de animação para que o novo sprite coubesse visualmente sem cortar ou flutuar.

# Cayo

Partes que fez:

- Alteração da tela
- Inimigos
- Correção de bugs diversos
- Conserto da lógica da pedra que cai
- Adição da música de fundo e efeitos sonoros

Maior dificuldade:

As colisões dos inimigos com o jogador e com o mapa. Inicialmente, os inimigos não respeitavam as paredes corretamente e os valores de velocidade causavam atravessamentos. Além disso, a lógica da pedra que cai estava muito instável: a pedra ativava longe demais ou não ativava quando deveria. Também tive trabalho ao adaptar os inimigos ao mapa carregado via Tiled, pois as posições padrão estavam fixas no código e não sincronizavam com o editor.

Como resolveu:

Na classe `InimigoBase`, refinei o teste de colisão com o mapa verificando o centro do inimigo para evitar trepidação. Para a pedra que cai, estabeleci uma condição de distância vertical máxima de 7 blocos e comparei as colunas exatas. Quanto ao Tiled, implementei a leitura da camada "objetos" para posicionar inimigos diretamente no editor, eliminando valores hardcoded.

# João Vitor

Partes que fez:

- Tela de início
- Menu de configurações
- Botão para reiniciar o jogo
- Tela de morte
- Tela de fim de jogo com estrelas de acordo com o desempenho

Maior dificuldade:

Mapear as coordenadas dos botões. Como a resolução da janela pode variar, os cliques precisam ser detectados corretamente independente do tamanho da tela. Inicialmente os botões não respondiam ou respondiam no lugar errado quando a janela era redimensionada.

Como resolveu:

Utilizei coordenadas relativas baseadas na largura e altura atuais da janela. Para os botões das telas de fim de jogo e game over, calculei posições percentuais e converti as coordenadas do mouse para o sistema de coordenadas da tela antes de comparar. Também usei `viewport.unproject()` no botão de reset da TelaTeste para garantir que o clique funcionasse com a câmera.

# Vinícius

Partes que fez:

- Entidades em geral (`Bau`, `Rubi`, `PedraEmpurravel`, `PedraQueCai`, `ObjetoJogo`, `Item`, `Obstaculo`)
- Revisão de código, integração entre sistemas e correção de bugs gerais
- Liderança da equipe e coordenação do projeto

Maior dificuldade:

Ficar à frente da equipe liderando boa parte do projeto e, ao mesmo tempo, garantir que o jogo funcionasse como um todo. A parte técnica envolvia fazer literalmente o jogo funcionar, conectar as entidades, garantir que as interações estivessem todas integradas sem conflitos.

Como resolveu:

Garanti que as classes de entidades seguissem um padrão consistente (herdando de `ObjetoJogo`) e que os métodos `update` e `render` estivessem sincronizados com o loop principal da `TelaTeste`.
