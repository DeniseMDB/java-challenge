package java_challenge.acreditaciones.repository;

import java_challenge.acreditaciones.model.Acreditacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcreditacionRepository extends JpaRepository<Acreditacion, Long> {
}
