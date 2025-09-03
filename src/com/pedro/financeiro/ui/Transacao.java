package com.pedro.financeiro.ui;

public class Transacao {
    private String descricao;
    private String categoria;
    private double valor;

    public Transacao(String descricao, String categoria, double valor) {
        this.descricao = descricao;
        this.categoria = categoria;
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getValor() {
        return valor;
    }
}
