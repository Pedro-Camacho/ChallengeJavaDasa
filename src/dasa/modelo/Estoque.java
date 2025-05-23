package dasa.modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para gerenciar o estoque de materiais do laboratório
 */
public class Estoque {
    private static final String ARQUIVO_ESTOQUE = "estoque.txt";
    private static final int QUANTIDADE_INICIAL = 1500;
    private static final int QUANTIDADE_MAXIMA = 2000;
    private List<Material> materiais;

    public Estoque() {
        materiais = new ArrayList<>();
        carregarEstoque();
    }

    /**
     * Inicializa o estoque com todos os materiais dos exames
     */
    private void inicializarEstoque() {
        // Tubos de coleta (Hemograma Completo)
        materiais.add(new Material(1051, "Tubo de Coleta Pequeno", 1000051, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        materiais.add(new Material(1052, "Tubo de Coleta Médio", 1000052, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        materiais.add(new Material(1053, "Tubo de Coleta Grande", 1000053, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));

        // Agulhas (Hemograma Completo e Glicemia)
        materiais.add(new Material(2071, "Agulha 2mm", 2000071, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        materiais.add(new Material(2072, "Agulha 3mm", 2000072, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        materiais.add(new Material(2073, "Agulha 5mm", 2000073, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));

        // Seringas (Hemograma Completo e Glicemia)
        materiais.add(new Material(3081, "Seringa 5ml", 3000081, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        materiais.add(new Material(3082, "Seringa 10ml", 3000082, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        materiais.add(new Material(3083, "Seringa 20ml", 3000083, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));

        // Recipiente estéril (Exame de Urina)
        materiais.add(new Material(4091, "Recipiente Estéril Pequeno", 4000091, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        materiais.add(new Material(4092, "Recipiente Estéril Médio", 4000092, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        materiais.add(new Material(4093, "Recipiente Estéril Grande", 4000093, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));

        // Tiras reagentes (Exame de Urina e Glicemia)
        materiais.add(new Material(5001, "Tira Reagente Tipo A", 5000001, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        materiais.add(new Material(5002, "Tira Reagente Tipo B", 5000002, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        materiais.add(new Material(5003, "Tira Reagente Tipo C", 5000003, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));

        // Lâmina para análise (Exame de Urina)
        materiais.add(new Material(6011, "Lâmina Análise Simples", 6000011, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        materiais.add(new Material(6012, "Lâmina Análise Dupla", 6000012, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        materiais.add(new Material(6013, "Lâmina Análise Tripla", 6000013, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));

        // Tubo sem anticoagulante (Exame de Glicemia)
        materiais.add(new Material(7021, "Tubo sem Anticoagulante Pequeno", 7000021, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        materiais.add(new Material(7022, "Tubo sem Anticoagulante Médio", 7000022, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        materiais.add(new Material(7023, "Tubo sem Anticoagulante Grande", 7000023, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));

        salvarEstoque();
    }

    /**
     * Carrega o estoque do arquivo ou inicializa se não existir
     */
    private void carregarEstoque() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_ESTOQUE))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    Material material = Material.fromStringArquivo(linha);
                    if (material != null) {
                        materiais.add(material);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // Arquivo não existe, inicializa estoque
            inicializarEstoque();
        } catch (IOException e) {
            System.out.println("Erro ao carregar estoque: " + e.getMessage());
            inicializarEstoque();
        }
    }

    /**
     * Salva o estoque no arquivo
     */
    public void salvarEstoque() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_ESTOQUE))) {
            for (Material material : materiais) {
                writer.println(material.paraStringArquivo());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar estoque: " + e.getMessage());
        }
    }

    /**
     * Exibe todos os materiais do estoque
     */
    public void exibirEstoque() {
        System.out.println();
        System.out.println("=== ESTOQUE DE MATERIAIS ===");

        for (Material material : materiais) {
            material.exibirDados();
        }
    }

    /**
     * Busca material por ID ou código de barras
     */
    public Material buscarMaterial(int identificador) {
        for (Material material : materiais) {
            if (material.getId() == identificador || material.getCodigoBarras() == identificador) {
                return material;
            }
        }
        return null;
    }

    /**
     * Adiciona quantidade a um material
     */
    public boolean adicionarQuantidade(int identificador, int quantidade) {
        Material material = buscarMaterial(identificador);
        if (material != null) {
            boolean sucesso = material.adicionarQuantidade(quantidade);
            if (sucesso) {
                salvarEstoque();
            }
            return sucesso;
        }
        return false;
    }

    /**
     * Remove quantidade de um material
     */
    public boolean removerQuantidade(int identificador, int quantidade) {
        Material material = buscarMaterial(identificador);
        if (material != null) {
            boolean sucesso = material.removerQuantidade(quantidade);
            if (sucesso) {
                salvarEstoque();
            }
            return sucesso;
        }
        return false;
    }

    /**
     * Calcula a quantidade máxima que pode ser adicionada a um material
     */
    public int calcularQuantidadeMaximaAdicao(int identificador) {
        Material material = buscarMaterial(identificador);
        if (material != null) {
            return material.getQuantidadeMaxima() - material.getQuantidadeDisponivel();
        }
        return 0;
    }

    public List<Material> getMateriais() {
        return materiais;
    }
}
