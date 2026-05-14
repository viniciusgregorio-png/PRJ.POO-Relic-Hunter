package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.some_example_name.Main;
import javax.swing.JOptionPane;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;

        // 1. Pergunta ao usuário o modo de tela
        Object[] options = {"Tela Cheia", "Janela"};
        int choice = JOptionPane.showOptionDialog(null,
            "Como deseja jogar o Relic Hunter?",
            "Configuração de Tela",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]);

        // 2. Inicia a aplicação passando a escolha (0 é Tela Cheia)
        createApplication(choice == 0);
    }

    private static Lwjgl3Application createApplication(boolean fullScreen) {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration(fullScreen));
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration(boolean fullScreen) {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Relic Hunter");

        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);

        // 3. Aplica a escolha da tela
        if (fullScreen) {
            configuration.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        } else {
            configuration.setWindowedMode(800, 500);
        }

        configuration.setWindowIcon("diamond128.png", "diamond64.png", "diamond32.png", "diamond16.png");

        return configuration;
    }
}
