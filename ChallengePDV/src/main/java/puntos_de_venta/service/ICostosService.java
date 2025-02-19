package puntos_de_venta.service;

import org.springframework.http.ResponseEntity;
import puntos_de_venta.dtos.CostosDTO;
import puntos_de_venta.dtos.PathDTO;
import puntos_de_venta.model.Costos;
import puntos_de_venta.model.PuntoDeVenta;

import java.util.List;
import java.util.Map;

public interface ICostosService {
    ResponseEntity<List<Costos>> findAll();
    ResponseEntity<String> addCost(CostosDTO costosDTO);
    PuntoDeVenta validateExistence(Long puntoDeVentaId);
    ResponseEntity<String> removeCost(Long originId, Long destinationId);
    Map<String, Double> getDirectConnections(Long originId);
    PathDTO getShortestPath(Long originId, Long destinationId);
}
