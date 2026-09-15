package com.indicador.config;

public class Configuracao {

    private String ultimaPasta = "";

    private String modoEntrada = "ARQUIVO";

    private int posicaoX = -1;

    private int posicaoY = -1;

    private int largura = 700;

    private int altura = 650;

    private boolean mostrarBarra = true;

    private boolean abrirPasta = false;

    private boolean abrirRelatorio = false;

    private boolean abrirLog = false;

    private int historicoMaximo = 20;

    public String getUltimaPasta() {
        return ultimaPasta;
    }

    public void setUltimaPasta(String ultimaPasta) {
        this.ultimaPasta = ultimaPasta;
    }

    public String getModoEntrada() {
        return modoEntrada;
    }

    public void setModoEntrada(String modoEntrada) {
        this.modoEntrada = modoEntrada;
    }

    public int getPosicaoX() {
        return posicaoX;
    }

    public void setPosicaoX(int posicaoX) {
        this.posicaoX = posicaoX;
    }

    public int getPosicaoY() {
        return posicaoY;
    }

    public void setPosicaoY(int posicaoY) {
        this.posicaoY = posicaoY;
    }

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public boolean isMostrarBarra() {
        return mostrarBarra;
    }

    public void setMostrarBarra(boolean mostrarBarra) {
        this.mostrarBarra = mostrarBarra;
    }

    public boolean isAbrirPasta() {
        return abrirPasta;
    }

    public void setAbrirPasta(boolean abrirPasta) {
        this.abrirPasta = abrirPasta;
    }

    public boolean isAbrirRelatorio() {
        return abrirRelatorio;
    }

    public void setAbrirRelatorio(boolean abrirRelatorio) {
        this.abrirRelatorio = abrirRelatorio;
    }

    public boolean isAbrirLog() {
        return abrirLog;
    }

    public void setAbrirLog(boolean abrirLog) {
        this.abrirLog = abrirLog;
    }

    public int getHistoricoMaximo() {
        return historicoMaximo;
    }

    public void setHistoricoMaximo(int historicoMaximo) {
        this.historicoMaximo = historicoMaximo;
    }

}