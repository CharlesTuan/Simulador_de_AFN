package br.univille.afn_simulator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class main {
	public static void main(String[] args) throws FileNotFoundException {

		Integer line = 0;
		Scanner nodeRulesDataArchive = new Scanner(new FileReader("src/archive/dados.txt"));

		List<String> nodeArray = null;
		List<String> dialect = null;
		List<String> rule = null;

		Map<String, Map<String, List<String>>> nodeFullData = new HashMap<String, Map<String, List<String>>>();
		Map<String, List<String>> nodeRule = new HashMap<String, List<String>>();

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

		Integer commandSize = null;

		String nextNodes = "";

		while (inputNodeAction.hasNextLine()) {

			line++;

			System.out.println("------------------------------");
			System.out.println("Linha de execução -> " + line.toString());
			System.out.println("Estado inicial ->  " + initialState);

			lineText = inputNodeAction.nextLine();

			List<String> commandList = new ArrayList<String>(Arrays.asList(lineText.split(" ")));

			nodeRule = nodeFullData.get(initialState);

			List<Map<String, List<String>>> nodeRuleList = new ArrayList<Map<String, List<String>>>();

			nodeRuleList.add(nodeRule);

			List<Map<String, List<String>>> nodeRuleListAux = new ArrayList<Map<String, List<String>>>();

			commandSize = 0;

			for (String command : commandList) {
				System.out.println("Simbolo lido ->  " + command);

				commandSize++;

				nodeRuleListAux = new ArrayList<Map<String, List<String>>>();

				for (Map<String, List<String>> nodeRuleItem : nodeRuleList) {

					nextNode = nodeRuleItem.get(command);

					for (String node : nextNode) {
						nextNodes += node + " ";
					}

					System.out.println("Estados correntes -> " + nextNodes);

					nextNodes = "";

					for (String node : nextNode) {
						if (commandList.size() == commandSize) {
							System.out.println("Estado de aceitação -> "
									+ (nextNode.get(0).equals(finalState) ? "Aceito" : "Rejeitado"));
						}
						nodeRule = nodeFullData.get(node);

						if (nodeRule != null)
							nodeRuleListAux.add(nodeRule);

					}

				}

				nodeRuleList = nodeRuleListAux;

			}
		}

	}
}