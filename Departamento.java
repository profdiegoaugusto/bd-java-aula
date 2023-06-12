package org.exemplo.models;

public class Departamento {

    private int id;
    private String nome;
    private String descricao;

    public Departamento() {

    }

    public Departamento(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Departamento(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%d - %s \n", id, nome));
        sb.append(String.format("%s\n", descricao));

        return sb.toString();
    }
}