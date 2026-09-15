package com.indicador.ui;

import javax.swing.*;
import java.awt.*;

public final class MensagemUtil {

    private MensagemUtil() {
    }

    public static void erro(
            Component parent,
            String mensagem
    ) {

        JOptionPane.showMessageDialog(

                parent,

                mensagem,

                "Erro",

                JOptionPane.ERROR_MESSAGE

        );

    }

    public static void informacao(
            Component parent,
            String mensagem
    ) {

        JOptionPane.showMessageDialog(

                parent,

                mensagem,

                "Informação",

                JOptionPane.INFORMATION_MESSAGE

        );

    }

    public static void aviso(
            Component parent,
            String mensagem
    ) {

        JOptionPane.showMessageDialog(

                parent,

                mensagem,

                "Aviso",

                JOptionPane.WARNING_MESSAGE

        );

    }

}