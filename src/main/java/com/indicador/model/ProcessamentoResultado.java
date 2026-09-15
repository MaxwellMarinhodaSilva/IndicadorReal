package com.indicador.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProcessamentoResultado {

    private int alteracoesRealizadas;

    private int jaEstavamCorretas;

    private final Set<String> matriculasEncontradasJson =
            new HashSet<>();

    private final List<String> relatorio =
            new ArrayList<>();

    public int getAlteracoesRealizadas() {
        return alteracoesRealizadas;
    }

    public void setAlteracoesRealizadas(
            int alteracoesRealizadas
    ) {
        this.alteracoesRealizadas = alteracoesRealizadas;
    }

    public int getJaEstavamCorretas() {
        return jaEstavamCorretas;
    }

    public void setJaEstavamCorretas(
            int jaEstavamCorretas
    ) {
        this.jaEstavamCorretas = jaEstavamCorretas;
    }

    public Set<String> getMatriculasEncontradasJson() {
        return matriculasEncontradasJson;
    }

    public List<String> getRelatorio() {
        return relatorio;
    }

    public void setMatriculasEncontradasJson(
            Set<String> matriculas
    ) {

        this.matriculasEncontradasJson.clear();

        this.matriculasEncontradasJson.addAll(
                matriculas
        );

    }

    public void setRelatorio(
            List<String> relatorio
    ) {

        this.relatorio.clear();

        this.relatorio.addAll(
                relatorio
        );

    }

}