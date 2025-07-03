package com.evaluacionPermanente.PA4.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
public class Academia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_liga")
    private int id;

    @Column(length = 100,nullable = false)
    private String nombre;

    @NotBlank
    @Size(min = 11,max = 11) //NOS ASEGURAMOS QUE EL RUC SI O SI SEA DE 11 DIGITOS
    @Column(length = 11,unique = true,nullable = false)
    private String ruc;
}
