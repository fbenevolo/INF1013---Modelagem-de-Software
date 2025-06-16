package model;
import java.time.LocalDateTime;

public class Mensagem {
    private Usuario autor;
    private String conteudo;
    private LocalDateTime dataHora;

    public Mensagem(Usuario autor, String conteudo) {
        this.autor = autor;
        this.conteudo = conteudo;
        this.dataHora = LocalDateTime.now();
    }
}
