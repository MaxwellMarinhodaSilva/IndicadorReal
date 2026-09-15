package com.indicador.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TxtProcessamentoResultado {

    private final Map<String, Integer> matriculasParaCorrigir =
            new HashMap<>();

    private final Set<String> cepsInvalidos =
            new HashSet<>();

    private final Set<String> variosEnderecos =
            new HashSet<>();

    public Map<String, Integer> getMatriculasParaCorrigir() {
        return matriculasParaCorrigir;
    }

    public Set<String> getCepsInvalidos() {
        return cepsInvalidos;
    }

    public Set<String> getVariosEnderecos() {
        return variosEnderecos;
    }

}