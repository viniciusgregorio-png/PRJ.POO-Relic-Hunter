package io.github.relichunter.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.relichunter.Main;
import javax.swing.JOptionPane;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;

        Object[] options = {"Sim", "Não"};
        int choice = JOptionPane.showOptionDialog(null,
            "Deseja iniciar o Relic Hunter?",
            "Inicialização do jogo",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]);

        if (choice != 0) {
            System.exit(0);
        }

        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Relic Hunter");

        com.badlogic.gdx.Graphics.DisplayMode displayMode =
            Lwjgl3ApplicationConfiguration.getDisplayMode();

        configuration.useVsync(true);
        configuration.setForegroundFPS(displayMode.refreshRate + 1);
        configuration.setWindowedMode(1250, 768);
        configuration.setWindowIcon("4.png", "3.png", "2.png", "1.png");

        return configuration;
    }
}
