package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Aluno;
import br.com.coelholimaisaias_dev.ordem_escola.service.AlunoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alunos")
public class AlunoController extends BaseController<Aluno> {

    public AlunoController(AlunoService service) {
        super(service);
    }
}
