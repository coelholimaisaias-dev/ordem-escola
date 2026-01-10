export enum TipoProduto {
  FARDA = 'FARDA',
  LIVRO = 'LIVRO',
  MATERIAL_ESCOLAR = 'MATERIAL_ESCOLAR',
  OUTRO = 'OUTRO'
}

export const TipoProdutoLabels: Record<TipoProduto, string> = {
  [TipoProduto.FARDA]: 'Farda',
  [TipoProduto.LIVRO]: 'Livro',
  [TipoProduto.MATERIAL_ESCOLAR]: 'Material Escolar',
  [TipoProduto.OUTRO]: 'Outro'
};
