-- V1 - Estrutura inicial do domínio
-- Cria: empresa, usuario (ADMIN/CLIENTE), aluno (vinculado a usuario),
-- turma, servico, matricula e aluno_servico

CREATE TABLE IF NOT EXISTS empresa (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(50),
    criado_em TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS usuario (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT REFERENCES empresa(id) ON DELETE CASCADE,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(20) NOT NULL CHECK (perfil IN ('ADMIN','CLIENTE')),
    criado_em TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Um usuário pode ser também um aluno; usamos tabela separada para dados específicos
CREATE TABLE IF NOT EXISTS aluno (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE REFERENCES usuario(id) ON DELETE CASCADE,
    data_nascimento DATE,
    criado_em TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS turma (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT REFERENCES empresa(id) ON DELETE CASCADE,
    nome VARCHAR(255) NOT NULL,
    turno VARCHAR(20) NOT NULL CHECK (turno IN ('MANHA','TARDE','INTEGRAL')),
    valor_base NUMERIC(12,2) NOT NULL DEFAULT 0,
    capacidade INTEGER,
    criado_em TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS servico (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT REFERENCES empresa(id) ON DELETE CASCADE,
    nome VARCHAR(255) NOT NULL,
    turno VARCHAR(20) NOT NULL CHECK (turno IN ('MANHA','TARDE','INTEGRAL')),
    valor_base NUMERIC(12,2) NOT NULL DEFAULT 0,
    criado_em TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Matrícula de aluno em turma (um aluno pode ter histórico de matrículas)
CREATE TABLE IF NOT EXISTS matricula (
    id BIGSERIAL PRIMARY KEY,
    aluno_id BIGINT NOT NULL REFERENCES aluno(id) ON DELETE CASCADE,
    turma_id BIGINT REFERENCES turma(id) ON DELETE SET NULL,
    data_matricula TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_cancelamento TIMESTAMP WITH TIME ZONE
);

-- Associação entre aluno e serviços (um aluno pode contratar vários serviços independentes da turma)
CREATE TABLE IF NOT EXISTS aluno_servico (
    id BIGSERIAL PRIMARY KEY,
    aluno_id BIGINT NOT NULL REFERENCES aluno(id) ON DELETE CASCADE,
    servico_id BIGINT NOT NULL REFERENCES servico(id) ON DELETE CASCADE,
    data_inicio TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Índices úteis
CREATE INDEX IF NOT EXISTS idx_usuario_empresa ON usuario(empresa_id);
CREATE INDEX IF NOT EXISTS idx_turma_empresa ON turma(empresa_id);
CREATE INDEX IF NOT EXISTS idx_servico_empresa ON servico(empresa_id);
CREATE INDEX IF NOT EXISTS idx_matricula_aluno ON matricula(aluno_id);
CREATE INDEX IF NOT EXISTS idx_aluno_servico_aluno ON aluno_servico(aluno_id);

-- Observação: parte financeira será implementada em migrations futuras
