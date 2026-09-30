package com.indicador;

import com.indicador.config.Configuracao;
import com.indicador.config.ConfiguracaoService;
import com.indicador.config.HistoricoItem;
import com.indicador.config.HistoricoService;
import com.indicador.log.ExecucaoLog;
import com.indicador.model.ContextoProcessamento;
import com.indicador.service.ProcessamentoService;
import com.indicador.theme.WindowsTheme;
import com.indicador.ui.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class IndicadorReal extends JFrame {

    private static final String VERSAO = "Indicador Real • v2.0.0";
    private static final String SEPARADOR = "=".repeat(50);

    private JRadioButton radioArquivo;
    private JRadioButton radioTexto;
    private PromptTextField txtArquivoErro;
    private JButton btnSelecionarTxt;
    private JTextArea caixaTextoErro;
    private PromptTextField txtArquivoJson;
    private JProgressBar barraProgresso;
    private JLabel lblStatusProgresso;
    private JButton btnProcessar;
    private File ultimoDiretorio = null;

    private String arquivoErroPath = "";
    private String arquivoJsonPath = "";
    private String conteudoErro = "";
    private String ultimoTextoColadoHistorico = "";
    private boolean carregandoHistorico = false;

    /*
     * Timer utilizado para evitar que o histórico
     * seja salvo a cada tecla digitada.
     *
     * O salvamento será realizado somente quando
     * o usuário parar de digitar por um pequeno intervalo.
     */
    private final Timer timerHistorico =
            new Timer(
                    1000,
                    e -> salvarHistoricoTextoSeCompleto()
            );

    private final Border bordaPadrao = UIManager.getBorder("TextField.border");

    private final Border bordaDrag =
            BorderFactory.createLineBorder(
                    new Color(0, 120, 215),
                    2
            );

    private Configuracao configuracao;

    public IndicadorReal() {

        timerHistorico.setRepeats(false);

        try {

            configuracao =
                    ConfiguracaoService.carregar();

        } catch (Exception ex) {

            configuracao =
                    new Configuracao();

            ex.printStackTrace();

        }
        if (!configuracao.getUltimaPasta().isBlank()) {

            File pasta =
                    new File(
                            configuracao.getUltimaPasta()
                    );

            if (pasta.exists() && pasta.isDirectory()) {

                ultimoDiretorio = pasta;

            }

        }

        setTitle("Indicador Real - (ONR)");

        setSize(
                configuracao.getLargura(),
                configuracao.getAltura()
        );
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {

                try {

                    configuracao.setPosicaoX(
                            getX()
                    );

                    configuracao.setPosicaoY(
                            getY()
                    );

                    configuracao.setLargura(
                            getWidth()
                    );

                    configuracao.setAltura(
                            getHeight()
                    );

                    ExecucaoLog.info(
                            "Programa encerrado."
                    );

                    ConfiguracaoService.salvar(
                            configuracao
                    );

                } catch (IOException ignored) {
                }

                try {

                    WindowsTheme.pararMonitor();

                } catch (Exception ignored) {
                }

                ExecucaoLog.fechar();

                dispose();

                System.exit(0);

            }

        });
        if (configuracao.getPosicaoX() >= 0
                && configuracao.getPosicaoY() >= 0) {

            setLocation(
                    configuracao.getPosicaoX(),
                    configuracao.getPosicaoY()
            );

        } else {

            setLocationRelativeTo(null);

        }
        setMinimumSize(new Dimension(780, 730));
        setResizable(true);

        // Busca o ícone sem acento na pasta!
        ImageIcon icone = new ImageIcon(
                Objects.requireNonNull(
                        getClass().getResource("/Icones/Indicador_32x32.png")
                )
        );

        setIconImage(icone.getImage());

        inicializarComponentes();

        if ("TEXTO".equals(configuracao.getModoEntrada())) {

            radioTexto.setSelected(true);

        } else {

            radioArquivo.setSelected(true);

        }

        atualizarInterface();
    }

    private void inicializarComponentes() {

        /*
         * ============================================================
         * PAINEL PRINCIPAL
         * ============================================================
         */
        JPanel painelPrincipal = new JPanel(new BorderLayout(0, 10));

        /*
         * Permite que o painel principal receba o foco inicial.
         *
         * Isso evita que o campo JSON fique focado automaticamente
         * quando o programa é aberto.
         */
        painelPrincipal.setFocusable(true);
        painelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        /*
         * ============================================================
         * BARRA SUPERIOR
         * ============================================================
         */
        JPanel painelCabecalho =
                new JPanel(
                        new BorderLayout(
                                15,
                                0
                        )
                );

        painelCabecalho.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                UIManager.getColor(
                                        "Separator.foreground"
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                4,
                                0,
                                10,
                                0
                        )
                )
        );

        /*
         * ------------------------------------------------------------
         * IDENTIFICAÇÃO DO APLICATIVO
         * ------------------------------------------------------------
         */
        JPanel painelIdentificacao =
                new JPanel(
                        new BorderLayout(
                                0,
                                2
                        )
                );

        JLabel titulo =
                new JLabel(
                        "Indicador Real"
                );

        titulo.setFont(
                titulo.getFont().deriveFont(
                        Font.BOLD,
                        20f
                )
        );

        JLabel subtitulo =
                new JLabel(
                        "Correção automática de TIPOENVIO"
                );

        subtitulo.setFont(
                subtitulo.getFont().deriveFont(
                        Font.PLAIN,
                        12f
                )
        );

        painelIdentificacao.add(
                titulo,
                BorderLayout.NORTH
        );

        painelIdentificacao.add(
                subtitulo,
                BorderLayout.SOUTH
        );

        /*
         * ------------------------------------------------------------
         * MONTA A BARRA SUPERIOR
         * ------------------------------------------------------------
         */
        painelCabecalho.add(
                painelIdentificacao,
                BorderLayout.CENTER
        );

        /*
         * ============================================================
         * ÁREA CENTRAL
         * ============================================================
         */
        JPanel painelCentro = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(4, 0, 4, 0);

        /*
         * ------------------------------------------------------------
         * JSON
         * ------------------------------------------------------------
         */
        JLabel lblJson = new JLabel("Arquivo JSON");
        gbc.gridy = 0;
        gbc.weighty = 0;
        painelCentro.add(lblJson, gbc);

        JPanel painelJson = new JPanel(new BorderLayout(8, 0));

        txtArquivoJson = new PromptTextField();

        txtArquivoJson.setTextoPrompt(
                "Arraste e solte o arquivo JSON aqui"
        );

        txtArquivoJson.setTextoPromptSecundario(
                "ou clique em \"Selecionar\""
        );

        txtArquivoJson.setPreferredSize(
                new Dimension(
                        0,
                        32
                )
        );

        txtArquivoJson.setEditable(false);
        txtArquivoJson.setToolTipText(
                "Selecione ou arraste um arquivo JSON"
        );

        txtArquivoJson.setTransferHandler(
                new ArquivoTransferHandler(
                        txtArquivoJson,
                        bordaPadrao,
                        bordaDrag,
                        ".json",
                        "JSON",
                        this::selecionarArquivoJson
                )
        );

        painelJson.add(txtArquivoJson, BorderLayout.CENTER);

        JButton btnSelecionarJson =
                new JButton(
                        "Selecionar",
                        IconeUtil.selecionar()
                );

        btnSelecionarJson.setPreferredSize(
                new Dimension(
                        110,
                        32
                )
        );

        btnSelecionarJson.setIconTextGap(6);

        btnSelecionarJson.addActionListener(
                e -> selecionarJson()
        );

        painelJson.add(
                btnSelecionarJson,
                BorderLayout.EAST
        );

        gbc.gridy = 1;
        painelCentro.add(painelJson, gbc);

        /*
         * ------------------------------------------------------------
         * HISTÓRICO
         * ------------------------------------------------------------
         */
        JPanel painelHistorico = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        0,
                        0
                )
        );

        painelHistorico.setPreferredSize(
                new Dimension(
                        110,
                        32
                )
        );

        painelHistorico.setMinimumSize(
                new Dimension(
                        110,
                        32
                )
        );

        painelHistorico.setMaximumSize(
                new Dimension(
                        110,
                        32
                )
        );

        JButton btnHistorico = new JButton(IconeUtil.historico());

        btnHistorico.setToolTipText(
                "Arquivos recentes"
        );

        btnHistorico.getAccessibleContext().setAccessibleName(
                "Arquivos recentes"
        );

        /*
         * Tamanho maior para facilitar a visualização
         * e o acionamento do botão.
         */
        btnHistorico.setPreferredSize(
                new Dimension(
                        48,
                        32
                )
        );

        btnHistorico.setMinimumSize(
                new Dimension(
                        48,
                        32
                )
        );

        btnHistorico.setMaximumSize(
                new Dimension(
                        48,
                        32
                )
        );
        btnHistorico.addActionListener(e -> {
            salvarHistoricoAtual();
            abrirDialogHistorico();
        });

        painelHistorico.add(
                btnHistorico
        );

        painelHistorico.add(
                Box.createHorizontalStrut(
                        18
                )
        );

        /*
         * ------------------------------------------------------------
         * BOTÃO LIMPAR TELA
         * ------------------------------------------------------------
         */
        JButton btnLimparTela =
                new JButton(
                        IconeUtil.limpar()
                );

        btnLimparTela.setToolTipText(
                "Limpar tela"
        );

        btnLimparTela.getAccessibleContext().setAccessibleName(
                "Limpar tela"
        );

        btnLimparTela.setPreferredSize(
                new Dimension(
                        48,
                        32
                )
        );

        btnLimparTela.setMinimumSize(
                new Dimension(
                        48,
                        32
                )
        );

        btnLimparTela.setMaximumSize(
                new Dimension(
                        48,
                        32
                )
        );

        btnLimparTela.addActionListener(e -> {
            limparInterface();
        });

        painelHistorico.add(
                btnLimparTela
        );

        gbc.gridy = 2;
        painelCentro.add(painelHistorico, gbc);

        /*
         * ------------------------------------------------------------
         * FONTE DOS DADOS
         * ------------------------------------------------------------
         */
        JLabel lblOrigem = new JLabel("Fonte dos dados");
        gbc.gridy = 3;
        painelCentro.add(lblOrigem, gbc);

        /*
         * ------------------------------------------------------------
         * RÁDIOS
         * ------------------------------------------------------------
         */
        JPanel painelRadios = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 5, 0)
        );

        radioArquivo = new JRadioButton(
                "Importar arquivo TXT"
        );

        radioTexto = new JRadioButton(
                "Colar texto diretamente"
        );

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(radioArquivo);
        grupo.add(radioTexto);

        radioArquivo.addActionListener(e -> {

            timerHistorico.stop();

            caixaTextoErro.setText("");

            ultimoTextoColadoHistorico = "";

            atualizarInterface();
        });

        radioTexto.addActionListener(e -> {
            atualizarInterface();
        });

        painelRadios.add(radioArquivo);
        painelRadios.add(radioTexto);

        gbc.gridy = 4;
        painelCentro.add(painelRadios, gbc);

        /*
         * ------------------------------------------------------------
         * ARQUIVO TXT
         * ------------------------------------------------------------
         */
        JPanel painelTxt = new JPanel(new BorderLayout(8, 0));

        txtArquivoErro = new PromptTextField();

        txtArquivoErro.setTextoPrompt(
                "Arraste e solte o arquivo TXT aqui"
        );

        txtArquivoErro.setTextoPromptSecundario(
                "ou clique em \"Selecionar\""
        );

        txtArquivoErro.setPreferredSize(
                new Dimension(
                        0,
                        32
                )
        );

        txtArquivoErro.setEditable(false);
        txtArquivoErro.setToolTipText(
                "Selecione ou arraste um arquivo TXT"
        );

        txtArquivoErro.setTransferHandler(
                new ArquivoTransferHandler(
                        txtArquivoErro,
                        bordaPadrao,
                        bordaDrag,
                        ".txt",
                        "TXT",
                        this::selecionarArquivoTxt
                )
        );

        btnSelecionarTxt =
                new JButton(
                        "Selecionar",
                        IconeUtil.selecionar()
                );

        btnSelecionarTxt.setPreferredSize(
                new Dimension(
                        110,
                        32
                )
        );

        btnSelecionarTxt.setIconTextGap(6);

        btnSelecionarTxt.addActionListener(
                e -> selecionarTxt()
        );

        painelTxt.add(
                txtArquivoErro,
                BorderLayout.CENTER
        );

        painelTxt.add(
                btnSelecionarTxt,
                BorderLayout.EAST
        );

        gbc.gridy = 5;
        painelCentro.add(painelTxt, gbc);

        /*
         * ------------------------------------------------------------
         * ÁREA DE TEXTO
         * ------------------------------------------------------------
         */
        caixaTextoErro = new JTextArea();
        caixaTextoErro.setLineWrap(false);
        caixaTextoErro.setWrapStyleWord(false);

        caixaTextoErro.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {

                    @Override
                    public void insertUpdate(
                            javax.swing.event.DocumentEvent e
                    ) {

                        agendarSalvamentoHistoricoTexto();
                    }

                    @Override
                    public void removeUpdate(
                            javax.swing.event.DocumentEvent e
                    ) {

                        agendarSalvamentoHistoricoTexto();
                    }

                    @Override
                    public void changedUpdate(
                            javax.swing.event.DocumentEvent e
                    ) {

                        agendarSalvamentoHistoricoTexto();
                    }
                }
        );

        JScrollPane scrollTexto = new JScrollPane(
                caixaTextoErro
        );

        scrollTexto.setMinimumSize(
                new Dimension(
                        0,
                        180
                )
        );

        /*
         * A área de texto recebe todo o espaço vertical disponível.
         */
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        painelCentro.add(
                scrollTexto,
                gbc
        );

        /*
         * ------------------------------------------------------------
         * PROGRESSO
         * ------------------------------------------------------------
         */
        JPanel painelProgresso = new JPanel(
                new BorderLayout(0, 5)
        );

        JLabel lblProgresso = new JLabel("Progresso");

        barraProgresso = new JProgressBar(0, 100);

        /*
         * Mostra o percentual dentro da barra.
         */
        barraProgresso.setStringPainted(true);
        barraProgresso.setString("0%");

        /*
         * Altura maior para facilitar a visualização.
         */
        barraProgresso.setPreferredSize(
                new Dimension(0, 26)
        );

        barraProgresso.setMinimumSize(
                new Dimension(0, 26)
        );

        barraProgresso.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 26)
        );

        /*
         * Status textual da etapa atual.
         */
        lblStatusProgresso =
                new JLabel(
                        "Aguardando processamento..."
                );

        lblStatusProgresso.setFont(
                lblStatusProgresso.getFont().deriveFont(
                        Font.PLAIN,
                        11f
                )
        );

        lblStatusProgresso.setForeground(
                UIManager.getColor(
                        "Label.disabledForeground"
                )
        );

        painelProgresso.add(
                lblProgresso,
                BorderLayout.NORTH
        );

        JPanel painelBarra =
                new JPanel(
                        new BorderLayout(0, 3)
                );

        painelBarra.add(
                barraProgresso,
                BorderLayout.NORTH
        );

        painelBarra.add(
                lblStatusProgresso,
                BorderLayout.SOUTH
        );

        painelProgresso.add(
                painelBarra,
                BorderLayout.CENTER
        );

        /*
         * ------------------------------------------------------------
         * OPÇÕES
         * ------------------------------------------------------------
         */
        JPanel painelOpcoes = new JPanel();
        painelOpcoes.setLayout(new BoxLayout(
                painelOpcoes,
                BoxLayout.Y_AXIS
        ));

        JCheckBox chkAbrirPasta = new JCheckBox(
                "Abrir pasta após processamento"
        );

        chkAbrirPasta.setSelected(
                configuracao.isAbrirPasta()
        );

        chkAbrirPasta.addActionListener(e -> {
            configuracao.setAbrirPasta(
                    chkAbrirPasta.isSelected()
            );

            try {
                ConfiguracaoService.salvar(
                        configuracao
                );
            } catch (IOException ignored) {
            }
        });

        JCheckBox chkAbrirRelatorio = new JCheckBox(
                "Abrir relatório após processamento"
        );

        chkAbrirRelatorio.setSelected(
                configuracao.isAbrirRelatorio()
        );

        chkAbrirRelatorio.addActionListener(e -> {
            configuracao.setAbrirRelatorio(
                    chkAbrirRelatorio.isSelected()
            );

            try {
                ConfiguracaoService.salvar(
                        configuracao
                );
            } catch (IOException ignored) {
            }
        });

        JCheckBox chkAbrirLog = new JCheckBox(
                "Abrir log automaticamente quando houver erro"
        );

        chkAbrirLog.setSelected(
                configuracao.isAbrirLog()
        );

        chkAbrirLog.addActionListener(e -> {
            configuracao.setAbrirLog(
                    chkAbrirLog.isSelected()
            );

            try {
                ConfiguracaoService.salvar(
                        configuracao
                );
            } catch (IOException ignored) {
            }
        });

        chkAbrirPasta.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkAbrirRelatorio.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkAbrirLog.setAlignmentX(Component.LEFT_ALIGNMENT);

        painelOpcoes.add(chkAbrirPasta);
        painelOpcoes.add(chkAbrirRelatorio);
        painelOpcoes.add(chkAbrirLog);

        /*
         * ------------------------------------------------------------
         * BOTÃO PROCESSAR
         * ------------------------------------------------------------
         */
        btnProcessar =
                new JButton(
                        "Processar",
                        IconeUtil.processar()
                );

        btnProcessar.setIconTextGap(8);

        btnProcessar.setFont(
                btnProcessar.getFont().deriveFont(
                        Font.BOLD,
                        15f
                )
        );

        /*
         * Tamanho maior para dar destaque à ação principal.
         */
        btnProcessar.setPreferredSize(
                new Dimension(150, 42)
        );

        btnProcessar.setMinimumSize(
                new Dimension(150, 42)
        );

        btnProcessar.setMaximumSize(
                new Dimension(150, 42)
        );

        btnProcessar.setToolTipText(
                "Iniciar o processamento do JSON"
        );

        btnProcessar.addActionListener(
                e -> iniciarProcessamento()
        );

        /*
         * Centraliza o botão horizontalmente.
         */
        JPanel painelBotao = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        0,
                        5
                )
        );

        painelBotao.add(btnProcessar);

        /*
         * ------------------------------------------------------------
         * VERSÃO NO RODAPÉ
         * ------------------------------------------------------------
         */
        JLabel lblVersaoRodape =
                new JLabel(
                        VERSAO
                );

        lblVersaoRodape.setFont(
                lblVersaoRodape.getFont().deriveFont(
                        Font.PLAIN,
                        11f
                )
        );

        lblVersaoRodape.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        JPanel painelRodape =
                new JPanel(
                        new BorderLayout()
                );

        painelRodape.add(
                lblVersaoRodape,
                BorderLayout.EAST
        );

        /*
         * ============================================================
         * MONTAGEM FINAL
         * ============================================================
         */
        JPanel painelInferior = new JPanel(
                new BorderLayout(0, 8)
        );

        painelInferior.add(
                painelProgresso,
                BorderLayout.NORTH
        );

        painelInferior.add(
                painelOpcoes,
                BorderLayout.CENTER
        );

        painelInferior.add(
                painelBotao,
                BorderLayout.SOUTH
        );

        painelPrincipal.add(
                painelCabecalho,
                BorderLayout.NORTH
        );

        painelPrincipal.add(
                painelCentro,
                BorderLayout.CENTER
        );

        JPanel painelSul = new JPanel(
                new BorderLayout(0, 4)
        );

        painelSul.add(
                painelInferior,
                BorderLayout.CENTER
        );

        painelSul.add(
                painelRodape,
                BorderLayout.SOUTH
        );

        painelPrincipal.add(
                painelSul,
                BorderLayout.SOUTH
        );

        setContentPane(painelPrincipal);

        /*
         * Depois que a interface estiver montada,
         * coloca o foco no painel principal.
         *
         * Assim nenhum campo de arquivo começa focado.
         */
        SwingUtilities.invokeLater(
                painelPrincipal::requestFocusInWindow
        );
    }

    private void atualizarInterface() {

        configuracao.setModoEntrada(
                radioArquivo.isSelected()
                        ? "ARQUIVO"
                        : "TEXTO"
        );

        try {

            ConfiguracaoService.salvar(
                    configuracao
            );

        } catch (IOException ignored) {
        }

        if (radioArquivo.isSelected()) {

            btnSelecionarTxt.setEnabled(true);

            caixaTextoErro.setText("");

            caixaTextoErro.setEnabled(false);

            caixaTextoErro.setBackground(
                    UIManager.getColor(
                            "TextArea.disabledBackground"
                    )
            );

            caixaTextoErro.setForeground(
                    UIManager.getColor(
                            "TextArea.disabledForeground"
                    )
            );

        } else {

            arquivoErroPath = "";

            txtArquivoErro.setText("");

            txtArquivoErro.setToolTipText(
                    "Selecione ou arraste um arquivo TXT"
            );

            btnSelecionarTxt.setEnabled(false);

            caixaTextoErro.setEnabled(true);

            caixaTextoErro.setBackground(
                    UIManager.getColor(
                            "TextArea.background"
                    )
            );

            caixaTextoErro.setForeground(
                    UIManager.getColor(
                            "TextArea.foreground"
                    )
            );

            caixaTextoErro.setCaretColor(
                    UIManager.getColor(
                            "TextArea.caretForeground"
                    )
            );

            caixaTextoErro.requestFocus();
        }
    }

    private void selecionarTxt() {

        File arquivo =
                SeletorArquivo.selecionar(

                        this,

                        ultimoDiretorio,

                        "Selecione o TXT",

                        "Arquivos TXT",

                        "txt"

                );

        if (arquivo == null) {

            return;

        }

        selecionarArquivoTxt(
                arquivo
        );
    }

    private void selecionarArquivoTxt(
            File arquivo
    ) {

        if (arquivo == null) {
            return;
        }

        arquivoErroPath =
                arquivo.getAbsolutePath();

        ultimoDiretorio =
                arquivo.getParentFile();

        if (ultimoDiretorio != null) {

            configuracao.setUltimaPasta(
                    ultimoDiretorio.getAbsolutePath()
            );

            try {

                ConfiguracaoService.salvar(
                        configuracao
                );

            } catch (IOException ignored) {
            }
        }

        txtArquivoErro.setText(
                arquivo.getName()
        );

        txtArquivoErro.setToolTipText(
                arquivoErroPath
        );

        radioArquivo.setSelected(true);

        atualizarInterface();

        /*
         * Só salva no histórico quando já existem
         * os dois lados do vínculo:
         *
         * JSON + TXT
         */
        salvarHistoricoAtual();
    }

    private void padronizarBotaoHistorico(
            JButton botao,
            String descricaoAcessivel
    ) {

        Dimension tamanho = new Dimension(132, 34);

        botao.setPreferredSize(tamanho);
        botao.setMinimumSize(tamanho);
        botao.setMaximumSize(tamanho);
        botao.setIconTextGap(7);
        botao.setToolTipText(descricaoAcessivel);
        botao.getAccessibleContext().setAccessibleName(descricaoAcessivel);
    }

    private void abrirDialogHistorico() {

        List<HistoricoItem> arquivos;

        try {

            arquivos =
                    HistoricoService.carregar();

        } catch (IOException ex) {

            MensagemUtil.erro(
                    this,
                    "Erro ao carregar histórico: "
                            + ex.getMessage()
            );

            return;
        }

        JDialog dialog =
                new JDialog(
                        this,
                        "Arquivos recentes",
                        true
                );

        dialog.setSize(
                650,
                450
        );

        dialog.setLocationRelativeTo(
                this
        );

        dialog.setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        JPanel painelPrincipal =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        painelPrincipal.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        0,
                        10
                )
        );

        JLabel lblTitulo =
                new JLabel(
                        "Arquivos recentes"
                );

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        painelPrincipal.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        DefaultListModel<HistoricoItem> modelo =
                new DefaultListModel<>();

        for (HistoricoItem item : arquivos) {

            modelo.addElement(
                    item
            );
        }

        JList<HistoricoItem> lista =
                new JList<>(
                        modelo
                );

        lista.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        lista.setCellRenderer(
                new ArquivoRecenteRenderer()
        );

        lista.setFixedCellHeight(
                65
        );

        lista.setVisibleRowCount(
                5
        );

        JScrollPane scroll =
                new JScrollPane(
                        lista
                );

        lista.getInputMap(
                JComponent.WHEN_FOCUSED
        ).put(
                KeyStroke.getKeyStroke("ENTER"),
                "abrirHistorico"
        );

        lista.getActionMap().put(
                "abrirHistorico",
                new AbstractAction() {

                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent e
                    ) {

                        HistoricoItem itemSelecionado =
                                lista.getSelectedValue();

                        if (itemSelecionado == null) {

                            return;
                        }

                        File arquivo =
                                new File(
                                        itemSelecionado.getCaminhoJson()
                                );

                        if (!arquivo.exists()) {

                            MensagemUtil.aviso(
                                    dialog,
                                    "O arquivo selecionado não foi encontrado:\n\n"
                                            + arquivo.getAbsolutePath()
                            );

                            return;
                        }

                        selecionarArquivoHistorico(
                                itemSelecionado
                        );

                        dialog.dispose();
                    }
                }
        );


        JPanel painelConteudoHistorico =
                new JPanel(
                        new BorderLayout()
                );

        painelConteudoHistorico.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        0,
                        0,
                        0
                )
        );

        painelPrincipal.add(
                painelConteudoHistorico,
                BorderLayout.CENTER
        );

        JPanel painelBotoes =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                10
                        )
                );

        JButton btnAbrir =
                new JButton(
                        "Abrir",
                        IconeUtil.abrir()
                );

        JButton btnRemover =
                new JButton(
                        "Remover",
                        IconeUtil.remover()
                );

        JButton btnLimpar =
                new JButton(
                        "Limpar",
                        IconeUtil.limpar()
                );

        JButton btnCancelar =
                new JButton(
                        "Cancelar",
                        IconeUtil.cancelar()
                );

        padronizarBotaoHistorico(btnAbrir, "Abrir o arquivo recente selecionado");
        padronizarBotaoHistorico(btnRemover, "Remover o arquivo selecionado do histórico");
        padronizarBotaoHistorico(btnLimpar, "Limpar todos os arquivos recentes");
        padronizarBotaoHistorico(btnCancelar, "Fechar arquivos recentes");

        boolean possuiHistorico =
                !arquivos.isEmpty();

        btnAbrir.setEnabled(
                possuiHistorico
        );

        btnRemover.setEnabled(
                possuiHistorico
        );

        btnLimpar.setEnabled(
                possuiHistorico
        );

        Runnable atualizarBotoesHistorico =
                () -> {

                    boolean possuiItens =
                            !modelo.isEmpty();

                    btnAbrir.setEnabled(
                            possuiItens
                    );

                    btnRemover.setEnabled(
                            possuiItens
                    );

                    btnLimpar.setEnabled(
                            possuiItens
                    );

                    painelConteudoHistorico.removeAll();

                    if (possuiItens) {

                        painelConteudoHistorico.add(
                                scroll,
                                BorderLayout.CENTER
                        );

                    } else {

                        JLabel lblSemHistorico =
                                new JLabel(
                                        "<html><center>"
                                                + "<b>Nenhum arquivo recente</b>"
                                                + "<br><br>"
                                                + "Os arquivos utilizados aparecerão aqui "
                                                + "quando houver histórico."
                                                + "</center></html>",
                                        SwingConstants.CENTER
                                );

                        lblSemHistorico.setFont(
                                lblSemHistorico.getFont().deriveFont(
                                        Font.PLAIN,
                                        13f
                                )
                        );

                        painelConteudoHistorico.add(
                                lblSemHistorico,
                                BorderLayout.CENTER
                        );
                    }

                    painelConteudoHistorico.revalidate();
                    painelConteudoHistorico.repaint();
                };

        atualizarBotoesHistorico.run();

        if (!modelo.isEmpty()) {

            lista.setSelectedIndex(0);

        }

        /*
         * Atalho de teclado:
         * DELETE remove o item selecionado.
         */
        lista.getInputMap(
                JComponent.WHEN_FOCUSED
        ).put(
                KeyStroke.getKeyStroke("DELETE"),
                "removerHistorico"
        );

        lista.getActionMap().put(
                "removerHistorico",
                new AbstractAction() {

                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent e
                    ) {

                        HistoricoItem itemSelecionado =
                                lista.getSelectedValue();

                        if (itemSelecionado == null) {

                            return;
                        }

                        int resposta =
                                JOptionPane.showConfirmDialog(
                                        dialog,
                                        "Deseja remover este arquivo do histórico?\n\n"
                                                + new File(
                                                itemSelecionado.getCaminhoJson()
                                        ).getName()
                                                + "\n\n"
                                                + "O arquivo original não será excluído.",
                                        "Remover do histórico",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.WARNING_MESSAGE
                                );

                        if (resposta != JOptionPane.YES_OPTION) {

                            return;
                        }

                        int indiceSelecionado =
                                lista.getSelectedIndex();

                        try {

                            HistoricoService.remover(
                                    itemSelecionado.getCaminhoJson()
                            );

                            modelo.removeElement(
                                    itemSelecionado
                            );

                            atualizarBotoesHistorico.run();

                            if (!modelo.isEmpty()) {

                                int novoIndice =
                                        Math.min(
                                                indiceSelecionado,
                                                modelo.size() - 1
                                        );

                                lista.setSelectedIndex(
                                        novoIndice
                                );
                            }

                        } catch (IOException ex) {

                            MensagemUtil.erro(
                                    dialog,
                                    "Erro ao remover arquivo do histórico: "
                                            + ex.getMessage()
                            );
                        }
                    }
                }
        );

        painelBotoes.add(
                btnAbrir
        );

        painelBotoes.add(
                btnRemover
        );

        painelBotoes.add(
                btnLimpar
        );

        painelBotoes.add(
                btnCancelar
        );

        dialog.add(
                painelPrincipal,
                BorderLayout.CENTER
        );

        dialog.add(
                painelBotoes,
                BorderLayout.SOUTH
        );

        btnAbrir.addActionListener(e -> {

            HistoricoItem itemSelecionado =
                    lista.getSelectedValue();

            if (itemSelecionado == null) {

                MensagemUtil.aviso(
                        dialog,
                        "Selecione um arquivo."
                );

                return;
            }

            File arquivo =
                    new File(
                            itemSelecionado.getCaminhoJson()
                    );

            if (!arquivo.exists()) {

                MensagemUtil.aviso(
                        dialog,
                        "O arquivo selecionado não foi encontrado:\n\n"
                                + arquivo.getAbsolutePath()
                );

                return;
            }

            selecionarArquivoHistorico(
                    itemSelecionado
            );

            dialog.dispose();
        });

        btnRemover.addActionListener(e -> {

            int indiceSelecionado =
                    lista.getSelectedIndex();

            HistoricoItem itemSelecionado =
                    lista.getSelectedValue();

            if (itemSelecionado == null) {

                MensagemUtil.aviso(
                        dialog,
                        "Selecione um arquivo para remover."
                );

                return;
            }

            int resposta =
                    JOptionPane.showConfirmDialog(
                            dialog,
                            "Deseja remover este arquivo do histórico?\n\n"
                                    + new File(
                                    itemSelecionado.getCaminhoJson()
                            ).getName()
                                    + "\n\n"
                                    + "O arquivo original não será excluído.",
                            "Remover do histórico",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (resposta != JOptionPane.YES_OPTION) {

                return;
            }

            try {

                HistoricoService.remover(
                        itemSelecionado.getCaminhoJson()
                );

                modelo.removeElement(
                        itemSelecionado
                );

                atualizarBotoesHistorico.run();

                if (!modelo.isEmpty()) {

                    int novoIndice =
                            Math.min(
                                    indiceSelecionado,
                                    modelo.size() - 1
                            );

                    lista.setSelectedIndex(
                            novoIndice
                    );
                }

            } catch (IOException ex) {

                MensagemUtil.erro(
                        dialog,
                        "Erro ao remover arquivo do histórico: "
                                + ex.getMessage()
                );
            }
        });

        btnLimpar.addActionListener(e -> {

            if (modelo.isEmpty()) {

                return;
            }

            int resposta =
                    JOptionPane.showConfirmDialog(
                            dialog,
                            "Deseja limpar todos os arquivos recentes?",
                            "Limpar histórico",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

            if (resposta != JOptionPane.YES_OPTION) {

                return;
            }

            try {

                HistoricoService.limpar();

                modelo.clear();

                atualizarBotoesHistorico.run();

            } catch (IOException ex) {

                MensagemUtil.erro(
                        dialog,
                        "Erro ao limpar histórico: "
                                + ex.getMessage()
                );
            }
        });

        btnCancelar.addActionListener(
                e -> dialog.dispose()
        );

        lista.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        if (e.getClickCount() == 2) {

                            HistoricoItem itemSelecionado =
                                    lista.getSelectedValue();

                            if (itemSelecionado == null) {

                                return;
                            }

                            File arquivo =
                                    new File(
                                            itemSelecionado.getCaminhoJson()
                                    );

                            if (!arquivo.exists()) {

                                MensagemUtil.aviso(
                                        dialog,
                                        "O arquivo selecionado não foi encontrado:\n\n"
                                                + arquivo.getAbsolutePath()
                                );

                                return;
                            }

                            selecionarArquivoHistorico(
                                    itemSelecionado
                            );

                            dialog.dispose();
                        }
                    }
                }
        );

        dialog.setVisible(
                true
        );
    }

    private void selecionarArquivoHistorico(
            HistoricoItem item
    ) {

        if (item == null) {
            return;
        }

        arquivoJsonPath =
                item.getCaminhoJson();

        txtArquivoJson.setText(
                new File(
                        arquivoJsonPath
                ).getName()
        );

        txtArquivoJson.setToolTipText(
                arquivoJsonPath
        );

        File arquivo =
                new File(
                        arquivoJsonPath
                );

        ultimoDiretorio =
                arquivo.getParentFile();

        if (ultimoDiretorio != null) {

            configuracao.setUltimaPasta(
                    ultimoDiretorio.getAbsolutePath()
            );

            try {

                ConfiguracaoService.salvar(
                        configuracao
                );

            } catch (IOException ignored) {
            }
        }

        carregarOrigemHistorico(item);
    }

    private void selecionarArquivoJson(
            File arquivo
    ) {

        if (arquivo == null) {
            return;
        }

        arquivoJsonPath =
                arquivo.getAbsolutePath();

        txtArquivoJson.setText(
                arquivo.getName()
        );

        txtArquivoJson.setToolTipText(
                arquivoJsonPath
        );

        ultimoDiretorio =
                arquivo.getParentFile();

        if (ultimoDiretorio != null) {

            configuracao.setUltimaPasta(
                    ultimoDiretorio.getAbsolutePath()
            );

            try {

                ConfiguracaoService.salvar(
                        configuracao
                );

            } catch (IOException ignored) {
            }
        }

        if (radioTexto.isSelected()) {
            return;
        }

        /*
         * Verifica se este JSON já possui
         * histórico salvo.
         */
        try {

            HistoricoItem item =
                    HistoricoService.encontrar(
                            arquivoJsonPath
                    );

            /*
             * JSON já possui histórico.
             *
             * Recupera automaticamente a origem
             * do erro: TXT ou texto colado.
             */
            if (item != null) {

                carregarOrigemHistorico(item);

                return;
            }

        } catch (IOException ex) {

            MensagemUtil.erro(
                    this,
                    "Erro ao consultar o histórico do JSON:\n"
                            + ex.getMessage()
            );

            return;
        }

        /*
         * JSON novo.
         *
         * Não existe histórico associado.
         * Portanto, limpa qualquer TXT ou texto
         * que tenha ficado do JSON anterior.
         */
        limparOrigemSemHistorico();
    }

    private void limparOrigemSemHistorico() {

        arquivoErroPath = "";

        ultimoTextoColadoHistorico = "";

        txtArquivoErro.setText("");

        txtArquivoErro.setToolTipText(
                "Selecione ou arraste um arquivo TXT"
        );

        caixaTextoErro.setText("");

        /*
         * JSON novo:
         *
         * Não existe histórico associado.
         * Portanto, não recupera TXT nem texto.
         */
        radioArquivo.setSelected(true);

        atualizarInterface();
    }

    private void salvarModoEntradaConfiguracao() {

        try {

            ConfiguracaoService.salvar(
                    configuracao
            );

        } catch (IOException ignored) {
        }
    }

    private void carregarOrigemHistorico(
            HistoricoItem item
    ) {

        if (item == null) {
            return;
        }

        carregandoHistorico = true;

        try {

            /*
             * HISTÓRICO DE ARQUIVO TXT
             */
            if (item.isArquivo()) {

                radioArquivo.setSelected(true);

                configuracao.setModoEntrada("ARQUIVO");

                salvarModoEntradaConfiguracao();

                arquivoErroPath =
                        item.getCaminhoTxt();

                ultimoTextoColadoHistorico = "";

                caixaTextoErro.setText("");

                if (arquivoErroPath == null
                        || arquivoErroPath.isBlank()) {

                    /*
                     * Histórico incompleto.
                     *
                     * Não trata isso como erro.
                     *
                     * O JSON continua selecionado e o usuário
                     * pode escolher novamente o TXT.
                     */

                    arquivoErroPath = "";

                    txtArquivoErro.setText("");

                    btnSelecionarTxt.setEnabled(true);

                    caixaTextoErro.setEnabled(false);

                    caixaTextoErro.setBackground(
                            UIManager.getColor(
                                    "TextArea.disabledBackground"
                            )
                    );

                    caixaTextoErro.setForeground(
                            UIManager.getColor(
                                    "TextArea.disabledForeground"
                            )
                    );

                    /*
                     * Não mostra erro aqui.
                     *
                     * Quando o TXT for selecionado,
                     * salvarHistoricoAtual() substituirá
                     * o registro incompleto pelo vínculo correto.
                     */

                    return;
                }

                txtArquivoErro.setText(
                        new File(
                                arquivoErroPath
                        ).getName()
                );

                txtArquivoErro.setToolTipText(
                        arquivoErroPath
                );

                File arquivoTxt =
                        new File(
                                arquivoErroPath
                        );

                if (!arquivoTxt.exists()) {

                    MensagemUtil.aviso(
                            this,
                            "O arquivo TXT associado ao JSON não foi encontrado:\n\n"
                                    + arquivoErroPath
                                    + "\n\nSelecione outro arquivo TXT."
                    );

                    arquivoErroPath = "";

                    txtArquivoErro.setText("");
                }

                btnSelecionarTxt.setEnabled(true);

                caixaTextoErro.setEnabled(false);

                caixaTextoErro.setBackground(
                        UIManager.getColor(
                                "TextArea.disabledBackground"
                        )
                );

                caixaTextoErro.setForeground(
                        UIManager.getColor(
                                "TextArea.disabledForeground"
                        )
                );

                return;
            }

            /*
             * HISTÓRICO DE TEXTO COLADO
             */
            if (item.isTexto()) {

                radioTexto.setSelected(true);

                configuracao.setModoEntrada("TEXTO");

                salvarModoEntradaConfiguracao();

                arquivoErroPath = "";

                txtArquivoErro.setText("");

                ultimoTextoColadoHistorico =
                        item.getTextoColado();

                if (ultimoTextoColadoHistorico == null
                        || ultimoTextoColadoHistorico.isBlank()) {

                    MensagemUtil.aviso(
                            this,
                            "O histórico deste JSON está incompleto.\n\n"
                                    + "Ele foi salvo como TEXTO, mas não possui "
                                    + "texto colado associado."
                    );

                    limparOrigemSemHistorico();

                    return;
                }

                btnSelecionarTxt.setEnabled(false);

                caixaTextoErro.setEnabled(true);

                caixaTextoErro.setBackground(
                        UIManager.getColor(
                                "TextArea.background"
                        )
                );

                caixaTextoErro.setForeground(
                        UIManager.getColor(
                                "TextArea.foreground"
                        )
                );

                caixaTextoErro.setCaretColor(
                        UIManager.getColor(
                                "TextArea.caretForeground"
                        )
                );

                caixaTextoErro.setText(
                        ultimoTextoColadoHistorico
                );

                caixaTextoErro.setCaretPosition(0);

                return;
            }

            /*
             * Histórico inválido ou desconhecido.
             *
             * Não mantém dados do JSON anterior.
             */
            limparOrigemSemHistorico();

        } finally {

            carregandoHistorico = false;
        }
    }

    // NOVO MÉTODO

    private void agendarSalvamentoHistoricoTexto() {

        if (carregandoHistorico) {
            return;
        }

        timerHistorico.restart();
    }

    private void salvarHistoricoTextoSeCompleto() {

        if (carregandoHistorico) {
            return;
        }

        if (!radioTexto.isSelected()) {
            return;
        }

        if (arquivoJsonPath == null
                || arquivoJsonPath.isBlank()) {

            return;
        }

        String texto =
                caixaTextoErro.getText();

        if (texto == null
                || texto.isBlank()) {

            return;
        }

        /*
         * Salva o histórico somente se o texto atual
         * for diferente do último texto já persistido.
         */
        if (texto.equals(
                ultimoTextoColadoHistorico
        )) {

            return;
        }

        salvarHistoricoAtual();
    }

    private void salvarHistoricoAtual() {

        /*
         * Nunca salva histórico enquanto um item
         * está sendo carregado do histórico.
         *
         * Isso evita que alterações automáticas
         * dos componentes da interface sejam
         * interpretadas como alterações feitas
         * pelo usuário.
         */
        if (carregandoHistorico) {

            return;

        }

        /*
         * Sem JSON não existe vínculo para salvar.
         */
        if (arquivoJsonPath == null
                || arquivoJsonPath.isBlank()) {

            return;

        }

        HistoricoItem item;

        /*
         * =====================================================
         * ENTRADA POR ARQUIVO TXT
         * =====================================================
         */
        if (radioArquivo.isSelected()) {

            /*
             * O histórico de ARQUIVO só pode ser salvo
             * quando existe um TXT associado.
             */
            if (arquivoErroPath == null
                    || arquivoErroPath.isBlank()) {

                return;

            }

            item =
                    new HistoricoItem(
                            arquivoJsonPath,
                            "ARQUIVO",
                            arquivoErroPath,
                            ""
                    );

        }

        /*
         * =====================================================
         * ENTRADA POR TEXTO COLADO
         * =====================================================
         */
        else {

            String texto =
                    caixaTextoErro.getText();

            /*
             * Não salva histórico com texto vazio.
             */
            if (texto == null
                    || texto.isBlank()) {

                return;

            }

            item =
                    new HistoricoItem(
                            arquivoJsonPath,
                            "TEXTO",
                            "",
                            texto
                    );
        }

        /*
         * =====================================================
         * SALVA O VÍNCULO
         * =====================================================
         */
        try {

            HistoricoService.adicionar(
                    item,
                    configuracao.getHistoricoMaximo()
            );

            /*
             * Mantém registrado o último texto que foi
             * efetivamente salvo no histórico.
             *
             * Isso deixa a variável sincronizada com
             * o conteúdo persistido.
             */
            if (item.isTexto()) {

                ultimoTextoColadoHistorico =
                        item.getTextoColado();

            } else {

                ultimoTextoColadoHistorico = "";

            }

        } catch (IOException ex) {

            ex.printStackTrace();

            MensagemUtil.erro(
                    this,
                    "Erro ao salvar o histórico:\n"
                            + ex.getMessage()
            );
        }
    }

    private void selecionarJson() {

        File arquivo =
                SeletorArquivo.selecionar(

                        this,

                        ultimoDiretorio,

                        "Selecione o JSON",

                        "Arquivos JSON",

                        "json"

                );

        if (arquivo == null) {

            return;

        }

        selecionarArquivoJson(
                arquivo
        );

    }

    private void iniciarProcessamento() {


        /*
         * Garante que o histórico da entrada atual
         * seja salvo antes do processamento.
         *
         * Isso é especialmente importante para
         * texto colado, pois normalmente o histórico
         * é salvo pelo Timer após o usuário parar
         * de digitar.
         */
        timerHistorico.stop();

        salvarHistoricoAtual();

        try {

            conteudoErro =
                    ProcessamentoService.prepararConteudoErro(
                            radioArquivo.isSelected(),
                            arquivoErroPath,
                            caixaTextoErro.getText(),
                            arquivoJsonPath
                    );

        } catch (IllegalArgumentException ex) {

            MensagemUtil.erro(
                    this,
                    ex.getMessage()
            );

            return;

        } catch (IOException ex) {

            MensagemUtil.erro(
                    this,
                    "Erro ao ler o TXT: "
                            + ex.getMessage()
            );

            return;

        } catch (Exception ex) {

            ex.printStackTrace();

            MensagemUtil.erro(
                    this,
                    ex.getMessage()
            );

            return;
        }

        atualizarProgresso(0);

        btnProcessar.setEnabled(false);

        ProcessamentoWorker worker =
                new ProcessamentoWorker();

        worker.addPropertyChangeListener(
                evt -> {

                    if ("progress".equals(
                            evt.getPropertyName()
                    )) {

                        int progresso =
                                (Integer) evt.getNewValue();

                        barraProgresso.setValue(
                                progresso
                        );

                        barraProgresso.setString(
                                progresso + "%"
                        );

                        if (progresso <= 5) {

                            lblStatusProgresso.setText(
                                    "Analisando arquivo de erros..."
                            );

                        } else if (progresso <= 25) {

                            lblStatusProgresso.setText(
                                    "Carregando e analisando o JSON..."
                            );

                        } else if (progresso < 100) {

                            lblStatusProgresso.setText(
                                    "Atualizando registros do JSON..."
                            );

                        } else if (progresso == 100) {

                            lblStatusProgresso.setText(
                                    "Processamento concluído."
                            );
                        }
                    }
                }
        );

        worker.execute();
    }

    private class ProcessamentoWorker
            extends SwingWorker<ContextoProcessamento, Void> {

        @Override
        protected ContextoProcessamento doInBackground()
                throws Exception {

            return ProcessamentoService.executar(
                    conteudoErro,
                    arquivoErroPath,
                    arquivoJsonPath,
                    configuracao.getHistoricoMaximo(),
                    this::setProgress
            );
        }

        @Override
        protected void done() {

            btnProcessar.setEnabled(true);

            try {

                ContextoProcessamento contexto =
                        get();

                /*
                 * Garante que a barra chegue a 100%
                 * e que o status mostre a conclusão
                 * antes da janela de resumo ser aberta.
                 */
                atualizarProgresso(100);

                /*
                 * Permite que a interface gráfica seja
                 * atualizada visualmente antes de abrir
                 * a janela de processamento concluído.
                 */
                SwingUtilities.invokeLater(
                        () -> mostrarResumoFinal(contexto)
                );

            } catch (InterruptedException ex) {

                Thread.currentThread().interrupt();

                MensagemUtil.erro(
                        IndicadorReal.this,
                        "O processamento foi interrompido."
                );

                atualizarProgresso(0);

            } catch (java.util.concurrent.CancellationException ex) {

                MensagemUtil.aviso(
                        IndicadorReal.this,
                        "O processamento foi cancelado."
                );

                atualizarProgresso(0);

            } catch (java.util.concurrent.ExecutionException ex) {

                Throwable causa =
                        ex.getCause();

                tratarErroProcessamento(
                        causa
                );
            }
        }
    }

    private void atualizarProgresso(
            int progresso
    ) {

        int valor =
                Math.max(
                        0,
                        Math.min(
                                100,
                                progresso
                        )
                );

        barraProgresso.setValue(
                valor
        );

        barraProgresso.setString(
                valor + "%"
        );

        if (valor == 0) {

            lblStatusProgresso.setText(
                    "Iniciando processamento..."
            );

        } else if (valor <= 5) {

            lblStatusProgresso.setText(
                    "Analisando arquivo de erros..."
            );

        } else if (valor <= 25) {

            lblStatusProgresso.setText(
                    "Carregando e analisando o JSON..."
            );

        } else if (valor < 100) {

            lblStatusProgresso.setText(
                    "Atualizando registros do JSON..."
            );

        } else {

            lblStatusProgresso.setText(
                    "Processamento concluído."
            );
        }
    }

    private void tratarErroProcessamento(
            Throwable erro
    ) {

        if (erro == null) {

            erro =
                    new Exception(
                            "Erro desconhecido durante o processamento."
                    );
        }

        erro.printStackTrace();

        MensagemUtil.erro(
                this,
                erro.getMessage() != null
                        ? erro.getMessage()
                        : erro.toString()
        );

        atualizarProgresso(0);
    }

    private void limparInterface() {
        arquivoErroPath = "";
        arquivoJsonPath = "";
        conteudoErro = "";
        ultimoTextoColadoHistorico = "";

        txtArquivoErro.setText("");
        txtArquivoErro.setToolTipText(
                "Selecione ou arraste um arquivo TXT"
        );
        txtArquivoJson.setText("");
        txtArquivoJson.setToolTipText(
                "Selecione ou arraste um arquivo JSON"
        );
        caixaTextoErro.setText("");
        barraProgresso.setValue(0);

        barraProgresso.setString(
                "0%"
        );

        lblStatusProgresso.setText(
                "Aguardando processamento..."
        );
        atualizarInterface();
    }

    private void mostrarResumoFinal(
            ContextoProcessamento contexto
    ) {

        if (configuracao.isAbrirPasta()) {

            try {

                Desktop.getDesktop().open(
                        contexto
                                .getArquivoSaida()
                                .getParentFile()
                );

            } catch (Exception ignored) {
            }
        }

        if (configuracao.isAbrirRelatorio()) {

            try {

                Desktop.getDesktop().open(
                        contexto
                                .getArquivoRelatorio()
                );

            } catch (Exception ignored) {
            }
        }

        new ResumoDialog(
                this,
                VERSAO,
                contexto
        ).setVisible(true);

        limparInterface();
    }

    public static void main(String[] args) {

        try {

            ExecucaoLog.iniciar();

            ExecucaoLog.info(
                    "Programa iniciado."
            );

            WindowsTheme.aplicarTemaInicial();

            WindowsTheme.iniciarMonitor();

            SwingUtilities.invokeLater(() -> {

                try {

                    new IndicadorReal().setVisible(true);

                } catch (Throwable ex) {

                    ex.printStackTrace();

                    try {

                        ExecucaoLog.erro(ex);

                    } catch (IOException ignored) {
                    }

                    JOptionPane.showMessageDialog(
                            null,
                            ex.toString(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );

                }

            });

        } catch (Throwable ex) {

            ex.printStackTrace();

            try {

                ExecucaoLog.erro(ex);

            } catch (IOException ignored) {
            }

            JOptionPane.showMessageDialog(
                    null,
                    ex.toString(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
