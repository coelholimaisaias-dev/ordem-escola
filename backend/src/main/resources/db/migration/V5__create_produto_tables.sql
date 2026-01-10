-- V5 - Cria tabela de produtos

-- Tabela de produtos (fardas, livros, materiais escolares, etc)
CREATE TABLE IF NOT EXISTS produto (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT REFERENCES empresa(id) ON DELETE CASCADE,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    tipo VARCHAR(50) NOT NULL CHECK (tipo IN ('FARDA','LIVRO','MATERIAL_ESCOLAR','OUTRO')),
    valor_unitario NUMERIC(12,2) NOT NULL DEFAULT 0,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_por VARCHAR(255),
    criado_em TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    atualizado_por VARCHAR(255),
    atualizado_em TIMESTAMP WITH TIME ZONE
);

-- Índices úteis
CREATE INDEX IF NOT EXISTS idx_produto_empresa ON produto(empresa_id);
CREATE INDEX IF NOT EXISTS idx_produto_tipo ON produto(tipo);

-- Comentários
COMMENT ON TABLE produto IS 'Produtos oferecidos pelas escolas (fardas, livros, materiais escolares, etc)';
COMMENT ON COLUMN produto.tipo IS 'Tipo do produto: FARDA, LIVRO, MATERIAL_ESCOLAR ou OUTRO';
