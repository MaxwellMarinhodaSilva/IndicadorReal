package com.indicador.ui;

import com.indicador.config.HistoricoItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class ArquivoRecenteRenderer
        extends JPanel
        implements ListCellRenderer<HistoricoItem> {

    private final JLabel lblIcone;
    private final JLabel lblNome;
    private final JLabel lblCaminho;

    public ArquivoRecenteRenderer() {

        setLayout(
                new BorderLayout(
                        10,
                        0
                )
        );

        setBorder(
                new EmptyBorder(
                        8,
                        10,
                        8,
                        10
                )
        );

        lblIcone =
                new JLabel();

        lblNome =
                new JLabel();

        lblCaminho =
                new JLabel();

        lblNome.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        lblCaminho.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        lblCaminho.setForeground(
                Color.GRAY
        );

        JPanel painelTexto =
                new JPanel();

        painelTexto.setOpaque(
                false
        );

        painelTexto.setLayout(
                new BoxLayout(
                        painelTexto,
                        BoxLayout.Y_AXIS
                )
        );

        painelTexto.add(
                lblNome
        );

        painelTexto.add(
                Box.createVerticalStrut(
                        3
                )
        );

        painelTexto.add(
                lblCaminho
        );

        add(
                lblIcone,
                BorderLayout.WEST
        );

        add(
                painelTexto,
                BorderLayout.CENTER
        );

        setOpaque(
                true
        );
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends HistoricoItem> lista,
            HistoricoItem item,
            int indice,
            boolean selecionado,
            boolean foco
    ) {

        if (item != null) {

            File arquivo =
                    new File(
                            item.getCaminhoJson()
                    );

            boolean arquivoExiste =
                    arquivo.exists();

            String caminhoCompleto = arquivo.getAbsolutePath();

            lblNome.setText(
                    truncar(
                            arquivo.getName(),
                            lista,
                            95
                    )
            );

            setToolTipText(caminhoCompleto);
            lblNome.setToolTipText(caminhoCompleto);
            lblCaminho.setToolTipText(caminhoCompleto);

            if (arquivoExiste) {

                lblCaminho.setText(
                        truncar(caminhoCompleto, lista, 95)
                );

            } else {

                lblCaminho.setText(
                        "Arquivo não encontrado"
                );
            }

            if (arquivoExiste) {

                lblIcone.setIcon(
                        IconeUtil.arquivoJson()
                );

            } else {

                lblIcone.setIcon(
                        IconeUtil.cancelar()
                );

            }
        }

        File arquivoAtual =
                new File(
                        item.getCaminhoJson()
                );

        boolean arquivoExiste =
                arquivoAtual.exists();

        if (selecionado) {

            setBackground(
                    lista.getSelectionBackground()
            );

            lblNome.setForeground(
                    lista.getSelectionForeground()
            );

            lblCaminho.setForeground(
                    lista.getSelectionForeground()
            );

        } else {

            setBackground(
                    lista.getBackground()
            );

            if (arquivoExiste) {

                lblNome.setForeground(
                        lista.getForeground()
                );

                lblCaminho.setForeground(
                        Color.GRAY
                );

            } else {

                lblNome.setForeground(
                        Color.RED
                );

                lblCaminho.setForeground(
                        Color.RED
                );
            }
        }

        return this;
    }

    private String truncar(
            String texto,
            JList<?> lista,
            int larguraReservada
    ) {

        if (texto == null) {
            return "";
        }

        int larguraDisponivel = Math.max(
                100,
                lista.getWidth() - larguraReservada
        );

        FontMetrics metricas =
                getFontMetrics(
                        lblNome.getFont()
                );

        if (metricas.stringWidth(texto) <= larguraDisponivel) {
            return texto;
        }

        String sufixo = "…";
        int limite = texto.length();

        while (limite > 0
                && metricas.stringWidth(
                texto.substring(0, limite) + sufixo
        ) > larguraDisponivel) {
            limite--;
        }

        return texto.substring(0, limite) + sufixo;
    }
}
