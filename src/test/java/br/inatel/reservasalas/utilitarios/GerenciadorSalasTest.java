package br.inatel.reservasalas.utilitarios;

import br.inatel.reservasalas.entidades.Funcionario;
import br.inatel.reservasalas.entidades.Sala;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GerenciadorSalasTest {

    private GerenciadorSalas gerenciadorSalas;
    private Funcionario funcionario;

    @BeforeEach
    void setUp() {
        gerenciadorSalas = new GerenciadorSalas();
        funcionario = new Funcionario("Ana", "ana@empresa.com", "123456");
    }

    @Test
    void CadastrarSalaPorFuncionarioComSucesso() {
        // Arrange
        Sala sala = criarSala(101, "Laboratorio de Software", 20);

        // Act
        String resultado = gerenciadorSalas.cadastrar(sala, funcionario);

        // Assert
        assertEquals("Sala cadastrada com sucesso.", resultado);
        assertEquals(1, gerenciadorSalas.listarSalas().size());
        assertSame(sala, gerenciadorSalas.buscarPorNumero(101));
    }

    @Test
    void ListarSalasDisponiveisCorretamente() {
        // Arrange
        Sala sala1 = criarSala(101, "Laboratorio de Software", 20);
        Sala sala2 = criarSala(202, "Sala de Reuniao", 10);
        gerenciadorSalas.cadastrar(sala1, funcionario);
        gerenciadorSalas.cadastrar(sala2, funcionario);

        // Act
        List<Sala> salasDisponiveis = gerenciadorSalas.listarSalas();

        // Assert
        assertEquals(2, salasDisponiveis.size());
        assertTrue(salasDisponiveis.contains(sala1));
        assertTrue(salasDisponiveis.contains(sala2));
    }

    private Sala criarSala(int numero, String descricao, int capacidade) {
        return new Sala(numero, descricao, capacidade);
    }
}
