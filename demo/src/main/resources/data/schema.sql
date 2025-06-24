DROP TABLE IF EXISTS Mensagens;
DROP TABLE IF EXISTS Avaliacoes;
DROP TABLE IF EXISTS Turma_Estudantes;
DROP TABLE IF EXISTS Disciplina_PreRequisitos;
DROP TABLE IF EXISTS Turmas;
DROP TABLE IF EXISTS Disciplinas;
DROP TABLE IF EXISTS Professores;
DROP TABLE IF EXISTS Estudantes;
DROP TABLE IF EXISTS Usuarios;

CREATE TABLE Usuarios (
    id INTEGER PRIMARY KEY,
    nome TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    senha TEXT NOT NULL
);

CREATE TABLE Estudantes (
    id INTEGER PRIMARY KEY, 
    matriculaEstudante TEXT UNIQUE NOT NULL,
    FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
);

CREATE TABLE Professores (
    id INTEGER PRIMARY KEY, -- This will also be the foreign key to Usuarios
    matriculaProfessor TEXT UNIQUE NOT NULL,
    FOREIGN KEY (id) REFERENCES Usuarios(id) ON DELETE CASCADE
);

CREATE TABLE Disciplinas (
    id INTEGER PRIMARY KEY,
    codigo TEXT UNIQUE NOT NULL,
    nome TEXT NOT NULL,
    numeroDeCreditos INTEGER NOT NULL
);

CREATE TABLE Disciplina_PreRequisitos (
    disciplina_id INTEGER NOT NULL,
    prerequisito_id INTEGER NOT NULL,
    PRIMARY KEY (disciplina_id, prerequisito_id),
    FOREIGN KEY (disciplina_id) REFERENCES Disciplinas(id) ON DELETE CASCADE,
    FOREIGN KEY (prerequisito_id) REFERENCES Disciplinas(id) ON DELETE CASCADE
);

CREATE TABLE Turmas (
    id INTEGER PRIMARY KEY,
    sala TEXT NOT NULL,
    horario TEXT NOT NULL, -- Consider a more specific type like TIME or DATETIME if formats are consistent
    codigo TEXT UNIQUE NOT NULL,
    disciplina_id INTEGER NOT NULL,
    professor_id INTEGER NOT NULL,
    FOREIGN KEY (disciplina_id) REFERENCES Disciplinas(id) ON DELETE CASCADE,
    FOREIGN KEY (professor_id) REFERENCES Professores(id) ON DELETE CASCADE
);

CREATE TABLE Turma_Estudantes (
    turma_id INTEGER NOT NULL,
    estudante_id INTEGER NOT NULL,
    PRIMARY KEY (turma_id, estudante_id),
    FOREIGN KEY (turma_id) REFERENCES Turmas(id) ON DELETE CASCADE,
    FOREIGN KEY (estudante_id) REFERENCES Estudantes(id) ON DELETE CASCADE
);

CREATE TABLE Avaliacoes (
    id INTEGER PRIMARY KEY,
    nota REAL NOT NULL CHECK (nota >= 0 AND nota <= 5),
    comentario TEXT,
    data TEXT NOT NULL, -- Storing LocalDate as ISO 8601 string (YYYY-MM-DD)
    titulo TEXT NOT NULL,
    estudante_id INTEGER NOT NULL,
    turma_id INTEGER NOT NULL,
    tag TEXT NOT NULL, -- Will store the enum name (e.g., 'RECLAMACAO', 'ELOGIO', 'DICA')
    FOREIGN KEY (estudante_id) REFERENCES Estudantes(id) ON DELETE CASCADE,
    FOREIGN KEY (turma_id) REFERENCES Turmas(id) ON DELETE CASCADE
);

CREATE TABLE Mensagens (
    id INTEGER PRIMARY KEY,
    conteudo TEXT NOT NULL,
    dataHora TEXT NOT NULL, 
    remetente_id INTEGER NOT NULL,
    mensagemPai_id INTEGER, -- Self-referencing foreign key for replies, NULLable
    FOREIGN KEY (remetente_id) REFERENCES Usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (mensagemPai_id) REFERENCES Mensagens(id) ON DELETE SET NULL -- If parent message is deleted, set this to NULL
);
