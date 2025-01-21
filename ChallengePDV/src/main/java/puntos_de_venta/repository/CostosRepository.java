package puntos_de_venta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import puntos_de_venta.model.Costos;
import puntos_de_venta.model.PuntoDeVenta;

import java.util.List;
import java.util.Optional;

@Repository
public interface CostosRepository extends JpaRepository<Costos, Long> {
    boolean existsByPointOfOriginIdAndPointOfDestinationId(Long originId, Long destinationId);

    Optional<Costos> findByPointOfOriginAndPointOfDestination(PuntoDeVenta origin, PuntoDeVenta destination);

    List<Costos> findByPointOfOrigin(PuntoDeVenta origin);
}
