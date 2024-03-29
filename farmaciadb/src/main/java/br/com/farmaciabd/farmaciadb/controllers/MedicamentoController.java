package br.com.farmaciabd.farmaciadb.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.farmaciabd.farmaciadb.model.Cliente;
import br.com.farmaciabd.farmaciadb.model.Medicamento;
import br.com.farmaciabd.farmaciadb.repository.MedicamentoRepository;
import jakarta.validation.Valid;

@Controller
public class MedicamentoController {

	@Autowired
	private MedicamentoRepository mr;
	
	// NEW
	@RequestMapping(value = "/newMedicamento", method = RequestMethod.GET)
	public String form() {
		return "medicamento/formMedicamento";
	}

	@RequestMapping(value = "/newMedicamento", method = RequestMethod.POST)
	public String form(@Valid Medicamento medicamento, BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos...");
			return "redirect:/newMedicamento";
		}

		mr.save(medicamento);
		attributes.addFlashAttribute("mensagem", "Medicamento criado com sucesso!");

		return "redirect:/medicamentos";
	}

	// LIST
	@RequestMapping("/medicamentos")
	public ModelAndView listMedicamentos() {
		ModelAndView mv = new ModelAndView("medicamento/listMedicamento");
		Iterable<Medicamento> medicamentos = mr.findAll();
		mv.addObject("medicamentos", medicamentos);
		return mv;
	}

	// DELETE
	@RequestMapping("/deleteMedicamento/{id}")
	public String deleteMedicamento(@PathVariable Long id) {
	    mr.deleteById(id);
	    return "redirect:/medicamentos";
	}

	
    // UPDATE
    @GetMapping("/formEditMedicamento/{id}")
    public ModelAndView editMedicamento(@PathVariable Long id) {
        var medicamento = mr.findById(id);
        var mv = new ModelAndView();

		if(medicamento.isPresent()) {
			mv.addObject("medicamento", medicamento.get());
			mv.setViewName("medicamento/editMedicamento");
		}else {
			mv.setViewName("redirect:/medicamentos");
			mv.addObject("mensagem", "Medicamento não encontrado!");
		}
		return mv;
	}

    @PostMapping("/editMedicamento/{id}")
    public String updateMedicamento(@PathVariable Long id, @Valid Medicamento medicamento, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/medicamentos";
        }

		var medicamentoExistente = mr.findById(id);

		if (medicamentoExistente.isPresent()) {

			var medicamentoAtualizado = medicamentoExistente.get();
			medicamentoAtualizado.setNome(medicamento.getNome());
			medicamentoAtualizado.setDosagem(medicamento.getDosagem());
			medicamentoAtualizado.setPreco(medicamento.getPreco());
			medicamentoAtualizado.setQuantidade(medicamento.getQuantidade());

			mr.save(medicamentoAtualizado);
			attributes.addFlashAttribute("mensagem", "Medicamento atualizado com sucesso!");
		} else {
			attributes.addFlashAttribute("mensagem", "Medicamento não encontrado!");
		}

		return "redirect:/medicamentos";
    }

	
}
