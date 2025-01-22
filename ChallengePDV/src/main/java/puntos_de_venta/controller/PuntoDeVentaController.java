package puntos_de_venta.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import puntos_de_venta.dtos.PuntoDeVentaDTO;
import puntos_de_venta.model.PuntoDeVenta;
import puntos_de_venta.service.PuntoDeVentaService;

import java.util.List;

@RestController
@RequestMapping("/v1/puntos")
public class PuntoDeVentaController {

    private final PuntoDeVentaService puntoDeVentaService;

    public PuntoDeVentaController(PuntoDeVentaService puntoDeVentaService) {
        this.puntoDeVentaService = puntoDeVentaService;
    }

    @GetMapping
    public ResponseEntity<List<PuntoDeVenta>> findAll() {
        return ResponseEntity.ok(puntoDeVentaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuntoDeVenta> findById(@PathVariable Long id) {
        return puntoDeVentaService.findById(id);
    }

    @PostMapping
    public ResponseEntity<String> addPuntoDeVenta(@RequestBody PuntoDeVentaDTO puntoDeVentaDTO) throws Exception {
        return puntoDeVentaService.savePuntoDeVenta(puntoDeVentaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modifyPuntoDeVenta(
            @PathVariable Long id,
            @RequestBody PuntoDeVentaDTO puntoDeVentaActualizado) {
        return puntoDeVentaService.updatePuntoDeVenta(id, puntoDeVentaActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePuntoDeVenta(@PathVariable Long id) {
        return puntoDeVentaService.deletePuntoDeVenta(id);
    }
}
