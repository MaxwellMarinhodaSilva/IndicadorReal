package com.indicador.model;

import java.util.List;
import java.util.Map;

public class JsonData {

    private Map<String, Object> dadosJson;

    private List<Map<String, Object>> listaReais;

    private String formatoJson;

    public Map<String, Object> getDadosJson() {
        return dadosJson;
    }

    public void setDadosJson(
            Map<String, Object> dadosJson
    ) {
        this.dadosJson = dadosJson;
    }

    public List<Map<String, Object>> getListaReais() {
        return listaReais;
    }

    public void setListaReais(
            List<Map<String, Object>> listaReais
    ) {
        this.listaReais = listaReais;
    }

    public String getFormatoJson() {
        return formatoJson;
    }

    public void setFormatoJson(
            String formatoJson
    ) {
        this.formatoJson = formatoJson;
    }

}