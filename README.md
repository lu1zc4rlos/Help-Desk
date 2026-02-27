## 🚀 OmegaTech API – Sistema de Help Desk
API REST desenvolvida em Java com arquitetura em camadas, voltada para gerenciamento de chamados técnicos (Help Desk), autenticação segura com JWT e integração com serviços externos.
> 🚧 *Projeto em desenvolvimento contínuo — Backend funcional e estruturado, com frontend em evolução futura.

<br>

## 📖 Sobre o Projeto
A OmegaTech API foi desenvolvida com o objetivo de simular um sistema real de suporte técnico, aplicando conceitos sólidos de arquitetura, segurança e organização de código.

### A aplicação permite:

* Cadastro e autenticação de usuários**: Segurança no acesso à plataforma.
* Controle de perfis**: Diferenciação entre Administrador, Técnico e Usuário.
* Abertura e gerenciamento de tickets**: Fluxo completo de chamados técnicos.
* Recuperação de senha com token**: Processo seguro via e-mail.
* Envio de e-mails automáticos**: Notificações automáticas do sistema.
* Integração com serviço externo de IA**: Funcionalidades inteligentes integradas.
* Controle completo de status e prioridade**: Organização eficiente das demandas.
> Foco principal:** Aplicar boas práticas de desenvolvimento backend e organização em camadas.

<br>

## 🏗️ Arquitetura

### O projeto foi estruturado utilizando arquitetura em camadas:

* Controller → Camada de exposição da API
* Service → Regras de negócio
* Repository → Acesso ao banco de dados
* DTOs → Transferência de dados
* Entities → Modelos de domínio
* Config → Configurações de segurança e autenticação

### Essa estrutura garante:

* Separação de responsabilidades
* Código limpo e organizado
* Facilidade de manutenção e testes
* Escalabilidade
  
<br>

## 🔐 Segurança

### A API utiliza:

* Spring Security
* Autenticação via JWT
* Filtro customizado (JwtAuthenticationFilter)
* Tratamento centralizado de exceções
* Controle de acesso por perfil
> Isso garante autenticação segura e controle de permissões.

<br>

## 🛠️ Tecnologias Utilizadas

* Java
* Spring Boot
* Spring Security
* JWT
* JPA / Hibernate
* Maven
* Testes unitários (JUnit)
* Integração com API externa (OpenAI)
* Envio de e-mails via serviço SMTP
