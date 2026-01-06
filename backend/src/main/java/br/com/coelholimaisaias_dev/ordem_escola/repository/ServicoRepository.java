package br.com.coelholimaisaias_dev.ordem_escola.repository;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicoRepository extends BaseRepository<Servico> {
}
