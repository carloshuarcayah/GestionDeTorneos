package com.evaluacionPermanente.PA4.controller.admin;

import com.evaluacionPermanente.PA4.model.Academia;
import com.evaluacionPermanente.PA4.repository.AcademiaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/academias")
public class AcademiaController {

    @Autowired
    private AcademiaRepository academiaRepository;

    //PAGINA PRINCIPAL DE ACADEMIAS
    @GetMapping({"","index"})
    public String index(Model model){
        //TRAEMOS LOS DATOS DE ACADEMIA DE LA BASE DE DATOS
        //Y LO ENVIAMOS AL FRONT COMO UN ATRIBUTO
        model.addAttribute("academias", academiaRepository.findAll());
        return "/admin/academias/index";
    }

    //PAGINA PARA REGISTRAR ACADEMIA
    @GetMapping("/registrar")
    public String registrar(Model model){
        //ENVIAMOS UN MODELO VACIO DE ACADEMIA PARA RELLENAR
        model.addAttribute("academia",new Academia());
        return "admin/academias/registrar";
    }

    //PAGINA PARA EDITAR ACADEMIA
    //ENVIAMOS EL ID DE LA ACADEMIA QUE VAMOS A EDITAR POR PATH
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id,Model model){
        //GUARDAMOS LOS DATOS DE LA ACADEMIA A EDITAR EN UNA VARIABLE USANDO SU ID
        //COMO NO ES SEGURO QUE EXISTA ESTE ID SE GUARDA EN UN OPTIONAL
        Optional<Academia> academia = academiaRepository.findById(id);

        //SI LA ACADEMIA EXISTE LA GUARDAMOS LA ENVIAMOS COMO ATRIBUTO
        if(academia.isPresent()){
            model.addAttribute("academia",academia.get());
            return "admin/academias/editar";
        }else{
            //FALTARIA AGREGAR UN RA QUE INDIQUE QUE NO EXISTE
            return "redirect:/admin/academias";
        }
    }


    //REGISTRAMOS LA ACADEMIA EN LA BASE DE DATOS
    @PostMapping("/registrar")
    public String registrarAcademia(@Valid @ModelAttribute Academia academia){

        academiaRepository.save(academia);
        return "redirect:/admin/academias";
    }
    @PostMapping("/editar/{id}")
    public String actualizarAcademia(@Valid @ModelAttribute Academia academia, BindingResult result) {
        if(result.hasErrors()){
            return "/admin/academias/registrar";
        }
        academiaRepository.save(academia);
        return "redirect:/admin/academias";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarAcademia(@PathVariable Integer id){
        academiaRepository.deleteById(id);
        return "redirect:/admin/academias";
    }

}
