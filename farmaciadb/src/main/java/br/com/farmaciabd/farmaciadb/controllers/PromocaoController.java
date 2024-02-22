package br.com.farmaciabd.farmaciadb.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.farmaciabd.farmaciadb.model.Promocao;
import br.com.farmaciabd.farmaciadb.repository.MedicamentoRepository;
import br.com.farmaciabd.farmaciadb.repository.PromocaoRepository;
import jakarta.validation.Valid;

@Controller
public class PromocaoController {

    @Autowired
    private PromocaoRepository pr;

    @Autowired
    private MedicamentoRepository mr;

    @GetMapping("/newPromocao")
    public String form(Model model) {
        model.addAttribute("medicamentos", mr.findAll());
        model.addAttribute("promocao", new Promocao());
        return "promocao/formPromocao";
    }

    @RequestMapping(value = "/newPromocao", method = RequestMethod.POST)
    public String form(@RequestParam("medicamentoId") long medicamentoId, @Valid Promocao promocao, BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/newPromocao";
        }

        // Buscar o medicamento no banco de dados pelo ID
        var medicamentoOptional = mr.findById(medicamentoId);
        if (medicamentoOptional.isPresent()) {
            var medicamento = medicamentoOptional.get();
            // Associar o medicamento à promoção
            promocao.setMedicamento(medicamento);
        } else {
            // Tratar o caso em que o medicamento não é encontrado
            attributes.addFlashAttribute("mensagem", "Medicamento não encontrado...");
            return "redirect:/newPromocao";
        }

        var dateInicio = promocao.getDataInicio().toLocalDate();
        var dataAtual = java.time.LocalDate.now();
        var dateFim = promocao.getDataFim().toLocalDate();
        if(dateFim.isBefore(dateInicio) || dateFim.isEqual(dateInicio)){
            attributes.addFlashAttribute("mensagem", "Data final deve ser maior que a data inicial...");
            return "redirect:/newPromocao";
        }
        if(dateInicio.isBefore(dataAtual)){
            attributes.addFlashAttribute("mensagem", "Data inicial deve ser maior ou igual a data atual...");
            return "redirect:/newPromocao";
        }

        pr.save(promocao);
        attributes.addFlashAttribute("mensagem", "Promoção criada com sucesso!");

        return "redirect:/promocoes";
    }

    // LIST
    @RequestMapping("/promocoes")
    public ModelAndView listPromocoes() {
        ModelAndView mv = new ModelAndView("promocao/listPromocao");
        Iterable<Promocao> promocoes = pr.findAll();
        mv.addObject("promocoes", promocoes);
        return mv;
    }

    // DELETE
    @RequestMapping("/deletePromocao/{id}")
    public String deletePromocao(@PathVariable("id") long id) {
        pr.deleteById(id);
        return "redirect:/promocoes";
    }


    // UPDATE
    @RequestMapping(value = "/editPromocao/{id}", method = RequestMethod.GET)
    public ModelAndView editPromocao(@PathVariable Long id) {
        Optional<Promocao> promocaoOptional = pr.findById(id);
        ModelAndView mv = new ModelAndView("promocao/editPromocao");
        promocaoOptional.ifPresent(promocao -> mv.addObject("promocao", promocao));
        mv.addObject("medicamentos", mr.findAll());
        return mv;
    }


    @RequestMapping(value = "/editPromocao/{id}", method = RequestMethod.POST)
    public String updatePromocao(@PathVariable Long id, @Valid Promocao promocao, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/editPromocao/" + id;
        }

        var promocaoExistente = pr.findById(id);

        if (promocaoExistente.isPresent()) {
            var promocaoAtualizada = promocaoExistente.get();
            promocaoAtualizada.setMedicamento(promocao.getMedicamento());
            promocaoAtualizada.setDesconto(promocao.getDesconto());
            promocaoAtualizada.setDataInicio(promocao.getDataInicio());
            promocaoAtualizada.setDataFim(promocao.getDataFim());

            pr.save(promocaoAtualizada);
            attributes.addFlashAttribute("mensagem", "Promoção atualizada com sucesso!");
        } else {
            attributes.addFlashAttribute("mensagem", "Promoção não encontrada");
        }

        return "redirect:/promocoes";
    }

}
