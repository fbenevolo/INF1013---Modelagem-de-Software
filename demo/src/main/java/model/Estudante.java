package model;

import java.util.Objects;

public class Estudante extends Usuario {
    private String matriculaEstudante;

    public Estudante(long id, String nome, String email, String senha, String matriculaEstudante) {
        super(id, nome, email, senha);
        this.matriculaEstudante = matriculaEstudante;
    }

    public String getMatriculaEstudante() {
        return matriculaEstudante;
    }

    public void setMatriculaEstudante(String matriculaEstudante) {
        this.matriculaEstudante = matriculaEstudante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false; // Comparar o ID da superclasse
        Estudante estudante = (Estudante) o;
        return Objects.equals(matriculaEstudante, estudante.matriculaEstudante);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), matriculaEstudante);
    }

    @Override
    public String toString() {
        return "Estudante{" +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", matriculaEstudante='" + matriculaEstudante + '\'' +
                '}';
    }
}