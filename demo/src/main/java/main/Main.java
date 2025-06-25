package main;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DBConnection.DBConnection;
import controller.Controller;
import model.Tag;

public class Main {
    public static void main(String[] args) throws Exception {
        DBConnection.initializeDatabaseIfNeeded();
        Controller controller = new Controller();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite um comando ou 'exit' para sair.");
        printUsage();
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line.trim().equalsIgnoreCase("exit")) {
                break;
            }
            Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
            Matcher matcher = pattern.matcher(line);
            java.util.List<String> inputArgsList = new java.util.ArrayList<>();
            while (matcher.find()) {
                if (matcher.group(1) != null) {
                    inputArgsList.add(matcher.group(1));
                } else {
                    inputArgsList.add(matcher.group(2));
                }
            }
            String[] inputArgs = inputArgsList.toArray(new String[0]);

            switch (inputArgs[0]) {
                case "ce" -> {
                    if (inputArgs.length == 5) {
                        controller.cadastrarEstudante(inputArgs[1], inputArgs[2], inputArgs[3], inputArgs[4]);
                    } else {
                        System.out.println("ERRO, TEMPLATE É: ce <NOME_EST> <EMAIL_EST> <SENHA_EST> <MATRICULA_EST>");
                    }
                }
                case "cp" -> {
                    if (inputArgs.length == 5) {
                        var p = controller.cadastrarProfessor(inputArgs[1], inputArgs[2], inputArgs[3], inputArgs[4]);
                        if (p != null) {
                            System.out.println("Professor cadastrado: " + p);
                        } else {
                            System.out.println("Erro ao cadastrar professor.");
                        }
                    } else {
                        System.out
                                .println("ERRO, TEMPLATE É: cp <NOME_PROF> <EMAIL_PROF> <SENHA_PROF> <MATRICULA_PROF>");
                    }
                }
                case "lu" -> {
                    if (inputArgs.length == 3) {
                        try {
                            String email = inputArgs[1];
                            String senha = inputArgs[2];
                            controller.login(email, senha);
                            System.out.println("Login realizado com sucesso!");
                        } catch (IllegalArgumentException e) {
                            System.out.println("ERRO: " + e.getMessage());
                        }
                    } else {
                        System.out.println("ERRO, TEMPLATE É: lu <EMAIL> <SENHA>");
                    }
                }
                case "eu" -> {
                    if (inputArgs.length == 1) {
                        var usuarioLogado = controller.getUsuarioLogado();
                        if (usuarioLogado != null) {
                            System.out.println("Usuário logado: " + usuarioLogado);
                        } else {
                            System.out.println("Nenhum usuário logado.");
                        }
                    } else {
                        System.out.println("ERRO, TEMPLATE É: eu");
                    }
                }
                case "fa" -> {
                    if (inputArgs.length == 6) {
                        try {
                            String titulo = inputArgs[1];
                            String comentario = inputArgs[4];
                            float nota = Float.parseFloat(inputArgs[2]);
                            Tag tag = Tag.valueOf(inputArgs[5]);
                            long turmaId = Long.parseLong(inputArgs[3]);
                            var a = controller.fazerAvaliacao(nota, titulo, comentario, turmaId, tag);
                            if (a != null) {
                                System.out.println("Avaliação feita: " + a);
                            } else {
                                System.out.println("Erro ao fazer avaliação.");
                            }

                        } catch (IllegalStateException e) {
                            System.out.println("ERRO: " + e.getMessage());
                        } catch (IllegalArgumentException e) {
                            System.out.println("ERRO: " + e.getMessage());
                        }
                    } else {
                        System.out.println(
                                "ERRO, TEMPLATE É: fa <TITULO_DISC> <NOTA_DISC> <ID_TURMA> <COMENTARIO> <TAG>");
                    }
                }
                case "la" -> {
                    if (inputArgs.length == 2){
                        System.out.println(controller.listarAvaliacaoPorDisciplina(args[1]));
                    }else {
                        System.out.println(controller.listarAvaliacoes());
                    }
                }
                case "lt" -> {
                    if (inputArgs.length == 1) {
                        System.out.println(controller.listarTurmas());
                    } else {
                        System.out.println("ERRO, TEMPLATE É: lt");
                    }
                }
                case "cd" -> {
                    if (inputArgs.length == 4) {
                        try {
                            String codigo = inputArgs[1];
                            String nome = inputArgs[2];
                            int creditos = Integer.parseInt(inputArgs[3]);
                            var d = controller.cadastrarDisciplina(codigo, nome, creditos);
                            if (d != null) {
                                System.out.println("Disciplina cadastrada: " + d);
                            } else {
                                System.out.println("Erro ao cadastrar disciplina.");
                            }
                        } catch (Exception e) {
                            System.out.println("ERRO: " + e.getMessage());
                        }
                    } else {
                        System.out.println(
                                "ERRO, TEMPLATE É: cd <CODIGO> <NOME> <CREDITOS>");
                    }
                }
                case "ct" -> {
                    if (inputArgs.length == 6) {
                        try {
                            var t = controller.cadastrarTurma(inputArgs[1], inputArgs[2], inputArgs[3], inputArgs[4],
                                    inputArgs[5]);
                            if (t != null) {
                                System.out.println("Turma cadastrada: " + t);
                            } else {
                                System.out.println("Erro ao cadastrar turma.");
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println("ERRO: " + e.getMessage());
                        }
                    } else {
                        System.out.println(
                                "ERRO, TEMPLATE É: ct <SALA> <HORA> <CODIGO> <NOME_DISCIPLINA> <PROFESSOR>");
                    }
                }
                case "ex" -> {
                    if (inputArgs.length == 1) {
                        try {
                            controller.logout();
                            System.out.println("Logout realizado com sucesso!");
                        } catch (IllegalStateException e) {
                            System.out.println("ERRO: " + e.getMessage());
                        }
                    } else {
                        System.out.println(
                                "ERRO, TEMPLATE É: ex");
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
                  ce (Cadastrar Estudante) <NOME_EST> <EMAIL_EST> <SENHA_EST> <MATRICULA_EST>
                  cp (Cadastrar Professor) <NOME_PROF> <EMAIL_PROF> <SENHA_PROF> <MATRICULA_PROF>
                  lu (Login Usuario ) <EMAIL> <SENHA>
                  fa (Fazer Avaliação) <TITULO_DISC> <NOTA_DISC> <NUM_TURMA> <COMENTARIO> <TAG>
                  la (Listar Avaliações) (entradas opcionais) <NOME_DISCIPLINA>
                  ct (Cadastar Turma) <SALA> <HORA> <CODIGO> <NOME_DISCIPLINA> <PROFESSOR>
                  cd (Cadastra Disciplina) <codigo> <HORA> <CODIGO> <NOME_DISCIPLINA> <PROFESSOR>
                  lt (Listar Turmas)
                  eu (Logout)
                  ex (Conseguir usuário logado)
                """);
    }
}
