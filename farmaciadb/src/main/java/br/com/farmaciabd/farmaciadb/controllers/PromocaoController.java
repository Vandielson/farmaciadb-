package br.com.farmaciabd.farmaciadb.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.farmaciabd.farmaciadb.model.Medicamento;
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
        Optional<Medicamento> medicamentoOptional = mr.findById(medicamentoId);
        if (medicamentoOptional.isPresent()) {
            Medicamento medicamento = medicamentoOptional.get();
            // Associar o medicamento à promoção
            promocao.setMedicamento(medicamento);
        } else {
            // Tratar o caso em que o medicamento não é encontrado
            attributes.addFlashAttribute("mensagem", "Medicamento não encontrado...");
            return "redirect:/newPromocao";
        }

        pr.save(promocao);
        attributes.addFlashAttribute("mensagem", "Promoção criada com sucesso!");

        return "redirect:/newPromocao";
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
    @RequestMapping("/deletePromocao")
    public String deletePromocao(long id) {
        pr.deleteById(id);
        return "redirect:/promocoes";
    }

    // UPDATE
    @RequestMapping(value = "/editPromocao", method = RequestMethod.GET)
    public ModelAndView editPromocao(long id) {
        Optional<Promocao> promocao = pr.findById(id);
        ModelAndView mv = new ModelAndView("promocao/formPromocao");
        mv.addObject("promocao", promocao.orElse(null));
        mv.setViewName("promocao/editPromocao");
        return mv;
    }

    @RequestMapping(value = "/editPromocao", method = RequestMethod.POST)
    public String updatePromocao(@Valid Promocao promocao, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/editPromocao";
        }

        pr.save(promocao);
        attributes.addFlashAttribute("mensagem", "Promoção atualizada com sucesso!");
        return "redirect:/promocoes";
    }
}