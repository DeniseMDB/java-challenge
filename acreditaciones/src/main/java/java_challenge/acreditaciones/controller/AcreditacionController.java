package java_challenge.acreditaciones.controller;

import java_challenge.acreditaciones.model.Acreditacion;
import java_challenge.acreditaciones.service.AcreditacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/acreditacion")
public class AcreditacionController {

    private final AcreditacionService acreditacionService;

    public AcreditacionController(AcreditacionService acreditacionService) {
        this.acreditacionService = acreditacionService;
    }

    @PostMapping
    public ResponseEntity<Acreditacion> createAcreditacion(@RequestParam Double cost, @RequestParam Long id) {
        return acreditacionService.saveAcreditacion(cost, id);
    }

    @GetMapping
    public ResponseEntity<List<Acreditacion>> findAcreditaciones() {
        return acreditacionService.findAll();
    }
}
