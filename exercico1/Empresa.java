package exercico1;
import java.util.ArrayList;

public class Empresa {

    String nome;
    ArrayList<Funcionario> funcionarios = new ArrayList<>();

    public Empresa(String nome) {
        this.nome = nome;
    }

    public void adicionarFuncionario(Funcionario f) {
        funcionarios.add(f);
    }

    public void listarFuncionarios() {
        System.out.println("Funcionários da empresa " + nome + ":");

        for (Funcionario f : funcionarios) {
            System.out.println(f.nome);
        }
    }
}
