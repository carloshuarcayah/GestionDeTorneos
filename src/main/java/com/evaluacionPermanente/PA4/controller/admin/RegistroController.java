package com.evaluacionPermanente.PA4.controller.admin;

import com.evaluacionPermanente.PA4.model.Usuario;
import com.evaluacionPermanente.PA4.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registro")
    public String index(Model model)
    {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Validated Usuario usuario, BindingResult bindingResult,
                            RedirectAttributes ra, Model model)
    {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("usuario", usuario);
            return "registro";
        }

        //Validar si el email existe, porque que ser único
        String email = usuario.getEmail();
        boolean existeUsuario = usuarioRepository.existsByEmail(email);

        if (existeUsuario)
            bindingResult.rejectValue("email", "EmailAlreadyExists");

        //validar la contraseñas, tiene que ser iguales
        if (! usuario.getPassword1().equals(usuario.getPassword2()))
            bindingResult.rejectValue("password1", "PasswordNotEquals");

        if (bindingResult.hasErrors())
        {
            model.addAttribute("usuario", usuario);
            return "registro";
        }

        //Asignamos los valores
        //Encryptar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword1()));
        //Asginar un role de estudiante por defecto
        usuario.setRol(Usuario.Rol.USUARIO);

        //grabar el usuario
        usuarioRepository.save(usuario);
        ra.addFlashAttribute("registroExitoso", "Usuario registrado");

        return "redirect:/login";
    }
}
