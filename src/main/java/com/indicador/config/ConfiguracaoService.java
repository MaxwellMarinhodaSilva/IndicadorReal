package com.indicador.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfiguracaoService {

    private static final String NOME_ARQUIVO =
            "config.properties";

    private ConfiguracaoService() {
    }

    public static Configuracao carregar()
            throws IOException {

        Configuracao configuracao =
                new Configuracao();

        File arquivo =
                new File(
                        System.getProperty("user.dir"),
                        NOME_ARQUIVO
                );

        if (!arquivo.exists()) {

            return configuracao;

        }

        Properties properties =
                new Properties();

        try (FileInputStream input =
                     new FileInputStream(arquivo)) {

            properties.load(input);

        }

        configuracao.setUltimaPasta(
                properties.getProperty(
                        "ultimaPasta",
                        ""
                )
        );

        configuracao.setModoEntrada(
                properties.getProperty(
                        "modoEntrada",
                        "ARQUIVO"
                )
        );

        configuracao.setPosicaoX(
                Integer.parseInt(
                        properties.getProperty(
                                "posicaoX",
                                "-1"
                        )
                )
        );

        configuracao.setPosicaoY(
                Integer.parseInt(
                        properties.getProperty(
                                "posicaoY",
                                "-1"
                        )
                )
        );

        configuracao.setLargura(
                Integer.parseInt(
                        properties.getProperty(
                                "largura",
                                "700"
                        )
                )
        );

        configuracao.setAltura(
                Integer.parseInt(
                        properties.getProperty(
                                "altura",
                                "650"
                        )
                )
        );

        configuracao.setMostrarBarra(
                Boolean.parseBoolean(
                        properties.getProperty(
                                "mostrarBarra",
                                "true"
                        )
                )
        );

        configuracao.setAbrirPasta(
                Boolean.parseBoolean(
                        properties.getProperty(
                                "abrirPasta",
                                "false"
                        )
                )
        );

        configuracao.setAbrirRelatorio(
                Boolean.parseBoolean(
                        properties.getProperty(
                                "abrirRelatorio",
                                "false"
                        )
                )
        );

        configuracao.setAbrirLog(
                Boolean.parseBoolean(
                        properties.getProperty(
                                "abrirLog",
                                "false"
                        )
                )
        );

        configuracao.setHistoricoMaximo(
                Integer.parseInt(
                        properties.getProperty(
                                "historicoMaximo",
                                "20"
                        )
                )
        );

        return configuracao;

    }

    public static void salvar(
            Configuracao configuracao
    ) throws IOException {

        Properties properties =
                new Properties();

        properties.setProperty(
                "ultimaPasta",
                configuracao.getUltimaPasta()
        );

        properties.setProperty(
                "modoEntrada",
                configuracao.getModoEntrada()
        );

        properties.setProperty(
                "posicaoX",
                String.valueOf(
                        configuracao.getPosicaoX()
                )
        );

        properties.setProperty(
                "posicaoY",
                String.valueOf(
                        configuracao.getPosicaoY()
                )
        );

        properties.setProperty(
                "largura",
                String.valueOf(
                        configuracao.getLargura()
                )
        );

        properties.setProperty(
                "altura",
                String.valueOf(
                        configuracao.getAltura()
                )
        );

        properties.setProperty(
                "mostrarBarra",
                String.valueOf(
                        configuracao.isMostrarBarra()
                )
        );

        properties.setProperty(
                "abrirPasta",
                String.valueOf(
                        configuracao.isAbrirPasta()
                )
        );

        properties.setProperty(
                "abrirRelatorio",
                String.valueOf(
                        configuracao.isAbrirRelatorio()
                )
        );

        properties.setProperty(
                "abrirLog",
                String.valueOf(
                        configuracao.isAbrirLog()
                )
        );

        properties.setProperty(
                "historicoMaximo",
                String.valueOf(
                        configuracao.getHistoricoMaximo()
                )
        );

        File arquivo =
                new File(
                        System.getProperty("user.dir"),
                        NOME_ARQUIVO
                );

        try (FileOutputStream output =
                     new FileOutputStream(arquivo)) {

            properties.store(
                    output,
                    "Configuracoes do Indicador Real"
            );

        }

    }

}