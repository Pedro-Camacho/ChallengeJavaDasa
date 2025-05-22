package dasa.modelo;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para representar os pacientes
 */
public class Paciente {
    private static int contadorId = 1;
    private static final String ARQUIVO_PACIENTES = "pacientes.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int id;
    private String nomeCompleto;
    private String cpf;
    private String dataNascimento;
    private String dataExame;
    private String convenio;
    private String preferencial;
    private String jejum;
    private String exame;
    private String status;
    private String enfermeiroResponsavel;
    private String responsavelColeta;

    // Construtor
    public Paciente(String nomeCompleto, String cpf, String dataNascimento,
                    String convenio, String preferencial, String jejum, String exame) {
        this.id = obterProximoId();
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.dataExame = LocalDateTime.now().format(formatter);
        this.convenio = convenio;
        this.preferencial = preferencial;
        this.jejum = jejum;
        this.exame = exame;
        this.status = "Em espera";
        this.enfermeiroResponsavel = "Em espera";
        this.responsavelColeta = "Em espera";
    }

    // Construtor para carregar do arquivo
    public Paciente(int id, String nomeCompleto, String cpf, String dataNascimento,
                    String dataExame, String convenio, String preferencial, String jejum,
                    String exame, String status, String enfermeiroResponsavel, String responsavelColeta) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.dataExame = dataExame;
        this.convenio = convenio;
        this.preferencial = preferencial;
        this.jejum = jejum;
        this.exame = exame;
        this.status = status;
        this.enfermeiroResponsavel = enfermeiroResponsavel;
        this.responsavelColeta = responsavelColeta;

        // Atualiza o contador se necessário
        if (id >= contadorId) {
            contadorId = id + 1;
        }
    }

    private static synchronized int obterProximoId() {
        return contadorId++;
    }

    public void salvarNoArquivo() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_PACIENTES, true))) {
            writer.println(this.paraStringArquivo());
        } catch (IOException e) {
            System.out.println("Erro ao salvar paciente: " + e.getMessage());
        }
    }

    public static List<Paciente> carregarPacientes() {
        List<Paciente> pacientes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_PACIENTES))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    Paciente paciente = fromStringArquivo(linha);
                    if (paciente != null) {
                        pacientes.add(paciente);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // Arquivo ainda não existe, retorna lista vazia
        } catch (IOException e) {
            System.out.println("Erro ao carregar pacientes: " + e.getMessage());
        }

        return pacientes;
    }

    private String paraStringArquivo() {
        return id + "|" + nomeCompleto + "|" + cpf + "|" + dataNascimento + "|" +
                dataExame + "|" + convenio + "|" + preferencial + "|" + jejum + "|" +
                exame + "|" + status + "|" + enfermeiroResponsavel + "|" + responsavelColeta;
    }

    /**
     * Cria um paciente a partir de uma string do arquivo
     */
    private static Paciente fromStringArquivo(String linha) {
        try {
            String[] dados = linha.split("\\|");
            if (dados.length == 12) {
                return new Paciente(
                        Integer.parseInt(dados[0]), // id
                        dados[1], // nomeCompleto
                        dados[2], // cpf
                        dados[3], // dataNascimento
                        dados[4], // dataExame
                        dados[5], // convenio
                        dados[6], // preferencial
                        dados[7], // jejum
                        dados[8], // exame
                        dados[9], // status
                        dados[10], // enfermeiroResponsavel
                        dados[11]  // responsavelColeta
                );
            }
        } catch (Exception e) {
            System.out.println("Erro ao processar linha do arquivo: " + linha);
        }
        return null;
    }

    /**
     * Exibe os dados do paciente formatados
     */
    public void exibirDados() {
        System.out.println("ID: #" + id);
        System.out.println("Status: " + status);
        System.out.println("\tNome Completo: " + nomeCompleto);
        System.out.println("\tCPF: " + cpf);
        System.out.println("\tData Nascimento: " + dataNascimento);
        System.out.println("\tConvenio: " + convenio);
        System.out.println("\tPreferencial: " + preferencial);
        System.out.println("\tJejum (min. 8 horas): " + jejum);
        System.out.println("\tExame: " + exame);
        System.out.println("\tData de Realização do Exame: " + dataExame);
        System.out.println("\tEnfermeiro Responsável: " + enfermeiroResponsavel);
        System.out.println("\tResponsável Coleta de Insumos: " + responsavelColeta);
        System.out.println("========================================================");
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getDataExame() {
        return dataExame;
    }

    public void setDataExame(String dataExame) {
        this.dataExame = dataExame;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public String getPreferencial() {
        return preferencial;
    }

    public void setPreferencial(String preferencial) {
        this.preferencial = preferencial;
    }

    public String getJejum() {
        return jejum;
    }

    public void setJejum(String jejum) {
        this.jejum = jejum;
    }

    public String getExame() {
        return exame;
    }

    public void setExame(String exame) {
        this.exame = exame;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnfermeiroResponsavel() {
        return enfermeiroResponsavel;
    }

    public void setEnfermeiroResponsavel(String enfermeiroResponsavel) {
        this.enfermeiroResponsavel = enfermeiroResponsavel;
    }

    public String getResponsavelColeta() {
        return responsavelColeta;
    }

    public void setResponsavelColeta(String responsavelColeta) {
        this.responsavelColeta = responsavelColeta;
    }
}
