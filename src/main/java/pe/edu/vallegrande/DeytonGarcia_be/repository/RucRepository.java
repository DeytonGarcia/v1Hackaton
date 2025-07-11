package pe.edu.vallegrande.DeytonGarcia_be.repository;

import pe.edu.vallegrande.DeytonGarcia_be.model.RucEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RucRepository extends MongoRepository<RucEntity, String> {
    Optional<RucEntity> findByRuc(String ruc);
}