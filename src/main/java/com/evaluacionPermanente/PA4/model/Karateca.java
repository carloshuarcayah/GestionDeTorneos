package com.evaluacionPermanente.PA4.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Karateca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_karateca")
    private int id;

    @NotBlank
    @Column(name = "DNI",nullable = false,unique = true)
    private String dni;

    @NotBlank
    @Column(name = "nom_completo",nullable = false)
    private String nombre_completo;

    @Min(0)
    @Max(120)
    @Column(nullable = false)
    private int edad;

    @Column(nullable = false)
    private float peso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sexo sexo;

    //IMPLEMENTAMOS UNA NUEVA TABLA PARA SABER A QUE CATEGORIA PERTENECE EL PELEADOR
    @Column(nullable = false)
    private String categoria;

    @Min(1)
    @Max(10)
    private int rango;

    @NotBlank
    @Column(nullable = false)
    private String modalidad;

    //HAY DOS ESTADOS "ACTIVO" O "ELIMINADO"
    @Column(nullable = false)
    private String estado;//

    @ManyToOne
    @JoinColumn(name = "id_liga", referencedColumnName = "id_liga",nullable = false)
    private Academia id_liga;

    @PrePersist
    private void estadoKarateca(){
        estado="activo";
    }

    //CREAMOS UN ENUM PARA SEXO
    public enum Sexo{
        M, F
    }
}
