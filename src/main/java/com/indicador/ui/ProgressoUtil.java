package com.indicador.ui;

import javax.swing.*;

public final class ProgressoUtil {

    private ProgressoUtil() {
    }

    public static void atualizar(

            JProgressBar barra,

            int valor

    ) {

        SwingUtilities.invokeLater(() ->

                barra.setValue(valor)

        );

    }

}