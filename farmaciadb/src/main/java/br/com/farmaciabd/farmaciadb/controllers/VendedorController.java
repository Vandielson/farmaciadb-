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

import br.com.farmaciabd.farmaciadb.model.Vendedor;
import br.com.farmaciabd.farmaciadb.repository.VendedorRepository;
import ch.qos.logback.core.model.Model;

@Controller
public class VendedorController {

	@Autowired
    private VendedorRepository vr;

    // NEW
    @RequestMapping(value = "/newVendedor", method = RequestMethod.GET)
    public String form() {
        return "vendedor/formVendedor";
    }

    @RequestMapping(value = "/newVendedor", method = RequestMethod.POST)
    public String form(@Valid Vendedor vendedor, BindingResult result, RedirectAttributes attributes) {
       
    	if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/newVendedor";
        }

        vr.save(vendedor);
        attributes.addFlashAttribute("mensagem", "Vendedor criado com sucesso!");
        
        return "redirect:/newVendedor";
    }

    // LIST
    @RequestMapping("/vendedores")
    public ModelAndView listVendedores() {
        ModelAndView mv = new ModelAndView("vendedor/listVendedor");
        Iterable<Vendedor> vendedores = vr.findAll();
        mv.addObject("vendedores", vendedores);
        return mv;
    }

    // DELETE
    @RequestMapping("/deleteVendedor")
    public String deleteVendedor(long id) {
        vr.deleteById(id);
        return "redirect:/vendedores";
    }

    // UPDATE
    @RequestMapping(value = "/editVendedor", method = RequestMethod.GET)
    public ModelAndView editVendedor(long id) {
        Optional<Vendedor> vendedor = vr.findById(id);
        ModelAndView mv = new ModelAndView("vendedor/formVendedor");
        mv.addObject("vendedor", vendedor.orElse(new Vendedor()));
        mv.setViewName("vendedor/editVendedor");
        return mv;
    }

    @RequestMapping(value = "/editVendedor", method = RequestMethod.POST)
    public String updateVendedor(@Valid Vendedor vendedor, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/editVendedor";
        }

        vr.save(vendedor);
        attributes.addFlashAttribute("mensagem", "Vendedor atualizado com sucesso!");
        return "redirect:/vendedores";
    }
}