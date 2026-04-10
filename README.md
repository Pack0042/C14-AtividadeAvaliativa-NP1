# Sistema de Reserva de Salas

## Descrição

Este projeto consiste no desenvolvimento de um sistema de reserva de salas, permitindo que usuários realizem agendamentos de forma simples e organizada.

---




## Tecnologias

- Java  
- Maven (gerenciamento de dependências)
- JUnit (testes unitários)  
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

## Autores

- Patrick Augusto Lins de Oliveira Damião - GES - 496
- Pedro Armengol de Oliveira - GEC - 2093
- Pedro Henrique de Paula Andrade - GES - 368

---
