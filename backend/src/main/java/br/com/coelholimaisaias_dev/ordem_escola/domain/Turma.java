package br.com.coelholimaisaias_dev.ordem_escola.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "turma")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Turma extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Turno turno;

    @Column(name = "valor_base", precision = 12, scale = 2)
    private BigDecimal valorBase;

    private Integer capacidade;

    @Column(nullable = false)
    private Boolean ativo = true;  // delete lógico
}
