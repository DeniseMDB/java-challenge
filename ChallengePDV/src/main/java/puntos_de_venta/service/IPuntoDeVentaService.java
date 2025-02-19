package puntos_de_venta.service;

import org.springframework.http.ResponseEntity;
import puntos_de_venta.dtos.PuntoDeVentaDTO;
import puntos_de_venta.model.PuntoDeVenta;

import java.util.List;

public interface IPuntoDeVentaService {
    List<PuntoDeVenta> findAll();
    ResponseEntity<String> savePuntoDeVenta(PuntoDeVentaDTO puntoDeVentaDTO);
    ResponseEntity<String> updatePuntoDeVenta(Long id, PuntoDeVentaDTO puntoDeVentaActualizado);
    ResponseEntity<String> deletePuntoDeVenta(Long id);
    ResponseEntity<PuntoDeVenta> findById(Long id);
}
