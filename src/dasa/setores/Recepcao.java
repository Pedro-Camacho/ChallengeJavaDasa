package dasa.setores;

import dasa.modelo.Exame;
import dasa.modelo.Paciente;
import java.util.*;

/**
 * Classe responsável pelas funcionalidades da Recepção
 */
public class Recepcao {
    private Scanner scanner;

    public Recepcao(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Menu da Recepção
     */
    public void exibirMenuRecepcao() {
        while (true) {
            try {
                System.out.println();
                System.out.println("=== RECEPÇÃO ===");
                System.out.println("1 - Cadastrar Paciente");
                System.out.println("2 - Visualizar Todos Pacientes Registrados");
                System.out.println("3 - Voltar");
                System.out.print("Opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consome a quebra de linha

                switch (opcao) {
                    case 1:
                        cadastrarPaciente();
                        break;
                    case 2:
                        visualizarPacientes();
                        break;
                    case 3:
                        return; // Volta ao menu anterior
                    default:
                        System.out.println("ERRO: Opção inválida! Digite um número de 1 a 3.");
                }

            } catch (InputMismatchException e) {
                System.out.println("ERRO: Digite apenas números!");
                scanner.nextLine(); // Limpa o buffer do scanner
            } catch (Exception e) {
                System.out.println("ERRO: " + e.getMessage());
            }
        }
    }

    /**
     * Cadastra um novo paciente
     */
    private void cadastrarPaciente() {
        try {
            System.out.println();
            System.out.println("=== CADASTRAR PACIENTE ===");

            // Coleta dos dados do paciente
            System.out.print("Nome completo: ");
            String nomeCompleto = scanner.nextLine().trim();

            System.out.print("CPF: ");
            String cpf = scanner.nextLine().trim();

            System.out.print("Data de Nascimento (dd/mm/aaaa): ");
            String dataNascimento = scanner.nextLine().trim();

            // Convenio
            String convenio = "";
            while (true) {
                try {
                    System.out.print("Convênio (1 - Sim, 2 - Não): ");
                    int opcaoConvenio = scanner.nextInt();
                    scanner.nextLine();

                    if (opcaoConvenio == 1) {
                        convenio = "Sim";
                        break;
                    } else if (opcaoConvenio == 2) {
                        convenio = "Não";
                        break;
                    } else {
                        System.out.println("ERRO: Digite 1 para Sim ou 2 para Não!");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("ERRO: Digite apenas números!");
                    scanner.nextLine();
                }
            }

            // Preferencial
            String preferencial = "";
            while (true) {
                try {
                    System.out.print("Preferencial (1 - Sim, 2 - Não): ");
                    int opcaoPreferencial = scanner.nextInt();
                    scanner.nextLine();

                    if (opcaoPreferencial == 1) {
                        preferencial = "Sim";
                        break;
                    } else if (opcaoPreferencial == 2) {
                        preferencial = "Não";
                        break;
                    } else {
                        System.out.println("ERRO: Digite 1 para Sim ou 2 para Não!");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("ERRO: Digite apenas números!");
                    scanner.nextLine();
                }
            }

            // Jejum
            String jejum = "";
            while (true) {
                try {
                    System.out.print("Em Jejum (min. 8 horas) (1 - Sim, 2 - Não): ");
                    int opcaoJejum = scanner.nextInt();
                    scanner.nextLine();

                    if (opcaoJejum == 1) {
                        jejum = "Sim";
                        break;
                    } else if (opcaoJejum == 2) {
                        jejum = "Não";
                        break;
                    } else {
                        System.out.println("ERRO: Digite 1 para Sim ou 2 para Não!");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("ERRO: Digite apenas números!");
                    scanner.nextLine();
                }
            }

            // Escolha do exame
            String exameEscolhido = adicionarExame();
            if (exameEscolhido == null) {
                return; // Retorna ao menu da recepção se houve erro na escolha do exame
            }

            // Cria e salva o paciente
            Paciente novoPaciente = new Paciente(nomeCompleto, cpf, dataNascimento,
                    convenio, preferencial, jejum, exameEscolhido);
            novoPaciente.salvarNoArquivo();

            System.out.println();
            System.out.println("✅ Paciente cadastrado com sucesso!");
            System.out.println("Status: Em espera");

        } catch (Exception e) {
            System.out.println("ERRO ao cadastrar paciente: " + e.getMessage());
        }
    }

    /**
     * Menu para adicionar exame ao paciente
     * @return Nome do exame escolhido ou null se houve erro
     */
    private String adicionarExame() {
        // Lista de exames disponíveis
        List<Exame> examesDisponiveis = new ArrayList<>();
        examesDisponiveis.add(new Exame(123, "Hemograma Completo"));
        examesDisponiveis.add(new Exame(456, "Exame de Urina"));
        examesDisponiveis.add(new Exame(789, "Exame de Glicemia"));

        while (true) {
            try {
                System.out.println();
                System.out.println("=== ADICIONAR EXAME ===");
                System.out.println("Menu de Opções de Exames Laboratoriais:");

                // Exibe os exames disponíveis
                for (Exame exame : examesDisponiveis) {
                    System.out.println(exame.toString());
                }

                System.out.print("Digite o ID do exame desejado: ");
                String input = scanner.nextLine().trim();

                // Converter para int
                int idEscolhido = Integer.parseInt(input);

                // Busca o exame pelo ID
                for (Exame exame : examesDisponiveis) {
                    if (exame.getId() == idEscolhido) {
                        return exame.getNome();
                    }
                }

                System.out.println("ERRO: ID inválido!");
                return null; // Retorna ao menu da recepção

            } catch (Exception e) {
                System.out.println("ERRO: " + e.getMessage());
                return null;
            }
        }
    }

    /**
     * Visualiza todos os pacientes registrados
     */
    private void visualizarPacientes() {
        System.out.println();
        System.out.println("=== TODOS OS PACIENTES REGISTRADOS ===");

        List<Paciente> pacientes = Paciente.carregarPacientes();

        if (pacientes.isEmpty()) {
            System.out.println("Não há pacientes registrados.");
        } else {
            for (Paciente paciente : pacientes) {
                paciente.exibirDados();
            }
        }
    }
}
