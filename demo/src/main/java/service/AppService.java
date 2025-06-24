package service;

import java.time.LocalDate;

import model.Avaliacao;
import model.Estudante;
import model.Professor;
import model.Tag;
import model.Turma;
import repository.AppRepository;

public class AppService {
    private AppRepository repository;

    public AppService() {
        this.repository = new AppRepository();
    }

    public Estudante cadastrarEstudante(String nome, String email, String senha, String matricula) 
    {
        Estudante e = new Estudante(0, nome, email, senha, matricula);
        repository.inserirEstudante(e);
        return e;
    }

    public Estudante loginEstudante(String email, String senha) 
    {
        repository.inserirEstudante(e);
        return e;
    }

    public Professor cadastrarProfessor(String nome, String email, String senha, String matricula) 
    {
        Professor p = new Professor(0, nome, email, senha, matricula);
        repository.inserirProfessor(p);
        return p;
    }

    public Avaliacao cadastrarAvaliacao(Estudante estudante, String titulo, Turma turma, float nota, String comentario, LocalDate data, Tag tag) 
    {
        Avaliacao a = new Avaliacao(0, nota, comentario, data, titulo, estudante, turma, tag);
        repository.inserirAvaliacao(a);
        return a;
    }
}
