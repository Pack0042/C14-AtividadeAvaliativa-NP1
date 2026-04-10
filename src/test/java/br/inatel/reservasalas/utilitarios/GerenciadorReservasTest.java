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

    private static final String DEFAULT_PASSWORD = "123456";

    private GerenciadorReservas gerenciadorReservas;
    private Usuario usuario;
    private Funcionario funcionario;

    @BeforeEach
    void setUp() {
        gerenciadorReservas = new GerenciadorReservas();
        usuario = criarUsuarioComum("Carlos", "carlos@teste.com");
        funcionario = criarFuncionario("Beatriz", "beatriz@empresa.com");
    }

    @Test
    void iniciarSemReservas() {
        assertTrue(gerenciadorReservas.listarReservas().isEmpty());
    }

    @Test
    void criarReservaComSucesso() {
        Sala sala = criarSala(301, "Sala de Estudos", 8);
        LocalDateTime inicio = criarInicioFuturo();

        String resultado = gerenciadorReservas.criar(usuario, sala, inicio);

        assertEquals("Reserva criada com sucesso.", resultado);
    }

    @Test
    void criarReservaComEstadoInicialCorreto() {
        Sala sala = criarSala(301, "Sala de Estudos", 8);
        LocalDateTime inicio = criarInicioFuturo();
        gerenciadorReservas.criar(usuario, sala, inicio);

        Reserva reserva = gerenciadorReservas.listarReservas().get(0);

        assertSame(usuario, reserva.getUsuario());
        assertSame(sala, reserva.getSala());
        assertEquals(inicio, reserva.getInicio());
        assertEquals(inicio.plusHours(2), reserva.getFim());
        assertTrue(reserva.isAtiva());
    }

    @Test
    void registrarReservaNaListagemAposCriacao() {
        Sala sala = criarSala(301, "Sala de Estudos", 8);
        LocalDateTime inicio = criarInicioFuturo();
        gerenciadorReservas.criar(usuario, sala, inicio);

        assertEquals(1, gerenciadorReservas.listarReservas().size());
        assertSame(sala, gerenciadorReservas.listarReservas().get(0).getSala());
    }

    @Test
    void indicarReservaAtivaParaUsuarioAposCriacao() {
        Sala sala = criarSala(301, "Sala de Estudos", 8);
        LocalDateTime inicio = criarInicioFuturo();
        gerenciadorReservas.criar(usuario, sala, inicio);

        assertTrue(gerenciadorReservas.possuiReservaAtiva(usuario));
    }

    @Test
    void cancelarPropriaReservaComSucesso() {
        Sala sala = criarSala(302, "Auditorio", 40);
        LocalDateTime inicio = criarInicioFuturo();
        gerenciadorReservas.criar(usuario, sala, inicio);
        Reserva reserva = gerenciadorReservas.listarReservas().get(0);

        String resultado = gerenciadorReservas.cancelar(reserva, usuario);

        assertEquals("Reserva cancelada com sucesso.", resultado);
        assertFalse(reserva.isAtiva());
        assertTrue(gerenciadorReservas.listarReservasAtivas().isEmpty());
    }

    @Test
    void alterarSalaDeReservaPorFuncionarioComSucesso() {
        Sala salaOriginal = criarSala(401, "Sala de Projetos", 12);
        Sala novaSala = criarSala(402, "Sala Executiva", 6);
        LocalDateTime inicio = criarInicioFuturo();
        gerenciadorReservas.criar(usuario, salaOriginal, inicio);
        Reserva reserva = gerenciadorReservas.listarReservas().get(0);

        String resultado = gerenciadorReservas.alterarSala(reserva, novaSala, funcionario);

        assertEquals("Sala da reserva alterada com sucesso.", resultado);
        assertSame(novaSala, reserva.getSala());
        assertTrue(reserva.isAtiva());
    }

    private Usuario criarUsuarioComum(String nome, String email) {
        return new Usuario(nome, email, DEFAULT_PASSWORD);
    }

    private Funcionario criarFuncionario(String nome, String email) {
        return new Funcionario(nome, email, DEFAULT_PASSWORD);
    }

    private Sala criarSala(int numero, String descricao, int capacidade) {
        return new Sala(numero, descricao, capacidade);
    }

    private LocalDateTime criarInicioFuturo() {
        return LocalDateTime.now().plusDays(1);
    }
}
