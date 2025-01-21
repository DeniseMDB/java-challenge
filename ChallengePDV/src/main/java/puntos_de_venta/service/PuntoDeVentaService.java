package puntos_de_venta.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import puntos_de_venta.dtos.PuntoDeVentaDTO;
import puntos_de_venta.model.PuntoDeVenta;
import puntos_de_venta.repository.PuntoDeVentaRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static puntos_de_venta.utils.Common.*;

@Service
@Transactional
public class PuntoDeVentaService {

    private final PuntoDeVentaRepository puntoDeVentaRepository;

    public PuntoDeVentaService(PuntoDeVentaRepository puntoDeVentaRepository) {
        this.puntoDeVentaRepository = puntoDeVentaRepository;
    }

    public List<PuntoDeVenta> findAll() {
        return (List<PuntoDeVenta>) puntoDeVentaRepository.findAll();
    }

    public ResponseEntity<String> savePuntoDeVenta(PuntoDeVentaDTO puntoDeVentaDTO) throws RuntimeException {
        if (puntoDeVentaRepository.findPuntoDeVentaByName(puntoDeVentaDTO.name).isPresent()){
            throw new RuntimeException("The PDV already exists");
        }
        try{
            PuntoDeVenta puntoDeVenta = new PuntoDeVenta();
            puntoDeVenta.setName(puntoDeVentaDTO.name);
            puntoDeVentaRepository.save(puntoDeVenta);
            return ResponseEntity.status(HttpStatus.CREATED).body(SUCCESFULLY_CREATED);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    public ResponseEntity<String> updatePuntoDeVenta(Long id, PuntoDeVentaDTO puntoDeVentaActualizado) {
        Optional<PuntoDeVenta> puntoDeVentaOptional= puntoDeVentaRepository.findById(id);
        if(puntoDeVentaOptional.isPresent()){
            PuntoDeVenta puntoDeVenta = puntoDeVentaOptional.get();
            puntoDeVenta.setName(puntoDeVentaActualizado.name);
            puntoDeVentaRepository.save(puntoDeVenta);
            return ResponseEntity.status(HttpStatus.OK).body(SUCCESFULLY_UPDATED);
        } else {throw new NoSuchElementException("Punto de venta with ID " + id + " not found");}
    }

    public ResponseEntity<String> deletePuntoDeVenta(Long id) {
        Optional<PuntoDeVenta> puntoDeVentaOptional= puntoDeVentaRepository.findById(id);
        if(puntoDeVentaOptional.isPresent()){
            puntoDeVentaRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(SUCCESFULLY_DELETED);
        } else {throw new NoSuchElementException("Punto de venta with ID " + id + " not found");}
    }
}