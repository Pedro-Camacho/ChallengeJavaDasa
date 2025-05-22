package model;

public class Enfermeiro {
    private String nomeEnfermeiro;
    private int crm;

    public Enfermeiro(String nome, int crm) {
        this.nomeEnfermeiro = nome;
        this.crm = crm;
    }

    public String getNome() {
        return nomeEnfermeiro;
    }

    public void setNome(String nome) {
        this.nomeEnfermeiro = nome;
    }

    public int getCrm() {
        return crm;
    }

    public void setCrm(int crm) {
        this.crm = crm;
    }
}
