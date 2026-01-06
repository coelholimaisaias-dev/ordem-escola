package br.com.coelholimaisaias_dev.ordem_escola.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class Auditable implements Identifiable {

    @CreatedBy
    @Column(name = "criado_por", length = 255)
    private String criadoPor;

    @CreatedDate
    @Column(name = "criado_em")
    private Instant criadoEm;

    @LastModifiedBy
    @Column(name = "atualizado_por", length = 255)
    private String atualizadoPor;

    @LastModifiedDate
    @Column(name = "atualizado_em")
    private Instant atualizadoEm;

    @Column(nullable = false)
    private Boolean ativo = true; // delete lógico
}
