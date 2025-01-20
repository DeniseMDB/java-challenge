package puntos_de_venta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import puntos_de_venta.model.Costos;

@Repository
public interface CostosRepository extends JpaRepository<Costos, Long> {
}
