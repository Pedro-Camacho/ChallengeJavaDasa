package dasa.modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para representar materiais/insumos do laboratório
 */
public class Material {
    private int id;
    private String nome;
    private int codigoBarras;
    private int quantidadeDisponivel;
    private int quantidadeMaxima;

    public Material(int id, String nome, int codigoBarras, int quantidadeDisponivel, int quantidadeMaxima) {
        this.id = id;
        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.quantidadeMaxima = quantidadeMaxima;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(int codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public int getQuantidadeMaxima() {
        return quantidadeMaxima;
    }

    public void setQuantidadeMaxima(int quantidadeMaxima) {
        this.quantidadeMaxima = quantidadeMaxima;
    }

    /**
     * Adiciona quantidade ao estoque se não ultrapassar o máximo
     */
    public boolean adicionarQuantidade(int quantidade) {
        if (quantidadeDisponivel + quantidade <= quantidadeMaxima) {
            quantidadeDisponivel += quantidade;
            return true;
        }
        return false;
    }

    /**
     * Remove quantidade do estoque se houver disponível
     */
    public boolean removerQuantidade(int quantidade) {
        if (quantidadeDisponivel >= quantidade) {
            quantidadeDisponivel -= quantidade;
            return true;
        }
        return false;
    }

    /**
     * Exibe os dados do material formatados
     */
    public void exibirDados() {
        System.out.println("ID material: " + id + " - nome material: " + nome);
        System.out.println("\tCódigo de Barras Produto: " + codigoBarras);
        System.out.println("\tQuantidade Disponível: " + quantidadeDisponivel);
        System.out.println("\tQuantidade Máxima: " + quantidadeMaxima);
        System.out.println("=============================================================");
    }

    /**
     * Converte o material para string para salvar no arquivo
     */
    public String paraStringArquivo() {
        return id + "|" + nome + "|" + codigoBarras + "|" + quantidadeDisponivel + "|" + quantidadeMaxima;
    }

    /**
     * Cria um material a partir de uma string do arquivo
     */
    public static Material fromStringArquivo(String linha) {
        try {
            String[] dados = linha.split("\\|");
            if (dados.length == 5) {
                return new Material(
                        Integer.parseInt(dados[0]), // id
                        dados[1], // nome
                        Integer.parseInt(dados[2]), // codigoBarras
                        Integer.parseInt(dados[3]), // quantidadeDisponivel
                        Integer.parseInt(dados[4])  // quantidadeMaxima
                );
            }
        } catch (Exception e) {
            System.out.println("Erro ao processar linha do arquivo: " + linha);
        }
        return null;
    }
}
