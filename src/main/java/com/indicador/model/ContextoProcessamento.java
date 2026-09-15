package com.indicador.model;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ContextoProcessamento {

    private Map<String, Object> dadosJson;

    private Map<String, Integer> matriculasParaCorrigir;

    private Set<String> cepsInvalidos;

    private Set<String> variosEnderecos;

    private List<String> naoEncontradas;

    private List<String> relatorio;

    private int alteracoesRealizadas;

    private int jaEstavamCorretas;

    private File arquivoSaida;

    private File arquivoRelatorio;

    private String caminhoArquivoJson;

    private String caminhoArquivoErro;

    private JsonData jsonData;

    private ProcessamentoResultado processamentoResultado;

    private File arquivoLog;

    public Map<String, Object> getDadosJson() {
        return dadosJson;
    }

    public void setDadosJson(Map<String, Object> dadosJson) {
        this.dadosJson = dadosJson;
    }

    public Map<String, Integer> getMatriculasParaCorrigir() {
        return matriculasParaCorrigir;
    }

    public void setMatriculasParaCorrigir(
            Map<String, Integer> matriculasParaCorrigir
    ) {
        this.matriculasParaCorrigir = matriculasParaCorrigir;
    }

    public Set<String> getCepsInvalidos() {
        return cepsInvalidos;
    }

    public void setCepsInvalidos(Set<String> cepsInvalidos) {
        this.cepsInvalidos = cepsInvalidos;
    }

    public Set<String> getVariosEnderecos() {
        return variosEnderecos;
    }

    public void setVariosEnderecos(Set<String> variosEnderecos) {
        this.variosEnderecos = variosEnderecos;
    }

    public List<String> getNaoEncontradas() {
        return naoEncontradas;
    }

    public void setNaoEncontradas(List<String> naoEncontradas) {
        this.naoEncontradas = naoEncontradas;
    }

    public List<String> getRelatorio() {
        return relatorio;
    }

    public void setRelatorio(List<String> relatorio) {
        this.relatorio = relatorio;
    }

    public int getAlteracoesRealizadas() {
        return alteracoesRealizadas;
    }

    public void setAlteracoesRealizadas(int alteracoesRealizadas) {
        this.alteracoesRealizadas = alteracoesRealizadas;
    }

    public int getJaEstavamCorretas() {
        return jaEstavamCorretas;
    }

    public void setJaEstavamCorretas(int jaEstavamCorretas) {
        this.jaEstavamCorretas = jaEstavamCorretas;
    }

    public File getArquivoSaida() {
        return arquivoSaida;
    }

    public void setArquivoSaida(File arquivoSaida) {
        this.arquivoSaida = arquivoSaida;
    }

    public File getArquivoRelatorio() {
        return arquivoRelatorio;
    }

    public void setArquivoRelatorio(File arquivoRelatorio) {
        this.arquivoRelatorio = arquivoRelatorio;
    }

    public File getArquivoLog() {
        return arquivoLog;
    }

    public void setArquivoLog(File arquivoLog) {
        this.arquivoLog = arquivoLog;
    }

    public JsonData getJsonData() {
        return jsonData;
    }

    public void setJsonData(JsonData jsonData) {
        this.jsonData = jsonData;
    }

    public ProcessamentoResultado getProcessamentoResultado() {
        return processamentoResultado;
    }

    public void setProcessamentoResultado(
            ProcessamentoResultado processamentoResultado
    ) {
        this.processamentoResultado = processamentoResultado;
    }

    public String getCaminhoArquivoJson() {
        return caminhoArquivoJson;
    }

    public void setCaminhoArquivoJson(
            String caminhoArquivoJson
    ) {
        this.caminhoArquivoJson = caminhoArquivoJson;
    }

    public String getCaminhoArquivoErro() {
        return caminhoArquivoErro;
    }

    public void setCaminhoArquivoErro(
            String caminhoArquivoErro
    ) {
        this.caminhoArquivoErro = caminhoArquivoErro;
    }

}