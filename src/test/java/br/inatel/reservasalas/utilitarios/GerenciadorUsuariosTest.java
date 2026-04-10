package br.inatel.reservasalas.utilitarios;

import br.inatel.reservasalas.entidades.Funcionario;
import br.inatel.reservasalas.entidades.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GerenciadorUsuariosTest {

    private static final String DEFAULT_PASSWORD = "123456";

    private GerenciadorUsuarios gerenciadorUsuarios;

    @BeforeEach
    void setUp() {
        gerenciadorUsuarios = new GerenciadorUsuarios();
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
    void realizarLoginComCredenciaisValidas() {
        Usuario usuario = criarUsuarioComum("Joao", "joao@teste.com");
        gerenciadorUsuarios.cadastrar(usuario);

        String resultado = gerenciadorUsuarios.login("joao@teste.com", DEFAULT_PASSWORD);

        assertEquals("Login realizado com sucesso. Bem-vindo, Joao!", resultado);
        assertTrue(gerenciadorUsuarios.estaLogado());
        assertSame(usuario, gerenciadorUsuarios.getUsuarioLogado());
    }

    private Usuario criarUsuarioComum(String nome, String email) {
        return new Usuario(nome, email, DEFAULT_PASSWORD);
    }

    private Funcionario criarFuncionario(String nome, String email) {
        return new Funcionario(nome, email, DEFAULT_PASSWORD);
    }
}
