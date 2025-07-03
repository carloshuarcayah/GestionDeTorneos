package com.evaluacionPermanente.PA4.repository;

import com.evaluacionPermanente.PA4.model.Karateca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KaratecaRepository extends JpaRepository<Karateca, Integer> {
    //BUSCAMOS A TODOS LOS KARATECAS QUE SIGAN EN COMPETENCIA
    List<Karateca> findByEstadoIgnoreCase(String estado);
    long countByEstadoIgnoreCase(String estado);
}
