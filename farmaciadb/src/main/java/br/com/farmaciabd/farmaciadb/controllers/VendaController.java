package br.com.farmaciabd.farmaciadb.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.farmaciabd.farmaciadb.dto.VendaFormularioDTO;
import br.com.farmaciabd.farmaciadb.dto.VendaMedicamentoDTO;
import lombok.AllArgsConstructor;
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
import br.com.farmaciabd.farmaciadb.repository.PromocaoRepository;
import jakarta.validation.Valid;

@AllArgsConstructor
@Controller
public class VendaController {

    private final VendaRepository vendaRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;
    private final PromocaoRepository promocaoRepository;

    @GetMapping("/newVenda")
    public String form(Model model) {
        List<Medicamento> medicamentos = (List<Medicamento>) medicamentoRepository.findAll();
        List<Cliente> clientes = (List<Cliente>) clienteRepository.findAll();
        List<Vendedor> vendedores = (List<Vendedor>) vendedorRepository.findAll();
        List<Promocao> promocoes = (List<Promocao>) promocaoRepository.findAll();
        List<VendaMedicamentoDTO> medicamentosVenda = (List<VendaMedicamentoDTO>) new ArrayList<VendaMedicamentoDTO>();
        
        model.addAttribute("venda", new Venda());
        model.addAttribute("medicamentos", medicamentos);
        model.addAttribute("clientes", clientes);
        model.addAttribute("vendedores", vendedores);
        model.addAttribute("promocoes", promocoes);
        model.addAttribute("medicamentosVenda", medicamentosVenda);
        
        return "venda/formVenda";
    }

    @PostMapping("/newVenda")
    public String form(@RequestBody VendaFormularioDTO vendaFormulario,
                       Venda venda,
                       RedirectAttributes attributes) {

        long clienteId = vendaFormulario.getClienteId();
        long vendedorId = vendaFormulario.getVendedorId();
        List<VendaMedicamentoDTO> medicamentosVenda = vendaFormulario.getMedicamentosVenda();

//        @PostMapping("/newVenda")
//        public String form(@RequestParam("clienteId") long clienteId,
//        @RequestParam("vendedorId") long vendedorId,
//        @RequestParam("medicamentosVenda") List<VendaMedicamentoDTO> medicamentosVenda,
//        Venda venda,
//        RedirectAttributes attributes) {



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

        List<Long> medicamentoIds = medicamentosVenda.stream()
                .map(VendaMedicamentoDTO::getMedicamentoId)
                .collect(Collectors.toList());

        Iterable<Medicamento> medicamentos = medicamentoRepository.findAllById(medicamentoIds);
        venda.setMedicamentos((List<Medicamento>) medicamentos);

        venda.calcularPrecoFinal();

        vendaRepository.save(venda);

        // Diminuir a quantidade do estoque dos medicamentos vendidos
        for (VendaMedicamentoDTO vendaMedicamentoDTO : medicamentosVenda) {
            Long medicamentoId = vendaMedicamentoDTO.getMedicamentoId();
            int quantidadeVendida = vendaMedicamentoDTO.getQuantidade();

            Medicamento medicamento = medicamentoRepository.findById(medicamentoId)
                    .orElseThrow(() -> new RuntimeException("Medicamento não encontrado"));

            int quantidadeDisponivel = medicamento.getQuantidade();
            if (quantidadeVendida > quantidadeDisponivel) {
                attributes.addFlashAttribute("mensagem", "Medicamento " + medicamento.getNome() + " não possui quantidade suficiente em estoque...");
                return "redirect:/newVenda";
            } else {
                medicamento.setQuantidade(quantidadeDisponivel - quantidadeVendida);
                medicamentoRepository.save(medicamento);
            }
        }

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
