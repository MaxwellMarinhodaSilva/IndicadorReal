package com.indicador.service;

import com.indicador.model.ProcessamentoResultado;

import javax.swing.*;
import java.util.*;
import java.util.function.IntConsumer;

public final class JsonUpdater {

    private JsonUpdater() {
    }

    public static ProcessamentoResultado atualizarTipoEnvio(
            List<Map<String, Object>> listaReais,
            Map<String, Integer> matriculasParaCorrigir,
            IntConsumer progressoCallback
    ) {

        ProcessamentoResultado resultado =
                new ProcessamentoResultado();

        int alteracoesRealizadas = 0;

        int jaEstavamCorretas = 0;

        Set<String> matriculasEncontradasJson =
                new HashSet<>();

        List<String> relatorio =
                new ArrayList<>();

        int totalRegistros =
                listaReais.size();

        for (int indice = 0; indice < totalRegistros; indice++) {

            final int progresso =
                    35 + (int) (((double) (indice + 1) / totalRegistros) * 60);

            SwingUtilities.invokeLater(() ->
                    progressoCallback.accept(progresso));

            Map<String, Object> registro =
                    listaReais.get(indice);

            String matriculaJson =
                    String.valueOf(
                            registro.getOrDefault(
                                    "NUMERO_REGISTRO",
                                    "")
                    ).trim();

            if (!matriculasParaCorrigir.containsKey(matriculaJson)) {
                continue;
            }

            matriculasEncontradasJson.add(matriculaJson);

            int novoTipoenvio =
                    matriculasParaCorrigir.get(matriculaJson);

            Object tipoObj =
                    registro.get("TIPOENVIO");

            Integer tipoAntigo = null;

            if (tipoObj instanceof Number) {

                tipoAntigo =
                        ((Number) tipoObj).intValue();

            } else if (tipoObj != null) {

                try {

                    tipoAntigo =
                            Integer.parseInt(tipoObj.toString());

                } catch (Exception ignored) {
                }

            }

            if (!Objects.equals(tipoAntigo, novoTipoenvio)) {

                registro.put(
                        "TIPOENVIO",
                        novoTipoenvio
                );

                alteracoesRealizadas++;

                String texto =
                        "Matrícula "
                                + matriculaJson
                                + ": "
                                + (tipoAntigo == null ? "None" : tipoAntigo)
                                + " -> "
                                + novoTipoenvio;

                System.out.println(texto);

                relatorio.add(texto);

            } else {

                jaEstavamCorretas++;

                String texto =
                        "Matrícula "
                                + matriculaJson
                                + ": "
                                + tipoAntigo
                                + " -> "
                                + novoTipoenvio
                                + " (já estava correta)";

                System.out.println(texto);

                relatorio.add(texto);

            }

        }

        resultado.setAlteracoesRealizadas(
                alteracoesRealizadas
        );

        resultado.setJaEstavamCorretas(
                jaEstavamCorretas
        );

        resultado.setMatriculasEncontradasJson(
                matriculasEncontradasJson
        );

        resultado.setRelatorio(
                relatorio
        );

        return resultado;

    }

}