package com.evaluacionPermanente.PA4.controller.admin;

import com.evaluacionPermanente.PA4.model.Karateca;
import com.evaluacionPermanente.PA4.repository.AcademiaRepository;
import com.evaluacionPermanente.PA4.repository.KaratecaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/karatecas")
public class KaratecaController {
    @Autowired
    private KaratecaRepository karatecaRepository;

    @Autowired
    private AcademiaRepository academiaRepository;

    // LISTAR
    @GetMapping({"","index"})
    public String index(Model model,@PageableDefault(size = 10) Pageable pageable) {
        model.addAttribute("karatecas", karatecaRepository.findAll(pageable));
        return "/admin/karatecas/index";
    }

    // FORMULARIO NUEVO
    @GetMapping("/registrar")
    public String registrar(Model model) {
        model.addAttribute("karateca", new Karateca());
        model.addAttribute("academias", academiaRepository.findAll());
        return "admin/karatecas/registrar";
    }

    @PostMapping("/registrar")
    public String guardar(@Valid @ModelAttribute Karateca karateca,
                          BindingResult result,
                          Model model) {

        if (result.hasErrors()) {
            // Reenvías los datos que necesita el formulario si hay errores
            model.addAttribute("academias", academiaRepository.findAll());
            model.addAttribute("sexos", Karateca.Sexo.values());
            return "admin/karatecas/registrar";
        }

        karatecaRepository.save(karateca);
        return "redirect:/admin/karatecas";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Optional<Karateca> karateca = karatecaRepository.findById(id);
        if (karateca.isPresent()) {
            model.addAttribute("karateca", karateca.get());
            model.addAttribute("academias", academiaRepository.findAll());
            model.addAttribute("sexos", Karateca.Sexo.values());
            return "admin/karatecas/editar";
        } else {
            return "redirect:/admin/karatecas";
        }
    }

    // ACTUALIZAR
    @PostMapping("/editar/{id}")
    public String actualizar(@Valid @ModelAttribute Karateca karateca) {
        karatecaRepository.save(karateca);
        return "redirect:/admin/karatecas";
    }

    // ELIMINAR
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        karatecaRepository.deleteById(id);
        return "redirect:/admin/karatecas";
    }
}

