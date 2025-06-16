package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.LinkedList;

public class Avaliacao {
    private Long id;
    private double nota;
    private String comentario;
    private LocalDate data;
    private String titulo;

    private Estudante estudante;
    private Turma turma;
    private Tag tag;

    private List<Mensagem> mensagens;

    public Avaliacao(Long id, double nota, String comentario, LocalDate data, String titulo, Estudante estudante,
            Turma turma, Tag tag) {
        this.id = id;
        this.nota = nota;
        this.comentario = comentario;
        this.data = data;
        this.titulo = titulo;
        this.estudante = estudante;
        this.turma = turma;
        this.tag = tag;
        this.mensagens = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Tag getTags() {
        return tag;
    }

    public void setTags(Tag tag) {
        this.tag = tag;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public Mensagem pegaUltimaMensagem() {
        if (this.mensagens.isEmpty()) {
            return null;
        }
        return this.mensagens.get(this.mensagens.size() - 1);
    }

    public void inserirMensagem(Usuario remetente, String conteudo) {
        var mensagem = new Mensagem(mensagens.size() + 1, conteudo,
                LocalDateTime.now(), remetente, this.pegaUltimaMensagem());
        this.mensagens.add(mensagem);
    }

    public void apagarMensagem(Mensagem mensagem) {
        var lenPrev = this.mensagens.size();
        this.mensagens.remove(mensagem);
        if (lenPrev == this.mensagens.size()) {
            System.out.println("Mensagem não encontrada: " + mensagem);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Avaliacao avaliacao = (Avaliacao) o;
        return Objects.equals(id, avaliacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Avaliacao{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", nota=" + nota +
                ", estudante=" + (estudante != null ? estudante.getNome() : "N/A") +
                ", turma=" + (turma != null ? turma.getCodigo() : "N/A") +
                ", tag=" + (tag != null ? tag.getNomeExibicao() : "N/A") +
                ", data=" + data +
                ", Quantidade de mensagens=" + mensagens.size() +
                '}';
    }
}