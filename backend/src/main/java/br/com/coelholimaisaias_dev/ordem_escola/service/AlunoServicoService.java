package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.AlunoServico;
import br.com.coelholimaisaias_dev.ordem_escola.repository.AlunoServicoRepository;
import org.springframework.stereotype.Service;

@Service
public class AlunoServicoService extends BaseService<AlunoServico> {

    public AlunoServicoService(AlunoServicoRepository repository) {
        super(repository);
    }
}
