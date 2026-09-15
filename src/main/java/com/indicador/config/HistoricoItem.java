package com.indicador.config;

public class HistoricoItem {

    private String caminhoJson = "";

    private String tipoEntrada = "ARQUIVO";

    private String caminhoTxt = "";

    private String textoColado = "";

    public HistoricoItem() {
    }

    public HistoricoItem(
            String caminhoJson,
            String tipoEntrada,
            String caminhoTxt,
            String textoColado
    ) {

        this.caminhoJson =
                caminhoJson != null
                        ? caminhoJson
                        : "";

        this.tipoEntrada =
                tipoEntrada != null
                        ? tipoEntrada
                        : "ARQUIVO";

        this.caminhoTxt =
                caminhoTxt != null
                        ? caminhoTxt
                        : "";

        this.textoColado =
                textoColado != null
                        ? textoColado
                        : "";
    }

    public String getCaminhoJson() {
        return caminhoJson;
    }

    public void setCaminhoJson(String caminhoJson) {
        this.caminhoJson = caminhoJson;
    }

    public String getTipoEntrada() {
        return tipoEntrada;
    }

    public void setTipoEntrada(String tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    public String getCaminhoTxt() {
        return caminhoTxt;
    }

    public void setCaminhoTxt(String caminhoTxt) {
        this.caminhoTxt = caminhoTxt;
    }

    public String getTextoColado() {
        return textoColado;
    }

    public void setTextoColado(String textoColado) {
        this.textoColado = textoColado;
    }

    public boolean isArquivo() {
        return "ARQUIVO".equals(tipoEntrada);
    }

    public boolean isTexto() {
        return "TEXTO".equals(tipoEntrada);
    }
}