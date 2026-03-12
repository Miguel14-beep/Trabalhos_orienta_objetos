package exercico1;

public class Exerci_B {

	public static void main(String[] args) {

        Professor prof = new Professor("Marcos");
        Disciplina disc = new Disciplina("Programação", prof);

        Estudante aluno = new Estudante("João", disc);

        aluno.mostrarInfo();
    }

}
