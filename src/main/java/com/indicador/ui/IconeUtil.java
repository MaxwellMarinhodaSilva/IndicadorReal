package com.indicador.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public final class IconeUtil {

    private IconeUtil() {
    }

    public static Icon selecionar() {

        return new Icon() {

            @Override
            public int getIconWidth() {
                return 18;
            }

            @Override
            public int getIconHeight() {
                return 18;
            }

            @Override
            public void paintIcon(
                    Component c,
                    Graphics g,
                    int x,
                    int y
            ) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                Color cor =
                        c.isEnabled()
                                ? c.getForeground()
                                : UIManager.getColor(
                                "Label.disabledForeground"
                        );

                if (cor == null) {

                    cor =
                            c.getForeground();
                }

                g2.setColor(cor);

                /*
                 * ====================================================
                 * PASTA
                 * ====================================================
                 */

                Path2D pasta =
                        new java.awt.geom.Path2D.Double();

                pasta.moveTo(
                        x + 2,
                        y + 5
                );

                pasta.lineTo(
                        x + 7,
                        y + 5
                );

                pasta.lineTo(
                        x + 9,
                        y + 3
                );

                pasta.lineTo(
                        x + 15,
                        y + 3
                );

                pasta.lineTo(
                        x + 16,
                        y + 5
                );

                pasta.lineTo(
                        x + 16,
                        y + 14
                );

                pasta.lineTo(
                        x + 2,
                        y + 14
                );

                pasta.closePath();

                g2.setStroke(
                        new BasicStroke(
                                1.5f,
                                BasicStroke.CAP_ROUND,
                                BasicStroke.JOIN_ROUND
                        )
                );

                g2.draw(pasta);

                g2.dispose();
            }
        };
    }

    public static Icon processar() {

        return new Icon() {

            @Override
            public int getIconWidth() {
                return 18;
            }

            @Override
            public int getIconHeight() {
                return 18;
            }

            @Override
            public void paintIcon(
                    Component c,
                    Graphics g,
                    int x,
                    int y
            ) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                Color cor =
                        c.isEnabled()
                                ? c.getForeground()
                                : UIManager.getColor(
                                "Label.disabledForeground"
                        );

                if (cor == null) {

                    cor =
                            c.getForeground();
                }

                g2.setColor(cor);

                /*
                 * ====================================================
                 * TRIÂNGULO DE EXECUÇÃO
                 * ====================================================
                 */

                Path2D triangulo =
                        new java.awt.geom.Path2D.Double();

                triangulo.moveTo(
                        x + 5,
                        y + 3
                );

                triangulo.lineTo(
                        x + 15,
                        y + 9
                );

                triangulo.lineTo(
                        x + 5,
                        y + 15
                );

                triangulo.closePath();

                g2.fill(triangulo);

                g2.dispose();
            }
        };
    }

    public static Icon limpar() {

        return new Icon() {

            @Override
            public int getIconWidth() {
                return 18;
            }

            @Override
            public int getIconHeight() {
                return 18;
            }

            @Override
            public void paintIcon(
                    Component c,
                    Graphics g,
                    int x,
                    int y
            ) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                Color cor =
                        c.isEnabled()
                                ? c.getForeground()
                                : UIManager.getColor(
                                "Label.disabledForeground"
                        );

                if (cor == null) {

                    cor =
                            c.getForeground();
                }

                g2.setColor(cor);

                /*
                 * ====================================================
                 * BORRACHA / LIMPEZA
                 * ====================================================
                 */

                Path2D borracha =
                        new java.awt.geom.Path2D.Double();

                borracha.moveTo(
                        x + 4,
                        y + 11
                );

                borracha.lineTo(
                        x + 10,
                        y + 5
                );

                borracha.lineTo(
                        x + 15,
                        y + 10
                );

                borracha.lineTo(
                        x + 9,
                        y + 16
                );

                borracha.lineTo(
                        x + 4,
                        y + 11
                );

                borracha.closePath();

                g2.setStroke(
                        new BasicStroke(
                                1.5f,
                                BasicStroke.CAP_ROUND,
                                BasicStroke.JOIN_ROUND
                        )
                );

                g2.draw(
                        borracha
                );

                g2.drawLine(
                        x + 8,
                        y + 7,
                        x + 13,
                        y + 12
                );

                g2.dispose();
            }
        };
    }
}