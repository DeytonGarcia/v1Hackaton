package pe.edu.vallegrande.DeytonGarcia_be.service.impl;

import pe.edu.vallegrande.DeytonGarcia_be.model.RucEntity;
import pe.edu.vallegrande.DeytonGarcia_be.repository.RucRepository;
import pe.edu.vallegrande.DeytonGarcia_be.service.RucService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RucServiceImpl implements RucService {

    @Autowired
    private RucRepository rucRepository;

    @Override
    public RucEntity save(RucEntity rucEntity) {
        return rucRepository.save(rucEntity);
    }

    @Override
    public List<RucEntity> findAllActive() {
        return rucRepository.findAll().stream()
                .filter(RucEntity::isActivo)
                .collect(Collectors.toList());
    }

    @Override
    public List<RucEntity> findAll() {
        return rucRepository.findAll();
    }

    @Override
    public Optional<RucEntity> findByRuc(String ruc) {
        return rucRepository.findByRuc(ruc);
    }

    @Override
    public void logicalDelete(String ruc) {
        Optional<RucEntity> rucEntityOptional = rucRepository.findByRuc(ruc);
        rucEntityOptional.ifPresent(rucEntity -> {
            rucEntity.setActivo(false);
            rucRepository.save(rucEntity);
        });
    }

    @Override
    public void restore(String ruc) {
        Optional<RucEntity> rucEntityOptional = rucRepository.findByRuc(ruc);
        rucEntityOptional.ifPresent(rucEntity -> {
            rucEntity.setActivo(true);
            rucRepository.save(rucEntity);
        });
    }
}