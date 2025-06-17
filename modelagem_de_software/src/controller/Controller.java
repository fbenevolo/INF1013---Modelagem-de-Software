package controller;

import java.util.List;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.function.Function;

import model.*;

public class Controller {
    private List<Avaliacao> avaliacoes;
    private List<Estudante> estudantes;
    private List<Professor> professores;
    private List<Turma> turmas;
    private List<Disciplina> disciplinas;

    public Controller() {
        this.avaliacoes = new LinkedList<>();
        this.estudantes = new LinkedList<>();
        this.turmas = new LinkedList<>();
        this.professores = new LinkedList<>();
        this.disciplinas = new LinkedList<>();
    }

    private <T> T buscarPorId(long id, List<T> l, Function<T, Long> idExtractor) {
        return l.stream()
                .filter(item -> idExtractor.apply(item) == id)
                .findFirst()
                .orElse(null);
    }

    private Disciplina buscarDisciplinaPorId(long id) {
        return this.buscarPorId(id, this.disciplinas, Disciplina::getId);
    }

    private Estudante buscarEstudantePorId(long id) {
        return this.buscarPorId(id, this.estudantes, Estudante::getId);
    }

    private Professor buscarProfessorPorId(long id) {
        return this.buscarPorId(id, this.professores, Professor::getId);
    }

    public Estudante cadastrarEstudante(String nome, String email, String senha, String matriculaEstudante) {
        var e = new Estudante(estudantes.size() + 1, nome, email, senha, matriculaEstudante);
        this.estudantes.add(e);
        return e;
    }

    public Professor cadastrarProfessor(String nome, String email, String senha, String matriculaProfessor) {
        var p = new Professor(professores.size() + 1, nome, email, senha, matriculaProfessor);
        this.professores.add(p);
        return p;
    }

    public Disciplina cadastrarDisciplina(String codigo, String nome, int creditos) {
        var d = new Disciplina(disciplinas.size() + 1, codigo, nome, creditos);
        this.disciplinas.add(d);
        return d;

    }

    public Turma cadastrarTurma(String sala, String horario, String codigo, long idDisciplina, long idProfessor) {
        var disc = this.buscarDisciplinaPorId(idDisciplina);
        if (disc == null) {
            System.out.println("Disciplina não encontrada. Cadastro de turma nao realizado.");
            return null;
        }

        var prof = this.buscarProfessorPorId(idProfessor);
        if (prof == null) {
            System.out.println("Professor não encontrado. Cadastro de turma nao realizado.");
            return null;
        }

        var t = new Turma(turmas.size() + 1, sala, horario, codigo, disc, prof);
        turmas.add(t);
        return t;
    }

    public void matricularEstudante(long idEstudante, long idTurma) {
        var estudante = this.buscarEstudantePorId(idEstudante);
        if (estudante == null) {
            System.out.println("Estudante não encontrado. Matrícula não realizada.");
            return;
        }

        var turma = this.buscarPorId(idTurma, this.turmas, Turma::getId);
        if (turma == null) {
            System.out.println("Turma não encontrada. Matrícula não realizada.");
            return;
        }

        if (turma.estaMatriculado(estudante)) {
            System.out.println("Estudante já está matriculado nesta turma.");
            return;
        }

        turma.adicionarEstudante(estudante);
        System.out.println(String.format("Estudante %s matriculado com sucesso!", estudante.getNome()));
    }

    public void fazerAvaliacao(float nota, String titulo, String comentario, long idEstudante, long idTurma, Tag t) {
        var estudante = this.buscarEstudantePorId(idEstudante);
        if (estudante == null) {
            System.out.println("Estudante não encontrado. Avaliação não realizada.");
            return;
        }
        var turma = this.buscarPorId(idTurma, this.turmas, Turma::getId);
        if (turma == null) {
            System.out.println("Turma não encontrada. Avaliação não realizada.");
            return;
        }
        if (!turma.estaMatriculado(estudante)) {
            System.out.println("Estudante não está matriculado nesta turma. Ele nao pode avaliar.");
            return;
        }

        var data = LocalDate.now();
        var nAvaliacao = new Avaliacao(
                this.avaliacoes.size() + 1, nota, comentario, data, titulo, estudante, turma, t);
        this.avaliacoes.add(nAvaliacao);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("=== CONTROLLER STATUS ===\n\n");

        // Disciplinas
        builder.append("DISCIPLINAS (").append(disciplinas.size()).append("):\n");
        if (disciplinas.isEmpty()) {
            builder.append("  Nenhuma disciplina cadastrada\n");
        } else {
            disciplinas.forEach(d -> builder.append("  ").append(d).append("\n"));
        }
        builder.append("\n");

        // Professores
        builder.append("PROFESSORES (").append(professores.size()).append("):\n");
        if (professores.isEmpty()) {
            builder.append("  Nenhum professor cadastrado\n");
        } else {
            professores.forEach(p -> builder.append("  ").append(p).append("\n"));
        }
        builder.append("\n");

        // Estudantes
        builder.append("ESTUDANTES (").append(estudantes.size()).append("):\n");
        if (estudantes.isEmpty()) {
            builder.append("  Nenhum estudante cadastrado\n");
        } else {
            estudantes.forEach(e -> builder.append("  ").append(e).append("\n"));
        }
        builder.append("\n");

        // Turmas
        builder.append("TURMAS (").append(turmas.size()).append("):\n");
        if (turmas.isEmpty()) {
            builder.append("  Nenhuma turma cadastrada\n");
        } else {
            turmas.forEach(t -> builder.append("  ").append(t).append("\n"));
        }
        builder.append("\n");

        // Avaliações
        builder.append("AVALIAÇÕES (").append(avaliacoes.size()).append("):\n");
        if (avaliacoes.isEmpty()) {
            builder.append("  Nenhuma avaliação cadastrada\n");
        } else {
            avaliacoes.forEach(a -> builder.append("  ").append(a).append("\n"));
        }

        return builder.toString();
    }
}
