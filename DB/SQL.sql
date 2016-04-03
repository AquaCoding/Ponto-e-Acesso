DROP DATABASE IF EXISTS PontoAcesso;
CREATE DATABASE PontoAcesso;
USE PontoAcesso;

CREATE TABLE Setor(
	idSetor		INT			NOT NULL auto_increment, 
	nome		VARCHAR(45)	NOT NULL,
    CONSTRAINT pk_setor PRIMARY KEY (idSetor)
);

CREATE TABLE Funcao(
	idFuncao	INT			NOT NULL auto_increment,
    idSetor		INT			NOT NULL,
	nome		VARCHAR(45)	NOT NULL,
	CONSTRAINT pk_funcao PRIMARY KEY (idFuncao),
    CONSTRAINT fk_funcao_setor FOREIGN KEY (idSetor)
		REFERENCES Setor(idSetor)
);

CREATE TABLE Horario (
	idHorario			INT			NOT NULL	auto_increment,
	nome				VARCHAR(45)	NOT NULL,
    inicio				TIME		NOT NULL,
    termino				TIME		NOT NULL,
	almocoInicio		TIME		NOT NULL,
    almocoTermino		TIME		NOT NULL,
    CONSTRAINT pk_horario PRIMARY KEY (idHorario)
);

CREATE TABLE usuario(
	idUsuario		INT			NOT NULL auto_increment,
    nome			VARCHAR(20)	NOT NULL,
    senha			VARCHAR(60)	NOT NULL,
	CONSTRAINT pk_usuario PRIMARY KEY (idUsuario)
);

CREATE TABLE Funcionario(
	idFuncionario	INT			NOT NULL auto_increment,
    nome			VARCHAR(45)	NOT NULL,
	sobrenome		VARCHAR(45)	NOT NULL,
	rg				VARCHAR(9)	NOT NULL,
    cpf				VARCHAR(11)	NOT NULL,
    ctps			VARCHAR(45)	NOT NULL,
    telefone		VARCHAR(15)	NOT NULL,
    rua				VARCHAR(45)	NOT NULL,
    numero			INT			NOT NULL,
    bairro			VARCHAR(45)	NOT NULL,
    cidade			VARCHAR(45)	NOT NULL,
    estado			VARCHAR(45)	NOT NULL,
    salarioHoras	DOUBLE		NOT NULL,
	idHorario		INT			NOT NULL,
	CONSTRAINT pk_funcionario PRIMARY KEY (idFuncionario),
	CONSTRAINT fk_funcionario_horario FOREIGN KEY (idHorario)
		REFERENCES Horario(idHorario)
);

CREATE TABLE Empresa(
	idEmpresa		INT			NOT NULL auto_increment,
    nome			VARCHAR(45)	NOT NULL,
    razaoSocial		VARCHAR(60)	NOT NULL,
    cnpj			VARCHAR(14)	NOT NULL,
    rua				VARCHAR(45)	NOT NULL,
    numero			INT			NOT NULL,
    bairro			VARCHAR(45)	NOT NULL,
    cidade			VARCHAR(45)	NOT NULL,
    estado			VARCHAR(45)	NOT NULL,
    CONSTRAINT pk_empresa PRIMARY KEY (idEmpresa)
);