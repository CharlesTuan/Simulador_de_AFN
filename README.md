Simulador de AUTÔMATO FINITO NÃO DETERMINÍSTICO

Neste repositório se encontra um pequeno simulador, dentro do projeto há 3 arquivos .txt: rules, input, output. No rules está a determinação das regras do autômato, o input estão os dados de entrada do usuário e no output está os dados referente ao processamento.

A primeira linha contém os nodes, ou estados. A segunda linha contém o alfabeto. As outras linhas com exceção das duas últimas contém uma representação da interação dos nodes com os alfabetos na ordem de apresentação A penúltima linha contém o estado inicial. A última linha contém o estado final.

EXEMPLO: Q1 Q2 0 1 Q1 \# input 0 no node Q1 vai para o Q1 Q2 \# input 1 no node Q1 vai para o Q2 Q2 \# input 0 no node Q2 vai para o Q2 Q2 \# input 1 no node Q2 vai para o Q2 Q1 \# node inicial Q2 \# node final
