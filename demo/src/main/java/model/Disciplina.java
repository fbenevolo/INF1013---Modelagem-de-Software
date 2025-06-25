package model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Disciplina {
    private long id;
    private String codigo;
    private String nome;
    private int numeroDeCreditos;

    private Set<Disciplina> preRequisitos;

    public Disciplina(){}

    public Disciplina(long id, String codigo, String nome, int numeroDeCreditos) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.numeroDeCreditos = numeroDeCreditos;
        this.preRequisitos = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumeroDeCreditos() {
        return numeroDeCreditos;
    }

    public void setNumeroDeCreditos(int numeroDeCreditos) {
        this.numeroDeCreditos = numeroDeCreditos;
    }

    public Set<Disciplina> getPreRequisitos() {
        return preRequisitos;
    }

    public void setPreRequisitos(Set<Disciplina> preRequisitos) {
        this.preRequisitos = preRequisitos;
    }

    public void adicionarPreRequisito(Disciplina preRequisito) {
        this.preRequisitos.add(preRequisito);
        // Gerenciamento de link bidirecional:
        // preRequisito.adicionarPosRequisito(this);
    }

    public void removerPreRequisito(Disciplina preRequisito) {
        this.preRequisitos.remove(preRequisito);
        // Gerenciamento de link bidirecional:
        // preRequisito.removerPosRequisito(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Disciplina that = (Disciplina) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String preRequisitosStr = preRequisitos != null ? preRequisitos.stream()
                .map(d -> d.getCodigo())
                .collect(java.util.stream.Collectors.joining(", ", "[", "]"))
                : "[]";
        return "Disciplina{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", numeroDeCreditos=" + numeroDeCreditos +
                ", preRequisitos=" + preRequisitosStr + '}';
    }
}