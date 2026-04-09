package br.inatel.reservasalas.utilitarios;

import br.inatel.reservasalas.entidades.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GerenciadorUsuariosTest {

    private GerenciadorUsuarios gerenciadorUsuarios;

    @BeforeEach
    void setUp() {
        gerenciadorUsuarios = new GerenciadorUsuarios();
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {
        // Arrange
        Usuario usuario = criarUsuarioComum("Maria", "maria@teste.com");

        // Act
        String resultado = gerenciadorUsuarios.cadastrar(usuario);

        // Assert
        assertEquals("Usuario cadastrado com sucesso.", resultado);
        assertEquals(1, gerenciadorUsuarios.getUsuarios().size());
        assertSame(usuario, gerenciadorUsuarios.getUsuarios().get(0));
    }

    @Test
    void deveRealizarLoginComCredenciaisValidas() {
        // Arrange
        Usuario usuario = criarUsuarioComum("Joao", "joao@teste.com");
        gerenciadorUsuarios.cadastrar(usuario);

        // Act
        String resultado = gerenciadorUsuarios.login("joao@teste.com", "123456");

        // Assert
        assertEquals("Login realizado com sucesso. Bem-vindo, Joao!", resultado);
        assertTrue(gerenciadorUsuarios.estaLogado());
        assertSame(usuario, gerenciadorUsuarios.getUsuarioLogado());
    }

    private Usuario criarUsuarioComum(String nome, String email) {
        return new Usuario(nome, email, "123456");
    }
}
