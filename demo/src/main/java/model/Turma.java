package model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Turma {
    private long id;
    private String sala;
    private String horario;
    private String codigo; // Identificador da turma (ex: INF-1010)

    private Disciplina disciplina;
    private Set<Estudante> estudantes;
    private Professor professor;

    public Turma(long id, String sala, String horario, String codigo, Disciplina disciplina, Professor professor) {
        this.id = id;
        this.sala = sala;
        this.horario = horario;
        this.codigo = codigo;
        this.disciplina = disciplina;
        this.professor = professor;
        this.estudantes = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Set<Estudante> getEstudantes() {
        return estudantes;
    }

    public void setEstudantes(Set<Estudante> estudantes) {
        this.estudantes = estudantes;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public void adicionarEstudante(Estudante estudante) {
        this.estudantes.add(estudante);
    }

    public void removerEstudante(Estudante estudante) {
        this.estudantes.remove(estudante);
    }

    public boolean estaMatriculado(Estudante estudante) {
        return this.estudantes.contains(estudante);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Turma turma = (Turma) o;
        return Objects.equals(id, turma.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String estudantesStr = estudantes != null ? estudantes.stream()
                .map(d -> d.getNome())
                .collect(java.util.stream.Collectors.joining(", ", "[", "]"))
                : "[]";
        return "Turma{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", disciplina=" + (disciplina != null ? disciplina.getNome() : "N/A") +
                ", professor=" + (professor != null ? professor.getNome() : "N/A") +
                ", sala='" + sala + '\'' +
                ", horario='" + horario + '\'' +
                ", estudantes=" + estudantesStr +
                '}';
    }
}