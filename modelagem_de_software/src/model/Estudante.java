package model;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

public class Estudante extends Usuario {
    private String matriculaEstudante;
    private Set<Turma> turmasMatriculadas;

    public Estudante(Long id, String nome, String email, String senha, String matriculaEstudante) {
        super(id, nome, email, senha);
        this.matriculaEstudante = matriculaEstudante;
        this.turmasMatriculadas = new HashSet<>();
    }

    public String getMatriculaEstudante() {
        return matriculaEstudante;
    }

    public void setMatriculaEstudante(String matriculaEstudante) {
        this.matriculaEstudante = matriculaEstudante;
    }

    public Set<Turma> getTurmasMatriculadas() {
        return turmasMatriculadas;
    }

    public void setTurmasMatriculadas(Set<Turma> turmasMatriculadas) {
        this.turmasMatriculadas = turmasMatriculadas;
    }

    public void adicionarTurmaMatriculada(Turma turma) {
        this.turmasMatriculadas.add(turma);
        // Gerenciamento de link bidirecional (se necessário por um ORM ou serviço):
        // turma.adicionarEstudante(this);
    }

    public void removerTurmaMatriculada(Turma turma) {
        this.turmasMatriculadas.remove(turma);
        // Gerenciamento de link bidirecional:
        // turma.removerEstudante(this);
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