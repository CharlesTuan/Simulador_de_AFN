Simulador de AUTÔMATO FINITO NÃO DETERMINÍSTICO

Neste repositório se encontra um pequeno simulador, dentro do projeto há 3 arquivos .txt: rules, input, output. No rules está a determinação das regras do autômato, o input estão os dados de entrada do usuário e no output está os dados referente ao processamento.

A primeira linha contém os nodes, ou estados. A segunda linha contém o alfabeto. As outras linhas com exceção das duas últimas contém uma representação da interação dos nodes com os alfabetos na ordem de apresentação. A penúltima linha contém o estado inicial. A última linha contém o estado final.

EXEMPLO DO RULE.TXT:

Q1 Q2

0 1

Q1 \# input 0 no node Q1 => Q1

Q2 \# input 1 no node Q1 => Q2

Q2 \# input 0 no node Q2 => 2 

Q2 \# input 1 no node Q2 => Q2 

Q1 \# estado inicial 

Q2 \# estado final

EXEMPLO DO INPUT.TXT:

0 1 0 1 0 1

0 0 0 0 1

Cada linha do input representa um processamento.



LEITURA DO ARQUIVO DE RESPOSTA

O arquivo de resposta vai demonstrar na ordem de execução os estados correntes e no final se aceita ou rejeita.
Para um rule.txt como o seguinte:

- q1 q2 q3 q4 # Linha com o conjunto de estados, separados por espaco

- 0 1 # Linha com o alfabeto, simbolos separados por espaco

- q1 # inicio da descricao da matriz linha 0, coluna 0

- q1 q2 # linha 0, coluna 1

- \* 

- q3 # matriz linha 1, coluna 0

- \* 

- q3

- \*  

- q4

- \* 

- q4 # matriz linha 3, coluna 0

- q4

- \* 

- q1 # estado inicial

- q4 # estados finais

Com o seguinte input.txt:

1 0 1 0

Teremos o seguinte arquivo de resposta:

Linha de execução -> 1

Estado inicial ->  q1

Simbolo lido ->  1

Estados correntes -> q1 q2 q3 

Simbolo lido ->  0

A PARTIR DESTE PONTO ELE EXECUTA O COMANDO 0 PARA Q1, Q2 e Q3

Estados correntes -> q1 

Estados correntes -> q3 

Estados correntes -> * 

COMO NÃO HÁ CAMINHO PARA O Q3 NO COMANDO 0 ELE JÁ REJEITA

Estado de aceitação -> Rejeitado

Simbolo lido ->  1

Estados correntes -> q1 q2 q3 

Estados correntes -> q4 

Simbolo lido ->  0

Estados correntes -> q1 

Estado de aceitação -> Rejeitado

Estados correntes -> q3 

Estado de aceitação -> Rejeitado

Estados correntes -> * 

Estado de aceitação -> Rejeitado

Estados correntes -> q4 

Estado de aceitação -> Aceito

