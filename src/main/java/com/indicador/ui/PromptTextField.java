package com.indicador.ui;

import javax.swing.*;
import java.awt.*;

public class PromptTextField extends JTextField {

    private String textoPrompt = "";
    private String textoPromptSecundario = "";

    public PromptTextField() {
        super();
    }

    public void setTextoPrompt(
            String textoPrompt
    ) {

        this.textoPrompt =
                textoPrompt != null
                        ? textoPrompt
                        : "";

        repaint();
    }

    public void setTextoPromptSecundario(
            String textoPromptSecundario
    ) {

        this.textoPromptSecundario =
                textoPromptSecundario != null
                        ? textoPromptSecundario
                        : "";

        repaint();
    }

    @Override
    protected void paintComponent(
            Graphics g
    ) {

        super.paintComponent(g);

        /*
         * ============================================================
         * PLACEHOLDER
         * ============================================================
         */
        if (!hasFocus()
                && getText().isEmpty()
                && (!textoPrompt.isEmpty()
                || !textoPromptSecundario.isEmpty())) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            /*
             * ========================================================
             * RENDERIZAÇÃO
             * ========================================================
             */
            g2.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            /*
             * ========================================================
             * FONTE
             * ========================================================
             */
            Font fonte =
                    getFont().deriveFont(
                            Font.PLAIN,
                            getFont().getSize2D()
                    );

            g2.setFont(
                    fonte
            );

            /*
             * ========================================================
             * COR
             * ========================================================
             */
            Color corPlaceholder =
                    UIManager.getColor(
                            "TextField.placeholderForeground"
                    );

            if (corPlaceholder == null) {

                corPlaceholder =
                        UIManager.getColor(
                                "Label.disabledForeground"
                        );
            }

            if (corPlaceholder == null) {

                corPlaceholder =
                        UIManager.getColor(
                                "Label.foreground"
                        );
            }

            if (corPlaceholder == null) {

                corPlaceholder =
                        getForeground();
            }

            g2.setColor(
                    corPlaceholder
            );

            /*
             * ========================================================
             * MONTA O TEXTO COMPLETO
             * ========================================================
             *
             * Existe um espaço entre o texto principal e o
             * texto secundário.
             */
            String textoCompleto;

            if (!textoPrompt.isEmpty()
                    && !textoPromptSecundario.isEmpty()) {

                textoCompleto =
                        textoPrompt
                                + "  "
                                + textoPromptSecundario;

            } else if (!textoPrompt.isEmpty()) {

                textoCompleto =
                        textoPrompt;

            } else {

                textoCompleto =
                        textoPromptSecundario;
            }

            /*
             * ========================================================
             * DIMENSÕES DO TEXTO
             * ========================================================
             */
            FontMetrics metrics =
                    g2.getFontMetrics();

            int larguraTexto =
                    metrics.stringWidth(
                            textoCompleto
                    );

            Insets insets =
                    getInsets();

            int larguraDisponivel =
                    getWidth()
                            - insets.left
                            - insets.right;

            /*
             * ========================================================
             * CENTRALIZAÇÃO HORIZONTAL
             * ========================================================
             */
            int x =
                    insets.left
                            + Math.max(
                            0,
                            (
                                    larguraDisponivel
                                            - larguraTexto
                            ) / 2
                    );

            /*
             * ========================================================
             * CENTRALIZAÇÃO VERTICAL
             * ========================================================
             */
            int y =
                    (
                            getHeight()
                                    - metrics.getHeight()
                    ) / 2
                            + metrics.getAscent();

            /*
             * ========================================================
             * DESENHA
             * ========================================================
             */
            g2.drawString(
                    textoCompleto,
                    x,
                    y
            );

            g2.dispose();
        }
    }
}