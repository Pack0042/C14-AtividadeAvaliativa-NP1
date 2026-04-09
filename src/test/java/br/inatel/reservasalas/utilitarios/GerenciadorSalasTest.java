package br.inatel.reservasalas.utilitarios;

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
}
