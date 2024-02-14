package br.com.farmaciabd.farmaciadb.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.farmaciabd.farmaciadb.model.Promocao;
import br.com.farmaciabd.farmaciadb.repository.PromocaoRepository;
import jakarta.validation.Valid;

@Controller
public class PromocaoController {

	@Autowired
    private PromocaoRepository pr;
    
    // NEW
    @RequestMapping(value = "/newPromocao", method = RequestMethod.GET)
    public String form() {
        return "promocao/formPromocao";
    }

    @RequestMapping(value = "/newPromocao", method = RequestMethod.POST)
    public String form(@Valid Promocao promocao, BindingResult result, RedirectAttributes attributes) {
      
    	if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
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
        ModelAndView mv = new ModelAndView("promocao/formPromocao");
        Optional<Promocao> promocao = pr.findById(id);
        mv.addObject("promocao", promocao.orElse(null));
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