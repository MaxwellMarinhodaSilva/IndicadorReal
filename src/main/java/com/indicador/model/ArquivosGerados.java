package com.indicador.model;

import java.io.File;

public class ArquivosGerados {

    private final File arquivoJson;
    private final File arquivoRelatorio;
    private final File arquivoLog;

    public ArquivosGerados(
            File arquivoJson,
            File arquivoRelatorio,
            File arquivoLog
    ) {

        this.arquivoJson = arquivoJson;
        this.arquivoRelatorio = arquivoRelatorio;
        this.arquivoLog = arquivoLog;

    }

    public File getArquivoJson() {
        return arquivoJson;
    }

    public File getArquivoRelatorio() {
        return arquivoRelatorio;
    }

    public File getArquivoLog() {
        return arquivoLog;
    }

}