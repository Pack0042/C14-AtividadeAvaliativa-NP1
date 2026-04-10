package br.inatel.reservasalas.utilitarios;

import br.inatel.reservasalas.entidades.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GerenciadorUsuariosTest {

    private static final String DEFAULT_PASSWORD = "123456";

    private GerenciadorUsuarios gerenciadorUsuarios;
    private Usuario usuarioExistente;

    @BeforeEach
    void setUp() {
        gerenciadorUsuarios = new GerenciadorUsuarios();
        usuarioExistente = new Usuario("Ana", "ana@email.com", DEFAULT_PASSWORD);
    }

    @Test
    void impedirCadastroDeUsuarioComEmailDuplicado() {
        Usuario usuarioDuplicado = new Usuario("Ana Clara", "ana@email.com", DEFAULT_PASSWORD);

        gerenciadorUsuarios.cadastrar(usuarioExistente);
        String resultado = gerenciadorUsuarios.cadastrar(usuarioDuplicado);

        assertEquals("Erro: ja existe um usuario com este email.", resultado);
        assertEquals(1, gerenciadorUsuarios.getUsuarios().size());
        assertFalse(gerenciadorUsuarios.getUsuarios().contains(usuarioDuplicado));
    }

    @Test
    void impedirLoginComCredenciaisInvalidas() {
        gerenciadorUsuarios.cadastrar(usuarioExistente);

        String resultado = gerenciadorUsuarios.login("ana@email.com", "senhaErrada");

        assertEquals("Erro: email ou senha incorretos.", resultado);
        assertFalse(gerenciadorUsuarios.estaLogado());
    }
}
