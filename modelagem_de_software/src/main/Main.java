package main;

import DBConnection.DBConnection;
import model.*;
import Service.AppService;

public class Main {
    public static void main(String[] args) {
        // Garante que o banco está criado com o schema
        DBConnection.initializeDatabaseIfNeeded();

        // Usa o serviço para cadastrar
        AppService service = new AppService();
        service.cadastrarEstudante("Maria Oliveira", "maria@e1.com", "senha", "6789");

        DBConnection.closeConnection();
    }
}
