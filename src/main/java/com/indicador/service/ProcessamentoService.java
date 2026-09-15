package com.indicador.service;

import com.indicador.config.HistoricoService;
import com.indicador.io.ArquivoUtil;
import com.indicador.log.LogService;
import com.indicador.model.ArquivosGerados;
import com.indicador.model.ContextoProcessamento;
import com.indicador.model.JsonData;
import com.indicador.model.ProcessamentoResultado;
import com.indicador.parser.TxtProcessamentoResultado;
import com.indicador.parser.TxtProcessor;

import java.io.File;
import java.util.*;
import java.util.function.IntConsumer;

public final class ProcessamentoService {

    private ProcessamentoService() {
    }

    public static ProcessamentoResultado processarRegistros(
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

    public static String prepararConteudoErro(
            boolean importarArquivo,
            String arquivoErroPath,
            String textoColado,
            String arquivoJsonPath
    ) throws Exception {

        String conteudoErro =
                ValidacaoService.carregarConteudoErro(
                        importarArquivo,
                        arquivoErroPath,
                        textoColado
                );

        ValidacaoService.validarJson(
                arquivoJsonPath
        );

        return conteudoErro;

    }

    public static JsonData carregarJson(
            String arquivoJsonPath
    ) throws Exception {

        String jsonString =
                ArquivoUtil.lerArquivoTexto(
                        new File(arquivoJsonPath)
                );

        return JsonReader.lerJson(
                jsonString
        );

    }

    public static ContextoProcessamento executar(
            String conteudoErro,
            String arquivoErroPath,
            String arquivoJsonPath,
            int historicoMaximo,
            IntConsumer progressoCallback
    ) throws Exception {

        ContextoProcessamento contexto =
                new ContextoProcessamento();

        contexto.setCaminhoArquivoErro(
                arquivoErroPath
        );

        contexto.setCaminhoArquivoJson(
                arquivoJsonPath
        );

        contexto.setArquivoLog(
                LogService.criarArquivoLog(
                        arquivoJsonPath
                )
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                "Processamento iniciado."
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                "Arquivo JSON: " + arquivoJsonPath
        );

        if (arquivoErroPath.isBlank()) {

            LogService.escrever(
                    contexto.getArquivoLog(),
                    "Origem: texto colado"
            );

        } else {

            LogService.escrever(
                    contexto.getArquivoLog(),
                    "Arquivo TXT: " + arquivoErroPath
            );

        }

        LogService.escrever(
                contexto.getArquivoLog(),
                ""
        );

        TxtProcessamentoResultado resultadoTxt =
                TxtProcessor.processar(
                        conteudoErro
                );

        LogService.escrever(
                contexto.getArquivoLog(),
                "Matrículas encontradas no TXT: "
                        + resultadoTxt.getMatriculasParaCorrigir().size()
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                "CEP(s) inválido(s): "
                        + resultadoTxt.getCepsInvalidos().size()
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                "Vários endereços: "
                        + resultadoTxt.getVariosEnderecos().size()
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                ""
        );

        contexto.setMatriculasParaCorrigir(
                resultadoTxt.getMatriculasParaCorrigir()
        );

        contexto.setCepsInvalidos(
                resultadoTxt.getCepsInvalidos()
        );

        contexto.setVariosEnderecos(
                resultadoTxt.getVariosEnderecos()
        );

        progressoCallback.accept(5);

        JsonData jsonData =
                carregarJson(
                        arquivoJsonPath
                );

        LogService.escrever(
                contexto.getArquivoLog(),
                "JSON carregado com sucesso."
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                "Formato encontrado: "
                        + jsonData.getFormatoJson()
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                "Quantidade de registros no JSON: "
                        + jsonData.getListaReais().size()
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                ""
        );

        contexto.setJsonData(
                jsonData
        );

        progressoCallback.accept(25);

        if (jsonData.getListaReais() == null) {

            throw new Exception(
                    "A estrutura do arquivo JSON é inválida.\n\n"
                            + "O programa esperava encontrar um dos formatos:\n\n"
                            + "• INDICADOR_REAL → REAL\n"
                            + "ou\n"
                            + "• REAL\n\n"
                            + "Verifique se foi selecionado o arquivo JSON correto."
            );

        }

        ProcessamentoResultado resultado =
                processarRegistros(
                        jsonData.getListaReais(),
                        contexto.getMatriculasParaCorrigir(),
                        progressoCallback
                );

        contexto.setProcessamentoResultado(
                resultado
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                "Alterações realizadas: "
                        + resultado.getAlteracoesRealizadas()
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                "Já estavam corretas: "
                        + resultado.getJaEstavamCorretas()
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                ""
        );

        contexto.setDadosJson(
                jsonData.getDadosJson()
        );

        contexto.setAlteracoesRealizadas(
                resultado.getAlteracoesRealizadas()
        );

        contexto.setJaEstavamCorretas(
                resultado.getJaEstavamCorretas()
        );

        contexto.setRelatorio(
                resultado.getRelatorio()
        );

        Set<String> naoEncontradas =
                new HashSet<>(
                        contexto.getMatriculasParaCorrigir().keySet()
                );

        naoEncontradas.removeAll(
                resultado.getMatriculasEncontradasJson()
        );

        ArrayList<String> lista =
                new ArrayList<>(naoEncontradas);

        lista.sort(
                Comparator.comparingInt(Integer::parseInt)
        );

        contexto.setNaoEncontradas(
                lista
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                "Matrículas não encontradas no JSON: "
                        + lista.size()
        );

        ArquivosGerados arquivos =
                salvarArquivos(
                        contexto,
                        contexto.getDadosJson(),
                        contexto.getCaminhoArquivoJson(),
                        contexto.getCaminhoArquivoErro(),
                        contexto.getMatriculasParaCorrigir(),
                        contexto.getAlteracoesRealizadas(),
                        contexto.getJaEstavamCorretas(),
                        contexto.getCepsInvalidos(),
                        contexto.getVariosEnderecos(),
                        contexto.getNaoEncontradas(),
                        contexto.getRelatorio()
                );

        contexto.setArquivoSaida(
                arquivos.getArquivoJson()
        );

        contexto.setArquivoRelatorio(
                arquivos.getArquivoRelatorio()
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                ""
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                "Arquivos gerados:"
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                "JSON: "
                        + arquivos.getArquivoJson().getAbsolutePath()
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                "Relatório: "
                        + arquivos.getArquivoRelatorio().getAbsolutePath()
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                "Log: "
                        + arquivos.getArquivoLog().getAbsolutePath()
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                ""
        );

        LogService.escrever(
                contexto.getArquivoLog(),
                "Processamento concluído com sucesso."
        );

        HistoricoService.adicionar(
                arquivoJsonPath,
                historicoMaximo
        );

        return contexto;

    }

    public static ArquivosGerados salvarArquivos(
            ContextoProcessamento contexto,
            Map<String, Object> dadosJson,
            String arquivoJsonPath,
            String arquivoErroPath,
            Map<String, Integer> matriculasParaCorrigir,
            int alteracoesRealizadas,
            int jaEstavamCorretas,
            Set<String> cepsInvalidos,
            Set<String> variosEnderecos,
            List<String> naoEncontradas,
            List<String> relatorio
    ) throws Exception {

        File fileJsonOriginal =
                new File(arquivoJsonPath);

        String dir =
                fileJsonOriginal.getParent();

        String nomeOriginal =
                fileJsonOriginal
                        .getName()
                        .replaceFirst("[.][^.]+$", "");

        File arquivoSaida =
                new File(
                        dir,
                        nomeOriginal + "_corrigido.json"
                );

        File arquivoRelatorio =
                new File(
                        dir,
                        nomeOriginal + "_RELATORIO.txt"
                );

        JsonProcessor.salvarJson(
                dadosJson,
                arquivoSaida
        );

        JsonProcessor.gerarRelatorio(
                arquivoRelatorio,
                arquivoErroPath,
                arquivoJsonPath,
                matriculasParaCorrigir.size(),
                alteracoesRealizadas,
                jaEstavamCorretas,
                cepsInvalidos,
                variosEnderecos,
                naoEncontradas,
                relatorio
        );

        return new ArquivosGerados(
                arquivoSaida,
                arquivoRelatorio,
                contexto.getArquivoLog()
        );

    }
}