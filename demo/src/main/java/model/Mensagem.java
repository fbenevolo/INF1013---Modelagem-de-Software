package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Mensagem {
    private long id;
    private String conteudo;
    private LocalDateTime dataHora;
    private Usuario remetente;
    private Mensagem mensagemPai;

    public Mensagem(long id, String conteudo, LocalDateTime dataHora, Usuario remetente, Mensagem mensagemPai) {
        this.id = id;
        this.conteudo = conteudo;
        this.dataHora = dataHora;
        this.remetente = remetente;
        this.mensagemPai = mensagemPai;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Usuario getRemetente() {
        return remetente;
    }

    public void setRemetente(Usuario remetente) {
        this.remetente = remetente;
    }

    public Mensagem getMensagemPai() {
        return mensagemPai;
    }

    public void setMensagemPai(Mensagem mensagemPai) {
        this.mensagemPai = mensagemPai;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Mensagem mensagem = (Mensagem) o;
        return Objects.equals(id, mensagem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Mensagem{" +
                "id=" + id +
                ", conteudo='" + conteudo + '\'' +
                ", dataHora=" + dataHora +
                ", remetente=" + (remetente != null ? remetente.getNome() : "N/A") +
                ", mensagemPaiId=" + (mensagemPai != null ? mensagemPai.getId() : "N/A") +
                '}';
    }
}