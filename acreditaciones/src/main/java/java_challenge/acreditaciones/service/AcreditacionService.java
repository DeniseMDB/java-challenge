package java_challenge.acreditaciones.service;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java_challenge.acreditaciones.dto.PuntoDeVentaDTO;
import java_challenge.acreditaciones.model.Acreditacion;
import java_challenge.acreditaciones.repository.AcreditacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java_challenge.acreditaciones.utils.CommonConstants.CHALLENGE_PVD_URL;
import static java_challenge.acreditaciones.utils.CommonConstants.PVD_NOT_FOUND;

@Service
@Transactional
public class AcreditacionService {

    private final AcreditacionRepository acreditacionRepository;
    private final RestTemplate restTemplate;

    public AcreditacionService(AcreditacionRepository acreditacionRepository, RestTemplate restTemplate) {
        this.acreditacionRepository = acreditacionRepository;
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<Acreditacion> saveAcreditacion(Double cost, Long id) {
        Acreditacion acreditacion = new Acreditacion();
        String challengePDV = String.format(CHALLENGE_PVD_URL, id);
        PuntoDeVentaDTO puntoDeVentaDTO = restTemplate.getForObject(challengePDV, PuntoDeVentaDTO.class);
        if (puntoDeVentaDTO == null) {
            throw new NotFoundException(String.format(PVD_NOT_FOUND, id));
        }

        acreditacion.setPuntoDeVentaId(puntoDeVentaDTO.getId());
        acreditacion.setNamePuntoDeVenta(puntoDeVentaDTO.getName());
        acreditacion.setCost(cost);
        acreditacion.setDate(LocalDateTime.now());
        acreditacionRepository.save(acreditacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(acreditacion);
    }

    public ResponseEntity<List<Acreditacion>> findAll() {
        List<Acreditacion> acreditaciones = acreditacionRepository.findAll();
        if (acreditaciones.isEmpty()){
            throw new NotFoundException("NO ACREDITACIONES FOUND");
        }
        return ResponseEntity.status(HttpStatus.OK).body(acreditaciones);
    }
}
