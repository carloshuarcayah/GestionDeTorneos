package com.evaluacionPermanente.PA4.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Llave {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_llave")
        private int id;

        @ManyToOne
        @JoinColumn(name = "id_karateca1", referencedColumnName = "id_karateca", nullable = false)
        private Karateca id_karateca1;

        @ManyToOne
        @JoinColumn(name = "id_karateca2", referencedColumnName = "id_karateca", nullable = true)//SEGUNDO KARATECA NO ES OBLIGATORIO POR SI NO HAY UN RIVAL
        private Karateca id_karateca2;

        @Column(nullable = false)
        private String ronda;

        //AGREMAOS ESTA VARIABLE PARA SABER SI LA LLAVE HA TERMINADO
        @Column(nullable = false)
        private String estado = "activo";

        @ManyToOne
        @JoinColumn(name = "ganador", referencedColumnName = "id_karateca")
        private Karateca ganador; // puede ser null inicialmente
}
