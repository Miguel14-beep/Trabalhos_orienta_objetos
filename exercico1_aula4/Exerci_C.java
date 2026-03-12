package exercico1;

public class Exerci_C {

	public static void main(String[] args) {

        Empresa emp = new Empresa("Tech Ltda");

        Funcionario f1 = new Funcionario("Lucas");
        Funcionario f2 = new Funcionario("Maria");

        emp.adicionarFuncionario(f1);
        emp.adicionarFuncionario(f2);

        emp.listarFuncionarios();
    }

}
