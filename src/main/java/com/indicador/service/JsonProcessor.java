package com.indicador.service;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indicador.model.ProcessamentoResultado;
import com.indicador.report.RelatorioService;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.IntConsumer;


public final class JsonProcessor {

    private JsonProcessor() {
    }

    public static ProcessamentoResultado processarAlteracoes(
            List<Map<String, Object>> listaReais,
            Map<String, Integer> matriculasParaCorrigir,
            IntConsumer progressoCallback
    ) {

        return JsonUpdater.atualizarTipoEnvio(
                listaReais,
                matriculasParaCorrigir,
                progressoCallback
        );

    }

    public static void salvarJson(

            Map<String, Object> dadosJson,

            File arquivoSaida

    ) throws Exception {

        ObjectMapper mapper =
                new ObjectMapper();

        mapper.findAndRegisterModules();

        DefaultPrettyPrinter pp =
                new DefaultPrettyPrinter();

        pp =
                pp.withObjectIndenter(
                        new DefaultIndenter(
                                "  ",
                                "\n"
                        )
                );

        pp =
                pp.withArrayIndenter(
                        new DefaultIndenter(
                                "  ",
                                "\n"
                        )
                );

        String jsonFinal =
                mapper.writer(pp)
                        .writeValueAsString(
                                dadosJson
                        );

        jsonFinal =
                jsonFinal.replaceAll(
                        "\"\\s*:\\s*",
                        "\": "
                );

        Files.write(

                arquivoSaida.toPath(),

                jsonFinal.getBytes(
                        StandardCharsets.UTF_8
                )

        );

    }

    public static void gerarRelatorio(

            File arquivoRelatorio,

            String arquivoErroPath,

            String arquivoJsonPath,

            int totalMatriculas,

            int alteracoesRealizadas,

            int jaEstavamCorretas,

            Set<String> cepsInvalidos,

            Set<String> variosEnderecos,

            List<String> naoEncontradas,

            List<String> relatorio

    ) throws Exception {

        RelatorioService.gerarRelatorio(

                arquivoRelatorio,

                arquivoErroPath,

                arquivoJsonPath,

                totalMatriculas,

                alteracoesRealizadas,

                jaEstavamCorretas,

                cepsInvalidos,

                variosEnderecos,

                naoEncontradas,

                relatorio

        );

    }

}