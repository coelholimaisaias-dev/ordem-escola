package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Turma;
import br.com.coelholimaisaias_dev.ordem_escola.repository.TurmaRepository;
import org.springframework.stereotype.Service;

@Service
public class TurmaService extends BaseService<Turma> {

    public TurmaService(TurmaRepository repository) {
        super(repository);
    }
}
