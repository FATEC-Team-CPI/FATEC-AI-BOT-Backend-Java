# FATEC-AI-BOT-Backend-Java
Repositório do Backend do FATEC AI BOT

## LocalStack (DynamoDB)

Este repositório possui um ambiente LocalStack minimo com apenas DynamoDB e bootstrap automatico de tabela na subida.

### O que foi configurado
- Servico LocalStack com `SERVICES=dynamodb`
- Script de inicializacao idempotente que cria a tabela `fatec-ai-bot-core`
- Comandos no `Makefile` para subir, parar, logs e listar tabelas

### Modelagem (single-table)

Tabela unica com chave composta:
- `pk` (partition key)
- `sk` (sort key)

Indices secundarios globais (GSI):
- `gsi1-email`: `gsi1pk` + `gsi1sk`
	- uso: busca de usuario por email para login/auth
	- exemplo: `gsi1pk=AUTH#EMAIL`, `gsi1sk=email@admin.com`
- `gsi2-unit-content`: `gsi2pk` + `gsi2sk`
	- uso: listagem de conteudos por unidade com ordenacao por status/data
	- exemplo: `gsi2pk=UNIT#FatecItaquera#CONTENT`, `gsi2sk=STATUS#ACTIVE#TS#2026-03-31T00:00:00Z#NomeConteudo`

Exemplos iniciais:
- `pk=FatecItaquera#users`, `sk=email@admin.com` (auth/login/cadastro)
- `pk=Unidades`, `sk=FatecItaquera` (unidades)
- `pk=FatecItaquera#Conteudos`, `sk=NomeConteudo` (conteudos e arquivos no S3)

O bootstrap inclui seed desses exemplos por padrao. Para desativar, altere em `docker-compose.localstack.yml`:
- `DDB_SEED_EXAMPLES=false`

### Como usar
1. Subir LocalStack:
	`make localstack-up`
2. Ver tabelas criadas:
	`make localstack-tables`
3. Acompanhar logs:
	`make localstack-logs`
4. Parar ambiente:
	`make localstack-down`

### Arquivos
- `docker-compose.localstack.yml`
- `localstack/init/ready.d/01-create-dynamodb-table.sh`

## OpenAPI e Swagger

Com a aplicacao rodando em dev, acesse:
- Swagger UI: `http://localhost:8080/swagger`
- OpenAPI: `http://localhost:8080/openapi`
