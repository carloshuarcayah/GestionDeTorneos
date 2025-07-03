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
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/seleccionarGanador")
    public String seleccionarGanador(@RequestParam int idLlave, @RequestParam int idGanador, RedirectAttributes redirectAttributes) {

        // SELECCIONAMOS LA LLAVE QUE VAMOS A MODIFICAR
        Llave llave = llaveRepository.findById(idLlave).orElse(null);

        //BUSCAMOS AL KARATECA
        Karateca ganador = karatecaRepository.findById(idGanador).orElse(null);

        //SI LOS IDS EXISTEN
        if (llave != null && ganador != null) {
            llave.setGanador(ganador);
            llaveRepository.save(llave);

            //VERIFICAMOS SI EL GANADOR ES EL KARATECA 1
            if (ganador.equals(llave.getId_karateca1())) {
                //SI ES ASI, ASEGURAMOS QUE SU ESTADO SEA ACTIVO
                ganador.setEstado("activo");
                //ESTADO DE KARATECA 2 PASA A SER ELIMINADO
                Karateca perdedor = llave.getId_karateca2();
                perdedor.setEstado("eliminado");
                //GUARDAMOS EL CAMBIO DE ESTADO EN LOS DOS KARATECAS
                karatecaRepository.save(ganador);//KARATECA 1
                karatecaRepository.save(perdedor);//KARATECA 2
            } else {
                //NOS ASEGURAMOS QUE KARATECA 2 TENGA EL ESTADO DE ACTIVO
                ganador.setEstado("activo");
                //EN CASO EL KARATECA 1 NO SEA EL GANADOR LE ASIGNAMOS EL ESTADO ELIMINADO
                Karateca perdedor = llave.getId_karateca1();
                perdedor.setEstado("eliminado");
                //GUARDAMOS LOS CAMBIOS DE ESTADO
                karatecaRepository.save(ganador);//KARATECA2
                karatecaRepository.save(perdedor);//KARATECA1
            }
            //MENSAJE DE EXITO AL ASIGNAR UN GANADOR
            redirectAttributes.addFlashAttribute("exito", "Ganador asignado exitosamente.");
        } else {
            //
            redirectAttributes.addFlashAttribute("error", "Error al asignar ganador.");
        }

        return "redirect:/admin/llaves"; // Redirige a la misma vista
    }

    @PostMapping("/generar")
    public String generar(RedirectAttributes ra){
        //PODRIA HABER CREADO UNA FUNCION QUE AGRUPE ESTAS DOS FUNCIONES
        if(llaveService.hayParticipantes()&&llaveService.todasLasLlavesTienenGanador()) {

            llaveService.cerrarLlaves();
            llaveService.generarRonda();//AQUI SE PODRIA GENERAR UN FLASH ATTRIBUTE CON UN MENSAJE DE EXITO SI HAY TIEMPO LO IMPLEMENTO
        }
        else{
            ra.addFlashAttribute("error", "Las llaves no han terminado");
        }
        //SEA CUAL SEA EL RESULTADO, LOS DOS REGRESAN AL INDEX DE LLAVE
        return "redirect:/admin/llaves";
    }
}
