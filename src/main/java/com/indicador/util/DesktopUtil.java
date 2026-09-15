package com.indicador.util;

import com.indicador.ui.MensagemUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public final class DesktopUtil {

    private DesktopUtil() {
    }

    public static void abrirArquivoNoSistema(
            Component parent,
            File arquivo
    ) {

        try {

            if (Desktop.isDesktopSupported()) {

                if (arquivo.exists()) {

                    Desktop.getDesktop().open(arquivo);

                } else {

                    MensagemUtil.erro(

                            parent,

                            "Arquivo não encontrado."

                    );

                }

            }

        } catch (IOException ex) {

            MensagemUtil.erro(

                    parent,

                    "Não foi possível abrir: "

                            + ex.getMessage()

            );

        }

    }

}