package com.evaluacionPermanente.PA4.repository;

import com.evaluacionPermanente.PA4.model.Academia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademiaRepository extends JpaRepository<Academia,Integer> {
}
