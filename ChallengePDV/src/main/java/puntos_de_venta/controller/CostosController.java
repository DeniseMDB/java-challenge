package puntos_de_venta.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import puntos_de_venta.dtos.CostosDTO;
import puntos_de_venta.dtos.PathDTO;
import puntos_de_venta.model.Costos;
import puntos_de_venta.service.CostosService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/costos")
public class CostosController {

    private final CostosService costosService;

    public CostosController(CostosService costosService) {
        this.costosService = costosService;
    }

    @GetMapping
    public ResponseEntity<List<Costos>> findAll() {
        return costosService.findAll();
    }

    @PostMapping
    public ResponseEntity<String> addCost(@RequestBody @Valid CostosDTO costosDTO) {
        return costosService.addCost(costosDTO);
    }

    @DeleteMapping
    public ResponseEntity<String> removeCost(@RequestParam Long originId, @RequestParam Long destinationId) {
        return costosService.removeCost(originId, destinationId);
    }

    @GetMapping("/direct")
    public ResponseEntity<Map<String, Double>> getDirectConnections(@RequestParam Long originId) {
        return ResponseEntity.ok(costosService.getDirectConnections(originId));
    }

    @GetMapping("/shortest-path")
    public ResponseEntity<PathDTO> getShortestPath(@RequestParam Long originId, @RequestParam Long destinationId) {
        return ResponseEntity.ok(costosService.getShortestPath(originId, destinationId));
    }
}
