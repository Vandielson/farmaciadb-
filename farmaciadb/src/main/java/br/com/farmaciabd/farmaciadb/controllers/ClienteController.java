package br.com.farmaciabd.farmaciadb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

        return "redirect:/clientes";
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
    @RequestMapping("/deleteCliente/{id}")
    public String deleteCliente(@PathVariable Long id) {
       cr.deleteById(id);
       return "redirect:/clientes";
    }

    // UPDATE
    @GetMapping("/formEditCliente/{id}")
    public ModelAndView editCliente(@PathVariable Long id) {
        var cliente = cr.findById(id);
        var mv = new ModelAndView();

        if(cliente.isPresent()){
            mv.addObject("cliente", cliente.get());
            mv.setViewName("cliente/editCliente");
        } else {
            mv.setViewName("redirect:/clientes");
            mv.addObject("mensagem", "Cliente não encontrado");
        }
        return mv;
    }

    @PostMapping("/editCliente/{id}")
    public String updateCliente(@PathVariable Long id, @Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/clientes";
        }

        var clienteExistente = cr.findById(id);

        if (clienteExistente.isPresent()) {

            var clienteAtualizado = clienteExistente.get();
            clienteAtualizado.setCpf(cliente.getCpf());
            clienteAtualizado.setNome(cliente.getNome());
            clienteAtualizado.setTelefone(cliente.getTelefone());

            cr.save(clienteAtualizado);
            attributes.addFlashAttribute("mensagem", "Cliente atualizado com sucesso!");
        } else {
            attributes.addFlashAttribute("mensagem", "Cliente não encontrado");
        }

        return "redirect:/clientes";
    }
}