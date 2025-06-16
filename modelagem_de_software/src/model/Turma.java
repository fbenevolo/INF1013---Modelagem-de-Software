package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Turma {
    private Long id;
    private String sala;
    private String horario;
    private String codigo; // Identificador da turma (ex: INF-1010)

    private Disciplina disciplina;
    private Set<Estudante> estudantes;
    private Professor professor;
    private List<Avaliacao> avaliacoes;

    public Turma(Long id, String sala, String horario, String codigo, Disciplina disciplina, Professor professor) {
        this.id = id;
        this.sala = sala;
        this.horario = horario;
        this.codigo = codigo;
        this.disciplina = disciplina;
        this.professor = professor;
        this.estudantes = new HashSet<>();
        this.avaliacoes = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    // Métodos para gerenciar coleções
    public void adicionarEstudante(Estudante estudante) {
        this.estudantes.add(estudante);
        // Gerenciamento de link bidirecional:
        // estudante.adicionarTurmaMatriculada(this);
    }

    public void removerEstudante(Estudante estudante) {
        this.estudantes.remove(estudante);
        // Gerenciamento de link bidirecional:
        // estudante.removerTurmaMatriculada(this);
    }

    public void adicionarAvaliacao(Avaliacao avaliacao) {
        this.avaliacoes.add(avaliacao);
        // Gerenciamento de link bidirecional:
        // avaliacao.setTurma(this);
    }

    public void removerAvaliacao(Avaliacao avaliacao) {
        this.avaliacoes.remove(avaliacao);
        // Gerenciamento de link bidirecional:
        // avaliacao.setTurma(null);
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
        return "Turma{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", disciplina=" + (disciplina != null ? disciplina.getNome() : "N/A") +
                '}';
    }
}