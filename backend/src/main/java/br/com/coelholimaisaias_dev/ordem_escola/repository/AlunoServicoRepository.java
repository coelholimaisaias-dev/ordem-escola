package br.com.coelholimaisaias_dev.ordem_escola.repository;

import br.com.coelholimaisaias_dev.ordem_escola.domain.AlunoServico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoServicoRepository extends JpaRepository<AlunoServico, Long> {
}
