package model;
import java.util.ArrayList;
import java.util.List;


public class Turma {
    private String sala;
    private String horario;
    private String codigo;
    private Disciplina disciplina;
    private Professor professor;
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    public Turma(String codigo, String sala, String horario, Disciplina disciplina, Professor professor) {
        this.codigo = codigo;
        this.sala = sala;
        this.horario = horario;
        this.disciplina = disciplina;
        this.professor = professor;
    }

    public void adicionarAvaliacao(Avaliacao avaliacao) {
        avaliacoes.add(avaliacao);
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public Professor getProfessor() {
        return professor;
    }
}
