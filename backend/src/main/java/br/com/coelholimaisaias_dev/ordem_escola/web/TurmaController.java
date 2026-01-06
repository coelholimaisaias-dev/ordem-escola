package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Turma;
import br.com.coelholimaisaias_dev.ordem_escola.service.TurmaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/turmas")
public class TurmaController extends BaseController<Turma> {

    public TurmaController(TurmaService service) {
        super(service);
    }
}
