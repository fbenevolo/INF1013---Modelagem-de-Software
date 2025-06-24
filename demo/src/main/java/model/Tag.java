package model;

public enum Tag {
    RECLAMACAO("Reclamação"),
    ELOGIO("Elogio"),
    DICA("Dica");

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