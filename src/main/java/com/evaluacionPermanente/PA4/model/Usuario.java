package com.evaluacionPermanente.PA4.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer id;

    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;

    @Column(name = "nom_completo")
    private String nombreCompleto;

    @NotEmpty
    @Email
    private String email;

    private String password;

    @NotBlank
    @Transient
    private String password1;

    @NotBlank
    @Transient
    private String password2;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    public enum Rol
    {
        ADMIN,
        LIGA,
        USUARIO,
    }
    @PrePersist //disparador antes de insertar un nuevo registro
    @PreUpdate //disparador antes de actualizar el registro existente
    void asignarNombreCompleto()
    {
        nombreCompleto = nombres + " " + apellidos;
    }

}
