package java_challenge.acreditaciones.service;

import jakarta.transaction.Transactional;
import java_challenge.acreditaciones.dto.PuntoDeVentaDTO;
import java_challenge.acreditaciones.model.Acreditacion;
import java_challenge.acreditaciones.repository.AcreditacionRepository;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;

import static java_challenge.acreditaciones.utils.CommonConstants.CHALLENGE_PVD_URL;


@Service
@Transactional
public class AcreditacionService implements IAcreditacionesService{

    private final AcreditacionRepository acreditacionRepository;
    private final RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(AcreditacionService.class);

    public AcreditacionService(AcreditacionRepository acreditacionRepository, RestTemplate restTemplate) {
        this.acreditacionRepository = acreditacionRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * Saves a new Acreditacion in the database.
     *
     * @param cost the cost of the acreditacion.
     * @param id   the ID of the PuntoDeVenta associated with the acreditacion.
     * @return a ResponseEntity containing the created Acreditacion.
     * @throws NoSuchElementException if the PuntoDeVenta with the given ID is not found.
     */
    @Override
    public ResponseEntity<Acreditacion> saveAcreditacion(Double cost, Long id) {
        log.info("Starting saveAcreditacion process with cost = {} and PuntoDeVenta ID = {}", cost, id);
        Acreditacion acreditacion = new Acreditacion();
        String challengePDV = String.format(CHALLENGE_PVD_URL, id);
        PuntoDeVentaDTO puntoDeVentaDTO = restTemplate.getForObject(challengePDV, PuntoDeVentaDTO.class);
        if (puntoDeVentaDTO == null) {
            log.error("PuntoDeVenta not found for ID = {}", id);
            throw new NoSuchElementException(String.format("PVD_%d_NOT_FOUND", id));
        }

        acreditacion.setPuntoDeVentaId(puntoDeVentaDTO.getId());
        acreditacion.setNamePuntoDeVenta(puntoDeVentaDTO.getName());
        acreditacion.setCost(cost);
        acreditacion.setDate(LocalDateTime.now());
        log.info("Saving Acreditacion: {}", acreditacion);
        acreditacionRepository.save(acreditacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(acreditacion);
    }

    /**
     * Retrieves all Acreditaciones from the database.
     *
     * @return a ResponseEntity containing a list of Acreditaciones.
     * @throws NoSuchElementException if no Acreditaciones are found in the database.
     */
    @Override
    public ResponseEntity<List<Acreditacion>> findAll() {
        log.info("Fetching all Acreditaciones from the database");
        List<Acreditacion> acreditaciones = acreditacionRepository.findAll();
        if (acreditaciones.isEmpty()){
            log.error("No Acreditaciones found in the database");
            throw new NoSuchElementException("NO ACREDITACIONES FOUND");
        }
        log.info("Retrieved {} Acreditaciones", acreditaciones.size());
        return ResponseEntity.status(HttpStatus.OK).body(acreditaciones);
    }
}
