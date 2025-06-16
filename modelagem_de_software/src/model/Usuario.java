package model;
public abstract class Usuario {
    protected String nome;
    protected String email;

    public Usuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public Mensagem criarMensagem(String conteudo) {
        return new Mensagem(this, conteudo);
    }

    public String getNome() {
        return nome;
    }
}
