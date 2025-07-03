package com.evaluacionPermanente.PA4.repository;


import com.evaluacionPermanente.PA4.model.Llave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LlaveRepository extends JpaRepository<Llave, Integer> {

    // SELECT * FROM llave Where estado = "activo" o "terminada"
    List<Llave> findByEstado(String estado);

    //SI EL GANADOR HA SIDO EL KARATECA 1

    //HAY AL MENOS UNA LLAVE
    long count();
    // PARA OBTENER SOLO LA CANTIDAD
    long countByEstadoIgnoreCase(String estado);

    //CONTAMOS LA CANTIDAD DE LLAVES QUE AUN NO TIENEN GANADOR
    long countByGanadorIsNull();

}
