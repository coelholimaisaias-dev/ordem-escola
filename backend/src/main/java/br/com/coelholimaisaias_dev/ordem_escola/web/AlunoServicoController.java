package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.domain.AlunoServico;
import br.com.coelholimaisaias_dev.ordem_escola.service.AlunoServicoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aluno-servicos")
public class AlunoServicoController extends BaseController<AlunoServico> {

    public AlunoServicoController(AlunoServicoService service) {
        super(service);
    }
}
