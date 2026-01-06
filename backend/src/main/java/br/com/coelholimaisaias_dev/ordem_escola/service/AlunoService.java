package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Aluno;
import br.com.coelholimaisaias_dev.ordem_escola.repository.AlunoRepository;
import org.springframework.stereotype.Service;

@Service
public class AlunoService extends BaseService<Aluno> {

    public AlunoService(AlunoRepository repository) {
        super(repository);
    }
}
