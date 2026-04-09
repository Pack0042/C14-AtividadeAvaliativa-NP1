package br.inatel.reservasalas;

import br.inatel.reservasalas.utilitarios.GerenciadorUsuarios;
import br.inatel.reservasalas.utilitarios.GerenciadorSalas;
import br.inatel.reservasalas.utilitarios.GerenciadorReservas;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        GerenciadorUsuarios gerenciadorUsuarios = new GerenciadorUsuarios();
        GerenciadorSalas gerenciadorSalas = new GerenciadorSalas();
        GerenciadorReservas gerenciadorReservas = new GerenciadorReservas();

        Menu menu = new Menu(scanner, gerenciadorUsuarios, gerenciadorSalas, gerenciadorReservas);
        menu.executar();

        scanner.close();
    }
}
