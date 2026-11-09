
package br.com.chatmultiusuario.model;

public class TesteModelos {

    public static void main(String[] args) {

        Usuario usuario = new Usuario("Miguel");

        Mensagem mensagem = new Mensagem(
            usuario,
            "Olá, pessoal!"
        );

        System.out.println("Usuário: " + usuario.getNome());
        System.out.println("Mensagem: " + mensagem.formatar());
    }
}
