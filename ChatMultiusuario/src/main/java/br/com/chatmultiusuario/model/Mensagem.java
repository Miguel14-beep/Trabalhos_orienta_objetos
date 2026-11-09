
package br.com.chatmultiusuario.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensagem {

    private Usuario remetente;
    private String conteudo;
    private LocalDateTime horario;

    private static final DateTimeFormatter FORMATO_HORARIO =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Mensagem(Usuario remetente, String conteudo) {
        this(remetente, conteudo, LocalDateTime.now());
    }

    public Mensagem(
            Usuario remetente,
            String conteudo,
            LocalDateTime horario) {

        if (remetente == null) {
            throw new IllegalArgumentException(
                "O remetente não pode ser nulo."
            );
        }

        if (conteudo == null || conteudo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "O conteúdo da mensagem não pode ser vazio."
            );
        }

        if (horario == null) {
            throw new IllegalArgumentException(
                "O horário não pode ser nulo."
            );
        }

        this.remetente = remetente;
        this.conteudo = conteudo.trim();
        this.horario = horario;
    }

    public Usuario getRemetente() {
        return remetente;
    }

    public String getConteudo() {
        return conteudo;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public String formatar() {
        return "[" + horario.format(FORMATO_HORARIO) + "] "
                + remetente.getNome() + ": " + conteudo;
    }

    @Override
    public String toString() {
        return formatar();
    }
}
