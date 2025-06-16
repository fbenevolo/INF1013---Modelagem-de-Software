package model;
import java.time.LocalDateTime;

public class Avaliacao {
    private int nota;
    private String comentario;
    private String titulo;
    private LocalDateTime data;
    private Estudante autor;
    private Tag tag;

    public Avaliacao(int nota, String comentario, String titulo, Estudante autor, String tipoTag) {
        this.nota = nota;
        this.comentario = comentario;
        this.titulo = titulo;
        this.data = LocalDateTime.now();
        this.autor = autor;
        this.tag = new Tag(tipoTag);
    }

    @Override
    public String toString() {
        return "[" + data + "] " + autor.getNome() + " avaliou '" + titulo + "' com nota " + nota + " e comentou: " + comentario + " [" + tag.getTipo() + "]";
    }
}
