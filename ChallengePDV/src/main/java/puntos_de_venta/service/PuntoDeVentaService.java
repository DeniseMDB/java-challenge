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

    /**
     * Retrieves all points of sale stored in the database.
     *
     * @return a list of all points of sale.
     */
    public List<PuntoDeVenta> findAll() {
        return (List<PuntoDeVenta>) puntoDeVentaRepository.findAll();
    }

    /**
     * Saves a new point of sale in the database.
     *
     * @param puntoDeVentaDTO the data transfer object containing the point of sale information.
     * @return a ResponseEntity indicating the result of the operation.
     * @throws RuntimeException if a point of sale with the same name already exists.
     */
    public ResponseEntity<String> savePuntoDeVenta(PuntoDeVentaDTO puntoDeVentaDTO) throws RuntimeException {
        if (puntoDeVentaRepository.findPuntoDeVentaByName(puntoDeVentaDTO.getName()).isPresent()){
            throw new RuntimeException("The PDV already exists");
        }
        try{
            PuntoDeVenta puntoDeVenta = new PuntoDeVenta();
            puntoDeVenta.setName(puntoDeVentaDTO.getName());
            puntoDeVentaRepository.save(puntoDeVenta);
            return ResponseEntity.status(HttpStatus.CREATED).body(SUCCESFULLY_CREATED);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Updates an existing point of sale in the database.
     *
     * @param id                    the ID of the point of sale to update.
     * @param puntoDeVentaActualizado the updated point of sale information.
     * @return a ResponseEntity indicating the result of the operation.
     * @throws NoSuchElementException if the point of sale does not exist.
     */
    public ResponseEntity<String> updatePuntoDeVenta(Long id, PuntoDeVentaDTO puntoDeVentaActualizado) {
        Optional<PuntoDeVenta> puntoDeVentaOptional= puntoDeVentaRepository.findById(id);
        if(puntoDeVentaOptional.isPresent()){
            PuntoDeVenta puntoDeVenta = puntoDeVentaOptional.get();
            puntoDeVenta.setName(puntoDeVentaActualizado.getName());
            puntoDeVentaRepository.save(puntoDeVenta);
            return ResponseEntity.status(HttpStatus.OK).body(SUCCESFULLY_UPDATED);
        } else {throw new NoSuchElementException("Punto de venta with ID " + id + " not found");}
    }

    /**
     * Deletes an existing point of sale from the repository.
     *
     * @param id the ID of the point of sale to delete.
     * @return a ResponseEntity indicating the result of the operation.
     * @throws NoSuchElementException if the point of sale does not exist.
     */
    public ResponseEntity<String> deletePuntoDeVenta(Long id) {
        Optional<PuntoDeVenta> puntoDeVentaOptional= puntoDeVentaRepository.findById(id);
        if(puntoDeVentaOptional.isPresent()){
            puntoDeVentaRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(SUCCESFULLY_DELETED);
        } else {throw new NoSuchElementException(String.format("PVD_%d_NOT_FOUND",id));}
    }
    
    /**
     * Finds a point of sale by its ID.
     *
     * @param id the ID of the point of sale.
     * @return a ResponseEntity containing the found point of sale.
     * @throws NoSuchElementException if the point of sale does not exist.
     */
    public ResponseEntity<PuntoDeVenta> findById(Long id) {
        Optional<PuntoDeVenta> puntoDeVentaOptional= puntoDeVentaRepository.findById(id);
        if(puntoDeVentaOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(puntoDeVentaOptional.get());
        } else {throw new NoSuchElementException(String.format(PVD_NOT_FOUND, id));}
    }
}