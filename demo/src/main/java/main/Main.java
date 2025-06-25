package main;

import java.time.LocalDate;
import java.util.Scanner;

import DBConnection.DBConnection;
import model.Disciplina;
import model.Estudante;
import model.Professor;
import model.Tag;
import model.Turma;
import service.AppService;

public class Main {
    public static void main(String[] args) throws Exception {
        DBConnection.initializeDatabaseIfNeeded();
        AppService service = new AppService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite um comando ou 'exit' para sair.");
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line.trim().equalsIgnoreCase("exit")) {
                break;
            }
            String[] inputArgs = line.trim().split("\\s+");
            if (inputArgs.length == 0 || inputArgs[0].isEmpty()) {
                System.out.println("ERRO: Nenhum argumento fornecido.");
                printUsage();
                continue;
            }

            switch (inputArgs[0]) {
                case "ce" -> {
                    if (inputArgs.length == 5) {
                        service.cadastrarEstudante(inputArgs[1], inputArgs[2], inputArgs[3], inputArgs[4]);
                    } else {
                        System.out.println("ERRO, TEMPLATE É: ce <NOME_EST> <EMAIL_EST> <SENHA_EST> <MATRICULA_EST>");
                    }
                }
                case "cp" -> {
                    if (inputArgs.length == 5) {
                        service.cadastrarProfessor(inputArgs[1], inputArgs[2], inputArgs[3], inputArgs[4]);
                    } else {
                        System.out
                                .println("ERRO, TEMPLATE É: cp <NOME_PROF> <EMAIL_PROF> <SENHA_PROF> <MATRICULA_PROF>");
                    }
                }
                case "lu" -> {
                    if (inputArgs.length == 3) {
                        System.out.println("Login não implementado.");
                    } else {
                        System.out.println("ERRO, TEMPLATE É: lu <EMAIL> <SENHA>");
                    }
                }
                case "ca" -> {
                    if (inputArgs.length == 6) {
                        Estudante estudante = new Estudante(0, "Ze das couves", "bla", "Bla", "Bla");
                        Disciplina d = new Disciplina(0, "bla", "bla", 4);
                        Professor prof = new Professor(0, "Ze das couves", "bla", "Bla", "Bla");
                        Turma turma = new Turma(0, "Bla", "Bla", "blA", d, prof);
                        try {
                            float nota = Float.parseFloat(inputArgs[2]);
                            Tag tag = Tag.valueOf(inputArgs[5]);
                            service.cadastrarAvaliacao(estudante, inputArgs[1], turma, nota, inputArgs[4],
                                    LocalDate.now(), tag);
                        } catch (NumberFormatException e) {
                            System.out.println("ERRO: Nota inválida.");
                        } catch (IllegalArgumentException e) {
                            System.out.println("ERRO: Tag inválida.");
                        }
                    } else {
                        System.out.println(
                                "ERRO, TEMPLATE É: ca <TITULO_DISC> <NOTA_DISC> <NUM_TURMA> <COMENTARIO> <TAG>");
                    }
                }
                case "la" -> {
                    System.out.println(service.listaAvaliacao());
                }
                case "ct" -> {
                    if (inputArgs.length == 6) {
                        try {
                            service.cadastrarTurma(inputArgs[1], inputArgs[2], inputArgs[3], inputArgs[4], inputArgs[5]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } 
                    } else {
                        System.out.println(
                                "ERRO, TEMPLATE É: ct <SALA> <HORA> <CODIGO> <NOME_DISCIPLINA> <PROFESSOR>");
                    }
                }
                default -> {
                    System.out.println("Comando não reconhecido.");
                    printUsage();
                }
            }
        }

        DBConnection.closeConnection();
        scanner.close();
    }

    private static void printUsage() {
        System.out.println("""
                Uso:
                  ce <NOME_EST> <EMAIL_EST> <SENHA_EST> <MATRICULA_EST>
                  cp <NOME_PROF> <EMAIL_PROF> <SENHA_PROF> <MATRICULA_PROF>
                  lu <EMAIL> <SENHA>
                  ca <TITULO_DISC> <NOTA_DISC> <NUM_TURMA> <COMENTARIO> <TAG>
                  la (entradas opcionais) <NOME_DISCIPLINA>
                  ct <SALA> <HORA> <CODIGO> <NOME_DISCIPLINA> <PROFESSOR> 
                """);
    }
}
