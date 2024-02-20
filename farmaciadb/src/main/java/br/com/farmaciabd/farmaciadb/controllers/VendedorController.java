package br.com.farmaciabd.farmaciadb.controllers;

import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
        
        return "redirect:/vendedores";
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
    @RequestMapping("/deleteVendedor/{id}")
    public String deleteVendedor(@PathVariable Long id) {
        vr.deleteById(id);
        return "redirect:/vendedores";
    }

    // UPDATE
    @GetMapping("/formEditVendedor/{id}")
    public ModelAndView editVendedor(@PathVariable Long id) {
        var vendedor = vr.findById(id);
        var mv = new ModelAndView();

        if (vendedor.isPresent()) {
            mv.addObject("vendedor", vendedor.get());
            mv.setViewName("vendedor/editVendedor");
        } else {
            mv.setViewName("redirect:/vendedores");
            mv.addObject("mensagem", "Vendedor não encontrado!");
        }

        return mv;
    }

    @PostMapping("/editVendedor/{id}")
    public String updateVendedor(@PathVariable Long id, @Valid Vendedor vendedor, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/editVendedor";
        }

        var vendedorExistente = vr.findById(id);

        if(vendedorExistente.isPresent()) {

            var vendedorAtualizado = vendedorExistente.get();
            vendedorAtualizado.setCpf(vendedor.getCpf());
            vendedorAtualizado.setNome(vendedor.getNome());
            vendedorAtualizado.setPercentualComissao(vendedor.getPercentualComissao());
            vendedorAtualizado.setSalario(vendedor.getSalario());
            vendedorAtualizado.setTelefone(vendedor.getTelefone());

            vr.save(vendedorAtualizado);
            attributes.addFlashAttribute("mensagem", "Vendedor atualizado com sucesso!");
        }else{
            attributes.addFlashAttribute("mensagem", "Vendedor não encontrado!");
        }

        return "redirect:/vendedores";
    }
}