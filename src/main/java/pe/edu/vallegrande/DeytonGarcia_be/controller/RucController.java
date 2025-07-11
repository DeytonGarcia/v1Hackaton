package pe.edu.vallegrande.DeytonGarcia_be.controller;

import pe.edu.vallegrande.DeytonGarcia_be.model.RucEntity;
import pe.edu.vallegrande.DeytonGarcia_be.service.RucService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

@RestController
@RequestMapping("/api/ruc")
public class RucController {

    @Autowired
    private RucService rucService;

    private final String API_URL = "https://dniruc.apisperu.com/api/v1/ruc/";
    private final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImRleXRvbi5nYXJjaWFAdmFsbGVncmFuZGUuZWR1LnBlIn0.DBnDFJkEeocMg2Ag6766npoZcRBk8DAipKHa5zta1Nk";

    @GetMapping("/consultar/{ruc}")
    public ResponseEntity<?> consultarRuc(@PathVariable String ruc) {
        // Check if RUC already exists in DB
        if (rucService.findByRuc(ruc).isPresent()) {
            return ResponseEntity.ok(rucService.findByRuc(ruc).get());
        }

        String url = API_URL + ruc + "?token=" + TOKEN;
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
            Map<String, Object> response = responseEntity.getBody();
            if (response != null && response.containsKey("ruc")) {
                RucEntity rucEntity = new RucEntity();
                rucEntity.setRuc((String) response.get("ruc"));
                rucEntity.setRazonSocial((String) response.get("razonSocial"));
                rucEntity.setCondicion((String) response.get("condicion"));
                rucEntity.setDireccion((String) response.get("direccion"));
                rucEntity.setDepartamento((String) response.get("departamento"));
                rucEntity.setProvincia((String) response.get("provincia"));
                rucEntity.setDistrito((String) response.get("distrito"));
                rucService.save(rucEntity);
                return ResponseEntity.ok(rucEntity);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RUC not found or invalid response");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error consulting RUC: " + e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<RucEntity>> listarRucs() {
        return ResponseEntity.ok(rucService.findAllActive());
    }

    @GetMapping("/listarAll")
    public ResponseEntity<List<RucEntity>> listarAllRucs() {
        return ResponseEntity.ok(rucService.findAll());
    }

    @DeleteMapping("/eliminar/{ruc}")
    public ResponseEntity<String> eliminarRuc(@PathVariable String ruc) {
        if (rucService.findByRuc(ruc).isPresent()) {
            rucService.logicalDelete(ruc);
            return ResponseEntity.ok("RUC " + ruc + " logically deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RUC not found.");
        }
    }

    @PutMapping("/restaurar/{ruc}")
    public ResponseEntity<String> restaurarRuc(@PathVariable String ruc) {
        if (rucService.findByRuc(ruc).isPresent()) {
            rucService.restore(ruc);
            return ResponseEntity.ok("RUC " + ruc + " restored.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("RUC not found.");
        }
    }
}