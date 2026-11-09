
package br.com.chatmultiusuario.model;

public class Usuario {

    private String nome;

    public Usuario(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "O nome do usuário não pode ser vazio."
            );
        }

        this.nome = nome.trim();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "O nome do usuário não pode ser vazio."
            );
        }

        this.nome = nome.trim();
    }

    @Override
    public String toString() {
        return nome;
    }
}
