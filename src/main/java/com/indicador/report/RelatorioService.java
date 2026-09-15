package com.indicador.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public final class RelatorioService {

    private static final String SEPARADOR = "=".repeat(50);

    private RelatorioService() {
    }

    public static void gerarRelatorio(
            File relatorioFile,
            String arquivoErro,
            String arquivoJson,
            int totalTxt,
            int alteracoes,
            int corretas,
            Set<String> ceps,
            Set<String> varios,
            List<String> naoEncontradas,
            List<String> detalhes
    ) throws IOException {

        try (PrintWriter writer = new PrintWriter(
                Files.newBufferedWriter(
                        relatorioFile.toPath(),
                        StandardCharsets.UTF_8))) {

            writer.println("RELATÓRIO DE CORREÇÕES");
            writer.println(SEPARADOR);
            writer.println();

            String nomeTxt =
                    arquivoErro.isBlank()
                            ? "Texto colado"
                            : new File(arquivoErro).getName();

            writer.printf(
                    "Arquivo TXT.......................: %s%n",
                    nomeTxt);

            writer.printf(
                    "Arquivo JSON......................: %s%n%n",
                    new File(arquivoJson).getName());

            writer.printf(
                    "%-40s%d%n",
                    "Total de matrículas únicas no TXT:",
                    totalTxt);

            writer.printf(
                    "%-40s%d%n",
                    "Alterações realizadas:",
                    alteracoes);

            writer.printf(
                    "%-40s%d%n%n",
                    "Já estavam corretas:",
                    corretas);

            writer.printf(
                    "%-40s%d%n",
                    "CEP inválido(s) ignorado(s):",
                    ceps.size());

            if (!ceps.isEmpty()) {

                List<String> lista =
                        new ArrayList<>(ceps);

                lista.sort(
                        Comparator.comparingInt(Integer::parseInt));

                writer.println(
                        "Matrícula(s): "
                                + String.join(", ", lista));
            }

            writer.println();

            writer.printf(
                    "%-40s%d%n",
                    "Vários endereços encontrado(s):",
                    varios.size());

            if (!varios.isEmpty()) {

                List<String> lista =
                        new ArrayList<>(varios);

                lista.sort(
                        Comparator.comparingInt(Integer::parseInt));

                writer.println(
                        "Matrícula(s): "
                                + String.join(", ", lista));
            }

            writer.println();

            writer.printf(
                    "%-40s%d%n",
                    "Matrículas não encontradas no JSON:",
                    naoEncontradas.size());

            if (!naoEncontradas.isEmpty()) {

                writer.println();

                for (String matricula : naoEncontradas) {

                    writer.println("  - " + matricula);

                }

            }

            writer.println();
            writer.println(SEPARADOR);
            writer.println();
            writer.println("DETALHAMENTO DAS ALTERAÇÕES");
            writer.println(SEPARADOR);
            writer.println();

            for (String texto : detalhes) {

                writer.println(texto);

            }

        }

    }

}