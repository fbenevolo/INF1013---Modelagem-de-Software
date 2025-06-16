package model;
public class Professor extends Usuario {
    private String matriculaProfessor;

    public Professor(String nome, String email, String matriculaProfessor) {
        super(nome, email);
        this.matriculaProfessor = matriculaProfessor;
    }

    public void visualizarAvaliacoes(Turma turma) {
        if (turma.getProfessor() == this) {
            for (Avaliacao a : turma.getAvaliacoes()) {
                System.out.println(a);
            }
        } else {
            System.out.println("Você não leciona essa turma.");
        }
    }
}
