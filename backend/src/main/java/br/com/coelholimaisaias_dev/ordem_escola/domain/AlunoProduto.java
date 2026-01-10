package br.com.coelholimaisaias_dev.ordem_escola.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "aluno_produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlunoProduto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "valor_total", precision = 12, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "data_compra")
    private Instant dataCompra;

    @Column(nullable = false)
    private Boolean ativo = true;
}
