## 🚀 FLAP Kanban System - Backend API

Este repositório contém a api, construída em **Java com Spring Boot**, para gerenciar tarefas e o fluxo de trabalho Kanban.

### **1. Visão Geral e Arquitetura**

O sistema segue uma arquitetura de camadas (Controller $\to$ Service $\to$ Repository) e é baseado em dois pilares avançados de design:

### **2. Tecnologias Principais**

| Tecnologia | Finalidade |
| :--- | :--- |
| **Java / Spring Boot 3.x** | Servidor de API REST e lógica de negócio. |
| **PostgreSQL** | Banco de dados relacional (Hospedado externamente, ex: Supabase/AWS RDS). |
| **Spring Security 6.x** | Autenticação Baseada em Sessão (Stateful) e RBAC. |
| **JPA / Hibernate** | Mapeamento Objeto-Relacional. |
| **Dropbox api** | Gerenciamento de arquivos e preview de arquivos diretamente pela aplicação. |
| **Google Calendar api** | Criação de eventos automatizada. |

### **3. Setup de Ambiente e Requisitos**

Para rodar o backend localmente, você precisa:

  * **Java JDK 17+** (Recomendado JDK 21+).
  * **Maven** (Para gerenciamento de dependências).
  * **Acesso a um Banco de Dados PostgreSQL**.

#### **Configuração do `application.properties`**

**Atenção:** Você deve substituir os placeholders com suas credenciais de acesso remoto.

```properties
# Conexão com o PostgreSQL Remoto (Mude a URL, User, e Senha)
spring.datasource.url=jdbc:postgresql://[SEU_ENDPOINT]:5432/postgres
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuração de Hibernate (usar 'validate' após a primeira inicialização)
spring.jpa.hibernate.ddl-auto=update 
spring.jpa.show-sql=true

# Servidor e Porta Padrão
server.port=8080 
```

### **4. Inicialização do Banco de Dados (RBAC Setup)**

Para que o sistema de segurança funcione, as tabelas `role`, `permissao` e o `Usuario Master` precisam ser preenchidos.

Execute os seguintes comandos SQL no seu cliente PostgreSQL (DBeaver/Supabase) na ordem, utilizando o seu **`BCryptPasswordEncoder`** para o hash do `Super Admin`:

1.  **Criação da Estrutura RBAC:** (Tabelas `role`, `permissao`, `role_permissao`).
2.  **Inserção de `Permissao`s:** Insere todos os verbos de ação (`TAREFA_CRIAR`, etc.).
3.  **Criação de Roles:** Insere `ADMINISTRADOR_MASTER` e `USUARIO_PADRAO`.
4.  **Associação Master:** Liga todas as permissões ao `ADMINISTRADOR_MASTER`.
5.  **Inserção de Usuário Master:** Insere o usuário `master@flap.com` com a senha **hasheada** (Ex: `admin123`).

### **5. Execução**

#### **Como Rodar a Aplicação:**

```bash
# Na pasta raiz do projeto
./mvnw spring-boot:run
```
