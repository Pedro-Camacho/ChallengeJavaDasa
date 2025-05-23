package dasa.sistema;

import dasa.funcionarios.TecnicoLaboratorio;
import dasa.funcionarios.Enfermeiro;
import dasa.modelo.Exame;
import dasa.modelo.Paciente;
import dasa.modelo.Estoque;
import dasa.modelo.Material;
import java.util.*;

/**
 * Classe principal do sistema do laboratório
 */
public class SistemaLaboratorio {
    private static List<TecnicoLaboratorio> tecnicos;
    private static List<Enfermeiro> enfermeiros;
    private static Estoque estoque;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        inicializarTecnicos();
        inicializarEnfermeiros();
        estoque = new Estoque();

        try {
            exibirBoasVindas();
            exibirTecnicosCadastrados();

            TecnicoLaboratorio tecnico = autenticarTecnico();
            if (tecnico != null) {
                tecnico.acessarSistema();
                exibirMenuPrincipal(tecnico);
            }

        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    /**
     * Inicializa a lista com 3 técnicos pré-definidos
     */
    private static void inicializarTecnicos() {
        tecnicos = new ArrayList<>();
        tecnicos.add(new TecnicoLaboratorio("João Silva", "12345"));
        tecnicos.add(new TecnicoLaboratorio("Maria Santos", "67890"));
        tecnicos.add(new TecnicoLaboratorio("Pedro Oliveira", "11223"));
    }

    /**
     * Inicializa a lista com 6 enfermeiros pré-definidos (2 para cada especialidade)
     */
    private static void inicializarEnfermeiros() {
        enfermeiros = new ArrayList<>();

        // Enfermeiros especializados em Hemograma Completo
        enfermeiros.add(new Enfermeiro("Ana Carolina Silva", "741321", "Hemograma Completo"));
        enfermeiros.add(new Enfermeiro("Roberto Fernandes", "741322", "Hemograma Completo"));

        // Enfermeiros especializados em Exame de Urina
        enfermeiros.add(new Enfermeiro("Mariana Costa", "852431", "Exame de Urina"));
        enfermeiros.add(new Enfermeiro("Carlos Eduardo", "852432", "Exame de Urina"));

        // Enfermeiros especializados em Exame de Glicemia
        enfermeiros.add(new Enfermeiro("Juliana Santos", "963541", "Exame de Glicemia"));
        enfermeiros.add(new Enfermeiro("Fernando Lima", "963542", "Exame de Glicemia"));
    }

    private static void exibirBoasVindas() {
        System.out.println("=================================================");
        System.out.println("    BEM-VINDO AO LABORATÓRIO DASA");
        System.out.println("=================================================");
        System.out.println();
    }

    private static void exibirTecnicosCadastrados() {
        System.out.println("Técnicos Cadastrados:");
        System.out.println("----------------------");

        for (TecnicoLaboratorio tecnico : tecnicos) {
            tecnico.apresentar();
            System.out.println();
        }
    }

    /**
     * Autentica o técnico pelo CRBM
     * @return TecnicoLaboratorio logado ou null se não encontrado
     */
    private static TecnicoLaboratorio autenticarTecnico() {
        while (true) {
            try {
                System.out.print("Digite o número do seu  para logar: ");
                String crbmInput = scanner.nextLine().trim();

                if (crbmInput.isEmpty()) {
                    System.out.println("ERRO: CRBM não pode estar vazio!");
                    continue;
                }

                // Busca o técnico pelo CRBM
                for (TecnicoLaboratorio tecnico : tecnicos) {
                    if (tecnico.getCrbm().equals(crbmInput)) {
                        return tecnico;
                    }
                }

                System.out.println("ERRO: CRBM não encontrado! Tente novamente.");
                System.out.println();

            } catch (Exception e) {
                System.out.println("ERRO: Entrada inválida! " + e.getMessage());
                scanner.nextLine(); // Limpa o buffer
            }
        }
    }

    /**
     * Menu da Recepção
     */
    private static void menuRecepcao() {
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
    private static void cadastrarPaciente() {
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
    private static String adicionarExame() {
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
    private static void visualizarPacientes() {
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

    /**
     * Menu do Almoxarifado
     */
    private static void menuAlmoxarifado() {
        while (true) {
            try {
                System.out.println();
                System.out.println("=== ALMOXARIFADO ===");
                System.out.println("1 - Retirar insumos para exame");
                System.out.println("2 - Verificar histórico de retirada de insumos");
                System.out.println("3 - Verificar Estoque");
                System.out.println("4 - Adicionar Estoque");
                System.out.println("5 - Voltar");
                System.out.print("Opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consome a quebra de linha

                switch (opcao) {
                    case 1:
                        System.out.println("Funcionalidade em desenvolvimento...");
                        break;
                    case 2:
                        System.out.println("Funcionalidade em desenvolvimento...");
                        break;
                    case 3:
                        verificarEstoque();
                        break;
                    case 4:
                        adicionarEstoque();
                        break;
                    case 5:
                        return; // Volta ao menu anterior
                    default:
                        System.out.println("ERRO: Opção inválida! Digite um número de 1 a 5.");
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
     * Verifica e exibe todo o estoque de materiais
     */
    private static void verificarEstoque() {
        estoque.exibirEstoque();
    }

    /**
     * Adiciona quantidade ao estoque de materiais
     */
    private static void adicionarEstoque() {
        try {
            // Exibe todos os materiais disponíveis
            estoque.exibirEstoque();

            System.out.print("Digite o ID ou o código de barras: ");
            int identificador = scanner.nextInt();
            scanner.nextLine(); // Consome a quebra de linha

            // Busca o material
            Material material = estoque.buscarMaterial(identificador);
            if (material == null) {
                System.out.println("ERRO: ID ou Código de Barras inválido!");
                return;
            }

            // Calcula quantidade máxima que pode ser adicionada
            int quantidadeMaximaAdicao = estoque.calcularQuantidadeMaximaAdicao(identificador);

            if (quantidadeMaximaAdicao <= 0) {
                System.out.println("ERRO: Este material já está no estoque máximo!");
                return;
            }

            System.out.print("Digite a quantidade que gostaria de adicionar: ");
            int quantidadeAdicionar = scanner.nextInt();
            scanner.nextLine(); // Consome a quebra de linha

            // Validações
            if (quantidadeAdicionar <= 0) {
                System.out.println("ERRO: Quantidade deve ser maior que zero!");
                return;
            }

            if (quantidadeAdicionar > quantidadeMaximaAdicao) {
                System.out.println("ERRO: A quantidade disponível + quantidade adicionada não pode ultrapassar 2000!");
                System.out.println("Quantidade máxima que pode ser adicionada: " + quantidadeMaximaAdicao);
                return;
            }

            // Adiciona a quantidade
            boolean sucesso = estoque.adicionarQuantidade(identificador, quantidadeAdicionar);

            if (sucesso) {
                // Busca novamente o material para obter os dados atualizados
                material = estoque.buscarMaterial(identificador);
                System.out.println();
                System.out.println("✅ " + quantidadeAdicionar + " de " + material.getNome() +
                        " adicionado com sucesso! Nova quantidade disponível deste material: " +
                        material.getQuantidadeDisponivel());
            } else {
                System.out.println("ERRO: Não foi possível adicionar a quantidade ao estoque!");
            }

        } catch (InputMismatchException e) {
            System.out.println("ERRO: Digite apenas números!");
            scanner.nextLine(); // Limpa o buffer
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    /**
     * Menu da Enfermaria
     */
    private static void menuEnfermaria() {
        while (true) {
            try {
                System.out.println();
                System.out.println("=== ENFERMARIA ===");
                System.out.println("1 - Listar todos os Enfermeiros");
                System.out.println("2 - Exames feitos por enfermeiro específico");
                System.out.println("3 - Voltar");
                System.out.print("Opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consome a quebra de linha

                switch (opcao) {
                    case 1:
                        listarEnfermeiros();
                        break;
                    case 2:
                        System.out.println("Funcionalidade em desenvolvimento...");
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
     * Lista todos os enfermeiros cadastrados
     */
    private static void listarEnfermeiros() {
        System.out.println();
        System.out.println("=== TODOS OS ENFERMEIROS CADASTRADOS ===");

        for (Enfermeiro enfermeiro : enfermeiros) {
            enfermeiro.apresentar();
            System.out.println();
        }
    }

    /**
     * Exibe o menu principal e processa as opções
     * @param tecnico Técnico logado no sistema
     */
    private static void exibirMenuPrincipal(TecnicoLaboratorio tecnico) {
        while (true) {
            try {
                System.out.println();
                System.out.println("Digite a opção desejada:");
                System.out.println("1 - Recepção");
                System.out.println("2 - Almoxarifado");
                System.out.println("3 - Enfermaria");
                System.out.println("4 - Sair");
                System.out.print("Opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consome a quebra de linha

                switch (opcao) {
                    case 1:
                        menuRecepcao();
                        break;
                    case 2:
                        menuAlmoxarifado();
                        break;
                    case 3:
                        menuEnfermaria();
                        break;
                    case 4:
                        System.out.println();
                        System.out.println("=================================================");
                        System.out.println("Obrigado por usar o Sistema do Laboratório DASA!");
                        System.out.println("Até logo, " + tecnico.getNome() + "!");
                        System.out.println("=================================================");
                        return;
                    default:
                        System.out.println("ERRO: Opção inválida! Digite um número de 1 a 4.");
                }

            } catch (InputMismatchException e) {
                System.out.println("ERRO: Digite apenas números!");
                scanner.nextLine(); // Limpa o buffer do scanner
            } catch (Exception e) {
                System.out.println("ERRO: " + e.getMessage());
            }
        }
    }
}
