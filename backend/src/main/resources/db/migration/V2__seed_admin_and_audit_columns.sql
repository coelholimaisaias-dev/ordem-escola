-- V2 - Adiciona colunas de auditoria e cria um usuário admin para login

-- Adiciona colunas de auditoria nas tabelas
ALTER TABLE empresa ADD COLUMN IF NOT EXISTS criado_por VARCHAR(255);
ALTER TABLE empresa ADD COLUMN IF NOT EXISTS atualizado_por VARCHAR(255);
ALTER TABLE empresa ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMP WITH TIME ZONE;

ALTER TABLE usuario ADD COLUMN IF NOT EXISTS criado_por VARCHAR(255);
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS atualizado_por VARCHAR(255);
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMP WITH TIME ZONE;

ALTER TABLE aluno ADD COLUMN IF NOT EXISTS criado_por VARCHAR(255);
ALTER TABLE aluno ADD COLUMN IF NOT EXISTS atualizado_por VARCHAR(255);
ALTER TABLE aluno ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMP WITH TIME ZONE;

ALTER TABLE turma ADD COLUMN IF NOT EXISTS criado_por VARCHAR(255);
ALTER TABLE turma ADD COLUMN IF NOT EXISTS atualizado_por VARCHAR(255);
ALTER TABLE turma ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMP WITH TIME ZONE;

ALTER TABLE servico ADD COLUMN IF NOT EXISTS criado_por VARCHAR(255);
ALTER TABLE servico ADD COLUMN IF NOT EXISTS atualizado_por VARCHAR(255);
ALTER TABLE servico ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMP WITH TIME ZONE;

ALTER TABLE matricula ADD COLUMN IF NOT EXISTS criado_por VARCHAR(255);
ALTER TABLE matricula ADD COLUMN IF NOT EXISTS atualizado_por VARCHAR(255);
ALTER TABLE matricula ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMP WITH TIME ZONE;

ALTER TABLE aluno_servico ADD COLUMN IF NOT EXISTS criado_por VARCHAR(255);
ALTER TABLE aluno_servico ADD COLUMN IF NOT EXISTS atualizado_por VARCHAR(255);
ALTER TABLE aluno_servico ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMP WITH TIME ZONE;

-- Seed: empresa e usuario admin
INSERT INTO empresa (nome, cnpj, criado_em, criado_por)
VALUES ('Empresa Teste', '00.000.000/0001-00', NOW(), 'system')
ON CONFLICT (id) DO NOTHING;

-- Insere usuário admin (senha em texto simples por enquanto)
INSERT INTO usuario (empresa_id, nome, email, senha, perfil, criado_em, criado_por)
VALUES (
  (SELECT id FROM empresa WHERE nome = 'Empresa Teste' LIMIT 1),
  'Administrador',
  'admin@empresa.test',
  'admin',
  'ADMIN',
  NOW(),
  'system'
)
ON CONFLICT (email) DO NOTHING;
