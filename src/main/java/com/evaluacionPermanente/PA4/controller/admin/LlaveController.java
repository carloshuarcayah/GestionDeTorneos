package com.evaluacionPermanente.PA4.controller.admin;

import com.evaluacionPermanente.PA4.model.Karateca;
import com.evaluacionPermanente.PA4.model.Llave;
import com.evaluacionPermanente.PA4.repository.KaratecaRepository;
import com.evaluacionPermanente.PA4.repository.LlaveRepository;
import com.evaluacionPermanente.PA4.service.LlaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("admin/llaves")
public class LlaveController {
    //GENERAMOS UNA CONEXION CON KARATECAS
    @Autowired
    KaratecaRepository karatecaRepository;

    @Autowired
    LlaveRepository llaveRepository;

    @Autowired
    LlaveService llaveService;

    //CUANLQUIERA DE ESAS DOS TE MANDA AL INDEX
    @GetMapping({"","index"})
    public String index(Model model)
    {
        //NOS DEVUELVE TODAS LAS LLAVES QUE HEOS CREADO, AHORA SOLO NOS FALTARIA ORGANIZARLAS EN LA VISTA
        model.addAttribute("llaves",llaveRepository.findAll());
        return "/admin/llaves/index";
    }

    @PostMapping("/generar")
    public String generar(RedirectAttributes ra){

        //VERIFICAMOS QUE HAYA PARTICIPANTES Y QUE TODAS LAS LLAVES SEAN "FINALIZADO"
        //PODRIA HABER CREADO UNA FUNCION QUE AGRUPE ESTAS DOS FUNCIONES
        if(llaveService.hayParticipantes()&&llaveService.todasLasLlavesHanFinalizado()) {
            llaveService.generarRonda();
            //AQUI SE PODRIA GENERAR UN FLASH ATTRIBUTE CON UN MENSAJE DE EXITO SI HAY TIEMPO LO IMPLEMENTO
        }
        else{
            ra.addFlashAttribute("mensaje", "Las llaves no han terminado");
        }
        //SEA CUAL SEA EL RESULTADO, LOS DOS REGRESAN AL INDEX DE LLAVE
        return "redirect:/admin/llaves";
    }
}
