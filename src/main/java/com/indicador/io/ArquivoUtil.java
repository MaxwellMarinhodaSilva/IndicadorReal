package com.indicador.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class ArquivoUtil {

    private ArquivoUtil() {
    }

    public static String lerArquivoTexto(File file) throws IOException {

        try {

            return Files.readString(
                    file.toPath(),
                    StandardCharsets.UTF_8
            );

        } catch (Exception ignored) {

            try {

                return Files.readString(
                        file.toPath(),
                        Charset.forName("windows-1252")
                );

            } catch (Exception ignored2) {

                return Files.readString(
                        file.toPath(),
                        StandardCharsets.ISO_8859_1
                );

            }

        }

    }

}