# Plataforma de Cursos Online — TP1 Microsserviços

## Descrição do Projeto
Sistema de gestão de cursos online, com cadastro de alunos, cadastro de cursos e matrícula de alunos em cursos. Construído como uma arquitetura de microsserviços para o TP1 da disciplina de Microsserviços e DevOps com Spring Boot e Spring Cloud.

## Arquitetura
A solução é composta por 4 componentes:
- **Eureka Server**: Discovery Server, permite que os microsserviços se encontrem pelo nome, sem IP/porta fixos.
- **alunoservice**: gerencia o cadastro de alunos, com banco PostgreSQL.
- **cursoservice**: gerencia cursos e matrículas, com banco MongoDB. Ao matricular um aluno, valida sua existência chamando o aluno-service (com Circuit Breaker/fallback caso ele esteja indisponível).
- **API Gateway**: ponto único de entrada da aplicação (porta 8080), roteando as chamadas externas para os microsserviços corretos via Eureka.

## Microservices

| Serviço | Responsabilidade | Porta | Banco |
|---|---|---|---|
| eureka-server | Discovery Server | 8761 | — |
| alunoservice | Cadastro de alunos | 8081 | PostgreSQL (aluno_db) |
| cursoservice | Cursos e matrículas | 8082 | MongoDB (curso_db) |
| gateway | Ponto único de entrada / roteamento | 8080 | — |

## Tecnologias utilizadas
- Java 21
- Spring Boot 4.1.1
- Spring Cloud 2025.1.3
- Spring Cloud Netflix Eureka (Discovery Server)
- Spring Cloud Gateway Server WebMVC
- Spring Data JPA + PostgreSQL
- Spring Data MongoDB
- OpenFeign (comunicação entre microsserviços)
- Resilience4j (Circuit Breaker)
- Docker / Docker Compose (bancos de dados)
- Maven

## Como executar

### Pré-requisitos
- JDK 21
- Docker Desktop
- IntelliJ IDEA
- Postman

### 1. Subir os bancos de dados
Na pasta raiz do projeto (onde está o `docker-compose.yml`):
```bash
docker compose up -d
```
Isso sobe o PostgreSQL (porta 5432) e o MongoDB (porta 27017).

### 2. Subir os serviços (nessa ordem)
Cada serviço é um projeto Maven independente. Rode a classe principal de cada um, na ordem abaixo:

1. `eureka-server` → `EurekaServerApplication`
2. `alunoservice` → `AlunoserviceApplication`
3. `cursoservice` → `CursoserviceApplication`
4. `gateway` → `GatewayApplication`

## Discovery Server (Eureka)
Acesse **http://localhost:8761** para visualizar os serviços registrados. Após subir todos os componentes, devem aparecer: `ALUNOSERVICE`, `CURSOSERVICE` e `GATEWAY`, todos com status `UP`.

## API Gateway
Todas as chamadas externas devem passar pelo Gateway, na porta **8080**:

| Rota | Destino |
|---|---|
| `/api/alunos/**` | aluno-service |
| `/api/cursos/**` | curso-service |

## Exemplos de requisições

### Criar um aluno

POST http://localhost:8080/api/alunos
Content-Type: application/json

{
"nome": "Apolo Souza",
"email": "apolo@email.com",
"cpf": "12345678900",
"dataNascimento": "2022-02-06"
}


### Listar alunos

GET http://localhost:8080/api/alunos

### Criar um curso

POST http://localhost:8080/api/cursos
Content-Type: application/json

{
"titulo": "Java Básico",
"descricao": "Curso introdutório",
"modulos": ["Variáveis", "Loops", "OOP"]
}


### Matricular um aluno em um curso

POST http://localhost:8080/api/cursos/{cursoId}/matriculas?alunoId=1

(troque `{cursoId}` pelo id retornado ao criar o curso)

### Testar a resiliência (Circuit Breaker)

1. Derrube o `aluno-service`.
2. Repita a chamada de matrícula acima.
3. Esperado: resposta `503` com a mensagem "Não foi possível validar o aluno no momento. Tente novamente mais tarde.", em vez de erro genérico.
