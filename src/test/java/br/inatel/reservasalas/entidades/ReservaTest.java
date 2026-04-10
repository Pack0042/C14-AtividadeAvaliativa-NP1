package br.inatel.reservasalas.entidades;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservaTest {

    private static final String SENHA_PADRAO = "123456";

    private Usuario usuario;
    private Sala sala;
    private LocalDateTime inicio;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Jorge", "jorge@teste.com", SENHA_PADRAO);
        sala = new Sala(14, "Sala da biblioteca", 1);
        inicio = LocalDateTime.of(2026, 10, 14, 10, 0);
    }

    @Test
    void reservaDeveIniciarComoAtiva() {
        Reserva reserva = criarReserva();

        assertTrue(reserva.isAtiva());
    }

    @Test
    void reservaDeveTerDuracaoDeDuasHoras() {
        Reserva reserva = criarReserva();

        assertEquals(inicio, reserva.getInicio());
        assertEquals(inicio.plusHours(2), reserva.getFim());
    }

    @Test
    void cancelarDeveDesativarReserva() {
        Reserva reserva = criarReserva();

        reserva.cancelar();

        assertFalse(reserva.isAtiva());
    }

    private Reserva criarReserva() {
        return new Reserva(usuario, sala, inicio);
    }
}