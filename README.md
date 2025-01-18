# LiterAlura

## Sobre o projeto
LiterAlura é uma aplicação desenvolvida com Spring Boot que conecta e permite o usuario buscar livros e autores disponibilizados em uma API externa [Gutendex](https://gutendex.com/). Essa interação é realizada através do terminal, permitindo uma serie de ações que podem ser realizadas, armazenando os dados no banco de dados PostgresSQL.

## Funcionalidade
A aplicação oferece as seguintes funcionalidades: 
```less
1 - Buscar livro pelo título
2 - Listar livros registrados
3 - Listar autores registrados
4 - Listar autores vivos em um determinado ano
5 - Listar livros em um determinado idioma
0 - Sair
```

## Tecnologias utilizadas
- Java 21
- Spring Boot 3
- Spring Data JPA
- PostgresSQL
- API Gutendex
- Maven

### Estrutura do projeto
- **Model**: Representa as entidades.

- **Service**: Contém a lógica da aplicação.

- **Repository**: Interface para interagir com o banco de dados.

- **Principal**: Responsável pela interação com o usuário.
  
  ### Banco de dados
  A conexão com o banco de dados é configurada no arquivo `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql:///${DB_HOST}/literalura_livros
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
hibernate.dialect=org.hibernate.dialect.HSQLDialect

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.format-sql=true

server.error.include-stacktrace=never
```
