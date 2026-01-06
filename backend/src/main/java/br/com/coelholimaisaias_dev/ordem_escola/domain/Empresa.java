package br.com.coelholimaisaias_dev.ordem_escola.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "empresa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa extends BaseEntity {

    private String nome;

    private String cnpj;

    @Column(nullable = false)
    private Boolean ativo = true;  // delete lógico
}

