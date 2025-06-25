package service;

import java.time.LocalDate;

import controller.Controller;           // ← novo
import model.Avaliacao;
import model.Disciplina;
import model.Estudante;
import model.Professor;
import model.Tag;
import model.Turma;
import repository.AppRepository;

public class AppService {

    // ───────────────────────────────────
    // CAMADAS
    private final AppRepository repository;
    private final Controller     controller;     // ← novo

    // ───────────────────────────────────
    public AppService() {
        this.repository = new AppRepository();
        this.controller = new Controller();      // se preferir, injete por construtor
    }

    // ───────────────────────────────────
    // ESTUDANTE
    public Estudante cadastrarEstudante(String nome, String email, String senha, String matricula) {
        Estudante est = new Estudante(0, nome, email, senha, matricula);
        repository.inserirEstudante(est);
        return est;
    }

    //public Estudante loginEstudante(String email, String senha) {
        // Exemplo de busca; ajuste ao seu AppRepository
      //  return repository.buscarEstudantePorEmailSenha(email, senha);
    //}

    // ───────────────────────────────────
    // PROFESSOR
    public Professor cadastrarProfessor(String nome, String email, String senha, String matricula) {
        Professor prof = new Professor(0, nome, email, senha, matricula);
        repository.inserirProfessor(prof);
        return prof;
    }

    // ───────────────────────────────────
    // DISCIPLINA (útil para CT)
    public Disciplina cadastrarDisciplina(String codigo, String nome, int creditos) {
        Disciplina d = new Disciplina(0, codigo, nome, creditos);
        repository.inserirDisciplina(d);
        return d;
    }

    // ───────────────────────────────────
    // TURMA  (usado pelo comando CT)
    public Turma cadastrarTurma(String sala, String horario, String codigo, long idDisciplina, long idProfessor) {
        // Verifica se a disciplina existe
        Disciplina disciplina = repository.buscarDisciplinaPorId(idDisciplina);
        if (disciplina == null) {
            System.out.println("Disciplina não encontrada. Cadastro de turma nao realizado.");
            return null;
        }

        // Verifica se o professor existe
        Professor professor = repository.buscarProfessorPorId(idProfessor);
        if (professor == null) {
            System.out.println("Professor não encontrado. Cadastro de turma nao realizado.");
            return null;
        }

        Turma turma = new Turma(0, sala, horario, codigo, disciplina, professor);
        repository.inserirTurma(turma);
        return turma;
    }


    // ───────────────────────────────────
    // AVALIAÇÃO
    public Avaliacao cadastrarAvaliacao(Estudante estudante,
                                        String titulo,
                                        Turma turma,
                                        float nota,
                                        String comentario,
                                        LocalDate data,
                                        Tag tag) {

        Avaliacao a = new Avaliacao(0, nota, comentario, data, titulo, estudante, turma, tag);
        repository.inserirAvaliacao(a);
        return a;
    }
}
