# IndicadorReal

Aplicação desktop em Java 21 para interpretar registros de erro, localizar matrículas em arquivos JSON e corrigir automaticamente o campo `TIPOENVIO`.

<div align="center">
  <img src="assets/indicador-real.png"
       alt="Visão geral do projeto IndicadorReal"
       width="100%">
</div>

## Visão geral

O IndicadorReal recebe os registros de erro por arquivo TXT ou texto colado e cruza as matrículas identificadas com os registros do JSON selecionado. Ao final do processamento, preserva o arquivo original e gera novos arquivos com o resultado da correção, o relatório e o log da execução.

## Funcionalidades

- importação dos erros por arquivo TXT;
- entrada alternativa por texto colado;
- leitura das estruturas JSON `INDICADOR_REAL → REAL` e `REAL`;
- identificação de matrículas e valores de `TIPOENVIO` por expressões regulares;
- correspondência pelo campo `NUMERO_REGISTRO`;
- atualização do campo `TIPOENVIO` somente nos registros identificados;
- registro de alterações, itens já corretos e matrículas não encontradas;
- identificação de ocorrências relacionadas a CEP inválido e vários endereços;
- geração de JSON corrigido, relatório TXT e log de execução;
- interface gráfica com seleção de arquivos, arrastar e soltar, progresso e histórico.

## Fluxo de processamento

```text
TXT ou texto colado
        │
        ▼
Extração das matrículas e do TIPOENVIO esperado
        │
        ▼
Leitura e validação do arquivo JSON
        │
        ▼
Correspondência por NUMERO_REGISTRO
        │
        ▼
Atualização do TIPOENVIO
        │
        ▼
JSON corrigido + relatório + log
```

## Tecnologias

- Java 21;
- Java Swing;
- FlatLaf;
- Jackson Databind;
- Maven;
- `jpackage` para o instalador Windows configurado no `pom.xml`.

## Estrutura principal

```text
src/main/java/com/indicador/
├── config/   # configurações e histórico
├── io/       # leitura de arquivos
├── log/      # logs de execução
├── model/    # modelos do domínio
├── parser/   # interpretação do TXT
├── report/   # conteúdo dos relatórios
├── service/  # processamento e atualização do JSON
├── theme/    # tema visual
└── ui/       # componentes da interface
```

## Requisitos

- JDK 21;
- Apache Maven;
- ambiente Windows configurado para a geração do instalador `.exe`, quando essa etapa for utilizada.

## Compilação

Para compilar as classes:

```bash
mvn clean compile
```

Para executar o empacotamento definido no `pom.xml`:

```bash
mvn clean package
```

O empacotamento inclui um JAR com dependências e a etapa configurada com `jpackage` para geração do instalador Windows.

## Arquivos gerados

Para um arquivo de origem `dados.json`, a aplicação gera no mesmo diretório:

- `dados_corrigido.json`;
- `dados_RELATORIO.txt`;
- arquivo de log da execução.

## Escopo

O parser foi desenvolvido para os padrões de mensagem tratados no código. Antes de utilizar os arquivos gerados em outro sistema, revise o relatório e valide o JSON resultante no fluxo de destino.
