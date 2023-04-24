package view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.swing.text.MaskFormatter;

import dao.BancoDAO;
import dao.ContaDAO;
import dao.UsuarioDAO;
import dao.UsuarioLoginDAO;
import jpaUtil.JPAUtil;
import model.Banco;
import model.Conta;
import model.TipoBanco;
import model.TipoConta;
import model.TipoUsuario;
import model.Usuario;
import model.UsuarioLogin;

public class View {

	public void HomePageLogar() {

		boolean rodando = true;
		Scanner sc = new Scanner(System.in);
		// UsuarioLogin uLogin = null;
		UsuarioLoginDAO uDAO = new UsuarioLoginDAO();

		System.out.println("\t  Bem vindo");
		while (rodando) {
			System.out.println();
			System.out.println("[1] -> Realizar Cadastro");
			System.out.println("[2] -> Realizar Login");
			System.out.println("[3] ->	  Sair	");
			System.out.print("Informe sua opção: ");
			int opcao = sc.nextInt();
			sc.nextLine();
			switch (opcao) {
			case 1:

				UsuarioLogin uLogin = new UsuarioLogin();

				System.out.print("Email: ");
				uLogin.setEmail(sc.nextLine());
				System.out.print("Senha: ");
				uLogin.setSenha(sc.nextLine());

				boolean cadastrou = uDAO.cadastrar(uLogin);
				if (cadastrou) {
					System.out.println("Cadastro realizado com Sucesso");
				} else {
					System.out.println("Falha ao realizar cadastro");
				}
				break;

			case 2:
				boolean logou = false;
				System.out.print("Email: ");
				String email = sc.nextLine();

				System.out.print("Senha: ");
				String senha = sc.nextLine();

				uLogin = uDAO.checarEmail(email, senha);
				if (uLogin != null) {
					logou = true;
					System.out.println("Login realizado com Sucesso");

				}

				if (logou == true) {
					homePageForAll();
				}

				break;

			case 3:
				System.out.println("Até a proxima...");
				rodando = false;
				break;
			}

		}
	}

	public void homePageForAll() {
		boolean rodando = true;

		Scanner sc = new Scanner(System.in);
		System.out.println("\t Bem vindo");
		while (rodando) {
			System.out.println();
			System.out.println("[1] -> Manipulação de dados para um Banco");
			System.out.println("[2] -> Manipulação de dados para uma Conta");
			System.out.println("[3] -> Manipulação de dados para um Usuario");
			System.out.println("[0] -> Finalizar Programa");
			System.out.print("Informe sua opção: ");
			int opcao = sc.nextInt();
			sc.nextLine();
			switch (opcao) {
			// Chamar o DAO do Banco
			case 1:

				try {
					bankView();
				} catch (Exception e) {
					System.out.println("Algo deu errado ao tentar utilizar a homePage bankView");
					e.printStackTrace();
				}

				break;

			// Chamar o DAO da Conta
			case 2:
				try {
					accountView();
				} catch (Exception e) {
					System.out.println("Algo deu errado ao tentar utilizar a homePage accountView");
					e.printStackTrace();
				}
				break;

			// chamar o DAO Do usuario
			case 3:
				try {
					userView();
				} catch (Exception e) {
					System.out.println("Algo deu errado ao tentar utilizar a homePage userView");
					e.printStackTrace();
				}
				break;

			case 0:
				System.out.println("Obrigado por utilizar o nosso Programa... Até logo");
				JPAUtil jpaUtil = new JPAUtil();
				jpaUtil.close();
				rodando = false;
				break;

			default:
				System.out.println("Digite um número valido de 0-3");
				break;
			}

		}
	}

	public void bankView() throws ParseException {
		boolean rodando = true;
		Scanner sc = new Scanner(System.in);

		MaskFormatter mask = new MaskFormatter("###.###.###/####-##");
		mask.setValueContainsLiteralCharacters(false);

		BancoDAO bDAO = new BancoDAO();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date data = new Date();

		Banco banco = null;
		while (rodando) {

			System.out.println();
			System.out.println("[1] -> Cadastrar Banco");
			System.out.println("[2] -> Deletar dados do Banco (* Obs: Todos os dados desse banco serão perdidos");
			System.out.println("[3] -> Listar dados do Banco");
			System.out.println("[4] -> Buscar dados de um Banco pelo ID");
			System.out.println(
					"[5] -> Atualizar dados do Banco (* OBS:Se o ID Informado não existir os dados Informados serão inseridos assim criando um novo Banco *");
			System.out.println("[0] -> Sair da Area de manipulação de dados");
			System.out.print("Informe sua opção: ");
			int opcao = sc.nextInt();
			sc.nextLine();
			switch (opcao) {
			case 1:
				banco = new Banco();

				System.out.print("Nome: ");
				banco.setNome(sc.nextLine());

				System.out.print("CNPJ apenas números sem pontos barras e traços e espaços(Ex: ## ### ### #### ##): ");

				banco.setCnpj(sc.nextLine());
				if (banco.getCnpj().length() != 14) {
					System.out.println("CNPJ devem possuir apenas 14 digitos, tente novamente...");
					break;

				}

				// Settando a data de agora
				banco.setData(data);
				System.out.print("Qual vai ser a localização desse banco:");
				banco.setLocalizacao(sc.nextLine());

				System.out.print(
						"Informe qual será o tipo desse Banco \n(Tipos de Bancos permitidos: *BancoComercial,CaixaEconomica,BancoDeInvestimento,CooperativaDeCredito* "
								+ "\npor favor digitar tudo minúsculo e sem espaços:");

				String tipoBancoString = sc.nextLine();
				// conversão de String para Enum
				if (tipoBancoString.equalsIgnoreCase("BANCOCOMERCIAL")
						|| tipoBancoString.equalsIgnoreCase("CAIXAECONOMICA")
						|| tipoBancoString.equalsIgnoreCase("BANCODEINVESTIMENTO")
						|| tipoBancoString.equalsIgnoreCase("COOPERATIVADECREDITO")) {
					TipoBanco tipoBanco = TipoBanco.valueOf(tipoBancoString.toUpperCase());
					banco.setTipoBanco(tipoBanco);
				} else {
					System.out.println(
							"Por favor digitar apenas os tipos de Bancos permitidos Tudo em minúsculo e sem espaços");
					System.out.println("Retornando ao Programa principal...");
					break;
				}

				// Sempre instanciando um novo bDAO para inicar a sessão do EntityManager e
				// fazer as transações em correntes
				boolean inseriu = bDAO.save(banco);
				if (inseriu) {
					System.out.println("Banco cadastrado com Sucesso");
				} else {
					System.out.println("Falha ao tentar cadastrar um Banco");
				}
				break;

			case 2:
				System.out.print("Informe o *ID* do banco que você deseja Deletar os dados:");
				Integer id = sc.nextInt();
				sc.nextLine();

				boolean deletou = bDAO.deleteById(id);
				if (deletou) {
					System.out.println("Dados de Banco deletado com Sucesso");
				} else {
					System.out.println(
							"Falha na deleção de dados, o Id do banco Informado não consta no nosso Banco de Dados , informe um id valido");
				}
				break;

			case 3:
				ArrayList<Banco> bancos = new ArrayList<>();

				bancos = bDAO.listOfBanks();

				if (bancos.size() != 0) {
					System.out.println("\t   LISTA DE BANCOS CADASTRADOS NO MOMENTO");
					for (int i = 0; i < bancos.size(); i++) {
						System.out.println("ID: " + bancos.get(i).getId());
						System.out.println("Nome: " + bancos.get(i).getNome());
						System.out.println("CNPJ: " + mask.valueToString(bancos.get(i).getCnpj()));
						System.out.println("Data de Criação: " + sdf.format(bancos.get(i).getData()));
						System.out.println("Tipo: " + bancos.get(i).getTipoBanco());
						System.out.println("Localização: " + bancos.get(i).getLocalizacao());
						System.out.println();
					}
				} else {
					System.out.println("No momento a lista está vazia");
					System.out.println();
				}
				break;

			case 4:
				System.out.print("Informe o id para a realização da Busca: ");
				id = sc.nextInt();
				banco = new Banco();

				banco = bDAO.searchById(id);

				if (banco != null) {
					System.out.println("\t  Banco encontrado com Sucesso");
					System.out.println("Nome: " + banco.getNome());
					System.out.println("CNPJ: " + mask.valueToString(banco.getCnpj()));
					System.out.println("Data de Criação: " + sdf.format(banco.getData()));
					System.out.println("Tipo: " + banco.getTipoBanco());
					System.out.println("Localização: " + banco.getLocalizacao());

				} else {
					System.out.println("Falha na busca o id informado não consta no nosso banco de dados");
				}
				break;

			case 5:
				banco = new Banco();
				System.out.print("Informe o id do Banco que você deseja atualizar os dados");
				id = sc.nextInt();
				banco.setId(id);
				sc.nextLine();
				System.out.print("Informe o novo nome que será dado para o banco: ");
				banco.setNome(sc.nextLine());
				System.out.print("Informe o novo CNPJ que será dado para o banco (Ex: ### ### ### #### ##)");

				banco.setCnpj(sc.nextLine());
				if (banco.getCnpj().length() != 14) {
					System.out.println("CNPJ devem possuir apenas 14 digitos, por favor tente novamente");
					break;
				}
				System.out.print(
						"Informe a nova categoria: \n(Tipos de Bancos permitidos: *BancoComercial,CaixaEconomica,BancoDeInvestimento,CooperativaDeCredito, por favor digitar tudo minúsculo e sem espaços");
				tipoBancoString = sc.nextLine();
				if (tipoBancoString.equalsIgnoreCase("BANCOCOMERCIAL")
						|| tipoBancoString.equalsIgnoreCase("CAIXAECONOMICA")
						|| tipoBancoString.equalsIgnoreCase("BANCODEINVESTIMENTO")
						|| tipoBancoString.equalsIgnoreCase("COOPERATIVADECREDITO")) {
					TipoBanco tipoBanco = TipoBanco.valueOf(tipoBancoString.toUpperCase());
					banco.setTipoBanco(tipoBanco);
				} else {
					System.out.println(
							"Por favor digitar apenas os tipos de Bancos permitidos Tudo em minúsculo e sem espaços");
					System.out.println("Retornando ao Programa principal...");
					break;
				}
				System.out.print("Informe a localização: ");
				banco.setLocalizacao(sc.nextLine());
				banco.setData(data);

				boolean atualizou = bDAO.update(banco);
				if (atualizou) {
					System.out.println("Dados atualizados com Sucesso");
				} else {
					System.out.println(
							"Falhou ao atualizar os dados atuais você digitou um Id que não existe, criando novos Dados para um novo Banco");
				}
				break;

			case 0:
				System.out.println("Saindo da area de Manipulação de dados...");
				rodando = false;
				break;
			default:
				System.out.println("Digite um número VALIDO de 0-5");
				break;
			}

		}

	}

	public void userView() throws ParseException {

		boolean rodando = true;
		Scanner sc = new Scanner(System.in);

		MaskFormatter mask = new MaskFormatter("###.###.###-##");
		mask.setValueContainsLiteralCharacters(false);

		MaskFormatter maskTelefone = new MaskFormatter("## #####-####");
		maskTelefone.setValueContainsLiteralCharacters(false);

		UsuarioDAO uDAO = new UsuarioDAO();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date data = new Date();

		Usuario u = null;
		while (rodando) {

			System.out.println();
			System.out.println("[1] -> Cadastrar Usuario");
			System.out.println("[2] -> Deletar dados do Usuario (* Obs: Todos os dados desse Usuario serão perdidos");
			System.out.println("[3] -> Listar dados do Usuario");
			System.out.println("[4] -> Buscar dados de um Usuario pelo ID");
			System.out.println(
					"[5] -> Atualizar dados do Usuario (* OBS:Se o ID Informado não existir os dados Informados serão inseridos assim criando um novo Usuario *");
			System.out.println("[0] -> Sair da Area de manipulação de dados");
			System.out.print("Informe sua opção: ");
			int opcao = sc.nextInt();
			sc.nextLine();

			switch (opcao) {
			case 1:
				u = new Usuario();
				System.out.print("Nome: ");
				u.setNome(sc.nextLine());
				System.out.print("CPF (Ex: ###.###.###-## favor ignorar pontos espaços e traços, apenas numeros): ");
				u.setCpf(sc.nextLine());
				if (u.getCpf().length() != 11) {
					System.out.println("CPF possuem apenas 11 digitos, favor não utilizar espaços extras");
					System.out.println("Retornando ao programa...");
					break;
				}

				System.out.print("Data de Nascimento: (##/##/####)");
				String dataNasci = sc.nextLine();
				try {
					data = sdf.parse(dataNasci);
					u.setDataNasc(data);
				} catch (ParseException pE) {
					System.out.println("Digite como o especificado (##/##/####)");
					System.out.println("Retornando ao Programa...");
					break;
				}

				System.out.print("Bairro: ");
				u.setBairro(sc.nextLine());

				System.out.print("Telefone: (Obs: apenas DDD 9 e os digitos sem espaços ");
				u.setTelefone(sc.nextLine());
				if (u.getTelefone().length() != 11) {
					System.out.println("Favor informar um número valido DDD 9 + digitos sem espaços");
					System.out.println("Retornando ao programa...");
					break;
				}

				System.out.print("Tipo de Pessoa (FISICA,JURIDICA): ");
				String tipoUsuario = sc.nextLine();

				if (tipoUsuario.equalsIgnoreCase("FISICA") || tipoUsuario.equalsIgnoreCase("JURIDICA")) {
					TipoUsuario tipoUserEnum = TipoUsuario.valueOf(tipoUsuario.toUpperCase());
					u.setTipoUsuario(tipoUserEnum);
				} else {
					System.out.println("Favor digitar um dos tipos de Usuario acima");
					System.out.println("Retornando ao programa principal...");
					break;
				}

				boolean inseriu = uDAO.save(u);
				if (inseriu) {
					System.out.println("Usuario cadastrado com Sucesso");
				} else {
					System.out.println("Falha ao tentar cadastrar usuario");
				}

				break;

			case 2:
				System.out.print("Informe o id do Usuario que você deseja remover os dados: ");

				Integer id = sc.nextInt();
				sc.nextLine(); // cleaning buffer
				boolean deletou = uDAO.deleteById(id);
				if (deletou) {
					System.out.println("Usuario deletado com Sucesso");
				} else {
					System.out.println(
							"Falha ao tentar realizar a deleção de daods, Id informado não consta no nosso Banco de dados");
				}

				break;

			case 3:
				ArrayList<Usuario> usuarios = new ArrayList<>();
				usuarios = uDAO.listOfUsers();
				if (usuarios.size() == 0) {
					System.out.println("NO MOMENTO A LISTA SE ENCONTRA VAZIA");
				} else {
					for (int i = 0; i < usuarios.size(); i++) {
						System.out.println("\t   ID: " + usuarios.get(i).getId());
						System.out.println("Nome: " + usuarios.get(i).getNome());
						System.out.println("CPF: " + mask.valueToString(usuarios.get(i).getCpf()));
						System.out.println("Data de Nascimento: " + sdf.format(usuarios.get(i).getDataNasc()));
						System.out.println("Bairro: " + usuarios.get(i).getBairro());
						System.out.println("Telefone " + maskTelefone.valueToString(usuarios.get(i).getTelefone()));
						System.out.println("Tipo de Usuario: " + usuarios.get(i).getTipoUsuario());
						System.out.println();
					}
				}
				break;

			case 4:
				System.out.print("Informe o ID do usuario que você deseja buscar: ");
				id = sc.nextInt();
				sc.nextLine();
				u = new Usuario();

				u = uDAO.searchById(id);
				if (u != null) {
					System.out.println("\t   ID: " + u.getId());
					System.out.println("Nome: " + u.getNome());
					System.out.println("CPF: " + mask.valueToString(u.getCpf()));
					System.out.println("Data de Nascimento: " + sdf.format(u.getDataNasc()));
					System.out.println("Bairro: " + u.getBairro());
					System.out.println("Telefone " + maskTelefone.valueToString(u.getTelefone()));
					System.out.println("Tipo de Usuario: " + u.getTipoUsuario());
					System.out.println();
				} else {
					System.out.println("Id informado não consta no nosso banco de dados ou Usuario não existe");
					System.out.println();
				}
				break;

			case 5:
				u = new Usuario();
				System.out.print("Informe o id do Usuario que você deseja Atualizar dados: ");
				id = sc.nextInt();
				u.setId(id);
				sc.nextLine();

				System.out.println("Informe os novos dados do usuario");
				System.out.print("Nome: ");
				u.setNome(sc.nextLine());
				System.out.print("CPF (Ex: ###.###.###-## favor ignorar pontos espaços e traços, apenas numeros):");
				u.setCpf(sc.nextLine());

				if (u.getCpf().length() != 11) {
					System.out.println("CPF possuem apenas 11 digitos, favor não utilizar espaços extras");
					System.out.println("Retornando ao programa...");
					break;
				}

				System.out.print("Data de Nascimento: (##/##/####)");
				dataNasci = sc.nextLine();

				try {
					data = sdf.parse(dataNasci);
					u.setDataNasc(data);
				} catch (ParseException pE) {
					System.out.println("Digite como o especificado (##/##/####)");
					System.out.println("Retornando ao Programa...");
					break;
				}

				System.out.print("Bairro: ");
				u.setBairro(sc.nextLine());

				System.out.print("Telefone: (Obs: apenas DDD 9 e os digitos sem espaços ");
				u.setTelefone(sc.nextLine());
				if (u.getTelefone().length() != 11) {
					System.out.println("Favor informar um número valido DDD 9 + digitos sem espaços");
					System.out.println("Retornando ao programa...");
					break;
				}

				System.out.print("Tipo de usuario (FISICA OU JURIDICA): ");
				tipoUsuario = sc.nextLine();

				if (tipoUsuario.equalsIgnoreCase("FISICA") || tipoUsuario.equalsIgnoreCase("JURIDICA")) {
					TipoUsuario tipoUserEnum = TipoUsuario.valueOf(tipoUsuario.toUpperCase());
					u.setTipoUsuario(tipoUserEnum);
				} else {
					System.out.println("Favor digitar um dos tipos de Usuario acima");
					System.out.println("Retornando ao programa principal...");
					break;
				}
				boolean atualizou = uDAO.update(u);
				if (atualizou) {
					System.out.println("Atualizou");
				} else {
					System.out.println(
							"Falha ao atualizar o ID que você informou não existe, um novo Usuario foi criado");
				}

				break;

			case 0:
				System.out.println("Saindo da area de Manipulação de dados...");
				rodando = false;
				break;

			default:
				System.out.println("Digite uma opção valida de 0-5");
				break;
			}

		}
	}

	public void accountView() throws ParseException {
		boolean rodando = true;
		Scanner sc = new Scanner(System.in);
		ArrayList<Conta> contas = new ArrayList<>();

		MaskFormatter mask = new MaskFormatter("######-#");
		mask.setValueContainsLiteralCharacters(false);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date data = new Date();

		ContaDAO cDAO = new ContaDAO();
		Conta c = null;

		while (rodando) {
			System.out.println();
			System.out.println("[1] -> Cadastrar Conta");
			System.out.println("[2] -> Deletar dados da Conta (* Obs: Todos os dados desse Usuario serão perdidos");
			System.out.println(
					"[3] -> Listar dados da Conta (Obs: se seu Saldo aparecer null significa que seu saldo está zerado.)");
			System.out.println("[4] -> Buscar dados de uma Conta pelo ID");
			System.out.println(
					"[5] -> Atualizar dados da Conta (* OBS:Se o ID Informado não existir os dados Informados serão inseridos assim criando um novo Usuario *)\n"
							+ " 	Nessa aba não é Possivel realizar um Novo Deposito a uma Conta existente");
			System.out.println("[6] -> Realizar Deposito na conta pelo ID");
			System.out.println("[0] -> Sair da Area de manipulação de dados");
			System.out.print("Informe sua opção: ");
			int opcao = sc.nextInt();
			sc.nextLine();
			switch (opcao) {
			case 1:
				c = new Conta();

				System.out.print("Tipo de Conta (CONTACORRENTE/CONTAPOUPANÇA) Sem ESPAÇOS: ");
				String tipoDeConta = sc.nextLine();

				if (tipoDeConta.equalsIgnoreCase("CONTACORRENTE") || tipoDeConta.equalsIgnoreCase("CONTAPOUPANÇA")) {
					TipoConta tipoConta = TipoConta.valueOf(tipoDeConta.toUpperCase());
					c.setTipoConta(tipoConta);
				} else {
					System.out.println("Favor digitar um dos tipos de Conta acima");
					System.out.println("Retornando ao programa principal...");
					break;
				}

				Double deposito;
				System.out
						.println("Quando iniciamos uma nova Conta é aconselhável que você faça um Deposito inical !!");
				System.out.print("Informe sua opção: (S/N)");
				String opcaoDeposit = sc.nextLine();

				if (opcaoDeposit.equalsIgnoreCase("s")) {
					System.out.print("Informe o valor do Deposito que você deseja realizar: ");
					deposito = sc.nextDouble();
					c.setSaldo(deposito);
					sc.nextLine();

				} else {
					System.out.println("sua Resposta foi  (N) prosseguindo com o cadastro...");
					c.setSaldo(0.0);
				}

				c.setData(data);

				System.out.print("Número da Conta (######-#) APENAS NUMEROS SEM ESPAÇOS NEM TRAÇOS: ");
				c.setNumConta(sc.nextLine());

				if (c.getNumConta().length() != 7) {
					System.out.println("Informe um número de conta VALIDO (######-#) SEM ESPAÇOS apenas 7 digitos");
					System.out.println("Retornando ao programa...");
					break;
				}

				boolean inseriu = cDAO.save(c);
				if (inseriu) {
					System.out.println("Nova conta cadastrada com Sucesso");
				} else {
					System.out.println("Falha ao cadastrar ");
				}

				break;

			case 2:
				System.out.print("Informe o ID da conta que você deseja deletar dados: ");
				Integer id = sc.nextInt();
				sc.nextLine();
				boolean deletou = cDAO.deleteById(id);

				if (deletou) {
					System.out.println("Dados deletados com Sucesso");
				} else {
					System.out.println("Id que você informou não consta no nosso banco de dados");
				}
				break;

			case 3:

				contas = cDAO.listOfAccounts();

				if (contas.isEmpty()) {
					System.out.println("NESTE MOMENTO A LISTA SE ENCONTRA VAZIA");
				} else {
					for (int i = 0; i < contas.size(); i++) {
						System.out.println("\t   ID " + contas.get(i).getId());
						System.out.println("Número da Conta: " + mask.valueToString(contas.get(i).getNumConta()));
						System.out.println("Tipo de Conta: " + contas.get(i).getTipoConta());
						System.out.printf("Saldo: R$ %.2f%n", contas.get(i).getSaldo());
						System.out.println("Data de abertura: " + sdf.format(contas.get(i).getData()));
						System.out.println();
					}
				}

				break;

			case 4:
				System.out.print("Informe o ID da conta que você deseja buscar: ");
				id = sc.nextInt();
				sc.nextLine();
				c = new Conta();

				c = cDAO.searchById(id);

				if (c != null) {
					System.out.println("\t   ID " + c.getId());
					System.out.println("Número da Conta: " + mask.valueToString(c.getNumConta()));
					System.out.println("Tipo de Conta: " + c.getTipoConta());
					System.out.printf("Saldo: R$ %.2f%n", c.getSaldo());
					System.out.println("Data de abertura: " + sdf.format(c.getData()));
					System.out.println();
				} else {
					System.out.println("Falha ao buscar Conta , o Id informado não consta no nosso banco de dados");
				}

				break;

			case 5:
				c = new Conta();
				System.out.print("Informe o ID da conta a ser Atualizado: ");
				id = sc.nextInt();
				sc.nextLine();
				c.setId(id);

				System.out.println("Informe os novos dados da conta");
				System.out.print("Tipo da conta (CONTACORRENTE/CONTAPOUPANÇA) SEM ESPAÇOS: ");
				tipoDeConta = sc.nextLine();

				if (tipoDeConta.equalsIgnoreCase("CONTACORRENTE") || tipoDeConta.equalsIgnoreCase("CONTAPOUPANÇA")) {
					TipoConta tipoConta = TipoConta.valueOf(tipoDeConta.toUpperCase());
					c.setTipoConta(tipoConta);
				} else {
					System.out.println("Favor digitar um dos tipos de Conta acima");
					System.out.println("Retornando ao programa principal...");
					break;
				}

				System.out.print("Número da Conta (######-#) APENAS NUMEROS SEM ESPAÇOS NEM TRAÇOS: ");

				c.setNumConta(sc.nextLine());

				if (c.getNumConta().length() != 7) {
					System.out.println("Informe um número de conta VALIDO (######-#) SEM ESPAÇOS apenas 7 digitos");
					System.out.println("Retornando ao programa...");
					break;
				}

				c.setData(data);

				boolean atualizou = cDAO.update(c);

				if (atualizou) {
					System.out.println("DADOS ATUALIZADOS COM SUCESSO");
				} else {
					System.out.println(
							"FALHA AO ATUALIZAR DADOS dessa Conta o ID que você informou não existe, uma nova Conta foi criada ");
				}

				break;

			case 6:
				c = new Conta();
				System.out.print("Informe o ID da conta que você deseja realizar o deposito: ");
				id = sc.nextInt();
				sc.nextLine();

				c = cDAO.searchById(id);
				if (c != null) {
					System.out.println();
					System.out.println("\t   ID " + c.getId());
					System.out.println("Número da Conta: " + mask.valueToString(c.getNumConta()));
					System.out.println("Tipo de Conta: " + c.getTipoConta());
					System.out.printf("	Saldo: R$ %.2f%n", c.getSaldo());
					System.out.println("Data de abertura: " + sdf.format(c.getData()));
					System.out.println();
				} else {
					System.out.println("Falha ao buscar Conta , o Id informado não consta no nosso banco de dados");
				}
				System.out.println("Todos os Dados constam (S/N)");
				String escolha = sc.nextLine();

				if (escolha.equalsIgnoreCase("s")) {
					System.out.print("Informe o valor do Deposito: ");
					c.depositar(sc.nextDouble());

					cDAO.updateForDeposit(c);
					System.out.println("Deposito realizado com Sucesso");
				} else {
					System.out
							.println("Você digitou (N) os dados não constam com o ID digitado\nRetornando ao Menu...");
					break;
				}

				break;

			case 0:
				System.out.println("Saindo da area de Manipulação de Dados");
				rodando = false;
				break;

			default:
				System.out.println("Informe uma opção valida de 0-5 como mostra o MENU");
				break;
			}
		}

	}
}
