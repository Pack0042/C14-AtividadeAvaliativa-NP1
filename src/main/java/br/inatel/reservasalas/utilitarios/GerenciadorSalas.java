package br.inatel.reservasalas.utilitarios;

import br.inatel.reservasalas.entidades.Sala;
import br.inatel.reservasalas.entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorSalas {
    private List<Sala> salas = new ArrayList<>();

    public String cadastrar(Sala sala, Usuario usuario) {
        if (!usuario.podeCadastrarSala()) {
            return "Erro: apenas funcionarios podem cadastrar salas.";
        }

        for (Sala s : salas) {
            if (s.getNumero() == sala.getNumero()) {
                return "Erro: ja existe uma sala com este numero.";
            }
        }

        salas.add(sala);
        return "Sala cadastrada com sucesso.";
    }

    public String remover(int numeroSala, Usuario usuario) {
        if (!usuario.podeRemoverSala()) {
            return "Erro: apenas funcionarios podem remover salas.";
        }

        for (int i = 0; i < salas.size(); i++) {
            if (salas.get(i).getNumero() == numeroSala) {
                salas.remove(i);
                return "Sala removida com sucesso.";
            }
        }

        return "Erro: sala nao encontrada.";
    }

    public Sala buscarPorNumero(int numero) {
        for (Sala s : salas) {
            if (s.getNumero() == numero) {
                return s;
            }
        }
        return null;
    }

    public List<Sala> listarSalas() {
        return salas;
    }
}
