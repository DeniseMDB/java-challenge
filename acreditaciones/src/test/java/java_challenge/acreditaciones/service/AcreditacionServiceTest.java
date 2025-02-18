package java_challenge.acreditaciones.service;

import jakarta.ws.rs.NotFoundException;
import java_challenge.acreditaciones.dto.PuntoDeVentaDTO;
import java_challenge.acreditaciones.model.Acreditacion;
import java_challenge.acreditaciones.repository.AcreditacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcreditacionServiceTest {

    @Mock
    private AcreditacionRepository acreditacionRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AcreditacionService acreditacionService;


    @Test
    void givenValidCostAndId_whenSaveAcreditacion_thenReturnsCreatedAcreditacion() {
        // Given
        Double cost = 100.0;
        Long id = 1L;
        PuntoDeVentaDTO puntoDeVentaDTO = new PuntoDeVentaDTO();
        puntoDeVentaDTO.setId(id);
        puntoDeVentaDTO.setName("Punto de Venta Test");

        when(restTemplate.getForObject(anyString(), eq(PuntoDeVentaDTO.class))).thenReturn(puntoDeVentaDTO);
        when(acreditacionRepository.save(any(Acreditacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ResponseEntity<Acreditacion> response = acreditacionService.saveAcreditacion(cost, id);

        // Then
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getPuntoDeVentaId());
        assertEquals("Punto de Venta Test", response.getBody().getNamePuntoDeVenta());
        assertEquals(cost, response.getBody().getCost());
        verify(acreditacionRepository, times(1)).save(any(Acreditacion.class));
    }
    /**
    @Test
    void givenInvalidId_whenSaveAcreditacion_thenThrowsNoSuchElementException() {
        // Given
        Double cost = 100.0;
        Long id = 1L;

        when(restTemplate.getForObject(anyString(), eq(PuntoDeVentaDTO.class))).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> acreditacionService.saveAcreditacion(cost, id));
        assertTrue(exception.getMessage().contains(String.format("PVD_%d_NOT_FOUND",1L)));
        verify(acreditacionRepository, never()).save(any(Acreditacion.class));
    }
     */

    @Test
    void givenAcreditacionesInRepository_whenFindAll_thenReturnsListOfAcreditaciones() {
        // Given
        Acreditacion acreditacion1 = new Acreditacion();
        acreditacion1.setId(1L);
        acreditacion1.setCost(100.0);
        acreditacion1.setPuntoDeVentaId(1L);
        acreditacion1.setNamePuntoDeVenta("Punto de Venta 1");
        acreditacion1.setDate(LocalDateTime.now());

        Acreditacion acreditacion2 = new Acreditacion();
        acreditacion2.setId(2L);
        acreditacion2.setCost(200.0);
        acreditacion2.setPuntoDeVentaId(2L);
        acreditacion2.setNamePuntoDeVenta("Punto de Venta 2");
        acreditacion2.setDate(LocalDateTime.now());

        List<Acreditacion> acreditaciones = Arrays.asList(acreditacion1, acreditacion2);

        when(acreditacionRepository.findAll()).thenReturn(acreditaciones);

        // When
        ResponseEntity<List<Acreditacion>> response = acreditacionService.findAll();

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(acreditacionRepository, times(1)).findAll();
    }
}