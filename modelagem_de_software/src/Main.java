import model.*;
import controller.Controller;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();

        var d1 = controller.cadastrarDisciplina("INF1013", "Modelagem de Software", 4);

        var p1 = controller.cadastrarProfessor("João Silva", "j@e1.com", "senha", "12345");

        var t1 = controller.cadastrarTurma("L546", "11-13", "3WA", d1.getId(), p1.getId());

        var e1 = controller.cadastrarEstudante("Maria Oliveira", "maria@e1.com", "senha", "6789");
        controller.matricularEstudante(e1.getId(), t1.getId());

        var e2 = controller.cadastrarEstudante("Carlos Souza", "calos@e2.com", "senha", "6789");
        controller.matricularEstudante(e2.getId(), t1.getId());

        controller.fazerAvaliacao(5, "Aprendi muito!", "Otima disciplina, consegui aprender bastante.", e1.getId(),
                t1.getId(), Tag.ELOGIO);

        System.out.println(controller);
    }
}
