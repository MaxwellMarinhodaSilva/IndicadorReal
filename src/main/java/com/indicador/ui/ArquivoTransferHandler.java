package com.indicador.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class ArquivoTransferHandler extends TransferHandler {

    private final JTextField campo;
    private final Border bordaPadrao;
    private final Border bordaDrag;
    private final String extensao;
    private final String descricao;
    private final Consumer<File> aoSelecionarArquivo;

    public ArquivoTransferHandler(
            JTextField campo,
            Border bordaPadrao,
            Border bordaDrag,
            String extensao,
            String descricao,
            Consumer<File> aoSelecionarArquivo
    ) {

        this.campo = campo;
        this.bordaPadrao = bordaPadrao;
        this.bordaDrag = bordaDrag;
        this.extensao = extensao.toLowerCase();
        this.descricao = descricao;
        this.aoSelecionarArquivo = aoSelecionarArquivo;
    }

    @Override
    public boolean canImport(
            TransferSupport support
    ) {

        /*
         * ============================================================
         * DURANTE O ARRASTE
         * ============================================================
         *
         * Aqui NÃO tentamos ler o arquivo.
         *
         * Apenas verificamos se o sistema está oferecendo
         * uma lista de arquivos.
         *
         * Isso evita consumir/interferir no Transferable
         * antes do momento real do Drop.
         */
        if (!support.isDataFlavorSupported(
                DataFlavor.javaFileListFlavor
        )) {

            if (support.isDrop()) {

                campo.setBorder(
                        bordaPadrao
                );
            }

            return false;
        }

        /*
         * Mostra visualmente que o campo aceita
         * uma operação de Drag & Drop.
         */
        if (support.isDrop()) {

            campo.setBorder(
                    bordaDrag
            );
        }

        return true;
    }

    @Override
    public boolean importData(
            TransferSupport support
    ) {

        /*
         * ============================================================
         * 1. VERIFICA O TIPO DE DADO
         * ============================================================
         */
        if (!support.isDataFlavorSupported(
                DataFlavor.javaFileListFlavor
        )) {

            campo.setBorder(
                    bordaPadrao
            );

            return false;
        }

        try {

            /*
             * ========================================================
             * 2. AGORA SIM O ARQUIVO É OBTIDO
             * ========================================================
             *
             * O acesso ao Transferable acontece somente
             * durante a importação real.
             */
            Transferable transferable =
                    support.getTransferable();

            List<?> arquivos =
                    (List<?>) transferable.getTransferData(
                            DataFlavor.javaFileListFlavor
                    );

            if (arquivos == null
                    || arquivos.isEmpty()) {

                campo.setBorder(
                        bordaPadrao
                );

                return false;
            }

            /*
             * ========================================================
             * 3. O PRIMEIRO ITEM PRECISA SER UM File
             * ========================================================
             */
            Object primeiro =
                    arquivos.getFirst();

            if (!(primeiro instanceof File arquivo)) {

                campo.setBorder(
                        bordaPadrao
                );

                return false;
            }

            /*
             * ========================================================
             * 4. VALIDA A EXTENSÃO
             * ========================================================
             */
            boolean extensaoValida =
                    arquivo.getName()
                            .toLowerCase()
                            .endsWith(extensao);

            if (!extensaoValida) {

                campo.setBorder(
                        bordaPadrao
                );

                MensagemUtil.erro(
                        campo,
                        "Selecione um arquivo "
                                + descricao
                                + "."
                );

                return false;
            }

            /*
             * ========================================================
             * 5. ARQUIVO VÁLIDO
             * ========================================================
             *
             * Remove o destaque visual.
             */
            campo.setBorder(
                    bordaPadrao
            );

            /*
             * Entrega o arquivo para IndicadorReal.
             */
            aoSelecionarArquivo.accept(
                    arquivo
            );

            return true;

        } catch (Exception ex) {

            campo.setBorder(
                    bordaPadrao
            );

            ex.printStackTrace();

            return false;
        }
    }
}