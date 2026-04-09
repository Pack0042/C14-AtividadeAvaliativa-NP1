package br.inatel.reservasalas.utilitarios;

import br.inatel.reservasalas.entidades.Funcionario;
import br.inatel.reservasalas.entidades.Reserva;
import br.inatel.reservasalas.entidades.Sala;
import br.inatel.reservasalas.entidades.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GerenciadorReservasTest {

    private GerenciadorReservas gerenciadorReservas;
    private Usuario usuario;
    private Funcionario funcionario;

    @BeforeEach
    void setUp() {
        gerenciadorReservas = new GerenciadorReservas();
        usuario = new Usuario("Carlos", "carlos@teste.com", "123456");
        funcionario = new Funcionario("Beatriz", "beatriz@empresa.com", "123456");
    }

    @Test
    void criarReservaValidaComSucesso() {
        
        Sala sala = new Sala(301, "Sala de Estudos", 8);
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);

        
        String resultado = gerenciadorReservas.criar(usuario, sala, inicio);

        
        assertEquals("Reserva criada com sucesso.", resultado);
        assertEquals(1, gerenciadorReservas.listarReservas().size());

        Reserva reserva = gerenciadorReservas.listarReservas().get(0);
        assertSame(usuario, reserva.getUsuario());
        assertSame(sala, reserva.getSala());
        assertEquals(inicio, reserva.getInicio());
        assertEquals(inicio.plusHours(2), reserva.getFim());
        assertTrue(reserva.isAtiva());
        assertTrue(gerenciadorReservas.possuiReservaAtiva(usuario));
    }

    @Test
    void cancelarPropriaReservaComSucesso() {
        
        Sala sala = new Sala(302, "Auditorio", 40);
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        gerenciadorReservas.criar(usuario, sala, inicio);
        Reserva reserva = gerenciadorReservas.listarReservas().get(0);

        
        String resultado = gerenciadorReservas.cancelar(reserva, usuario);

        
        assertEquals("Reserva cancelada com sucesso.", resultado);
        assertFalse(reserva.isAtiva());
        assertTrue(gerenciadorReservas.listarReservasAtivas().isEmpty());
    }

    @Test
    void alterarSalaDeReservaPorFuncionarioComSucesso() {
        
        Sala salaOriginal = new Sala(401, "Sala de Projetos", 12);
        Sala novaSala = new Sala(402, "Sala Executiva", 6);
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        gerenciadorReservas.criar(usuario, salaOriginal, inicio);
        Reserva reserva = gerenciadorReservas.listarReservas().get(0);

       
        String resultado = gerenciadorReservas.alterarSala(reserva, novaSala, funcionario);

        
        assertEquals("Sala da reserva alterada com sucesso.", resultado);
        assertSame(novaSala, reserva.getSala());
        assertTrue(reserva.isAtiva());
    }
}
