package br.com.coelholimaisaias_dev.ordem_escola.repository;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends BaseRepository<Aluno> {
}
