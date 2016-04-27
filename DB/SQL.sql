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
    segunda				BOOLEAN		NOT NULL,
    terca				BOOLEAN		NOT NULL,
    quarta				BOOLEAN		NOT NULL,
    quinta				BOOLEAN		NOT NULL,
    sexta				BOOLEAN		NOT NULL,
    sabado				BOOLEAN		NOT NULL,
    domingo				BOOLEAN		NOT NULL,
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
	suspensao 				BOOLEAN		NOT NULL,
	CONSTRAINT pk_funcionario PRIMARY KEY (idFuncionario)
);

CREATE TABLE HorarioFuncionario (
	idHorarioFuncionario		INT			NOT NULL	auto_increment,
    idHorario					INT			NOT NULL,
    idFuncionario				INT			NOT NULL,
	CONSTRAINT pk_horario_funcionario PRIMARY KEY (idHorarioFuncionario),
    CONSTRAINT fk_horario FOREIGN KEY (idHorario)
		REFERENCES Horario(idHorario),
    CONSTRAINT fk_funcionario FOREIGN KEY (idFuncionario)
		REFERENCES Funcionario(idFuncionario)
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

CREATE TABLE Ferias(
	idFerias		INT			NOT NULL auto_increment,
	nome			VARCHAR(45)	NOT NULL,
	inicio			DATE		NOT NULL,
	termino			DATE		NOT NULL,
	CONSTRAINT pk_ferias PRIMARY KEY (idFerias)
);

CREATE TABLE FuncionarioFerias(
	idFeriasfuncionario		INT 		NOT NULL auto_increment,
    idFuncionario				INT		NOT NULL,
    idFerias					INT 		NOT NULL,
    CONSTRAINT pk_funcionario_ferias PRIMARY KEY (idFeriasfuncionario),
    CONSTRAINT fk_funcionario_ferias FOREIGN KEY (idFuncionario)
		REFERENCES Funcionario(idFuncionario),
	CONSTRAINT fk_ferias FOREIGN KEY (idFerias)
		REFERENCES Ferias(idFerias)
);

CREATE TABLE Abono(
	idAbono			INT			NOT NULL auto_increment,
    dataFalta		DATE		NOT NULL,
	descricao		VARCHAR(60)	NOT NULL,
	idFuncionario	INT			NOT NULL,
	CONSTRAINT pk_abono PRIMARY KEY (idAbono),
    CONSTRAINT fk_funcionario_abono FOREIGN KEY (idFuncionario)
		REFERENCES Funcionario(idFuncionario)
);


CREATE TABLE Bonificacao (
	idBonificacao		INT		NOT NULL	auto_increment,
    nome				VARCHAR(45)		NOT NULL,
    valor				FLOAT			NOT NULL,
    idFuncionario		INT				NOT NULL,
    CONSTRAINT pk_bonificacao PRIMARY KEY (idBonificacao),
    CONSTRAINT fk_bonificacao_funcionario FOREIGN KEY (idFuncionario)
		REFERENCES Funcionario(idFuncionario)
);

CREATE TABLE FuncionarioTag(
	idFuncionarioTag		INT				NOT NULL auto_increment,
    codigo					VARCHAR(20)		NOT NULL,
    ativo					BOOLEAN			NOT NULL,
    idFuncionario			INT				NOT NULL,
    CONSTRAINT pk_funcionario_tag PRIMARY KEY (idFuncionarioTag),
    CONSTRAINT fk_funcionario_funcionario_tag FOREIGN KEY (idFuncionario)
		REFERENCES Funcionario(idFuncionario)
);

CREATE TABLE Ponto(
	idPonto				INT			NOT NULL	auto_increment,
    horario				DATETIME	NOT NULL,
    idFuncionario		INT			NOT NULL,
    idFuncionarioTag	INT			NOT NULL,
	CONSTRAINT pk_ponto PRIMARY KEY (idPonto),
    CONSTRAINT fk_funcionario_ponto FOREIGN KEY (idFuncionario)
		REFERENCES Funcionario(idFuncionario),
	CONSTRAINT fk_ponto_tag FOREIGN KEY (idFuncionarioTag)
		REFERENCES FuncionarioTag(idFuncionarioTag)
);

CREATE TABLE FuncaoFuncionario(
	idFuncaoFuncionario  	INT   NOT NULL auto_increment,
	idFuncionario   		INT   NOT NULL,
	idFuncao    			INT   NOT NULL,
	CONSTRAINT pk_funcao_funcionario_idFuncaoFuncionario PRIMARY KEY (idFuncaoFuncionario),
	CONSTRAINT fk_funcao_funcionario_idFuncionario FOREIGN KEY (idFuncionario)
		REFERENCES Funcionario(idFuncionario),
	CONSTRAINT fk_funcao_funcionario_idFuncao FOREIGN KEY (idFuncao)
		REFERENCES Funcao(idFuncao) 
);
  
CREATE TABLE Imposto (
	idImposto		INT				NOT NULL auto_increment,
    nome			VARCHAR(45)		NOT NULL,
    valor			DOUBLE			NOT NULL,
    CONSTRAINT pk_imposto PRIMARY KEY (idImposto)
);

CREATE TABLE Logs (
	idLog			INT				NOT NULL auto_increment,
    idUsuario		INT				NOT NULL,
    objeto			VARCHAR(30)		NOT NULL,
    objetoId		INT				NOT NULL,
    acao			VARCHAR(60)		NOT NULL,
    data			DATETIME		NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_logs PRIMARY KEY (idLog),
    CONSTRAINT fk_log_usuario FOREIGN KEY (idUsuario)
		REFERENCES Usuario(idUsuario)
);

CREATE TABLE Acesso(
	idAcesso			INT			NOT NULL	auto_increment,
    horario				DATETIME	NOT NULL,
    idFuncionario		INT			NOT NULL,
    idFuncionarioTag	INT			NOT NULL,
	CONSTRAINT pk_acesso PRIMARY KEY (idAcesso),
    CONSTRAINT fk_funcionario_acesso FOREIGN KEY (idFuncionario)
		REFERENCES Funcionario(idFuncionario),
	CONSTRAINT fk_acesso_tag FOREIGN KEY (idFuncionarioTag)
		REFERENCES FuncionarioTag(idFuncionarioTag)
);

CREATE VIEW ShowTags as SELECT ft.idFuncionarioTag, concat(f.nome, ' ', f.sobrenome) as nome, f.cpf, ft.codigo, ft.ativo
FROM Funcionario as f JOIN FuncionarioTag as ft;

CREATE VIEW FuncionarioTodasFerias as SELECT ff.idFeriasfuncionario, ff.idFerias, f.nome, f.inicio, f.termino, ff.idFuncionario
FROM FuncionarioFerias as ff JOIN Ferias as f;