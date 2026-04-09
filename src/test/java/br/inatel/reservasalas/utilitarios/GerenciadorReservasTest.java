package br.inatel.reservasalas.utilitarios;

import br.inatel.reservasalas.entidades.Reserva;
import br.inatel.reservasalas.entidades.Sala;
import br.inatel.reservasalas.entidades.Usuario;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GerenciadorReservasTest {

    @Test
    void impedirReservaEmHorarioPassado() {
        GerenciadorReservas gerenciadorReservas = new GerenciadorReservas();
        Usuario usuario = new Usuario("Chris", "chris@email.com", "shampooantiquedasnaofunciona");
        Sala sala = new Sala(101, "Sala de Aula", 30);
        LocalDateTime horarioPassado = LocalDateTime.now().minusHours(1);

        String resultado = gerenciadorReservas.criar(usuario, sala, horarioPassado);

        assertEquals("Erro: nao e permitido reservar em horarios passados.", resultado);
        assertTrue(gerenciadorReservas.listarReservas().isEmpty());
    }

    @Test
    void encerrarReservasExpiradas() {
        GerenciadorReservas gerenciadorReservas = new GerenciadorReservas();
        Usuario usuario = new Usuario("Mosca", "mosquinha@email.com", "flamengo");
        Sala sala = new Sala(4, "Sala de Reunioes", 20);
        Reserva reservaExpirada = new Reserva(usuario, sala, LocalDateTime.now().minusHours(3));

        gerenciadorReservas.listarReservas().add(reservaExpirada);
        gerenciadorReservas.encerrarExpiradas();

        assertTrue(gerenciadorReservas.listarReservas().contains(reservaExpirada));
        assertTrue(gerenciadorReservas.listarReservasAtivas().isEmpty());
        assertEquals(false, reservaExpirada.isAtiva());
    }
}
