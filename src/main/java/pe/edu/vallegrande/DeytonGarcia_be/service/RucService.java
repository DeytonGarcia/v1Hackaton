package pe.edu.vallegrande.DeytonGarcia_be.service;

import pe.edu.vallegrande.DeytonGarcia_be.model.RucEntity;
import java.util.List;
import java.util.Optional;

public interface RucService {
    RucEntity save(RucEntity rucEntity);
    List<RucEntity> findAllActive();
    List<RucEntity> findAll();
    Optional<RucEntity> findByRuc(String ruc);
    void logicalDelete(String ruc);
    void restore(String ruc);
}