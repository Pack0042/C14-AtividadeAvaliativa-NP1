package br.inatel.reservasalas.entidades;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class UsuarioTest {

    private static final String SENHA_PADRAO = "123456";

    @Test
    void usuarioComumNaoDeveTerPermissoes() {
        Usuario usuario = new Usuario("Alberto", "alberto@teste.com", SENHA_PADRAO);

        assertFalse(usuario.podeCadastrarSala());
        assertFalse(usuario.podeCancelarReservaDeOutros());
        assertFalse(usuario.podeRemoverSala());
        assertFalse(usuario.podeAlterarReserva());
    }
}