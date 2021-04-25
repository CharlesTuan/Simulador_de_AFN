package br.univille.afn_simulator;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class main {
	public static void main(String[] args) throws IOException {

		// Faz a leitura do arquivo de regras do automato
		Scanner nodeRulesDataArchive = new Scanner(new FileReader("src/archive/rules.txt"));

		// Cria o arquivo de saída
		FileWriter outPutArquive = new FileWriter("src/archive/outPut.txt");
		PrintWriter writerOutPutArquive = new PrintWriter(outPutArquive);

		// Lista dos nodes que serão conhecidos pelo automato
		List<String> nodeArray = null;
		// Dialeto conhecido pelo automato
		List<String> dialect = null;
		// Regras de saida para cada dialeto
		List<String> rule = null;

		// Mapa que garda a relação de node para dialeto e regra
		Map<String, Map<String, List<String>>> nodeFullData = new HashMap<String, Map<String, List<String>>>();
		// Mapa que garda a regra do node
		Map<String, List<String>> nodeRule = new HashMap<String, List<String>>();

		// Linha do arquivo sendo executado
		Integer line = 0;
		// Informação da linha sendo executada
		String lineText = null;
		// Números de regras de nodes nas regras gerais
		Integer ruleLines = 0;
		// Estado inicial do automato
		String initialState = null;
		// Estado final desejado para o Automato
		List<String> finalState = null;

		//Roda enquando tiver linhas no documento
		while (nodeRulesDataArchive.hasNextLine()) {

			//Conta mais 1 linha
			line++;

			//Pega o conteudo da nova linha
			lineText = nodeRulesDataArchive.nextLine();

			//Remove os comentários da leitura
			if (lineText.indexOf("#") > -1) {
				lineText = lineText.subSequence(0, lineText.indexOf("#")).toString();
			}

			//Recebe os Nodes parametrizados
			if (line == 1) {
				nodeArray = new ArrayList<String>(Arrays.asList(lineText.split(" ")));
			}

			//Recebe o alfabeto
			if (line == 2) {
				dialect = new ArrayList<String>(Arrays.asList(lineText.split(" ")));

				dialect.add("Épsylon");

				ruleLines = (!nodeArray.isEmpty() ? nodeArray.size() : 0) * (!dialect.isEmpty() ? dialect.size() : 0);
			}

			//Recebe as gras parametrizadas no arquivo
			if (line > 2 && line < ruleLines + 2) {

				//Executa para cada node recebido
				for (String node : nodeArray) {
					nodeRule = new HashMap<String, List<String>>();

					//Executa para cada dialeto
					for (String dialectString : dialect) {
						rule = new ArrayList<String>(Arrays.asList(lineText.split(" ")));

						//Relaciona o dialeto com a regra
						nodeRule.put(dialectString, rule);

						//Relaciona os dados anteriores com o Node
						nodeFullData.put(node, nodeRule);

						//Pega a proxima linha
						lineText = nodeRulesDataArchive.nextLine();

						//Reemove os comentários da proxima linha
						if (lineText.indexOf("#") > 0) {
							lineText = lineText.subSequence(0, lineText.indexOf("#")).toString();
						}

						//Adiciona uma linha
						line++;

					}
				}

				if (lineText.indexOf(" ") > 0) {
					lineText = lineText.subSequence(0, lineText.indexOf(" ")).toString();
				}
				//Popula o estado inicial
				initialState = lineText;

				lineText = nodeRulesDataArchive.nextLine();

				if (lineText.indexOf("#") > 0) {
					lineText = lineText.subSequence(0, lineText.indexOf("#")).toString();
					//Popula o estado final
					finalState = new ArrayList<String>(Arrays.asList(lineText.split(" ")));
				}
			}
		}

		//Recebe os comandos
		Scanner inputNodeAction = new Scanner(new FileReader("src/archive/input.txt"));

		line = 0;

		List<String> nextNode;

		List<String> epsylonNextNode;

		Integer commandSize = null;

		String nextNodes = "";

		String acceptOrNot = "";

		while (inputNodeAction.hasNextLine()) {

			line++;

			System.out.println("Linha de execução -> " + line.toString());
			System.out.println("Estado inicial ->  " + initialState);
			//Escreve no documento de texto
			writerOutPutArquive.printf("Linha de execução -> " + line.toString() + "\n");
			writerOutPutArquive.printf("Estado inicial ->  " + initialState + "\n");

			lineText = inputNodeAction.nextLine();

			//Popula os comandos
			List<String> commandList = new ArrayList<String>(Arrays.asList(lineText.split(" ")));

			//Pega as regras para o estado inicial
			nodeRule = nodeFullData.get(initialState);

			List<Map<String, List<String>>> nodeRuleList = new ArrayList<Map<String, List<String>>>();

			nodeRuleList.add(nodeRule);

			List<Map<String, List<String>>> nodeRuleListAux = new ArrayList<Map<String, List<String>>>();

			List<String> epsylonNode;

			Map<String, List<String>> epsylonRules = new HashMap<String, List<String>>();

			commandSize = 0;

			//Executa uma ver para cada comando
			for (String command : commandList) {
				System.out.println("Simbolo lido ->  " + command);
				writerOutPutArquive.printf("Simbolo lido ->  " + command + "\n");

				commandSize++;

				nodeRuleListAux = new ArrayList<Map<String, List<String>>>();

				//Executa para cada regra
				for (Map<String, List<String>> nodeRuleItem : nodeRuleList) {

					nextNode = nodeRuleItem.get(command);

					epsylonNextNode = new ArrayList<String>();

					epsylonNextNode.addAll(nextNode);

					//Valida se existe condição para o epsylon
					for (String node : nextNode) {
						epsylonRules = nodeFullData.get(node);
						if (epsylonRules != null) {
							epsylonNode = epsylonRules.get("Épsylon");

							for (String epsonNodeItem : epsylonNode) {
								if (epsylonNode != null && !epsonNodeItem.equals("*")) {
									epsylonNextNode.add(epsonNodeItem);
								}
							}
						}
					}

					//Concatena a string para printar do lado um do outro
					for (String node : epsylonNextNode) {
						nextNodes += node + " ";
					}

					System.out.println("Estados correntes -> " + nextNodes);
					writerOutPutArquive.printf("Estados correntes -> " + nextNodes + "\n");

					nextNodes = "";
					acceptOrNot = "";

					//Valida se o node chegou ao final dos comandos, e tambem valida se a entrada é aceita ou não
					for (String node : epsylonNextNode) {
						if (commandList.size() == commandSize) {
							for (String states : finalState) {
								if (epsylonNextNode.get(0).equals(states)) {
									acceptOrNot = "Aceito";
								} else if (acceptOrNot.isEmpty()) {
									acceptOrNot = "Rejeitado";
								} else if (!acceptOrNot.isEmpty() && acceptOrNot.equals("Aceito")) {
									acceptOrNot = "Aceito";
								}
							}
							System.out.println("Estado de aceitação -> " + acceptOrNot);
							writerOutPutArquive.printf("Estado de aceitação -> " + acceptOrNot);
						}
						nodeRule = nodeFullData.get(node);

						if (nodeRule != null) {

							nodeRuleListAux.add(nodeRule);
						} else if (commandList.size() != commandSize) {
							System.out.println("Estado de aceitação -> Rejeitado");
							writerOutPutArquive.printf("Estado de aceitação -> Rejeitado" + "\n");
						}
					}
				}
				//Caso não tenha terminado ele seta a lista dos proximos nodes
				nodeRuleList = nodeRuleListAux;
			}
		}
		//Finaliza a gravação do arquivo
		writerOutPutArquive.close();
	}
}