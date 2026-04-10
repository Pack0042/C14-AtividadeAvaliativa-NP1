package br.inatel.reservasalas.utilitarios;

import br.inatel.reservasalas.entidades.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void cadastrarUsuarioComSucesso() {
        Usuario usuario = criarUsuarioComum("Maria", "maria@teste.com");

        String resultado = gerenciadorUsuarios.cadastrar(usuario);

        assertEquals("Usuario cadastrado com sucesso.", resultado);
        assertEquals(1, gerenciadorUsuarios.getUsuarios().size());
        assertSame(usuario, gerenciadorUsuarios.getUsuarios().get(0));
    }

    @Test
    void impedirLoginComEmailInexistente() {
        String resultado = gerenciadorUsuarios.login("inexistente@teste.com", DEFAULT_PASSWORD);

        assertEquals("Erro: email ou senha incorretos.", resultado);
        assertFalse(gerenciadorUsuarios.estaLogado());
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
    void realizarLoginComCredenciaisValidas() {
        Usuario usuario = criarUsuarioComum("Joao", "joao@teste.com");
        gerenciadorUsuarios.cadastrar(usuario);

        String resultado = gerenciadorUsuarios.login("joao@teste.com", DEFAULT_PASSWORD);

        assertEquals("Login realizado com sucesso. Bem-vindo, Joao!", resultado);
        assertTrue(gerenciadorUsuarios.estaLogado());
        assertSame(usuario, gerenciadorUsuarios.getUsuarioLogado());
    }

    @Test
    void impedirLoginComCredenciaisInvalidas() {
        gerenciadorUsuarios.cadastrar(usuarioExistente);

        String resultado = gerenciadorUsuarios.login("ana@email.com", "senhaErrada");

        assertEquals("Erro: email ou senha incorretos.", resultado);
        assertFalse(gerenciadorUsuarios.estaLogado());
    }

    private Usuario criarUsuarioComum(String nome, String email) {
        return new Usuario(nome, email, DEFAULT_PASSWORD);
    }
}
