package io.github.relichunter.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.relichunter.Main;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;

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
        configuration.setWindowIcon("Logo3(1).png", "Logo3(2).png", "Logo3(3).png", "Logo3(4).png");

        return configuration;
    }
}
