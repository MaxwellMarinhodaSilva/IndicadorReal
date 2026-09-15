package com.indicador.theme;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class WindowsTheme {

    private WindowsTheme() {
    }

    private static boolean ultimoModoEscuro = windowsEstaNoModoEscuro();

    private static final ScheduledExecutorService monitorTema =
            Executors.newSingleThreadScheduledExecutor();

    public static boolean windowsEstaNoModoEscuro() {

        try {

            Process process = new ProcessBuilder(
                    "reg",
                    "query",
                    "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
                    "/v",
                    "AppsUseLightTheme"
            ).start();

            String texto =
                    new String(process.getInputStream().readAllBytes());

            return texto.contains("0x0");

        } catch (Exception e) {

            return false;

        }

    }

    public static void aplicarTemaInicial() {

        FlatLaf.setup(
                ultimoModoEscuro
                        ? new FlatDarkLaf()
                        : new FlatIntelliJLaf()
        );

    }

    public static void iniciarMonitor() {

        monitorTema.scheduleAtFixedRate(() -> {

            try {

                boolean modoEscuroAtual =
                        windowsEstaNoModoEscuro();

                if (modoEscuroAtual != ultimoModoEscuro) {

                    ultimoModoEscuro = modoEscuroAtual;

                    SwingUtilities.invokeLater(() -> {

                        FlatLaf.setup(
                                modoEscuroAtual
                                        ? new FlatDarkLaf()
                                        : new FlatIntelliJLaf()
                        );

                        FlatLaf.updateUI();

                        for (Window window : Window.getWindows()) {

                            SwingUtilities.updateComponentTreeUI(window);

                            window.invalidate();
                            window.validate();
                            window.repaint();

                        }

                    });

                }

            } catch (Exception ex) {

                ex.printStackTrace();

            }

        }, 2, 2, TimeUnit.SECONDS);

    }

    public static void pararMonitor() {

        monitorTema.shutdownNow();

    }

}