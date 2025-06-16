package model;
import java.util.ArrayList;
import java.util.List;

public class Estudante extends Usuario {
    private String matriculaEstudante;
    private List<Turma> turmas = new ArrayList<>();

    public Estudante(String nome, String email, String matriculaEstudante) {
        super(nome, email);
        this.matriculaEstudante = matriculaEstudante;
    }

    public Avaliacao avaliarDisciplina(Turma turma, int nota, String comentario, String titulo, String tipoTag) {
        Avaliacao avaliacao = new Avaliacao(nota, comentario, titulo, this, tipoTag);
        turma.adicionarAvaliacao(avaliacao);
        return avaliacao;
    }

    public void adicionarTurma(Turma turma) {
        turmas.add(turma);
    }
}
