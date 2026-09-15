package com.indicador.service;

import com.indicador.io.ArquivoUtil;

import java.io.File;
import java.io.IOException;

public final class ValidacaoService {

    private ValidacaoService() {
    }

    public static String carregarConteudoErro(

            boolean importarArquivo,

            String arquivoErroPath,

            String textoColado

    ) throws IOException {

        if (importarArquivo) {

            if (arquivoErroPath.isEmpty()) {

                throw new IllegalArgumentException(
                        "Selecione um arquivo TXT."
                );

            }

            return ArquivoUtil.lerArquivoTexto(
                    new File(arquivoErroPath)
            );

        }

        String conteudo =
                textoColado.trim();

        if (conteudo.isEmpty()) {

            throw new IllegalArgumentException(
                    "Cole os erros na caixa de texto."
            );

        }

        return conteudo;

    }

    public static void validarJson(

            String arquivoJsonPath

    ) {

        if (arquivoJsonPath.isEmpty()) {

            throw new IllegalArgumentException(
                    "Selecione o JSON."
            );

        }

    }

}