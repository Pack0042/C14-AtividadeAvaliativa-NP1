package br.inatel.reservasalas.utilitarios;

import br.inatel.reservasalas.entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorUsuarios {
    private List<Usuario> usuarios = new ArrayList<>();
    private Usuario usuarioLogado = null;

    public String cadastrar(Usuario usuario) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(usuario.getEmail())) {
                return "Erro: ja existe um usuario com este email.";
            }
        }
        usuarios.add(usuario);
        return "Usuario cadastrado com sucesso.";
    }

    public String login(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getSenha().equals(senha)) {
                usuarioLogado = u;
                return "Login realizado com sucesso. Bem-vindo, " + u.getNome() + "!";
            }
        }
        return "Erro: email ou senha incorretos.";
    }

    public void logout() {
        usuarioLogado = null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public boolean estaLogado() {
        return usuarioLogado != null;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}
