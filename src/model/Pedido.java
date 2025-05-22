package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {
    private List<Item> itensPedido = new ArrayList<>();
    private int codigo;
    private Enfermeiro enfermeiroRetirada;
    private Date dataRetirada;

    public Pedido( int codigo, Enfermeiro enfermeiroRetirada, Date data) {
        this.codigo = codigo;
        this.enfermeiroRetirada = enfermeiroRetirada;
        this.dataRetirada = data;
    }

    public List<Item> getItens() {
        return itensPedido;
    }

    public int getCodigo() {
        return codigo;
    }

    public Enfermeiro getEnfermeiroRetirada() {
        return enfermeiroRetirada;
    }

    public void adicionarItemLista(Item itemAdicionar){
        itensPedido.add(itemAdicionar);
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setEnfermeiroRetirada(Enfermeiro enfermeiroRetirada) {
        this.enfermeiroRetirada = enfermeiroRetirada;
    }
}
