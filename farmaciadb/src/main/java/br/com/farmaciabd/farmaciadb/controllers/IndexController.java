package br.com.farmaciabd.farmaciadb.controllers;

import br.com.farmaciabd.farmaciadb.model.Promocao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String form(Model model) {
        return "index";
    }

}
