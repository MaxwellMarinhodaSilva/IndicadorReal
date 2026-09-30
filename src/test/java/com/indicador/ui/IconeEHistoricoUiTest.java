package com.indicador.ui;

import com.indicador.config.HistoricoItem;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IconeEHistoricoUiTest {

    @Test
    void iconesVetoriaisPossuemDimensoesConsistentes() {
        List<Icon> icones = List.of(
                IconeUtil.selecionar(),
                IconeUtil.processar(),
                IconeUtil.limpar(),
                IconeUtil.historico(),
                IconeUtil.abrir(),
                IconeUtil.remover(),
                IconeUtil.cancelar(),
                IconeUtil.arquivoJson()
        );

        for (Icon icone : icones) {
            assertNotNull(icone);
            assertEquals(18, icone.getIconWidth());
            assertEquals(18, icone.getIconHeight());
        }
    }

    @Test
    void rendererTruncaNomeLongoEMantemCaminhoNoTooltip() {
        String caminho = new File(
                "C:/arquivos/muito/longo/" + "nome_".repeat(30) + ".json"
        ).getAbsolutePath();
        HistoricoItem item = new HistoricoItem(
                caminho,
                "ARQUIVO",
                "",
                ""
        );

        JList<HistoricoItem> lista = new JList<>();
        lista.setSize(260, 65);

        ArquivoRecenteRenderer renderer = new ArquivoRecenteRenderer();
        Component componente = renderer.getListCellRendererComponent(
                lista,
                item,
                0,
                false,
                false
        );

        assertEquals(caminho, ((JComponent) componente).getToolTipText());
        assertTrue(contemReticencia(componente));
    }

    private boolean contemReticencia(Component componente) {
        if (componente instanceof JLabel label
                && label.getText().contains("…")) {
            return true;
        }

        if (componente instanceof Container container) {
            for (Component filho : container.getComponents()) {
                if (contemReticencia(filho)) {
                    return true;
                }
            }
        }

        return false;
    }
}
