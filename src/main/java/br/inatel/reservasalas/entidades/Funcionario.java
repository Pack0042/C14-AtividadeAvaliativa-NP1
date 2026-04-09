package br.inatel.reservasalas.entidades;

public class Funcionario extends Usuario {

    public Funcionario(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    @Override
    public boolean podeCadastrarSala() {
        return true;
    }

    @Override
    public boolean podeCancelarReservaDeOutros() {
        return true;
    }

    @Override
    public boolean podeRemoverSala() {
        return true;
    }

    @Override
    public boolean podeAlterarReserva() {
        return true;
    }

    @Override
    public String toString() {
        return "[Funcionario] " + super.toString();
    }
}
