package com.indicador.ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public final class SeletorArquivo {

    private SeletorArquivo() {
    }

    public static File selecionar(

            JFrame parent,

            File ultimoDiretorio,

            String titulo,

            String descricao,

            String extensao

    ) {

        JFileChooser chooser =
                new JFileChooser();


        if (ultimoDiretorio != null) {

            chooser.setCurrentDirectory(
                    ultimoDiretorio
            );

        }

        chooser.setDialogTitle(
                titulo
        );

        chooser.setFileFilter(

                new FileNameExtensionFilter(

                        descricao,

                        extensao

                )

        );

        if (chooser.showOpenDialog(parent)
                != JFileChooser.APPROVE_OPTION) {

            return null;

        }

        File arquivoSelecionado =
                chooser.getSelectedFile();

        return arquivoSelecionado;

    }

}