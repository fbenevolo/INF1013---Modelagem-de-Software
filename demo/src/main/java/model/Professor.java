package model;

import java.util.Objects;

public class Professor extends Usuario {
    private String matriculaProfessor;

    public Professor(long id, String nome, String email, String senha, String matriculaProfessor) {
        super(id, nome, email, senha);
        this.matriculaProfessor = matriculaProfessor;
    }

    public String getMatriculaProfessor() {
        return matriculaProfessor;
    }

    public void setMatriculaProfessor(String matriculaProfessor) {
        this.matriculaProfessor = matriculaProfessor;
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