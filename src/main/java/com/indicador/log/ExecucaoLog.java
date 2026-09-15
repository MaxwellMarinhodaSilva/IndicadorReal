package com.indicador.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ExecucaoLog {

    private static BufferedWriter writer;

    private static final DateTimeFormatter HORA =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final DateTimeFormatter DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private ExecucaoLog() {
    }

    public static void iniciar() throws IOException {

        File pastaAplicacao =
                new File(
                        System.getProperty("user.dir")
                );

        File pastaLog =
                new File(
                        pastaAplicacao,
                        "Log" + File.separator + "Execucao"
                );

        if (!pastaLog.exists()) {

            pastaLog.mkdirs();

        }

        File arquivoLog =
                new File(
                        pastaLog,
                        "Execucao_"
                                + LocalDate.now()
                                + ".log"
                );

        boolean novoArquivo =
                !arquivoLog.exists();

        writer =
                Files.newBufferedWriter(
                        arquivoLog.toPath(),
                        StandardCharsets.UTF_8,
                        java.nio.file.StandardOpenOption.CREATE,
                        java.nio.file.StandardOpenOption.APPEND
                );

        if (novoArquivo) {

            writer.write("==================================================");
            writer.newLine();

            writer.write("Indicador Real - Log de Execução");
            writer.newLine();

            writer.write("==================================================");
            writer.newLine();
            writer.newLine();

            writer.write(
                    String.format(
                            "%-20s %s",
                            "Data:",
                            LocalDate.now().format(DATA)
                    )
            );
            writer.newLine();

            writer.write(
                    String.format(
                            "%-20s %s",
                            "Java:",
                            System.getProperty("java.version")
                    )
            );
            writer.newLine();

            writer.write(
                    String.format(
                            "%-20s %s",
                            "Sistema:",
                            System.getProperty("os.name")
                    )
            );
            writer.newLine();

            writer.write(
                    String.format(
                            "%-20s %s",
                            "Arquitetura:",
                            System.getProperty("os.arch")
                    )
            );
            writer.newLine();

            writer.write(
                    String.format(
                            "%-20s %s",
                            "Usuário:",
                            System.getProperty("user.name")
                    )
            );
            writer.newLine();

            writer.write(
                    String.format(
                            "%-20s %s",
                            "Pasta da aplicação:",
                            System.getProperty("user.dir")
                    )
            );
            writer.newLine();

            writer.newLine();

            writer.write("--------------------------------------------------");
            writer.newLine();
            writer.newLine();

            writer.flush();

        }

    }

    public static void info(
            String mensagem
    ) throws IOException {

        escrever(
                LocalDateTime.now().format(HORA)
                        + " - "
                        + mensagem
        );

    }

    public static void erro(
            Throwable erro
    ) throws IOException {

        escrever("");

        escrever("================ ERRO ================");

        escrever(
                LocalDateTime.now().format(HORA)
                        + " - "
                        + erro.toString()
        );

        for (StackTraceElement item : erro.getStackTrace()) {

            escrever(
                    "    at " + item
            );

        }

    }

    public static void fechar() {

        if (writer == null) {

            return;

        }

        try {

            writer.close();

        } catch (IOException ignored) {
        }

        writer = null;

    }

    private static void escrever(
            String texto
    ) throws IOException {

        if (writer == null) {

            return;

        }

        writer.write(texto);

        writer.newLine();

        writer.flush();

    }

}