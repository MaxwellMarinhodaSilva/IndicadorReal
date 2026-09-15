package com.indicador.ui;

import com.indicador.model.ContextoProcessamento;
import com.indicador.util.DesktopUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ResumoDialog extends JDialog {

    private final ContextoProcessamento contexto;

    public ResumoDialog(
            JFrame parent,
            String versao,
            ContextoProcessamento contexto
    ) {

        super(parent, "Processamento concluído", true);

        this.contexto = contexto;

        setSize(750, 680);

        setLocationRelativeTo(parent);

        setLayout(
                new BorderLayout(10, 10)
        );

        URL urlIcone =
                getClass().getResource(
                        "/Icones/Indicador_32x32.png"
                );

        if (urlIcone != null) {

            setIconImage(
                    new ImageIcon(urlIcone).getImage()
            );

        }

        JPanel painelPrincipal = new JPanel();

        painelPrincipal.setLayout(
                new BoxLayout(
                        painelPrincipal,
                        BoxLayout.Y_AXIS
                )
        );

        painelPrincipal.setBorder(
                new EmptyBorder(
                        15,
                        20,
                        10,
                        10
                )
        );

        add(
                painelPrincipal,
                BorderLayout.CENTER
        );

        JPanel painelStats =
                new JPanel(
                        new GridLayout(
                                6,
                                2,
                                10,
                                8
                        )
                );

        painelStats.setBorder(
                BorderFactory.createTitledBorder(
                        "Resumo do processamento"
                )
        );

        painelStats.setAlignmentX(
                LEFT_ALIGNMENT
        );

        adicionarLinhaStat(
                painelStats,
                "Total de matrículas únicas no TXT:",
                String.valueOf(this.contexto.getMatriculasParaCorrigir().size())
        );

        adicionarLinhaStat(
                painelStats,
                "Alterações realizadas:",
                String.valueOf(this.contexto.getAlteracoesRealizadas())
        );

        adicionarLinhaStat(
                painelStats,
                "Já estavam corretas:",
                String.valueOf(this.contexto.getJaEstavamCorretas())
        );

        adicionarLinhaStat(
                painelStats,
                "CEP inválido(s) ignorado(s):",
                String.valueOf(this.contexto.getCepsInvalidos().size()),
                this.contexto.getCepsInvalidos().isEmpty()
                        ? TipoIndicador.SUCESSO
                        : TipoIndicador.ATENCAO
        );

        adicionarLinhaStat(
                painelStats,
                "Vários endereços encontrado(s):",
                String.valueOf(this.contexto.getVariosEnderecos().size()),
                this.contexto.getVariosEnderecos().isEmpty()
                        ? TipoIndicador.SUCESSO
                        : TipoIndicador.ATENCAO
        );

        adicionarLinhaStat(
                painelStats,
                "Matrículas não encontradas no JSON:",
                String.valueOf(this.contexto.getNaoEncontradas().size()),
                this.contexto.getNaoEncontradas().isEmpty()
                        ? TipoIndicador.SUCESSO
                        : TipoIndicador.ERRO
        );

        painelPrincipal.add(
                painelStats
        );

        painelPrincipal.add(
                Box.createVerticalStrut(
                        15
                )
        );

        JPanel painelVarios =
                new JPanel(
                        new BorderLayout()
                );

        painelVarios.setBorder(
                BorderFactory.createTitledBorder(
                        "Matrículas com vários endereços"
                )
        );

        painelVarios.setAlignmentX(
                LEFT_ALIGNMENT
        );

        JTextArea txtVarios =
                new JTextArea(
                        2,
                        50
                );

        txtVarios.setEditable(false);
        txtVarios.setLineWrap(true);
        txtVarios.setWrapStyleWord(true);

        if (this.contexto.getVariosEnderecos().isEmpty()) {

            txtVarios.setText(
                    "Nenhuma matrícula."
            );

        } else {

            List<String> lista =
                    new ArrayList<>(this.contexto.getVariosEnderecos());

            lista.sort(
                    Comparator.comparingInt(
                            Integer::parseInt
                    )
            );

            txtVarios.setText(
                    String.join(
                            ", ",
                            lista
                    )
            );

        }

        JScrollPane scrollVarios =
                new JScrollPane(
                        txtVarios,
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                );

        painelVarios.add(
                scrollVarios,
                BorderLayout.CENTER
        );

        painelPrincipal.add(
                painelVarios
        );

        painelPrincipal.add(
                Box.createVerticalStrut(
                        10
                )
        );

        /*
         * ==========================================================
         * MATRÍCULAS COM CEP INVÁLIDO(S)
         * ==========================================================
         */
        JPanel painelCepsInvalidos =
                new JPanel(
                        new BorderLayout()
                );

        painelCepsInvalidos.setBorder(
                BorderFactory.createTitledBorder(
                        "Matrículas com CEP inválido(s) ignorado(s)"
                )
        );

        painelCepsInvalidos.setAlignmentX(
                LEFT_ALIGNMENT
        );

        JTextArea txtCepsInvalidos =
                new JTextArea(
                        2,
                        50
                );

        txtCepsInvalidos.setEditable(false);
        txtCepsInvalidos.setLineWrap(true);
        txtCepsInvalidos.setWrapStyleWord(true);

        if (this.contexto.getCepsInvalidos().isEmpty()) {

            txtCepsInvalidos.setText(
                    "Nenhuma matrícula."
            );

        } else {

            List<String> lista =
                    new ArrayList<>(
                            this.contexto.getCepsInvalidos()
                    );

            lista.sort(
                    Comparator.comparingInt(
                            Integer::parseInt
                    )
            );

            txtCepsInvalidos.setText(
                    String.join(
                            ", ",
                            lista
                    )
            );
        }

        JScrollPane scrollCepsInvalidos =
                new JScrollPane(
                        txtCepsInvalidos,
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                );

        painelCepsInvalidos.add(
                scrollCepsInvalidos,
                BorderLayout.CENTER
        );

        painelPrincipal.add(
                painelCepsInvalidos
        );

        painelPrincipal.add(
                Box.createVerticalStrut(
                        10
                )
        );

        JPanel painelArquivos =
                new JPanel();

        painelArquivos.setLayout(
                new BoxLayout(
                        painelArquivos,
                        BoxLayout.Y_AXIS
                )
        );

        painelArquivos.setBorder(
                BorderFactory.createTitledBorder(
                        "Arquivos gerados"
                )
        );

        painelArquivos.setAlignmentX(
                LEFT_ALIGNMENT
        );

        JLabel lblJsonNome =
                new JLabel(
                        "<html><b>JSON:</b><br>&nbsp;&nbsp;&nbsp;&nbsp;"
                                + this.contexto.getArquivoSaida().getName()
                                + "</html>"
                );

        JLabel lblTxtNome =
                new JLabel(
                        "<html><br><b>Relatório:</b><br>&nbsp;&nbsp;&nbsp;&nbsp;"
                                + this.contexto.getArquivoRelatorio().getName()
                                + "</html>"
                );

        JLabel lblLogNome =
                new JLabel(
                        "<html><br><b>Log:</b><br>&nbsp;&nbsp;&nbsp;&nbsp;"
                                + this.contexto.getArquivoLog().getName()
                                + "</html>"
                );

        painelArquivos.add(
                lblJsonNome
        );

        painelArquivos.add(
                lblTxtNome
        );

        painelArquivos.add(
                lblLogNome
        );

        painelPrincipal.add(
                painelArquivos
        );

        JPanel painelBotoes =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                10
                        )
                );

        JButton btnAbrirJson =
                new JButton("Abrir JSON");

        JButton btnAbrirTxt =
                new JButton("Abrir Relatório");

        JButton btnAbrirLog =
                new JButton("Abrir Log");

        JButton btnAbrirPasta =
                new JButton("Abrir Pasta");

        JButton btnFechar =
                new JButton("Fechar");

        btnAbrirJson.addActionListener(e ->
                DesktopUtil.abrirArquivoNoSistema(
                        this,
                        this.contexto.getArquivoSaida()
                )
        );

        btnAbrirTxt.addActionListener(e ->
                DesktopUtil.abrirArquivoNoSistema(
                        this,
                        this.contexto.getArquivoRelatorio()
                )
        );

        btnAbrirLog.addActionListener(e ->
                DesktopUtil.abrirArquivoNoSistema(
                        this,
                        this.contexto.getArquivoLog()
                )
        );

        btnAbrirPasta.addActionListener(e ->
                DesktopUtil.abrirArquivoNoSistema(
                        this,
                        this.contexto.getArquivoSaida().getParentFile()
                )
        );


        btnFechar.addActionListener(e ->
                dispose()
        );

        painelBotoes.add(
                btnAbrirJson
        );

        painelBotoes.add(
                btnAbrirTxt
        );

        painelBotoes.add(
                btnAbrirLog
        );

        painelBotoes.add(
                btnAbrirPasta
        );

        painelBotoes.add(
                btnFechar
        );

        JPanel painelRodape =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        JLabel lblVersaoRodape =
                new JLabel(
                        versao
                );

        lblVersaoRodape.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        lblVersaoRodape.setForeground(
                Color.GRAY
        );

        painelRodape.add(
                lblVersaoRodape
        );

        JPanel painelSul =
                new JPanel(
                        new BorderLayout()
                );

        painelSul.add(
                painelBotoes,
                BorderLayout.CENTER
        );

        painelSul.add(
                painelRodape,
                BorderLayout.SOUTH
        );

        add(
                painelSul,
                BorderLayout.SOUTH
        );



    }

    private enum TipoIndicador {

        SUCESSO,
        ATENCAO,
        ERRO

    }

    private void adicionarLinhaStat(
            JPanel painel,
            String label,
            String valor
    ) {

        JLabel lblTexto =
                new JLabel(label);

        lblTexto.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        JLabel lblValor =
                new JLabel(
                        valor
                );

        lblValor.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        painel.add(
                lblTexto
        );

        painel.add(
                lblValor
        );
    }

    private void adicionarLinhaStat(
            JPanel painel,
            String label,
            String valor,
            TipoIndicador tipoIndicador
    ) {

        JLabel lblTexto =
                new JLabel(label);

        lblTexto.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        JPanel painelValor =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                0
                        )
                );

        painelValor.setOpaque(false);

        JLabel lblValor =
                new JLabel(
                        valor
                );

        lblValor.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        JLabel lblIndicador =
                new JLabel();

        lblIndicador.setFont(
                new Font(
                        "Segoe UI Symbol",
                        Font.BOLD,
                        14
                )
        );

        switch (tipoIndicador) {

            case SUCESSO:

                lblIndicador.setText(
                        "\u2714"
                );

                lblIndicador.setForeground(
                        new Color(
                                0,
                                180,
                                0
                        )
                );

                break;

            case ATENCAO:

                lblIndicador.setText(
                        "\u26A0"
                );

                lblIndicador.setForeground(
                        new Color(
                                220,
                                170,
                                0
                        )
                );

                break;

            case ERRO:

                lblIndicador.setText(
                        "\u2716"
                );

                lblIndicador.setForeground(
                        new Color(
                                220,
                                60,
                                60
                        )
                );

                break;
        }

        painelValor.add(
                lblValor
        );

        painelValor.add(
                Box.createHorizontalStrut(
                        6
                )
        );

        painelValor.add(
                lblIndicador
        );

        painel.add(
                lblTexto
        );

        painel.add(
                painelValor
        );
    }

}