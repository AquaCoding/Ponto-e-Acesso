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