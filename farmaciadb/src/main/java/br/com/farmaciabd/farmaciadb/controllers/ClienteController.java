package br.com.farmaciabd.farmaciadb.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.farmaciabd.farmaciadb.model.Cliente;
import br.com.farmaciabd.farmaciadb.repository.ClienteRepository;
import jakarta.validation.Valid;

@Controller
public class ClienteController {

    @Autowired
    private ClienteRepository cr;

    // NEW
    @RequestMapping(value = "/newCliente", method = RequestMethod.GET)
    public String form() {
        return "cliente/formCliente";
    }

    @RequestMapping(value = "/newCliente", method = RequestMethod.POST)
    public String form(@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/newCliente";
        }

        cr.save(cliente);
        attributes.addFlashAttribute("mensagem", "Cliente criado com sucesso!");

        return "redirect:/newCliente";
    }

    // LIST
    @RequestMapping("/clientes")
    public ModelAndView listClientes() {
        ModelAndView mv = new ModelAndView("cliente/listCliente");
        Iterable<Cliente> clientes = cr.findAll();
        mv.addObject("clientes", clientes);
        return mv;
    }

    // DELETE
    @RequestMapping("/deleteCliente")
    public String deleteCliente(long id) {
       cr.deleteById(id);
       return "redirect:/clientes";
    }

    // UPDATE
    @RequestMapping(value = "/editCliente", method = RequestMethod.GET)
    public ModelAndView editCliente(long id) {
        Optional<Cliente> cliente = cr.findById(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("cliente", cliente.orElse(null));
        mv.setViewName("cliente/editCliente");
        return mv;
    }

    @RequestMapping(value = "/editCliente", method = RequestMethod.POST)
    public String updateCliente(@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/clientes";
        }

        cr.save(cliente);
        attributes.addFlashAttribute("mensagem", "Cliente atualizado com sucesso!");
        return "redirect:/clientes";
    }
}