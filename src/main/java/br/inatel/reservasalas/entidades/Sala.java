package br.inatel.reservasalas.entidades;

public class Sala {
    private int numero;
    private String descricao;
    private int capacidade;

    public Sala(int numero, String descricao, int capacidade) {
        this.numero = numero;
        this.descricao = descricao;
        this.capacidade = capacidade;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    @Override
    public String toString() {
        return "Sala " + numero + " - " + descricao + " (Capacidade: " + capacidade + ")";
    }
}
