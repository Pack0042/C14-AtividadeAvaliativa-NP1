package br.inatel.reservasalas.utilitarios;

import br.inatel.reservasalas.entidades.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GerenciadorUsuariosTest {

    @Test
    void impedirCadastroDeUsuarioComEmailDuplicado() {
        GerenciadorUsuarios gerenciadorUsuarios = new GerenciadorUsuarios();
        Usuario primeiroUsuario = new Usuario("Ana", "ana@email.com", "123456");
        Usuario usuarioDuplicado = new Usuario("Ana Clara", "ana@email.com", "abcdef");

        gerenciadorUsuarios.cadastrar(primeiroUsuario);
        String resultado = gerenciadorUsuarios.cadastrar(usuarioDuplicado);

        assertEquals("Erro: ja existe um usuario com este email.", resultado);
        assertEquals(1, gerenciadorUsuarios.getUsuarios().size());
        assertFalse(gerenciadorUsuarios.getUsuarios().contains(usuarioDuplicado));
    }
}
