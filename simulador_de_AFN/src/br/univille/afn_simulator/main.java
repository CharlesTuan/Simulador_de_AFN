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

		//Faz a leitura do arquivo de regras do automato
		Scanner nodeRulesDataArchive = new Scanner(new FileReader("src/archive/rules.txt"));

		//Cria o arquivo de saída
		FileWriter outPutArquive = new FileWriter("src/archive/outPut.txt");
		PrintWriter writerOutPutArquive = new PrintWriter(outPutArquive);

		//Lista dos nodes que serão conhecidos pelo automato
		List<String> nodeArray = null;
		//Dialeto conhecido pelo automato
		List<String> dialect = null;
		//Regras de saida para cada dialeto
		List<String> rule = null;

		//Mapa que garda a relação de node para dialeto e regra
		Map<String, Map<String, List<String>>> nodeFullData = new HashMap<String, Map<String, List<String>>>();
		//Mapa que garda a regra do node
		Map<String, List<String>> nodeRule = new HashMap<String, List<String>>();

		//Linha do arquivo sendo executado
		Integer line = 0;
		//Informação da linha sendo executada
		String lineText = null;
		Integer ruleLines = 0;
		String initialState = null;
		String finalState = null;

		while (nodeRulesDataArchive.hasNextLine()) {

			line++;

			lineText = nodeRulesDataArchive.nextLine();

			if (lineText.indexOf("#") > -1) {
				lineText = lineText.subSequence(0, lineText.indexOf("#")).toString();
			}

			if (line == 1) {
				nodeArray = new ArrayList<String>(Arrays.asList(lineText.split(" ")));
			}

			if (line == 2) {
				dialect = new ArrayList<String>(Arrays.asList(lineText.split(" ")));

				dialect.add("Épsylon");

				ruleLines = (!nodeArray.isEmpty() ? nodeArray.size() : 0) * (!dialect.isEmpty() ? dialect.size() : 0);
			}

			if (line > 2 && line < ruleLines + 2) {

				for (String node : nodeArray) {
					nodeRule = new HashMap<String, List<String>>();

					for (String dialectString : dialect) {
						rule = new ArrayList<String>(Arrays.asList(lineText.split(" ")));

						nodeRule.put(dialectString, rule);

						nodeFullData.put(node, nodeRule);

						lineText = nodeRulesDataArchive.nextLine();

						if (lineText.indexOf("#") > 0) {
							lineText = lineText.subSequence(0, lineText.indexOf("#")).toString();
						}

						line++;

					}
				}

				if (lineText.indexOf(" ") > 0) {
					lineText = lineText.subSequence(0, lineText.indexOf(" ")).toString();
				}
				initialState = lineText;

				lineText = nodeRulesDataArchive.nextLine();

				if (lineText.indexOf("#") > 0) {
					lineText = lineText.subSequence(0, lineText.indexOf("#") - 1).toString();
				}

				finalState = lineText;
			}
		}

		Scanner inputNodeAction = new Scanner(new FileReader("src/archive/input.txt"));

		line = 0;

		List<String> nextNode;

		List<String> epsomNextNode;

		Integer commandSize = null;

		String nextNodes = "";

		while (inputNodeAction.hasNextLine()) {

			line++;

			System.out.println("Linha de execução -> " + line.toString());
			System.out.println("Estado inicial ->  " + initialState);
			writerOutPutArquive.printf("Linha de execução -> " + line.toString() + "\n");
			writerOutPutArquive.printf("Estado inicial ->  " + initialState + "\n");

			lineText = inputNodeAction.nextLine();

			List<String> commandList = new ArrayList<String>(Arrays.asList(lineText.split(" ")));

			nodeRule = nodeFullData.get(initialState);

			List<Map<String, List<String>>> nodeRuleList = new ArrayList<Map<String, List<String>>>();

			nodeRuleList.add(nodeRule);

			List<Map<String, List<String>>> nodeRuleListAux = new ArrayList<Map<String, List<String>>>();

			List<String> epsomNode;

			Map<String, List<String>> epsomRules = new HashMap<String, List<String>>();

			commandSize = 0;

			for (String command : commandList) {
				System.out.println("Simbolo lido ->  " + command);
				writerOutPutArquive.printf("Simbolo lido ->  " + command + "\n");

				commandSize++;

				nodeRuleListAux = new ArrayList<Map<String, List<String>>>();

				for (Map<String, List<String>> nodeRuleItem : nodeRuleList) {

					nextNode = nodeRuleItem.get(command);

					epsomNextNode = new ArrayList<String>();

					epsomNextNode.addAll(nextNode);

					for (String node : nextNode) {
						epsomRules = nodeFullData.get(node);
						if (epsomRules != null) {
							epsomNode = epsomRules.get("Épsylon");

							for (String epsonNodeItem : epsomNode) {
								if (epsomNode != null && !epsonNodeItem.equals("*")) {
									epsomNextNode.add(epsonNodeItem);
								}
							}
						}
					}

					for (String node : epsomNextNode) {
						nextNodes += node + " ";
					}

					System.out.println("Estados correntes -> " + nextNodes);
					writerOutPutArquive.printf("Estados correntes -> " + nextNodes + "\n");

					nextNodes = "";

					for (String node : epsomNextNode) {
						if (commandList.size() == commandSize) {
							System.out.println("Estado de aceitação -> "
									+ (epsomNextNode.get(0).equals(finalState) ? "Aceito" : "Rejeitado"));
							writerOutPutArquive.printf("Estado de aceitação -> "
									+ (epsomNextNode.get(0).equals(finalState) ? "Aceito" : "Rejeitado") + "\n");
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
				nodeRuleList = nodeRuleListAux;
			}
		}
		writerOutPutArquive.close();
	}
}