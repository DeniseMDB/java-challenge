package puntos_de_venta.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import puntos_de_venta.dtos.PuntoDeVentaDTO;
import puntos_de_venta.model.PuntoDeVenta;
import puntos_de_venta.repository.PuntoDeVentaRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PuntoDeVentaServiceTest {

    @Mock
    private PuntoDeVentaRepository puntoDeVentaRepository;

    @InjectMocks
    private PuntoDeVentaService puntoDeVentaService;

    @Test
    void savePuntoDeVenta_shouldSaveSuccessfully() {
        // Given
        PuntoDeVentaDTO puntoDeVentaDTO = PuntoDeVentaDTO.builder()
                .name("New PDV")
                .build();
        PuntoDeVenta puntoDeVenta = new PuntoDeVenta(null, "New PDV");

        given(puntoDeVentaRepository.findPuntoDeVentaByName("New PDV")).willReturn(Optional.empty());
        given(puntoDeVentaRepository.save(any(PuntoDeVenta.class))).willReturn(puntoDeVenta);

        // When
        ResponseEntity<String> response = puntoDeVentaService.savePuntoDeVenta(puntoDeVentaDTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("SUCCESFULLY_CREATED", response.getBody());
        then(puntoDeVentaRepository).should().save(any(PuntoDeVenta.class));
    }

    @Test
    void savePuntoDeVenta_shouldThrowExceptionWhenPDVExists() {
        // Given
        PuntoDeVentaDTO puntoDeVentaDTO = PuntoDeVentaDTO.builder()
                .name("Existing PDV")
                .build();
        PuntoDeVenta existingPuntoDeVenta = new PuntoDeVenta(1L, "Existing PDV");

        given(puntoDeVentaRepository.findPuntoDeVentaByName("Existing PDV"))
                .willReturn(Optional.of(existingPuntoDeVenta));

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> puntoDeVentaService.savePuntoDeVenta(puntoDeVentaDTO));

        assertEquals("The PDV already exists", exception.getMessage());
    }

    @Test
    void deletePuntoDeVenta_shouldDeleteSuccessfully() {
        // Given
        PuntoDeVenta puntoDeVenta = new PuntoDeVenta(1L, "PDV to delete");

        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.of(puntoDeVenta));

        // When
        ResponseEntity<String> response = puntoDeVentaService.deletePuntoDeVenta(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SUCCESFULLY_DELETED", response.getBody());
        then(puntoDeVentaRepository).should().deleteById(1L);
    }

    @Test
    void deletePuntoDeVenta_shouldThrowExceptionWhenPDVNotFound() {
        // Given
        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.empty());

        // When / Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> puntoDeVentaService.deletePuntoDeVenta(1L));

        assertEquals((String.format("PVD_%d_NOT_FOUND",1L)), exception.getMessage());
    }
}
