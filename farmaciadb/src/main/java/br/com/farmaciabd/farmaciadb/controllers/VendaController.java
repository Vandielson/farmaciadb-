package br.com.farmaciabd.farmaciadb.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.farmaciabd.farmaciadb.model.Cliente;
import br.com.farmaciabd.farmaciadb.model.Medicamento;
import br.com.farmaciabd.farmaciadb.model.Venda;
import br.com.farmaciabd.farmaciadb.model.Vendedor;
import br.com.farmaciabd.farmaciadb.model.Promocao;
import br.com.farmaciabd.farmaciadb.repository.ClienteRepository;
import br.com.farmaciabd.farmaciadb.repository.MedicamentoRepository;
import br.com.farmaciabd.farmaciadb.repository.VendaRepository;
import br.com.farmaciabd.farmaciadb.repository.VendedorRepository;
import jakarta.validation.Valid;
import br.com.farmaciabd.farmaciadb.repository.PromocaoRepository;


@Controller
public class VendaController {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendedorRepository vendedorRepository;
    
    @Autowired
    private PromocaoRepository promocaoRepository;

    @GetMapping("/newVenda")
    public String form(Model model) {
        List<Medicamento> medicamentos = (List<Medicamento>) medicamentoRepository.findAll();
        List<Cliente> clientes = (List<Cliente>) clienteRepository.findAll();
        List<Vendedor> vendedores = (List<Vendedor>) vendedorRepository.findAll();
        List<Promocao> promocoes = (List<Promocao>) promocaoRepository.findAll();
        
        model.addAttribute("venda", new Venda());
        model.addAttribute("medicamentos", medicamentos);
        model.addAttribute("clientes", clientes);
        model.addAttribute("vendedores", vendedores);
        model.addAttribute("promocoes", promocoes);
        
        return "venda/formVenda";
    }

    @PostMapping("/newVenda")
    public String form(@RequestParam("clienteId") long clienteId,
                       @RequestParam("vendedorId") long vendedorId,
                       @RequestParam("medicamentoIds") List<Long> medicamentoIds,
                       Venda venda,
                       RedirectAttributes attributes) {

        var clienteOptional = clienteRepository.findById(clienteId);
        if (clienteOptional.isPresent()) {
            var cliente = clienteOptional.get();
            venda.setCliente(cliente);
        } else {
            attributes.addFlashAttribute("mensagem", "Cliente não encontrado...");
            return "redirect:/newVenda";
        }

        var vendedorOptional = vendedorRepository.findById(vendedorId);
        if (vendedorOptional.isPresent()) {
            var vendedor = vendedorOptional.get();
            venda.setVendedor(vendedor);
        } else {
            attributes.addFlashAttribute("mensagem", "Vendedor não encontrado...");
            return "redirect:/newVenda";
        }

        Iterable<Medicamento> medicamentos = medicamentoRepository.findAllById(medicamentoIds);
        venda.setMedicamentos(medicamentos);

        venda.calcularPrecoFinal();

        vendaRepository.save(venda);

        attributes.addFlashAttribute("mensagem", "Venda criada com sucesso!");
        return "redirect:/vendas";
    }

    @GetMapping("/vendas")
    public ModelAndView listVendas() {
        ModelAndView mv = new ModelAndView("venda/listVenda");
        Iterable<Venda> vendas = vendaRepository.findAll();
        mv.addObject("vendas", vendas);
        return mv;
    }

    @GetMapping("/deleteVenda/{id}")
    public String deleteVenda(@PathVariable("id") long id) {
        vendaRepository.deleteById(id);
        return "redirect:/vendas";
    }

    @GetMapping("/editVenda/{id}")
    public ModelAndView editVenda(@PathVariable("id") long id) {
        ModelAndView mv = new ModelAndView("venda/editVenda");
        Venda venda = vendaRepository.findById(id).orElse(null);
        mv.addObject("venda", venda);
        List<Medicamento> medicamentos = (List<Medicamento>) medicamentoRepository.findAll();
        List<Cliente> clientes = (List<Cliente>) clienteRepository.findAll();
        List<Vendedor> vendedores = (List<Vendedor>) vendedorRepository.findAll();
        mv.addObject("medicamentos", medicamentos);
        mv.addObject("clientes", clientes);
        mv.addObject("vendedores", vendedores);
        return mv;
    }

    @PostMapping("/editVenda/{id}")
    public String updateVenda(@PathVariable Long id, @Valid Venda venda, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/editVenda/" + id;
        }

        Optional<Venda> vendaExistenteOptional = vendaRepository.findById(id);

        if (vendaExistenteOptional.isPresent()) {
            Venda vendaExistente = vendaExistenteOptional.get();
            vendaExistente.setCliente(venda.getCliente());
            vendaExistente.setVendedor(venda.getVendedor());
            vendaExistente.setPromocao(venda.getPromocao());
            vendaExistente.setMedicamentos(venda.getMedicamentos());
            
            vendaExistente.calcularPrecoFinal();

            vendaRepository.save(vendaExistente);
            attributes.addFlashAttribute("mensagem", "Venda atualizada com sucesso!");
        } else {
            attributes.addFlashAttribute("mensagem", "Venda não encontrada");
        }

        return "redirect:/vendas";
    }

}
