package br.inatel.reservasalas.entidades;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FuncionarioTest {

    private static final String SENHA_PADRAO = "123456";

    @Test
    void funcionarioDeveTerTodasPermissoes() {
        Funcionario funcionario = new Funcionario("Moises", "moises@empresa.com", SENHA_PADRAO);

        assertTrue(funcionario.podeCadastrarSala());
        assertTrue(funcionario.podeCancelarReservaDeOutros());
        assertTrue(funcionario.podeRemoverSala());
        assertTrue(funcionario.podeAlterarReserva());
    }
}