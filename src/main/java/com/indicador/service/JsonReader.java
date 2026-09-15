package com.indicador.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indicador.model.JsonData;

import java.util.List;
import java.util.Map;

public final class JsonReader {

    private JsonReader() {
    }

    @SuppressWarnings("unchecked")
    public static JsonData lerJson(

            String jsonString

    ) throws Exception {

        ObjectMapper mapper =
                new ObjectMapper();

        mapper.findAndRegisterModules();

        Map<String, Object> dadosJson =
                mapper.readValue(

                        jsonString,

                        new TypeReference<Map<String, Object>>() {
                        }

                );

        String formatoJson = "";

        List<Map<String, Object>> listaReais = null;

        if (dadosJson.containsKey("INDICADOR_REAL")
                && dadosJson.get("INDICADOR_REAL") instanceof Map) {

            Map<String, Object> indicador =
                    (Map<String, Object>) dadosJson.get("INDICADOR_REAL");

            if (indicador.get("REAL") instanceof List) {

                formatoJson = "INDICADOR_REAL";

                listaReais =
                        (List<Map<String, Object>>) indicador.get("REAL");

            }

        } else if (dadosJson.get("REAL") instanceof List) {

            formatoJson = "REAL";

            listaReais =
                    (List<Map<String, Object>>) dadosJson.get("REAL");

        }

        JsonData jsonData =
                new JsonData();

        jsonData.setDadosJson(dadosJson);

        jsonData.setListaReais(listaReais);

        jsonData.setFormatoJson(formatoJson);

        return jsonData;

    }

}