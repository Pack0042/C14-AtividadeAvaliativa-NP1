package br.inatel.reservasalas;

import br.inatel.reservasalas.utilitarios.GerenciadorUsuarios;
import br.inatel.reservasalas.utilitarios.GerenciadorSalas;
import br.inatel.reservasalas.utilitarios.GerenciadorReservas;
import br.inatel.reservasalas.entidades.Usuario;
import br.inatel.reservasalas.entidades.Funcionario;
import br.inatel.reservasalas.entidades.Sala;
import br.inatel.reservasalas.entidades.Reserva;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner;
    private final GerenciadorUsuarios gerenciadorUsuarios;
    private final GerenciadorSalas gerenciadorSalas;
    private final GerenciadorReservas gerenciadorReservas;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Menu(Scanner scanner, GerenciadorUsuarios gerenciadorUsuarios,
                GerenciadorSalas gerenciadorSalas, GerenciadorReservas gerenciadorReservas) {
        this.scanner = scanner;
        this.gerenciadorUsuarios = gerenciadorUsuarios;
        this.gerenciadorSalas = gerenciadorSalas;
        this.gerenciadorReservas = gerenciadorReservas;
    }

    public void executar() {
        boolean rodando = true;

        while (rodando) {
            gerenciadorReservas.encerrarExpiradas();

            if (!gerenciadorUsuarios.estaLogado()) {
                rodando = menuInicial();
            } else {
                rodando = menuPrincipal();
            }
        }

        System.out.println("Sistema encerrado.");
    }

    private boolean menuInicial() {
        System.out.println("\n=== SISTEMA DE RESERVA DE SALAS ===");
        System.out.println("1 - Cadastrar usuario");
        System.out.println("2 - Cadastrar funcionario");
        System.out.println("3 - Login");
        System.out.println("0 - Sair");
        System.out.print("Opcao: ");

        String opcao = scanner.nextLine().trim();

        switch (opcao) {
            case "1":
                cadastrarUsuario(false);
                break;
            case "2":
                cadastrarUsuario(true);
                break;
            case "3":
                login();
                break;
            case "0":
                return false;
            default:
                System.out.println("Opcao invalida.");
        }
        return true;
    }

    private boolean menuPrincipal() {
        Usuario logado = gerenciadorUsuarios.getUsuarioLogado();
        System.out.println("\n=== MENU PRINCIPAL === Logado como: " + logado);
        System.out.println("1 - Listar salas");
        System.out.println("2 - Reservar sala");
        System.out.println("3 - Cancelar minha reserva");
        System.out.println("4 - Listar reservas ativas");
        System.out.println("5 - Renovar reserva");

        if (logado.podeCadastrarSala()) {
            System.out.println("6 - Cadastrar sala");
            System.out.println("7 - Remover sala");
            System.out.println("8 - Cancelar reserva de outro usuario");
            System.out.println("9 - Alterar sala de uma reserva");
        }

        System.out.println("0 - Logout");
        System.out.print("Opcao: ");

        String opcao = scanner.nextLine().trim();

        switch (opcao) {
            case "1":
                listarSalas();
                break;
            case "2":
                reservarSala();
                break;
            case "3":
                cancelarMinhaReserva();
                break;
            case "4":
                listarReservasAtivas();
                break;
            case "5":
                renovarReserva();
                break;
            case "6":
                cadastrarSala();
                break;
            case "7":
                removerSala();
                break;
            case "8":
                cancelarReservaDeOutro();
                break;
            case "9":
                alterarSalaReserva();
                break;
            case "0":
                gerenciadorUsuarios.logout();
                System.out.println("Logout realizado.");
                break;
            default:
                System.out.println("Opcao invalida.");
        }
        return true;
    }

    private void cadastrarUsuario(boolean ehFuncionario) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();

        Usuario usuario;
        if (ehFuncionario) {
            usuario = new Funcionario(nome, email, senha);
        } else {
            usuario = new Usuario(nome, email, senha);
        }

        System.out.println(gerenciadorUsuarios.cadastrar(usuario));
    }

    private void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();

        System.out.println(gerenciadorUsuarios.login(email, senha));
    }

    private void listarSalas() {
        List<Sala> salas = gerenciadorSalas.listarSalas();
        if (salas.isEmpty()) {
            System.out.println("Nenhuma sala cadastrada.");
            return;
        }
        System.out.println("\n--- Salas Cadastradas ---");
        for (Sala s : salas) {
            System.out.println(s);
        }
    }

    private void reservarSala() {
        System.out.print("Numero da sala: ");
        int numero = lerInteiro();
        Sala sala = gerenciadorSalas.buscarPorNumero(numero);
        if (sala == null) {
            System.out.println("Erro: sala nao encontrada.");
            return;
        }

        System.out.print("Data e hora de inicio (DD/MM/AAAA HH:MM): ");
        LocalDateTime inicio = lerDataHora();
        if (inicio == null) return;

        System.out.println(gerenciadorReservas.criar(gerenciadorUsuarios.getUsuarioLogado(), sala, inicio));
    }

    private void cancelarMinhaReserva() {
        Usuario logado = gerenciadorUsuarios.getUsuarioLogado();
        Reserva reserva = buscarReservaAtiva(logado);
        if (reserva == null) {
            System.out.println("Voce nao possui reserva ativa.");
            return;
        }
        System.out.println(gerenciadorReservas.cancelar(reserva, logado));
    }

    private void listarReservasAtivas() {
        List<Reserva> ativas = gerenciadorReservas.listarReservasAtivas();
        if (ativas.isEmpty()) {
            System.out.println("Nenhuma reserva ativa.");
            return;
        }
        System.out.println("\n--- Reservas Ativas ---");
        for (int i = 0; i < ativas.size(); i++) {
            System.out.println((i + 1) + ". " + ativas.get(i));
        }
    }

    private void renovarReserva() {
        Usuario logado = gerenciadorUsuarios.getUsuarioLogado();
        Reserva reserva = buscarReservaAtiva(logado);
        if (reserva == null) {
            System.out.println("Voce nao possui reserva ativa.");
            return;
        }
        System.out.println(gerenciadorReservas.renovar(reserva));
    }

    private void cadastrarSala() {
        Usuario logado = gerenciadorUsuarios.getUsuarioLogado();
        if (!logado.podeCadastrarSala()) {
            System.out.println("Erro: sem permissao.");
            return;
        }

        System.out.print("Numero da sala: ");
        int numero = lerInteiro();
        System.out.print("Descricao: ");
        String descricao = scanner.nextLine().trim();
        System.out.print("Capacidade: ");
        int capacidade = lerInteiro();

        Sala sala = new Sala(numero, descricao, capacidade);
        System.out.println(gerenciadorSalas.cadastrar(sala, logado));
    }

    private void removerSala() {
        Usuario logado = gerenciadorUsuarios.getUsuarioLogado();
        System.out.print("Numero da sala a remover: ");
        int numero = lerInteiro();
        System.out.println(gerenciadorSalas.remover(numero, logado));
    }

    private void cancelarReservaDeOutro() {
        List<Reserva> ativas = gerenciadorReservas.listarReservasAtivas();
        if (ativas.isEmpty()) {
            System.out.println("Nenhuma reserva ativa.");
            return;
        }

        System.out.println("\n--- Reservas Ativas ---");
        for (int i = 0; i < ativas.size(); i++) {
            System.out.println((i + 1) + ". " + ativas.get(i));
        }
        System.out.print("Selecione o numero da reserva: ");
        int indice = lerInteiro() - 1;

        if (indice < 0 || indice >= ativas.size()) {
            System.out.println("Erro: selecao invalida.");
            return;
        }

        System.out.println(gerenciadorReservas.cancelar(ativas.get(indice), gerenciadorUsuarios.getUsuarioLogado()));
    }

    private void alterarSalaReserva() {
        List<Reserva> ativas = gerenciadorReservas.listarReservasAtivas();
        if (ativas.isEmpty()) {
            System.out.println("Nenhuma reserva ativa.");
            return;
        }

        System.out.println("\n--- Reservas Ativas ---");
        for (int i = 0; i < ativas.size(); i++) {
            System.out.println((i + 1) + ". " + ativas.get(i));
        }
        System.out.print("Selecione o numero da reserva: ");
        int indice = lerInteiro() - 1;

        if (indice < 0 || indice >= ativas.size()) {
            System.out.println("Erro: selecao invalida.");
            return;
        }

        System.out.print("Numero da nova sala: ");
        int numSala = lerInteiro();
        Sala novaSala = gerenciadorSalas.buscarPorNumero(numSala);
        if (novaSala == null) {
            System.out.println("Erro: sala nao encontrada.");
            return;
        }

        System.out.println(gerenciadorReservas.alterarSala(ativas.get(indice), novaSala, gerenciadorUsuarios.getUsuarioLogado()));
    }

    private Reserva buscarReservaAtiva(Usuario usuario) {
        for (Reserva r : gerenciadorReservas.listarReservasAtivas()) {
            if (r.getUsuario().getEmail().equals(usuario.getEmail())) {
                return r;
            }
        }
        return null;
    }

    private int lerInteiro() {
        try {
            int valor = Integer.parseInt(scanner.nextLine().trim());
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("Entrada invalida. Usando 0.");
            return 0;
        }
    }

    private LocalDateTime lerDataHora() {
        try {
            String entrada = scanner.nextLine().trim();
            return LocalDateTime.parse(entrada, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Formato invalido. Use DD/MM/AAAA HH:MM");
            return null;
        }
    }
}
