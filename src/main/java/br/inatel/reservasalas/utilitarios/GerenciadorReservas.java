package br.inatel.reservasalas.utilitarios;

import br.inatel.reservasalas.entidades.Reserva;
import br.inatel.reservasalas.entidades.Sala;
import br.inatel.reservasalas.entidades.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorReservas {
    private List<Reserva> reservas = new ArrayList<>();

    public String criar(Usuario usuario, Sala sala, LocalDateTime inicio) {
        if (inicio.isBefore(LocalDateTime.now())) {
            return "Erro: nao e permitido reservar em horarios passados.";
        }

        if (possuiReservaAtiva(usuario)) {
            return "Erro: usuario ja possui uma reserva ativa.";
        }

        LocalDateTime fim = inicio.plusHours(2);
        if (existeConflitoHorario(sala, inicio, fim)) {
            return "Erro: a sala ja esta reservada neste horario.";
        }

        Reserva reserva = new Reserva(usuario, sala, inicio);
        reservas.add(reserva);
        return "Reserva criada com sucesso.";
    }

    public String cancelar(Reserva reserva, Usuario usuario) {
        if (!reserva.isAtiva()) {
            return "Erro: esta reserva ja foi encerrada.";
        }

        boolean ehDono = reserva.getUsuario().getEmail().equals(usuario.getEmail());
        if (!ehDono && !usuario.podeCancelarReservaDeOutros()) {
            return "Erro: voce nao tem permissao para cancelar esta reserva.";
        }

        reserva.cancelar();
        return "Reserva cancelada com sucesso.";
    }

    public String alterarSala(Reserva reserva, Sala novaSala, Usuario usuario) {
        if (!usuario.podeAlterarReserva()) {
            return "Erro: apenas funcionarios podem alterar a sala de uma reserva.";
        }

        if (!reserva.isAtiva()) {
            return "Erro: esta reserva ja foi encerrada.";
        }

        if (existeConflitoHorario(novaSala, reserva.getInicio(), reserva.getFim())) {
            return "Erro: a nova sala ja esta reservada neste horario.";
        }

        reserva.setSala(novaSala);
        return "Sala da reserva alterada com sucesso.";
    }

    public String renovar(Reserva reserva) {
        if (!reserva.isAtiva()) {
            return "Erro: esta reserva ja foi encerrada.";
        }

        if (!reserva.estaDentroJanelaRenovacao()) {
            return "Erro: a renovacao so pode ser solicitada nos ultimos 15 minutos da reserva.";
        }

        LocalDateTime novoInicio = reserva.getFim();
        LocalDateTime novoFim = novoInicio.plusHours(2);

        if (existeConflitoHorario(reserva.getSala(), novoInicio, novoFim)) {
            return "Erro: a sala ja esta reservada no horario seguinte.";
        }

        Reserva novaReserva = new Reserva(reserva.getUsuario(), reserva.getSala(), novoInicio);
        reservas.add(novaReserva);
        return "Reserva renovada com sucesso.";
    }

    public void encerrarExpiradas() {
        for (Reserva r : reservas) {
            if (r.isAtiva() && r.estaExpirada()) {
                r.encerrar();
            }
        }
    }

    public boolean possuiReservaAtiva(Usuario usuario) {
        for (Reserva r : reservas) {
            if (r.isAtiva() && r.getUsuario().getEmail().equals(usuario.getEmail())) {
                return true;
            }
        }
        return false;
    }

    private boolean existeConflitoHorario(Sala sala, LocalDateTime inicio, LocalDateTime fim) {
        for (Reserva r : reservas) {
            if (r.isAtiva() && r.getSala().getNumero() == sala.getNumero()) {
                if (inicio.isBefore(r.getFim()) && fim.isAfter(r.getInicio())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Reserva> listarReservas() {
        return reservas;
    }

    public List<Reserva> listarReservasAtivas() {
        List<Reserva> ativas = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.isAtiva()) {
                ativas.add(r);
            }
        }
        return ativas;
    }
}
