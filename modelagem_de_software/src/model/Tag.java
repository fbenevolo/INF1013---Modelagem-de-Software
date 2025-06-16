package model;
public class Tag {
    private String tipo; // Ex: Dica, Reclamação, Resumo

    public Tag(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
