package com.indicador.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

public final class HistoricoService {

    private static final String PASTA =
            "Historico";

    private static final String ARQUIVO =
            "historico.txt";

    private HistoricoService() {
    }

    private static File obterPastaHistorico() {

        File pasta =
                new File(
                        System.getProperty("user.dir"),
                        PASTA
                );

        if (!pasta.exists()) {

            pasta.mkdirs();

        }

        return pasta;
    }

    private static File obterArquivoHistorico() {

        return new File(
                obterPastaHistorico(),
                ARQUIVO
        );
    }

    public static List<HistoricoItem> carregar()
            throws IOException {

        File arquivo =
                obterArquivoHistorico();

        if (!arquivo.exists()) {

            return new ArrayList<>();

        }

        List<HistoricoItem> lista =
                new ArrayList<>();

        List<String> linhas =
                Files.readAllLines(
                        arquivo.toPath(),
                        StandardCharsets.UTF_8
                );

        for (String linha : linhas) {

            if (linha == null || linha.isBlank()) {

                continue;

            }

            try {

                String[] partes =
                        linha.split("\\|", -1);

                /*
                 * Novo formato:
                 *
                 * caminhoJson
                 * tipoEntrada
                 * caminhoTxt
                 * textoColado
                 */

                if (partes.length >= 4) {

                    String caminhoJson =
                            decodificar(partes[0]);

                    String tipoEntrada =
                            decodificar(partes[1]);

                    String caminhoTxt =
                            decodificar(partes[2]);

                    String textoColado =
                            decodificar(partes[3]);

                    File json =
                            new File(caminhoJson);

                    if (json.exists()) {

                        lista.add(
                                new HistoricoItem(
                                        caminhoJson,
                                        tipoEntrada,
                                        caminhoTxt,
                                        textoColado
                                )
                        );

                    }

                    continue;
                }

                /*
                 * Compatibilidade com o histórico antigo.
                 *
                 * O formato antigo possuía somente
                 * o caminho do JSON.
                 */

                File json =
                        new File(linha);

                if (json.exists()) {

                    lista.add(
                            new HistoricoItem(
                                    linha,
                                    "ARQUIVO",
                                    "",
                                    ""
                            )
                    );

                }

            } catch (Exception ignored) {

                /*
                 * Ignora uma linha inválida sem
                 * interromper o carregamento
                 * do restante do histórico.
                 */

            }

        }

        return lista;
    }

    public static List<File> carregarArquivos()
            throws IOException {

        List<File> arquivos =
                new ArrayList<>();

        for (HistoricoItem item : carregar()) {

            File arquivo =
                    new File(
                            item.getCaminhoJson()
                    );

            if (arquivo.exists()) {

                arquivos.add(
                        arquivo
                );

            }

        }

        return arquivos;
    }

    public static HistoricoItem encontrar(
            String caminhoJson
    ) throws IOException {

        if (caminhoJson == null
                || caminhoJson.isBlank()) {

            return null;

        }

        for (HistoricoItem item : carregar()) {

            if (caminhoJson.equals(
                    item.getCaminhoJson()
            )) {

                return item;

            }

        }

        return null;
    }

    public static void adicionar(
            String caminhoJson,
            int limite
    ) throws IOException {

        /*
         * Este método antigo não possui informação suficiente
         * para criar um vínculo válido.
         *
         * Não deve mais criar:
         *
         * JSON | ARQUIVO | vazio | vazio
         */
        if (caminhoJson == null
                || caminhoJson.isBlank()) {

            return;
        }

        /*
         * Não salva somente o JSON.
         *
         * O histórico só deve ser criado através de:
         *
         * adicionar(HistoricoItem, limite)
         *
         * quando já existir TXT ou texto colado.
         */
    }

    public static void adicionar(
            HistoricoItem novoItem,
            int limite
    ) throws IOException {

        if (novoItem == null) {
            return;
        }

        if (novoItem.getCaminhoJson() == null
                || novoItem.getCaminhoJson().isBlank()) {

            return;
        }

        /*
         * Não permite histórico incompleto.
         */
        if (novoItem.isArquivo()) {

            if (novoItem.getCaminhoTxt() == null
                    || novoItem.getCaminhoTxt().isBlank()) {

                return;
            }
        }

        if (novoItem.isTexto()) {

            if (novoItem.getTextoColado() == null
                    || novoItem.getTextoColado().isBlank()) {

                return;
            }
        }

        List<HistoricoItem> historico =
                carregar();

        historico.removeIf(
                item ->
                        item.getCaminhoJson()
                                .equals(
                                        novoItem.getCaminhoJson()
                                )
        );

        historico.add(
                0,
                novoItem
        );

        if (limite < 1) {
            limite = 20;
        }

        if (historico.size() > limite) {

            historico =
                    new ArrayList<>(
                            historico.subList(
                                    0,
                                    limite
                            )
                    );
        }

        salvar(historico);
    }

    public static void remover(
            String caminhoJson
    ) throws IOException {

        List<HistoricoItem> historico =
                carregar();

        historico.removeIf(
                item ->
                        item.getCaminhoJson()
                                .equals(caminhoJson)
        );

        salvar(
                historico
        );
    }

    public static void limpar()
            throws IOException {

        salvar(
                new ArrayList<>()
        );
    }

    private static void salvar(
            List<HistoricoItem> historico
    ) throws IOException {

        List<String> linhas =
                new ArrayList<>();

        for (HistoricoItem item : historico) {

            linhas.add(
                    codificar(
                            item.getCaminhoJson()
                    )
                            + "|"
                            + codificar(
                            item.getTipoEntrada()
                    )
                            + "|"
                            + codificar(
                            item.getCaminhoTxt()
                    )
                            + "|"
                            + codificar(
                            item.getTextoColado()
                    )
            );

        }

        Files.write(
                obterArquivoHistorico().toPath(),
                linhas,
                StandardCharsets.UTF_8
        );
    }

    private static String codificar(
            String valor
    ) {

        if (valor == null) {

            valor = "";

        }

        return Base64.getEncoder()
                .encodeToString(
                        valor.getBytes(
                                StandardCharsets.UTF_8
                        )
                );
    }

    private static String decodificar(
            String valor
    ) {

        if (valor == null
                || valor.isEmpty()) {

            return "";

        }

        return new String(
                Base64.getDecoder().decode(valor),
                StandardCharsets.UTF_8
        );
    }
}