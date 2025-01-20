package puntos_de_venta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import puntos_de_venta.model.PuntoDeVenta;

import java.util.Optional;

@Repository
public interface PuntoDeVentaRepository extends JpaRepository<PuntoDeVenta, Long> {
    Optional<PuntoDeVenta> findPuntoDeVentaByName(String nombre);

    @Override
    Optional<PuntoDeVenta> findById(Long id);
}
