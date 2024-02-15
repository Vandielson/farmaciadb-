package br.com.farmaciabd.farmaciadb.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

		return "redirect:/newMedicamento";
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
	@RequestMapping("/deleteMedicamento")
	public String deleteMedicamento(long id) {
	    mr.deleteById(id);
	    return "redirect:/medicamentos";
	}

	
    // UPDATE
    @RequestMapping(value = "/editMedicamento", method = RequestMethod.GET)
    public ModelAndView editMedicamento(long id) {
        Optional<Medicamento> medicamento = mr.findById(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("medicamento", medicamento.orElse(null));
        mv.setViewName("medicamento/editMedicamento");
        return mv;
    }

    @RequestMapping(value = "/editMedicamento", method = RequestMethod.POST)
    public String updateMedicamento(@Valid Medicamento medicamento, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/medicamentos";
        }

        mr.save(medicamento);
        attributes.addFlashAttribute("mensagem", "Medicamento atualizado com sucesso!");
        return "redirect:/medicamentos";
    }

	
}
