package java_challenge.acreditaciones.service;

import java_challenge.acreditaciones.model.Acreditacion;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAcreditacionesService {
    ResponseEntity<Acreditacion> saveAcreditacion(Double cost, Long id);
    ResponseEntity<List<Acreditacion>> findAll();
}
