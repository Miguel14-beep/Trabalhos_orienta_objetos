
package br.com.chatmultiusuario.client;

import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class ClienteChat {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new TelaLogin((nomeUsuario, conexao) -> {
                try {
                    new TelaChat(nomeUsuario, conexao);

                } catch (RuntimeException e) {
                    try {
                        conexao.close();
                    } catch (Exception erro) {
                        e.addSuppressed(erro);
                    }

                    JOptionPane.showMessageDialog(
                            null,
                            "Não foi possível abrir o chat: "
                                    + e.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        });
    }
}
