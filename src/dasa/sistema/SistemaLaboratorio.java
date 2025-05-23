package dasa.sistema;

import dasa.funcionarios.TecnicoLaboratorio;
import dasa.funcionarios.Enfermeiro;
import dasa.modelo.Exame;
import dasa.modelo.Paciente;
import dasa.modelo.Estoque;
import dasa.modelo.Insumo;
import dasa.modelo.ItemCesta;
import dasa.modelo.HistoricoRetirada;
import java.util.*;

/**
 * Classe principal do sistema do laboratório
 */
public class SistemaLaboratorio {
    private static List<TecnicoLaboratorio> tecnicos;
    private static List<Enfermeiro> enfermeiros;
    private static Estoque estoque;
    private static Scanner scanner;
    private static TecnicoLaboratorio tecnicoLogado;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        inicializarTecnicos();
        inicializarEnfermeiros();
        estoque = new Estoque();

        try {
            exibirBoasVindas();
            exibirTecnicosCadastrados();

            tecnicoLogado = autenticarTecnico();
            if (tecnicoLogado != null) {
                tecnicoLogado.acessarSistema();
                exibirMenuPrincipal(tecnicoLogado);
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
                System.out.print("Digite o número do seu CRBM: ");
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
                        retirarInsumosExame();
                        break;
                    case 2:
                        verificarHistoricoRetiradas();
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
     * Verifica e exibe todo o estoque de insumos
     */
    private static void verificarEstoque() {
        estoque.exibirEstoque();
    }

    /**
     * Adiciona quantidade ao estoque de insumos
     */
    private static void adicionarEstoque() {
        try {
            // Exibe todos os insumos disponíveis
            estoque.exibirEstoque();

            System.out.print("Digite o ID ou o código de barras: ");
            int identificador = scanner.nextInt();
            scanner.nextLine(); // Consome a quebra de linha

            // Busca o insumo
            Insumo insumo = estoque.buscarInsumo(identificador);
            if (insumo == null) {
                System.out.println("ERRO: ID ou Código de Barras inválido!");
                return;
            }

            // Calcula quantidade máxima que pode ser adicionada
            int quantidadeMaximaAdicao = estoque.calcularQuantidadeMaximaAdicao(identificador);

            if (quantidadeMaximaAdicao <= 0) {
                System.out.println("ERRO: Este insumo já está no estoque máximo!");
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
                // Busca novamente o insumo para obter os dados atualizados
                insumo = estoque.buscarInsumo(identificador);
                System.out.println();
                System.out.println("✅ " + quantidadeAdicionar + " " + insumo.getNome() +
                        " adicionado com sucesso! Nova quantidade disponível deste insumo: " +
                        insumo.getQuantidadeDisponivel());
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
     * Retira insumos para um exame específico
     */
    private static void retirarInsumosExame() {
        try {
            // Busca pacientes com status "Em espera"
            List<Paciente> pacientesEmEspera = Paciente.filtrarPorStatus("Em espera");

            if (pacientesEmEspera.isEmpty()) {
                System.out.println("Não há pacientes cadastrados com status 'Em espera'.");
                return;
            }

            // Exibe pacientes em espera
            System.out.println();
            System.out.println("=== PACIENTES EM ESPERA ===");
            for (Paciente paciente : pacientesEmEspera) {
                paciente.exibirDados();
            }

            // Solicita ID do paciente
            System.out.print("Qual o ID do paciente a retirar o Exame: ");
            int idPaciente = scanner.nextInt();
            scanner.nextLine();

            // Busca o paciente
            Paciente pacienteSelecionado = null;
            for (Paciente paciente : pacientesEmEspera) {
                if (paciente.getId() == idPaciente) {
                    pacienteSelecionado = paciente;
                    break;
                }
            }

            if (pacienteSelecionado == null) {
                System.out.println("ERRO: ID inválido!");
                return;
            }

            // Exibe exame do paciente
            System.out.println("\n\nID Exame - " + pacienteSelecionado.getExame());

            // Inicia processo de coleta de insumos
            List<ItemCesta> cesta = new ArrayList<>();
            boolean continuarColetando = true;

            while (continuarColetando) {
                // Exibe insumos do exame
                List<Insumo> insumosExame = estoque.getInsumosPorExame(pacienteSelecionado.getExame());

                System.out.println();
                System.out.println("=== INSUMOS PARA " + pacienteSelecionado.getExame().toUpperCase() + " ===");
                for (Insumo insumo : insumosExame) {
                    System.out.println("ID " + insumo.getId() + " - " + insumo.getNome());
                    System.out.println("\tCódigo de Barras Produto: " + insumo.getCodigoBarras());
                    System.out.println("\tQuantidade Disponível: " + insumo.getQuantidadeDisponivel());
                    System.out.println();
                }

                // Solicita insumo
                Insumo insumoSelecionado = null;
                while (insumoSelecionado == null) {
                    try {
                        System.out.print("\nDigite o ID ou o código de barras do insumo que gostaria de adicionar à cesta: ");
                        int identificador = scanner.nextInt();
                        scanner.nextLine();

                        // Busca o insumo entre os do exame
                        for (Insumo insumo : insumosExame) {
                            if (insumo.getId() == identificador || insumo.getCodigoBarras() == identificador) {
                                insumoSelecionado = insumo;
                                break;
                            }
                        }

                        if (insumoSelecionado == null) {
                            System.out.println("ERRO: ID ou Código de Barras inválido!");
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("ERRO: Entrada inválida!");
                        scanner.nextLine();
                        return;
                    }
                }

                // Solicita quantidade
                int quantidade = 0;
                while (quantidade <= 0) {
                    try {
                        System.out.print("Digite a quantidade: ");
                        quantidade = scanner.nextInt();
                        scanner.nextLine();

                        if (quantidade <= 0) {
                            System.out.println("ERRO: Quantidade deve ser maior que zero!");
                        } else if (quantidade > insumoSelecionado.getQuantidadeDisponivel()) {
                            System.out.println("ERRO: Quantidade não disponível em estoque! Disponível: " +
                                    insumoSelecionado.getQuantidadeDisponivel());
                            quantidade = 0;
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("ERRO: Entrada inválida!");
                        scanner.nextLine();
                        return;
                    }
                }

                // Adiciona à cesta
                cesta.add(new ItemCesta(insumoSelecionado, quantidade));
                System.out.println("✅ " + insumoSelecionado.getNome() + " adicionado à cesta!");

                // Pergunta se deseja adicionar mais insumos
                int opcaoContinuar = 0;
                while (opcaoContinuar != 1 && opcaoContinuar != 2) {
                    try {
                        System.out.print("Deseja adicionar mais Insumos à cesta (1 - Sim, 2 - Não): ");
                        opcaoContinuar = scanner.nextInt();
                        scanner.nextLine();

                        if (opcaoContinuar == 1) {
                            continuarColetando = true;
                        } else if (opcaoContinuar == 2) {
                            continuarColetando = false;
                        } else {
                            System.out.println("ERRO: Digite 1 para Sim ou 2 para Não!");
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("ERRO: Entrada inválida!");
                        scanner.nextLine();
                        return;
                    }
                }
            }

            // Seleciona enfermeiro responsável
            Enfermeiro enfermeiroSelecionado = selecionarEnfermeiro(pacienteSelecionado.getExame());
            if (enfermeiroSelecionado == null) {
                return;
            }

            // Processa a retirada
            finalizarRetirada(pacienteSelecionado, cesta, enfermeiroSelecionado);

        } catch (InputMismatchException e) {
            System.out.println("ERRO: Digite apenas números!");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    /**
     * Seleciona o enfermeiro responsável pelo exame
     */
    private static Enfermeiro selecionarEnfermeiro(String exame) {
        try {
            // Filtra enfermeiros pela especialidade
            List<Enfermeiro> enfermeirosEspecialidade = new ArrayList<>();
            for (Enfermeiro enfermeiro : enfermeiros) {
                if (enfermeiro.getEspecialidade().equals(exame)) {
                    enfermeirosEspecialidade.add(enfermeiro);
                }
            }

            System.out.println();
            System.out.println("=== ENFERMEIROS DISPONÍVEIS ===");
            for (Enfermeiro enfermeiro : enfermeirosEspecialidade) {
                enfermeiro.apresentar();
                System.out.println();
            }

            // Solicita COREN
            while (true) {
                try {
                    System.out.print("Digite o COREN do enfermeiro responsável: ");
                    String coren = scanner.nextLine().trim();

                    // Busca enfermeiro pelo COREN
                    for (Enfermeiro enfermeiro : enfermeirosEspecialidade) {
                        if (enfermeiro.getCoren().equals(coren)) {
                            return enfermeiro;
                        }
                    }

                    System.out.println("ERRO: COREN inválido!");

                } catch (Exception e) {
                    System.out.println("ERRO: Entrada inválida!");
                    return null;
                }
            }

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            return null;
        }
    }

    /**
     * Finaliza a retirada de insumos
     */
    private static void finalizarRetirada(Paciente paciente, List<ItemCesta> cesta, Enfermeiro enfermeiro) {
        try {
            // Atualiza estoque
            for (ItemCesta item : cesta) {
                estoque.removerQuantidade(item.getInsumo().getId(), item.getQuantidade());
            }

            // Atualiza status do paciente
            paciente.setStatus("Atendido");
            paciente.setEnfermeiroResponsavel(enfermeiro.getNome() + " - " + enfermeiro.getCoren());
            paciente.setResponsavelColeta(tecnicoLogado.getNome() + " - " + tecnicoLogado.getCrbm());

            // Atualiza paciente no arquivo
            Paciente.atualizarPacienteNoArquivo(paciente);

            // Salva no histórico
            HistoricoRetirada.salvarRetirada(paciente, cesta, tecnicoLogado, enfermeiro);

            // Exibe confirmação
            System.out.println();
            System.out.println("✅ Insumos coletados por " + tecnicoLogado.getNome() + " para exame " + paciente.getExame() + ",");
            for (ItemCesta item : cesta) {
                System.out.println(item.toString());
            }
            System.out.println("Enfermeiro responsável pelo atendimento: " + enfermeiro.getNome() + " - " + enfermeiro.getCoren());
            System.out.println("Disponibilidade de insumos atualizadas no SAP");

        } catch (Exception e) {
            System.out.println("ERRO ao finalizar retirada: " + e.getMessage());
        }
    }

    /**
     * Verifica o histórico de retiradas
     */
    private static void verificarHistoricoRetiradas() {
        HistoricoRetirada.exibirHistorico();
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
