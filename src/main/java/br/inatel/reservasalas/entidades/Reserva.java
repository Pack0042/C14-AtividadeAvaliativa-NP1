package br.inatel.reservasalas.entidades;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reserva {
    private static final DateTimeFormatter FORMATO_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final int DURACAO_HORAS = 2;

    private Usuario usuario;
    private Sala sala;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private boolean ativa;

    public Reserva(Usuario usuario, Sala sala, LocalDateTime inicio) {
        this.usuario = usuario;
        this.sala = sala;
        this.inicio = inicio;
        this.fim = inicio.plusHours(DURACAO_HORAS);
        this.ativa = true;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void cancelar() {
        this.ativa = false;
    }

    public void encerrar() {
        this.ativa = false;
    }

    public boolean estaExpirada() {
        return LocalDateTime.now().isAfter(fim);
    }

    public boolean estaDentroJanelaRenovacao() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicioJanela = fim.minusMinutes(15);
        return agora.isAfter(inicioJanela) && agora.isBefore(fim);
    }

    @Override
    public String toString() {
        return "Reserva: " + sala + " | " + usuario.getNome()
                + " | " + inicio.format(FORMATO_BR) + " - " + fim.format(FORMATO_BR)
                + " | " + (ativa ? "Ativa" : "Encerrada");
    }
}
