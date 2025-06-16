package model;

public enum Tag {
    DICA("Dica"),
    RECLAMACAO("Reclamação"),
    RESUMO("Resumo");

    private final String nomeExibicao;

    Tag(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }

    @Override
    public String toString() {
        return nomeExibicao;
    }
}