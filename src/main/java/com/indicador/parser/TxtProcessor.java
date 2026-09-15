package com.indicador.parser;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TxtProcessor {

    private TxtProcessor() {
    }

    public static TxtProcessamentoResultado processar(
            String conteudoErro
    ) {
        TxtProcessamentoResultado resultado =
                new TxtProcessamentoResultado();

        Map<String, Integer> matriculasParaCorrigir =
                resultado.getMatriculasParaCorrigir();

        Set<String> cepsInvalidos =
                resultado.getCepsInvalidos();

        Set<String> variosEnderecos =
                resultado.getVariosEnderecos();

        Matcher m1 = Pattern.compile(
                "Matrícula\\s+(\\d+).*?TIPOENVIO\\s+ID\\s+(\\d+)",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL
        ).matcher(conteudoErro);

        while (m1.find()) {

            matriculasParaCorrigir.put(
                    m1.group(1),
                    Integer.parseInt(m1.group(2))
            );

        }

        Matcher m2 = Pattern.compile(
                "NUMERO_REGISTRO\\s+(\\d+):\\s*[\\r\\n]+VARIOS_ENDERECOS",
                Pattern.CASE_INSENSITIVE
        ).matcher(conteudoErro);

        while (m2.find()) {

            matriculasParaCorrigir.put(
                    m2.group(1),
                    2
            );

            variosEnderecos.add(
                    m2.group(1)
            );

        }

        Matcher m3 = Pattern.compile(
                "NUMERO_REGISTRO\\s+(\\d+):\\s*[\\r\\n]+CEP:",
                Pattern.CASE_INSENSITIVE
        ).matcher(conteudoErro);

        while (m3.find()) {

            cepsInvalidos.add(
                    m3.group(1)
            );

        }
    return resultado;
    }

}