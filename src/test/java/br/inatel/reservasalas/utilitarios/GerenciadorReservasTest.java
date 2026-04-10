package br.inatel.reservasalas.utilitarios;

import br.inatel.reservasalas.entidades.Reserva;
import br.inatel.reservasalas.entidades.Sala;
import br.inatel.reservasalas.entidades.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GerenciadorReservasTest {

    private static final String DEFAULT_PASSWORD = "123456";

    private GerenciadorReservas gerenciadorReservas;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        gerenciadorReservas = new GerenciadorReservas();
        usuario = new Usuario("Chris", "chris@email.com", DEFAULT_PASSWORD);
    }

    @Test
    void impedirReservaEmHorarioPassado() {
        Sala sala = criarSala(101, "Sala de Aula", 30);
        LocalDateTime horarioPassado = LocalDateTime.now().minusHours(1);

        String resultado = gerenciadorReservas.criar(usuario, sala, horarioPassado);

        assertEquals("Erro: nao e permitido reservar em horarios passados.", resultado);
        assertTrue(gerenciadorReservas.listarReservas().isEmpty());
    }

    @Test
    void encerrarReservasExpiradas() {
        Sala sala = criarSala(4, "Sala de Reunioes", 20);
        LocalDateTime agora = LocalDateTime.of(2026, 4, 10, 10, 0);
        LocalDateTime inicioReserva = agora.plusHours(1);

        try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(agora);
            gerenciadorReservas.criar(usuario, sala, inicioReserva);

            Reserva reservaExpirada = gerenciadorReservas.listarReservas().get(0);

            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(agora.plusHours(4));
            gerenciadorReservas.encerrarExpiradas();

            assertTrue(gerenciadorReservas.listarReservas().contains(reservaExpirada));
            assertTrue(gerenciadorReservas.listarReservasAtivas().isEmpty());
            assertFalse(reservaExpirada.isAtiva());
        }
    }

    private Sala criarSala(int numero, String descricao, int capacidade) {
        return new Sala(numero, descricao, capacidade);
    }
}
