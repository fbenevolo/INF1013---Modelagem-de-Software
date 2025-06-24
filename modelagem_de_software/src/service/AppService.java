package service;

import model.Estudante;
import repository.AppRepository;

public class AppService {
    private AppRepository repository;

    public AppService() {
        this.repository = new AppRepository();
    }

    public Estudante cadastrarEstudante(String nome, String email, String senha, String matricula) 
    {
        return null;
    }
}
