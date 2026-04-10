package br.inatel.reservasalas.utilitarios;

import br.inatel.reservasalas.entidades.Funcionario;
import br.inatel.reservasalas.entidades.Sala;
import br.inatel.reservasalas.entidades.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GerenciadorSalasTest {

    private static final String DEFAULT_PASSWORD = "123456";

    private GerenciadorSalas gerenciadorSalas;
    private Funcionario funcionario;
    private Usuario usuarioComum;

    @BeforeEach
    void setUp() {
        gerenciadorSalas = new GerenciadorSalas();
        funcionario = criarFuncionario("Ana", "ana@empresa.com");
        usuarioComum = new Usuario("Ana", "ana@email.com", DEFAULT_PASSWORD);
    }

    @Test
    void cadastrarSalaPorFuncionarioComSucesso() {
        Sala sala = criarSala(101, "Laboratorio de Software", 20);

        String resultado = gerenciadorSalas.cadastrar(sala, funcionario);

        assertEquals("Sala cadastrada com sucesso.", resultado);
        assertEquals(1, gerenciadorSalas.listarSalas().size());
        assertSame(sala, gerenciadorSalas.buscarPorNumero(101));
    }

    @Test
    void listarTodasAsSalasCadastradas() {
        Sala sala1 = criarSala(101, "Laboratorio de Software", 20);
        Sala sala2 = criarSala(202, "Sala de Reuniao", 10);
        gerenciadorSalas.cadastrar(sala1, funcionario);
        gerenciadorSalas.cadastrar(sala2, funcionario);

        List<Sala> salas = gerenciadorSalas.listarSalas();

        assertEquals(2, salas.size());
        assertTrue(salas.contains(sala1));
        assertTrue(salas.contains(sala2));
    }

    @Test
    void impedirCadastroDeSalaPorUsuarioComum() {
        Sala sala = criarSala(101, "Laboratorio", 30);

        String resultado = gerenciadorSalas.cadastrar(sala, usuarioComum);

        assertEquals("Erro: apenas funcionarios podem cadastrar salas.", resultado);
        assertTrue(gerenciadorSalas.listarSalas().isEmpty());
    }

    @Test
    void impedirCadastroDeSalaComNumeroDuplicado() {
        Sala salaOriginal = criarSala(101, "Laboratorio de Redes", 30);
        Sala salaDuplicada = criarSala(101, "Sala do Panico", 12);

        gerenciadorSalas.cadastrar(salaOriginal, funcionario);
        String resultado = gerenciadorSalas.cadastrar(salaDuplicada, funcionario);

        assertEquals("Erro: ja existe uma sala com este numero.", resultado);
        assertEquals(1, gerenciadorSalas.listarSalas().size());
        assertEquals(salaOriginal, gerenciadorSalas.buscarPorNumero(101));
        assertEquals("Laboratorio de Redes", gerenciadorSalas.buscarPorNumero(101).getDescricao());
    }

    @Test
    void removerSalaComSucesso() {
        Sala sala = criarSala(67, "Laboratorio de Redes", 67);

        gerenciadorSalas.cadastrar(sala, funcionario);
        String resultado = gerenciadorSalas.remover(67, funcionario);

        assertEquals("Sala removida com sucesso.", resultado);
        assertTrue(gerenciadorSalas.listarSalas().isEmpty());
        assertNull(gerenciadorSalas.buscarPorNumero(67));
    }

    private Sala criarSala(int numero, String descricao, int capacidade) {
        return new Sala(numero, descricao, capacidade);
    }

    private Funcionario criarFuncionario(String nome, String email) {
        return new Funcionario(nome, email, DEFAULT_PASSWORD);
    }
}
