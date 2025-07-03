package com.evaluacionPermanente.PA4.controller.admin;

import com.evaluacionPermanente.PA4.repository.AcademiaRepository;
import com.evaluacionPermanente.PA4.repository.KaratecaRepository;
import com.evaluacionPermanente.PA4.repository.LlaveRepository;
import com.evaluacionPermanente.PA4.service.LlaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class HomeController {
    @Autowired
    AcademiaRepository academiaRepository;
    @Autowired
    KaratecaRepository karatecaRepository;
    @Autowired
    LlaveRepository llaveRepository;

    @Autowired
    LlaveService llaveService;
    //SOLO HABRA METODOS GET, TAL VEZ POST PARA MOSTRAR TADOS RELEVANTES?

    @GetMapping({"","/home"})
    public String home(Model model){

        //PODRIAMOS USAR SIEMPLEMENTE COUNT PERO ESTO CONTARIA LAS RONDAS QUE YA HAN TERMINADO
        long llaveCantidad = llaveRepository.countByEstadoIgnoreCase("activo");

        model.addAttribute("totalKaratecas",karatecaRepository.count());
        model.addAttribute("totalAcademias",academiaRepository.count());
        model.addAttribute("rondaActual",llaveService.nombreRonda(llaveCantidad));
        return "admin/home";
    }
}
