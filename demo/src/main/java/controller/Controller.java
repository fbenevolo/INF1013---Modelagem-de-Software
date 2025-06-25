package controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import model.Avaliacao;
import model.Disciplina;
import model.Estudante;
import model.Professor;
import model.Tag;
import model.Turma;
import model.Usuario;
import repository.AvaliacaoRepository;
import repository.DisciplinaRepository;
import repository.EstudanteRepository;
import repository.ProfessorRepository;
import repository.TurmaRepository;

public class Controller {

    private final EstudanteRepository estudanteRepository;
    private final ProfessorRepository professorRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final TurmaRepository turmaRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private Usuario usuarioLogado;

    public Controller() {
        this.estudanteRepository = new EstudanteRepository();
        this.professorRepository = new ProfessorRepository();
        this.disciplinaRepository = new DisciplinaRepository();
        this.turmaRepository = new TurmaRepository();
        this.avaliacaoRepository = new AvaliacaoRepository();
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public boolean login(String email, String senha) {
        Estudante estudante = estudanteRepository.buscarPorEmailESenha(email, senha);
        if (estudante != null) {
            usuarioLogado = estudante;
            return true;
        }

        Professor professor = professorRepository.buscarPorEmailESenha(email, senha);
        if (professor != null) {
            usuarioLogado = professor;
            return true;
        }

        throw new IllegalArgumentException("Usuário não encontrado ou senha incorreta.");
    }

    public void logout() {
        if (usuarioLogado == null) {
            throw new IllegalStateException("Nenhum usuário está logado.");
        }
        usuarioLogado = null;
    }

    public Estudante cadastrarEstudante(String nome, String email, String senha, String matriculaEstudante) {
        Estudante e = new Estudante(0, nome, email, senha, matriculaEstudante);
        estudanteRepository.inserir(e);
        return e;
    }

    public Professor cadastrarProfessor(String nome, String email, String senha, String matriculaProfessor) {
        Professor p = new Professor(0, nome, email, senha, matriculaProfessor);
        professorRepository.inserir(p);
        return p;
    }

    public Disciplina cadastrarDisciplina(String codigo, String nome, int creditos) {
        Disciplina d = new Disciplina(0, codigo, nome, creditos);
        disciplinaRepository.inserir(d);
        return d;
    }

    public Turma cadastrarTurma(String sala, String horario, String codigo, String disciplinaNome,
            String professorNome) {
        Disciplina disciplina = disciplinaRepository.buscarPorNome(disciplinaNome);
        Professor professor = professorRepository.buscarPorNome(professorNome);

        if (disciplina == null) {
            throw new IllegalArgumentException("Erro: Disciplina '" + disciplinaNome + "' não encontrada.");
        }
        if (professor == null) {
            throw new IllegalArgumentException("Erro: Professor '" + professorNome + "' não encontrado.");
        }

        Turma turma = new Turma(0, sala, horario, codigo, disciplina, professor);
        turmaRepository.inserir(turma);
        return turma;
    }

    public void matricularEstudante(long idEstudante, long idTurma) {
        turmaRepository.matricularEstudante(idEstudante, idTurma);
    }

    public Avaliacao fazerAvaliacao(float nota, String titulo, String comentario, long idTurma, Tag t) {
        if (usuarioLogado == null) {
            throw new IllegalStateException("Nenhum usuário logado. Faça login primeiro.");
        }
        Estudante estudante = estudanteRepository.buscarPorId(this.usuarioLogado.getId());
        Turma turma = turmaRepository.buscarPorId(idTurma);

        if (estudante == null) {
            throw new IllegalArgumentException("Voce, provavelmente não está logado como estudante.");
        }
        if (turma == null) {
            throw new IllegalArgumentException("Turma não encontrada com ID: " + idTurma);
        }

        Avaliacao avaliacao = new Avaliacao(0, nota, comentario, LocalDate.now(), titulo, estudante, turma, t);
        avaliacaoRepository.inserir(avaliacao);
        return avaliacao;
    }

    public List<Disciplina> listarDisciplinas() {
        return disciplinaRepository.listar();
    }

    public List<Professor> listarProfessores() {
        return professorRepository.listar();
    }

    public List<Estudante> listarEstudantes() {
        return estudanteRepository.listar();
    }

    public List<Turma> listarTurmas() {
        return turmaRepository.listar();
    }

    public List<Avaliacao> listarAvaliacoes() {
        return avaliacaoRepository.listar();
    }

    public Disciplina buscarDisciplinaPorNome(String nome) {
        return disciplinaRepository.buscarPorNome(nome);
    }

    public List<Avaliacao> listarAvaliacaoPorDisciplina(String nomeDisciplina) {
        return avaliacaoRepository.buscarPorDisciplina(Optional.empty(), Optional.of(nomeDisciplina));
    }

    public List<Disciplina> listarDisciplinasProfessor(String nomeProfessor){
        return professorRepository.buscarDisciplinasProfessor(nomeProfessor);
    }

    public List<Turma> listarTurmasAluno(String nomeEstudante){
        return estudanteRepository.buscarTurmasAluno(nomeEstudante);
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("=== CONTROLLER STATUS ===\n\n");

        List<Disciplina> disciplinas = listarDisciplinas();
        builder.append("DISCIPLINAS (").append(disciplinas.size()).append("):\n");
        if (disciplinas.isEmpty()) {
            builder.append("  Nenhuma disciplina cadastrada\n");
        } else {
            disciplinas.forEach(d -> builder.append("  ").append(d).append("\n"));
        }
        builder.append("\n");

        List<Professor> professores = listarProfessores();
        builder.append("PROFESSORES (").append(professores.size()).append("):\n");
        if (professores.isEmpty()) {
            builder.append("  Nenhum professor cadastrado\n");
        } else {
            professores.forEach(p -> builder.append("  ").append(p).append("\n"));
        }
        builder.append("\n");

        List<Estudante> estudantes = listarEstudantes();
        builder.append("ESTUDANTES (").append(estudantes.size()).append("):\n");
        if (estudantes.isEmpty()) {
            builder.append("  Nenhum estudante cadastrado\n");
        } else {
            estudantes.forEach(e -> builder.append("  ").append(e).append("\n"));
        }
        builder.append("\n");

        List<Turma> turmas = listarTurmas();
        builder.append("TURMAS (").append(turmas.size()).append("):\n");
        if (turmas.isEmpty()) {
            builder.append("  Nenhuma turma cadastrada\n");
        } else {
            turmas.forEach(t -> builder.append("  ").append(t).append("\n"));
        }
        builder.append("\n");

        List<Avaliacao> avaliacoes = listarAvaliacoes();
        builder.append("AVALIAÇÕES (").append(avaliacoes.size()).append("):\n");
        if (avaliacoes.isEmpty()) {
            builder.append("  Nenhuma avaliação cadastrada\n");
        } else {
            avaliacoes.forEach(a -> builder.append("  ").append(a).append("\n"));
        }

        return builder.toString();
    }

}