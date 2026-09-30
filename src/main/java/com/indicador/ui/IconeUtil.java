package com.indicador.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

public final class IconeUtil {

    private static final int TAMANHO = 18;
    private static final BasicStroke TRACO = new BasicStroke(
            1.65f,
            BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND
    );

    private IconeUtil() {
    }

    public static Icon selecionar() {
        return icone((g2, x, y) -> {
            Path2D pasta = new Path2D.Double();
            pasta.moveTo(x + 2, y + 5);
            pasta.lineTo(x + 7, y + 5);
            pasta.lineTo(x + 9, y + 3);
            pasta.lineTo(x + 15, y + 3);
            pasta.lineTo(x + 16, y + 5);
            pasta.lineTo(x + 16, y + 14);
            pasta.lineTo(x + 2, y + 14);
            pasta.closePath();
            g2.draw(pasta);
        });
    }

    public static Icon processar() {
        return icone((g2, x, y) -> {
            g2.draw(new Ellipse2D.Double(x + 2, y + 2, 14, 14));
            Path2D executar = new Path2D.Double();
            executar.moveTo(x + 7, y + 5);
            executar.lineTo(x + 13, y + 9);
            executar.lineTo(x + 7, y + 13);
            executar.closePath();
            g2.fill(executar);
        });
    }

    public static Icon limpar() {
        return icone((g2, x, y) -> {
            Path2D borracha = new Path2D.Double();
            borracha.moveTo(x + 4, y + 11);
            borracha.lineTo(x + 10, y + 5);
            borracha.lineTo(x + 15, y + 10);
            borracha.lineTo(x + 9, y + 16);
            borracha.closePath();
            g2.draw(borracha);
            g2.drawLine(x + 3, y + 16, x + 10, y + 16);
        });
    }

    public static Icon historico() {
        return icone((g2, x, y) -> {
            g2.draw(new Ellipse2D.Double(x + 3, y + 3, 12, 12));
            g2.drawLine(x + 9, y + 6, x + 9, y + 10);
            g2.drawLine(x + 9, y + 10, x + 12, y + 11);
            g2.drawLine(x + 2, y + 8, x + 4, y + 8);
            g2.drawLine(x + 2, y + 8, x + 4, y + 6);
        });
    }

    public static Icon abrir() {
        return icone((g2, x, y) -> {
            g2.draw(new RoundRectangle2D.Double(x + 2, y + 3, 11, 12, 2, 2));
            g2.drawLine(x + 8, y + 9, x + 16, y + 9);
            g2.drawLine(x + 13, y + 6, x + 16, y + 9);
            g2.drawLine(x + 13, y + 12, x + 16, y + 9);
        });
    }

    public static Icon remover() {
        return icone((g2, x, y) -> {
            g2.drawLine(x + 5, y + 5, x + 13, y + 5);
            g2.drawLine(x + 7, y + 3, x + 11, y + 3);
            g2.draw(new RoundRectangle2D.Double(x + 5, y + 5, 8, 10, 1, 1));
            g2.drawLine(x + 8, y + 8, x + 8, y + 12);
            g2.drawLine(x + 10, y + 8, x + 10, y + 12);
        });
    }

    public static Icon cancelar() {
        return icone((g2, x, y) -> {
            g2.draw(new Ellipse2D.Double(x + 3, y + 3, 12, 12));
            g2.drawLine(x + 6, y + 6, x + 12, y + 12);
            g2.drawLine(x + 12, y + 6, x + 6, y + 12);
        });
    }

    public static Icon arquivoJson() {
        return icone((g2, x, y) -> {
            Path2D documento = new Path2D.Double();
            documento.moveTo(x + 4, y + 2);
            documento.lineTo(x + 11, y + 2);
            documento.lineTo(x + 15, y + 6);
            documento.lineTo(x + 15, y + 16);
            documento.lineTo(x + 4, y + 16);
            documento.closePath();
            g2.draw(documento);
            g2.drawLine(x + 11, y + 2, x + 11, y + 6);
            g2.drawLine(x + 11, y + 6, x + 15, y + 6);
            g2.drawLine(x + 7, y + 10, x + 6, y + 11);
            g2.drawLine(x + 6, y + 11, x + 7, y + 12);
            g2.drawLine(x + 12, y + 10, x + 13, y + 11);
            g2.drawLine(x + 13, y + 11, x + 12, y + 12);
        });
    }

    private static Icon icone(Desenho desenho) {
        return new Icon() {

            @Override
            public int getIconWidth() {
                return TAMANHO;
            }

            @Override
            public int getIconHeight() {
                return TAMANHO;
            }

            @Override
            public void paintIcon(Component componente, Graphics graphics, int x, int y) {
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2.setStroke(TRACO);
                g2.setColor(corDoComponente(componente));
                desenho.desenhar(g2, x, y);
                g2.dispose();
            }
        };
    }

    private static Color corDoComponente(Component componente) {
        if (componente.isEnabled()) {
            return componente.getForeground();
        }

        Color corDesabilitada = UIManager.getColor("Label.disabledForeground");
        return corDesabilitada != null ? corDesabilitada : componente.getForeground();
    }

    @FunctionalInterface
    private interface Desenho {
        void desenhar(Graphics2D g2, int x, int y);
    }
}
