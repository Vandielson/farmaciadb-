package br.com.farmaciabd.farmaciadb.controllers;

import br.com.farmaciabd.farmaciadb.model.Medicamento;
import br.com.farmaciabd.farmaciadb.model.Promocao;
import br.com.farmaciabd.farmaciadb.repository.MedicamentoRepository;
import br.com.farmaciabd.farmaciadb.repository.PromocaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Controller
public class RelatorioController {

    private final PromocaoRepository promocoesRepository;
    private final MedicamentoRepository medicamentoRepository;

    @GetMapping("/relatorios")
    public String form(Model model) {
        return "relatorios/index";
    }

//    @GetMapping("/relatorios/promocoes")
//    public String promocoes(Model model) {
//        return "relatorios/relatorioPromocoes";
//    }

    @GetMapping("/relatorios/promocoes")
    public ModelAndView promocoes(@RequestParam(name = "dataInicio", required = false) String dataInicio,
                            @RequestParam(name = "dataFim", required = false) String dataFim,
                            @RequestParam(name = "idMedicamento", required = false) Long idMedicamento) {

        ModelAndView model = new ModelAndView("relatorios/relatorioPromocoes");

        Date dataInicioDate = parseDate(dataInicio);
        Date dataFimDate = parseDate(dataFim);


        var promocoes = promocoesRepository.findByFilter(dataInicioDate, dataFimDate, idMedicamento);

        model.addObject("promocoes", promocoes);
        model.addObject("medicamentos", medicamentoRepository.findAll());

        return model;
    }

    private Date parseDate(String dateString) {
        if (dateString != null && !dateString.isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return dateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
