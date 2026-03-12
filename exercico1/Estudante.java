package exercico1;

public class Estudante {

    String nome;
    Disciplina disciplina;

    public Estudante(String nome, Disciplina disciplina) {
        this.nome = nome;
        this.disciplina = disciplina;
    }

    public void mostrarInfo() {
        System.out.println("Estudante: " + nome);
        System.out.println("Disciplina: " + disciplina.nome);
        System.out.println("Professor: " + disciplina.professor.nome);
    }
}