package br.inatel.reservasalas.utilitarios;

import br.inatel.reservasalas.entidades.Funcionario;
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
    void impedirReservaEmHorarioPassado() {
        Sala sala = criarSala(101, "Sala de Aula", 30);
        LocalDateTime horarioPassado = LocalDateTime.now().minusHours(1);

        String resultado = gerenciadorReservas.criar(usuario, sala, horarioPassado);

        assertEquals("Erro: nao e permitido reservar em horarios passados.", resultado);
        assertTrue(gerenciadorReservas.listarReservas().isEmpty());
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

    @Test
    void renovarReservaComSucesso() {
        Sala sala = criarSala(14, "Sala da biblioteca", 8);
        LocalDateTime momentoCriacao = LocalDateTime.of(2026, 10, 14, 10, 0);
        LocalDateTime inicioReserva = momentoCriacao.plusHours(1); // 11:00
        // fim = 13:00, renovacao = 12:45 - 13:00
        LocalDateTime momentoRenovacao = LocalDateTime.of(2026, 10, 14, 12, 50);

        final LocalDateTime[] agoraRef = { momentoCriacao };

        try (MockedStatic<LocalDateTime> mockedTime =
                     Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mockedTime.when(LocalDateTime::now).thenAnswer(invocation -> agoraRef[0]);

            gerenciadorReservas.criar(usuario, sala, inicioReserva);
            Reserva reservaOriginal = gerenciadorReservas.listarReservas().get(0);

            agoraRef[0] = momentoRenovacao;

            String resultado = gerenciadorReservas.renovar(reservaOriginal);

            assertEquals("Reserva renovada com sucesso.", resultado);
            assertEquals(2, gerenciadorReservas.listarReservas().size());

            Reserva novaReserva = gerenciadorReservas.listarReservas().get(1);
            assertEquals(reservaOriginal.getFim(), novaReserva.getInicio());
            assertEquals(reservaOriginal.getFim().plusHours(2), novaReserva.getFim());
            assertTrue(novaReserva.isAtiva());
        }
    }

    @Test
    void impedirCancelamentoDeReservaPorOutroUsuario() {
        Sala sala = criarSala(14, "Sala da biblioteca", 8);
        LocalDateTime inicio = criarInicioFuturo();
        gerenciadorReservas.criar(usuario, sala, inicio);
        Reserva reserva = gerenciadorReservas.listarReservas().get(0);

        Usuario outroUsuario = criarUsuarioComum("Patrick", "patrick@teste.com");
        String resultado = gerenciadorReservas.cancelar(reserva, outroUsuario);

        assertEquals("Erro: voce nao tem permissao para cancelar esta reserva.", resultado);
        assertTrue(reserva.isAtiva());
    }

    @Test
    void impedirReservaComConflitoDeSala() {
        Sala sala = criarSala(14, "Sala da biblioteca", 8);
        LocalDateTime inicio = criarInicioFuturo();
        gerenciadorReservas.criar(usuario, sala, inicio);

        Usuario outroUsuario = criarUsuarioComum("Bernardo", "bernardo@teste.com");
        String resultado = gerenciadorReservas.criar(outroUsuario, sala, inicio);

        assertEquals("Erro: a sala ja esta reservada neste horario.", resultado);
        assertEquals(1, gerenciadorReservas.listarReservas().size());
    }

    @Test
    void impedirAlteracaoDeSalaPorUsuarioComum() {
        Sala salaOriginal = criarSala(14, "Sala da biblioteca", 8);
        Sala novaSala = criarSala(15, "Sala da biblioteca", 12);
        LocalDateTime inicio = criarInicioFuturo();
        gerenciadorReservas.criar(usuario, salaOriginal, inicio);
        Reserva reserva = gerenciadorReservas.listarReservas().get(0);

        String resultado = gerenciadorReservas.alterarSala(reserva, novaSala, usuario);

        assertEquals("Erro: apenas funcionarios podem alterar a sala de uma reserva.", resultado);
        assertSame(salaOriginal, reserva.getSala());
    }

    @Test
    void impedirCancelamentoDeReservaJaCancelada() {
        Sala sala = criarSala(14, "Sala da biblioteca", 8);
        LocalDateTime inicio = criarInicioFuturo();
        gerenciadorReservas.criar(usuario, sala, inicio);
        Reserva reserva = gerenciadorReservas.listarReservas().get(0);

        gerenciadorReservas.cancelar(reserva, usuario);
        String resultado = gerenciadorReservas.cancelar(reserva, usuario);

        assertEquals("Erro: esta reserva ja foi encerrada.", resultado);
    }

    @Test
    void impedirCriacaoDeReservaComReservaAtivaExistente() {
        Sala sala1 = criarSala(301, "Sala de Estudos", 8);
        Sala sala2 = criarSala(302, "Auditorio", 40);
        LocalDateTime inicio = criarInicioFuturo();

        gerenciadorReservas.criar(usuario, sala1, inicio);
        String resultado = gerenciadorReservas.criar(usuario, sala2, inicio.plusHours(3));

        assertEquals("Erro: usuario ja possui uma reserva ativa.", resultado);
        assertEquals(1, gerenciadorReservas.listarReservas().size());
    }

    @Test
    void impedirRenovacaoForaDaJanela() {
        Sala sala = criarSala(301, "Sala de Estudos", 8);
        LocalDateTime inicio = criarInicioFuturo();

        gerenciadorReservas.criar(usuario, sala, inicio);
        Reserva reserva = gerenciadorReservas.listarReservas().get(0);

        String resultado = gerenciadorReservas.renovar(reserva);

        assertEquals("Erro: a renovacao so pode ser solicitada nos ultimos 15 minutos da reserva.", resultado);
        assertEquals(1, gerenciadorReservas.listarReservas().size());
    }

    @Test
    void impedirAlteracaoParaSalaComConflito() {
        Sala sala1 = criarSala(301, "Sala de Estudos", 8);
        Sala sala2 = criarSala(302, "Auditorio", 40);
        LocalDateTime inicio = criarInicioFuturo();

        Usuario outroUsuario = criarUsuarioComum("Ana", "ana@teste.com");

        gerenciadorReservas.criar(usuario, sala1, inicio);
        gerenciadorReservas.criar(outroUsuario, sala2, inicio);

        Reserva primeiraReserva = gerenciadorReservas.listarReservas().get(0);

        String resultado = gerenciadorReservas.alterarSala(primeiraReserva, sala2, funcionario);

        assertEquals("Erro: a nova sala ja esta reservada neste horario.", resultado);
        assertSame(sala1, primeiraReserva.getSala());
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
