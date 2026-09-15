package com.indicador.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LogService {

    private static final DateTimeFormatter NOME_ARQUIVO =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private static final DateTimeFormatter DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private LogService() {
    }

    @SuppressWarnings("unused")
    public static File criarArquivoLog(
            String caminhoArquivoJson
    ) throws IOException {

        File pastaAplicacao =
                new File(
                        System.getProperty("user.dir")
                );

        File pastaLog =
                new File(
                        pastaAplicacao,
                        "Log" + File.separator + "Processamento"
                );

        if (!pastaLog.exists()) {

            pastaLog.mkdirs();

        }

        String nome =
                "IndicadorReal_Log_"
                        + LocalDateTime.now().format(NOME_ARQUIVO)
                        + ".txt";

        File arquivoLog =
                new File(
                        pastaLog,
                        nome
                );

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             arquivoLog.toPath(),
                             StandardCharsets.UTF_8
                     )) {

            writer.write("==================================================");
            writer.newLine();

            writer.write("Indicador Real");
            writer.newLine();

            writer.write("==================================================");
            writer.newLine();
            writer.newLine();

            writer.write(
                    "Data/Hora: "
                            + LocalDateTime.now().format(DATA_HORA)
            );

            writer.newLine();
            writer.newLine();

        }

        return arquivoLog;

    }

    public static void escrever(
            File arquivoLog,
            String texto
    ) throws IOException {

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             arquivoLog.toPath(),
                             StandardCharsets.UTF_8,
                             java.nio.file.StandardOpenOption.APPEND
                     )) {

            writer.write(texto);

            writer.newLine();

        }

    }

    public static void escreverErro(
            File arquivoLog,
            Throwable erro
    ) throws IOException {

        escrever(
                arquivoLog,
                ""
        );

        escrever(
                arquivoLog,
                "================ ERRO ================"
        );

        escrever(
                arquivoLog,
                erro.toString()
        );

        for (StackTraceElement item : erro.getStackTrace()) {

            escrever(
                    arquivoLog,
                    "    at " + item
            );

        }

    }

}