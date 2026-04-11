[![CI/CD Pipeline](https://github.com/Pack0042/C14-AtividadeAvaliativa-NP1/actions/workflows/ci-cd.yml/badge.svg?branch=main)](https://github.com/Pack0042/C14-AtividadeAvaliativa-NP1/actions/workflows/ci-cd.yml)

[![GitHub Pages](https://img.shields.io/badge/GitHub%20Pages-online-brightgreen?logo=github)](https://pack0042.github.io/C14-AtividadeAvaliativa-NP1/)

# Sistema de Reserva de Salas

## Autores

- [Patrick Augusto Lins de Oliveira Damião](https://github.com/Pack0042) - GES - 496
- [Pedro Armengol de Oliveira](https://github.com/Armengolz) - GEC - 2093
- [Pedro Henrique de Paula Andrade](https://github.com/phandrad3) - GES - 368

---

## Descrição

Este projeto consiste no desenvolvimento de um sistema de reserva de salas, permitindo que usuários realizem agendamentos de forma simples e organizada.

---

## Tecnologias

- Java  
- Maven (gerenciamento de dependências)
- JUnit (testes unitários)
- JaCoCo (cobertura de testes)
- GitHub Actions (integração contínua e deploy)  

---
## Tipos de Usuário

### Usuário Comum
- Realizar cadastro no sistema  
- Efetuar login  
- Reservar salas  
- Cancelar suas próprias reservas  
- Visualizar salas disponíveis  

### Funcionário
- Herda as funcionalidades de um usuário comum  
- Cadastrar novas salas  
- Remover salas existentes  
- Cancelar reservas de qualquer usuário  
- Alterar a sala de uma reserva existente 

---

## Funcionalidades

### Autenticação
- Cadastro de usuário  
- Login no sistema  

### Gerenciamento de Salas
- Cadastro de salas  
- Remoção de salas  
- Listagem de salas disponíveis  

### Gerenciamento de Reservas
- Criação de reservas  
- Cancelamento de reservas  
- Listagem de reservas  
- Alteração de sala de uma reserva  

### Controle de Reserva
- Não é permitido reservar horários já ocupados  
- Não é permitido realizar reservas em horários passados  
- Cada reserva possui duração fixa de 2 horas  
- Cada usuário pode possuir apenas 1 reserva ativa por vez  
- Ao término do horário da reserva, ela será encerrada automaticamente  

### Renovação de Sala

- A renovação da sala só poderá ser solicitada quando faltarem 15 minutos ou menos para o término da reserva atual  
- A renovação só será permitida caso não exista outra reserva para a sala no horário seguinte 
- O usuário será notificado quando o horário da reserva estiver próximo do término

---

## Comandos (Scripts)

## Scripts

**Compila o projeto.**

```bash
mvn compile
```

**Executa os testes unitários.**

```bash
mvn test  
```
**Executa os testes e gera o relatório de cobertura com JaCoCo.**

```bash
mvn verify  
```

**Gera o arquivo `.jar` do projeto.**

```bash
mvn package
```
**Remove os arquivos gerados anteriormente na pasta `target`.**

```bash
mvn clean  
```

---

## Prompts [Patrick Augusto Lins de Oliveira Damião](https://github.com/Pack0042)

Foi utilizado o Claude Code, sempre com o contexto do README explicando a estrutura do projeto e o PDF com as orientações da tarefa.

### Estou querendo desenvolver um projeto simples para a faculdade com a finalidade de realizar testes unitários e automação de CI/CD. Poderia dar uma olhada no README e criar a base do projeto?


Gerou um bom resultado a partir do README que montamos antes de dar início ao desenvolvimento do projeto, só precisou de alguns ajustes na estrutura e a mudança de algumas lógicas


### De acordo com os testes que já foram feitos no repositório teria boas recomendações para alguns outros testes?

Resultado não foi muito bom, gerou alguns testes interessantes mas a grande maioria sem sentido ou redundantes com outros testes já existentes. Essa etapa precisou de bastante ajuste manual.


### Poderia gerar a pipeline de notificação?

Resultado foi bom, precisei ir atrás de alguns detalhes extras para entender como configurar o envio de emails através da App Password do Google, mas ele explicou bem como configurar as variáveis de ambiente e as modificações feitas no script foram apenas estéticas. 


---

## Prompts [Pedro Armengol de Oliveira](https://github.com/Armengolz)

Ajuste os testes unitários já existentes do projeto Java usando JUnit 5, corrigindo os pontos levantados no review da PR.

Regras obrigatórias:
- NÃO alterar código de produção, a menos que seja estritamente necessário para os testes compilarem
- Priorizar alterações apenas nos arquivos de teste
- Manter os testes organizados por classe, no mesmo arquivo correspondente
- Continuar usando JUnit 5
- Usar Mockito apenas se realmente necessário
- Não criar comentários desnecessários no código
- Preservar o padrão de nomes e a estrutura já existente, mas melhorando clareza e consistência

Mudanças que devem ser feitas:

1. Remover senha hardcoded repetida
- Atualmente a senha "123456" está repetida em vários testes
- Extrair esse valor para uma constante compartilhada dentro de cada classe de teste em que ela for usada
- Usar algo como:
  private static final String DEFAULT_PASSWORD = "123456";
- Substituir todas as ocorrências repetidas pela constante

2. Corrigir o teste de listagem de salas disponíveis
- Revisar o teste atualmente nomeado como algo na linha de listarSalasDisponiveisCorretamente
- O nome do teste sugere validação de disponibilidade, mas hoje ele apenas valida listagem de todas as salas
- Verificar o comportamento real do método listarSalas
- Se o método realmente lista todas as salas, renomear o teste para refletir isso corretamente
- Se existir lógica real de disponibilidade, ajustar o teste para reservar uma sala antes e validar corretamente o comportamento esperado
- O nome do teste deve refletir exatamente o que ele verifica

3. Quebrar o teste criarReservaValidaComSucesso
- O teste atual está grande demais e valida muitas coisas ao mesmo tempo
- Dividir esse teste em testes menores, mais claros e coesos
- Separar, por exemplo, validações como:
  - criação da reserva com sucesso
  - estado inicial correto da reserva criada
  - existência de reserva ativa para o usuário
  - presença da reserva na listagem
- Cada teste deve verificar uma única responsabilidade principal
- Evitar um único teste com muitas asserções

4. Adicionar teste baseline para lista de reservas vazia
- Criar um teste simples que valide que a lista de reservas começa vazia antes de qualquer criação
- Esse teste deve ajudar a validar que o setup e o @BeforeEach estão funcionando corretamente
- Nome sugerido:
  deveIniciarSemReservas()

5. Padronizar helper de criação de funcionário
- Hoje existe inconsistência entre os arquivos de teste:
  GerenciadorUsuariosTest não possui helper criarFuncionario, enquanto outras classes criam funcionário diretamente no setup
- Padronizar isso
- Criar helpers privados coerentes para montagem de objetos de teste, principalmente para funcionário
- Pode ser um helper em cada classe de teste, se for o padrão mais simples
- O importante é manter consistência entre GerenciadorUsuariosTest, GerenciadorSalasTest e GerenciadorReservasTest
- Evitar duplicação desnecessária de construção manual de objetos

6. Melhorar legibilidade e coesão geral
- Revisar nomes de testes para que descrevam exatamente o comportamento validado
- Garantir que os testes de uma mesma classe permaneçam no mesmo arquivo
- Remover excesso de responsabilidade em testes únicos
- Manter setup limpo e reutilizável

No final:
- Executar os testes
- Garantir que todos compilem e passem
- Não adicionar novos testes além dos necessários para corrigir os pontos do review, exceto os desdobramentos necessários da divisão do teste grande e o baseline da lista vazia


O resultado da IA que utilizou o prompt foi satisfatório, apenas pequenas coisas foram necessárias serem corrigidas. 

---

## Prompts [Pedro Henrique de Paula Andrade](https://github.com/phandrad3)

Utilizo o ChatGPT para criar prompts e os envio para Codex, Claude, Copilot e Gemini. Depois, comparo as respostas e sigo com a solução mais adequada ao projeto. 

=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
Estou trabalhando em um projeto Java com Maven, JUnit 5 e Mockito, e preciso ajustar os testes unitários com base em comentários de review.

Quero que você:
- padronize os testes usando `@BeforeEach` quando fizer sentido
- mantenha os testes adicionados, mas sem exagerar na quantidade de casos nem “explodir” a suíte desnecessariamente
- corrija testes que quebram encapsulamento, por exemplo quando acessam ou modificam diretamente listas internas retornadas por métodos do gerenciador
- prefira abordagens mais seguras, usando API pública ou controle de tempo com mock quando necessário
- troque asserts pouco idiomáticos por versões melhores, como:
  - `assertEquals(false, ...)` por `assertFalse(...)`
  - `assertEquals(null, ...)` por `assertNull(...)`
- preserve compatibilidade com o código existente
- não crie classes de teste duplicadas
- se houver conflito entre testes já existentes e testes novos, consolide tudo no mesmo arquivo
- valide no final com `mvn test`

Contexto:
- Existem testes para `GerenciadorReservas`, `GerenciadorSalas` e `GerenciadorUsuarios`
- O projeto já possui branch principal com testes e outra branch com testes adicionais

=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

Estou trabalhando em um projeto Java com Maven, JUnit 5, JaCoCo e GitHub Actions, e quero ajustar o pipeline CI/CD.

Quero que você atualize o workflow para:
- ter um job `test` e um job `coverage`
- os dois jobs devem ficar em paralelo, no mesmo nível dentro de `jobs:`
- o job `test` deve:
  - gerar um resumo dos testes no `GITHUB_STEP_SUMMARY`
  - mostrar uma tabela com:
    - total de testes
    - passed
    - failures
    - errors
    - skipped
  - fazer upload dos relatórios de teste como artifact
- o job `coverage` deve:
  - ler o arquivo `target/site/jacoco/jacoco.xml`
  - gerar um resumo de cobertura no `GITHUB_STEP_SUMMARY`
  - mostrar uma tabela com:
    - Line
    - Branch
    - Method
  - fazer upload do relatório JaCoCo como artifact
- evitar ações que possam gerar erro `HttpError: Resource not accessible by integration`, como `dorny/test-reporter`
- manter o workflow simples, compatível com repositório público e sem depender de permissões especiais desnecessárias

=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
