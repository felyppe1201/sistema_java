drop database facul;

create database facul;

use facul;

CREATE TABLE Usuario (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(128) NOT NULL,
    cargo ENUM('alu', 'prof', 'coord', 'adm'),
    ativo BOOLEAN DEFAULT TRUE,
    stat INT UNSIGNED NOT NULL DEFAULT 1
);

CREATE TABLE Curso (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    periodo int,
    ativo BOOLEAN DEFAULT TRUE,
    nome VARCHAR(255) NOT NULL
);

CREATE TABLE aluno_matriculado (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    id_usuario BIGINT UNSIGNED NOT NULL,
    curso_id BIGINT UNSIGNED NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_usuario)
        REFERENCES usuario (id),
    FOREIGN KEY (curso_id)
        REFERENCES Curso (id)
);

CREATE TABLE Disciplina (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    curso_id BIGINT UNSIGNED NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    nome VARCHAR(255) NOT NULL,
    FOREIGN KEY (curso_id)
        REFERENCES Curso (id)
);

CREATE TABLE Turma (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    disciplina_id BIGINT UNSIGNED NOT NULL,
    codigo_turma VARCHAR(50) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    numero_vagas INT,
    periodo INT,
    stat INT UNSIGNED NOT NULL DEFAULT 1,
    FOREIGN KEY (disciplina_id)
        REFERENCES Disciplina (id)
);

CREATE TABLE Matricula (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    turma_id BIGINT UNSIGNED NOT NULL,
    aluno_id BIGINT UNSIGNED NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    stat INT UNSIGNED NOT NULL DEFAULT 1,
    FOREIGN KEY (turma_id)
        REFERENCES Turma (id),
    FOREIGN KEY (aluno_id)
        REFERENCES Usuario (id)
);

CREATE TABLE AtribuicaoProfessor (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    turma_id BIGINT UNSIGNED NOT NULL,
    professor_id BIGINT UNSIGNED UNSIGNED NOT NULL,
    FOREIGN KEY (turma_id)
        REFERENCES Turma (id),
    FOREIGN KEY (professor_id)
        REFERENCES Usuario (id)
);

CREATE TABLE ProcessoAvaliativo (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    periodo INT,
    stat INT UNSIGNED NOT NULL DEFAULT 1
);

CREATE TABLE Formulario (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    processo_id BIGINT UNSIGNED NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    identificado BOOLEAN NOT NULL DEFAULT TRUE,
    stat INT UNSIGNED NOT NULL DEFAULT 1,
    FOREIGN KEY (processo_id)
        REFERENCES ProcessoAvaliativo (id)
);

CREATE TABLE Questao (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    formulario_id BIGINT UNSIGNED NOT NULL,
    texto TEXT NOT NULL,
    tipo ENUM('disc', 'obj', 'vf') NOT NULL,
    obrigatoria BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (formulario_id)
        REFERENCES Formulario (id)
);

CREATE TABLE Opcao (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    questao_id BIGINT UNSIGNED NOT NULL,
    texto VARCHAR(255) NOT NULL,
    vf BOOLEAN DEFAULT FALSE NOT NULL,
    respostavf BOOLEAN,
    correta BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (questao_id)
        REFERENCES Questao (id)
);

CREATE TABLE Submissao (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    formulario_id BIGINT UNSIGNED NOT NULL,
    turma_id BIGINT UNSIGNED NOT NULL,
    usuario_id BIGINT UNSIGNED,
    data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    nota DECIMAL(5 , 2 ),
    FOREIGN KEY (formulario_id)
        REFERENCES Formulario (id),
    FOREIGN KEY (turma_id)
        REFERENCES Turma (id),
    FOREIGN KEY (usuario_id)
        REFERENCES Usuario (id)
);

CREATE TABLE Resposta (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    submissao_id BIGINT UNSIGNED NOT NULL,
    questao_id BIGINT UNSIGNED NOT NULL,
    opcao_id BIGINT UNSIGNED,
    texto TEXT,
    FOREIGN KEY (submissao_id)
        REFERENCES Submissao (id),
    FOREIGN KEY (questao_id)
        REFERENCES Questao (id),
    FOREIGN KEY (opcao_id)
        REFERENCES Opcao (id)
);

CREATE TABLE Peso (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    questao_id BIGINT UNSIGNED,
    opcao_id BIGINT UNSIGNED,
    peso DECIMAL(4 , 2 ) NOT NULL,
    FOREIGN KEY (questao_id)
        REFERENCES Questao (id),
    FOREIGN KEY (opcao_id)
        REFERENCES Opcao (id)
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


-- dados teste:

INSERT INTO Usuario (nome, email, senha, cargo, ativo, stat)
VALUES
('Administrador Geral', 'admin@teste.com', '$2a$10$gh3qXRA14m0eiZnm9mweUuNskXxCM3IHcnn6QZNjWt3gLZ/eCpXdG', 'adm', TRUE, 1),
('Carlos Coordenador', 'coord@teste.com', '$2a$10$gh3qXRA14m0eiZnm9mweUuNskXxCM3IHcnn6QZNjWt3gLZ/eCpXdG', 'coord', TRUE, 1),
('Paulo Professor', 'prof@teste.com', '$2a$10$gh3qXRA14m0eiZnm9mweUuNskXxCM3IHcnn6QZNjWt3gLZ/eCpXdG', 'prof', TRUE, 1),
('Ana Aluna', 'aluna1@teste.com', '$2a$10$gh3qXRA14m0eiZnm9mweUuNskXxCM3IHcnn6QZNjWt3gLZ/eCpXdG', 'alu', TRUE, 1),
('Bruno Aluno', 'aluno2@teste.com', '$2a$10$gh3qXRA14m0eiZnm9mweUuNskXxCM3IHcnn6QZNjWt3gLZ/eCpXdG', 'alu', TRUE, 1);

INSERT INTO Usuario (nome, email, senha, cargo, ativo, stat)
VALUES
('Pedro Professor', 'prof2@teste.com', '$2a$10$gh3qXRA14m0eiZnm9mweUuNskXxCM3IHcnn6QZNjWt3gLZ/eCpXdG', 'prof', TRUE, 1);

INSERT INTO Curso (nome, ativo)
VALUES ('Ciência da Computação', TRUE);

INSERT INTO aluno_matriculado (id_usuario, curso_id, ativo)
VALUES
(4, 1, TRUE),
(5, 1, TRUE);

INSERT INTO Disciplina (curso_id, nome, ativo)
VALUES
(1, 'Programação I', TRUE),
(1, 'Banco de Dados', TRUE);

INSERT INTO Turma (disciplina_id, codigo_turma, ativo, numero_vagas, periodo, stat)
VALUES
(1, 'PROG1-A', TRUE, 40, 1, 1),
(2, 'BD-A', TRUE, 40, 1, 1);

INSERT INTO AtribuicaoProfessor (turma_id, professor_id)
VALUES
(1, 3),
(2, 3);

INSERT INTO Matricula (turma_id, aluno_id, ativo, stat)
VALUES
(1, 4, TRUE, 1),
(1, 5, TRUE, 1),
(2, 4, TRUE, 1);

INSERT INTO ProcessoAvaliativo (nome, ativo, periodo, stat)
VALUES
('Avaliação Docente 2025', TRUE, 1, 1);


INSERT INTO Formulario (processo_id, titulo, identificado, stat)
VALUES
(1, 'Avaliação da Turma PROG1-A', TRUE, 102);


INSERT INTO Questao (formulario_id, texto, tipo, obrigatoria)
VALUES
(1, 'O professor demonstra domínio do conteúdo?', 'obj', TRUE),
(1, 'Comente sobre os pontos fortes do professor.', 'disc', FALSE);

INSERT INTO Opcao (questao_id, texto, vf, respostavf, correta)
VALUES
(1, 'Excelente', FALSE, NULL, FALSE),
(1, 'Bom', FALSE, NULL, FALSE),
(1, 'Regular', FALSE, NULL, FALSE),
(1, 'Ruim', FALSE, NULL, FALSE);


INSERT INTO Peso (questao_id, opcao_id, peso)
VALUES
(1, 1, 1.0),
(1, 2, 0.8),
(1, 3, 0.5),
(1, 4, 0.2);

INSERT INTO Submissao (formulario_id, turma_id, usuario_id, nota)
VALUES
(1, 1, 4, 0),  -- Ana
(1, 1, 5, 0);  -- Bruno

INSERT INTO Resposta (submissao_id, questao_id, opcao_id, texto)
VALUES
(1, 1, 1, NULL),
(1, 2, NULL, 'Professor explica muito bem e dá exemplos.');

INSERT INTO Resposta (submissao_id, questao_id, opcao_id, texto)
VALUES
(2, 1, 3, NULL),
(2, 2, NULL, 'Acho que poderia melhorar a dinâmica das aulas.');

select * from usuario;
