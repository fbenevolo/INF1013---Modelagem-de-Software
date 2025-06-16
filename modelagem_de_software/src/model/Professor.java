package model;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

public class Professor extends Usuario {
    private String matriculaProfessor;
    private Set<Turma> turmasLecionadas;

    public Professor(Long id, String nome, String email, String senha, String matriculaProfessor) {
        super(id, nome, email, senha);
        this.matriculaProfessor = matriculaProfessor;
        this.turmasLecionadas = new HashSet<>();
    }

    public String getMatriculaProfessor() {
        return matriculaProfessor;
    }

    public void setMatriculaProfessor(String matriculaProfessor) {
        this.matriculaProfessor = matriculaProfessor;
    }

    public Set<Turma> getTurmasLecionadas() {
        return turmasLecionadas;
    }

    public void setTurmasLecionadas(Set<Turma> turmasLecionadas) {
        this.turmasLecionadas = turmasLecionadas;
    }

    // Métodos para gerenciar a coleção
    public void adicionarTurmaLecionada(Turma turma) {
        this.turmasLecionadas.add(turma);
        // Gerenciamento de link bidirecional:
        // turma.adicionarProfessor(this);
    }

    public void removerTurmaLecionada(Turma turma) {
        this.turmasLecionadas.remove(turma);
        // Gerenciamento de link bidirecional:
        // turma.removerProfessor(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Professor professor = (Professor) o;
        return Objects.equals(matriculaProfessor, professor.matriculaProfessor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), matriculaProfessor);
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", matriculaProfessor='" + matriculaProfessor + '\'' +
                '}';
    }
}