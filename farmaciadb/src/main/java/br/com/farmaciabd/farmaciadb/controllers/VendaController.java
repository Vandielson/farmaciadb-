package br.com.farmaciabd.farmaciadb.controllers;

import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.com.farmaciabd.farmaciadb.model.Venda;
import br.com.farmaciabd.farmaciadb.repository.VendaRepository;

@Controller
public class VendaController {

	@Autowired
    private VendaRepository vendaRepository;

    // NEW
    @RequestMapping(value = "/newVenda", method = RequestMethod.GET)
    public String form() {
        return "venda/formVenda";
    }

    @RequestMapping(value = "/newVenda", method = RequestMethod.POST)
    public String form(@Valid Venda venda, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/newVenda";
        }

        vendaRepository.save(venda);
        attributes.addFlashAttribute("mensagem", "Venda criada com sucesso!");
        return "redirect:/newVenda";
    }

    // LIST
    @RequestMapping("/vendas")
    public ModelAndView listVendas() {
        ModelAndView mv = new ModelAndView("venda/listVenda");
        Iterable<Venda> vendas = vendaRepository.findAll();
        mv.addObject("vendas", vendas);
        return mv;
    }

    // DELETE
    @RequestMapping("/deleteVenda")
    public String deleteVenda(long id) {
        vendaRepository.deleteById(id);
        return "redirect:/vendas";
    }

    // UPDATE
    @RequestMapping(value = "/editVenda", method = RequestMethod.GET)
    public ModelAndView editVenda(long id) {
        ModelAndView mv = new ModelAndView("venda/formVenda");
        Optional<Venda> venda = vendaRepository.findById(id);
        mv.addObject("venda", venda);
        return mv;
    }

    @RequestMapping(value = "/editVenda", method = RequestMethod.POST)
    public String updateVenda(@Valid Venda venda, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/editVenda";
        }

        vendaRepository.save(venda);
        attributes.addFlashAttribute("mensagem", "Venda atualizada com sucesso!");
        return "redirect:/vendas";
    }
}
