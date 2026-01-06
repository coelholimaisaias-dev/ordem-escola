package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Matricula;
import br.com.coelholimaisaias_dev.ordem_escola.service.MatriculaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController extends BaseController<Matricula> {

    public MatriculaController(MatriculaService service) {
        super(service);
    }
}
