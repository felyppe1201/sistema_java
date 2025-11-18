CREATE TABLE Usuario (
    id BIGINT UNSIGNED PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(128) NOT NULL,
    cargo ENUM('alu', 'prof', 'coord', 'adm'),
    ativo BOOLEAN DEFAULT TRUE,
    stat INT UNSIGNED NOT NULL DEFAULT 1
);

CREATE TABLE Curso (
    id BIGINT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

CREATE TABLE Disciplina (
    id BIGINT PRIMARY KEY,
    curso_id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    FOREIGN KEY (curso_id) REFERENCES Curso(id)
);

CREATE TABLE Turma (
    id BIGINT PRIMARY KEY,
    disciplina_id BIGINT NOT NULL,
    codigo_turma VARCHAR(50) NOT NULL,
    numero_vagas INT,
    periodo int,
    stat INT UNSIGNED NOT NULL DEFAULT 1,
    FOREIGN KEY (disciplina_id) REFERENCES Disciplina(id)
);

CREATE TABLE Matricula (
    id BIGINT PRIMARY KEY,
    turma_id BIGINT NOT NULL,
    aluno_id BIGINT NOT NULL,
    stat INT UNSIGNED NOT NULL DEFAULT 1,
    FOREIGN KEY (turma_id) REFERENCES Turma(id),
    FOREIGN KEY (aluno_id) REFERENCES Aluno(id)
);

CREATE TABLE AtribuicaoProfessor (
    id BIGINT PRIMARY KEY,
    turma_id BIGINT NOT NULL,
    professor_id BIGINT NOT NULL,
    FOREIGN KEY (turma_id) REFERENCES Turma(id),
    FOREIGN KEY (professor_id) REFERENCES Usuario(id)
);

CREATE TABLE ProcessoAvaliativo (
    id BIGINT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    periodo int,
    stat INT UNSIGNED NOT NULL DEFAULT 1
);

CREATE TABLE Formulario (
    id BIGINT PRIMARY KEY,
    processo_id BIGINT NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    identificado BOOLEAN NOT NULL default true,
    stat INT UNSIGNED NOT NULL DEFAULT 1,
    FOREIGN KEY (processo_id) REFERENCES ProcessoAvaliativo(id)
);

CREATE TABLE Questao (
    id BIGINT PRIMARY KEY,
    formulario_id BIGINT NOT NULL,
    texto TEXT NOT NULL,
    tipo enum('disc', 'obj', 'vf') NOT NULL,
    obrigatoria BOOLEAN NOT NULL default false,
    FOREIGN KEY (formulario_id) REFERENCES Formulario(id)
);

CREATE TABLE Opcao (
    id BIGINT PRIMARY KEY,
    questao_id BIGINT NOT NULL,
    texto VARCHAR(255) NOT NULL,
    FOREIGN KEY (questao_id) REFERENCES Questao(id)
);

CREATE TABLE Submissao (
    id BIGINT PRIMARY KEY,
    formulario_id BIGINT NOT NULL,
    turma_id BIGINT NOT NULL,
    usuario_id BIGINT,
    data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (formulario_id)
        REFERENCES Formulario (id),
    FOREIGN KEY (turma_id)
        REFERENCES Turma (id),
    FOREIGN KEY (usuario_id)
        REFERENCES Usuario (id)
);

CREATE TABLE Resposta (
    id BIGINT PRIMARY KEY,
    submissao_id BIGINT NOT NULL,
    questao_id BIGINT NOT NULL,
    opcao_id BIGINT,
    texto TEXT,
    FOREIGN KEY (submissao_id) REFERENCES Submissao(id),
    FOREIGN KEY (questao_id) REFERENCES Questao(id),
    FOREIGN KEY (opcao_id) REFERENCES Opcao(id)
);

CREATE TABLE Peso (
    id BIGINT PRIMARY KEY,
    questao_id BIGINT,
    opcao_id BIGINT,
    peso DECIMAL(4,2) NOT NULL,
    FOREIGN KEY (questao_id) REFERENCES Questao(id),
    FOREIGN KEY (opcao_id) REFERENCES Opcao(id)
);

-- Impedir submissão duplicada para formulários identificados
DELIMITER $$
CREATE TRIGGER trg_no_duplicate_submissao
BEFORE INSERT ON Submissao
FOR EACH ROW
BEGIN
    IF NEW.usuario_id IS NOT NULL THEN
        IF (SELECT COUNT(*) FROM Submissao 
            WHERE formulario_id = NEW.formulario_id
              AND turma_id = NEW.turma_id
              AND usuario_id = NEW.usuario_id) > 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Aluno já respondeu este formulário.';
        END IF;
    END IF;
END$$
DELIMITER ;

-- Impedir submissão se formulário estiver fechado
DELIMITER $$
CREATE TRIGGER trg_formulario_fechado
BEFORE INSERT ON Submissao
FOR EACH ROW
BEGIN
    DECLARE form_status INT UNSIGNED DEFAULT 1;

    SELECT stat INTO form_status
    FROM Formulario
    WHERE id = NEW.formulario_id;

    IF form_status <> 102 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Formulário não está aberto para respostas.';
    END IF;
END$$
DELIMITER ;