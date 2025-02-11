# Teste Desenvolvedor Java Back-End

Teste para a vaga de desenvolvedo Java Back-End.

# Tecnologias utilizadas:

- Java;
- Spark Framework;
- Docker;
- Maven;
- Mockito;

# Instruções para executar a aplicação:

Na pasta raiz do projeto executar a seguinte ordem de comando:

1. docker build . -t axreng/backend
2. docker run -e BASE_URL=http://hiring.axreng.com/ -p 4567:4567 --rm axreng/backend

Observação: As collections das requisições estão dentro do diretorio raiz do projeto (Teste-Backend.postman_collection).
