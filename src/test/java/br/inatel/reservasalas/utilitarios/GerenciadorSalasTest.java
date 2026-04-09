package br.inatel.reservasalas.utilitarios;

import br.inatel.reservasalas.entidades.Funcionario;
import br.inatel.reservasalas.entidades.Sala;
import br.inatel.reservasalas.entidades.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GerenciadorSalasTest {

    @Test
    void impedirCadastroDeSalaPorUsuarioComum() {
        GerenciadorSalas gerenciadorSalas = new GerenciadorSalas();
        Usuario usuario = new Usuario("Ana", "ana@email.com", "123456");
        Sala sala = new Sala(101, "Laboratorio", 30);

        String resultado = gerenciadorSalas.cadastrar(sala, usuario);

        assertEquals("Erro: apenas funcionarios podem cadastrar salas.", resultado);
        assertTrue(gerenciadorSalas.listarSalas().isEmpty());
    }

    @Test
    void impedirCadastroDeSalaComNumeroDuplicado() {
        GerenciadorSalas gerenciadorSalas = new GerenciadorSalas();
        Funcionario funcionario = new Funcionario("Pedro", "pedro@email.com", "123456");
        Sala salaOriginal = new Sala(101, "Laboratorio de Redes", 30);
        Sala salaDuplicada = new Sala(101, "Sala do Pânico", 12);

        gerenciadorSalas.cadastrar(salaOriginal, funcionario);
        String resultado = gerenciadorSalas.cadastrar(salaDuplicada, funcionario);

        assertEquals("Erro: ja existe uma sala com este numero.", resultado);
        assertEquals(1, gerenciadorSalas.listarSalas().size());
        assertEquals(salaOriginal, gerenciadorSalas.buscarPorNumero(101));
        assertEquals("Laboratorio de Redes", gerenciadorSalas.buscarPorNumero(101).getDescricao());
    }
    @Test
    void removerSalaComSucesso() {
        GerenciadorSalas gerenciadorSalas = new GerenciadorSalas();
        Funcionario funcionario = new Funcionario("Patrick", "patrick@email.com", "alodamiao");
        Sala sala = new Sala(67, "Laboratorio de Redes", 67);

        gerenciadorSalas.cadastrar(sala, funcionario);
        String resultado = gerenciadorSalas.remover(67, funcionario);

        assertEquals("Sala removida com sucesso.", resultado);
        assertTrue(gerenciadorSalas.listarSalas().isEmpty());
        assertEquals(null, gerenciadorSalas.buscarPorNumero(67));
    }
}
