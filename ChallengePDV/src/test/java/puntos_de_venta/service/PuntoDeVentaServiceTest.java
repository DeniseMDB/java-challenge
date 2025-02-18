package puntos_de_venta.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import puntos_de_venta.dtos.PuntoDeVentaDTO;
import puntos_de_venta.exceptions.PuntoDeVentaNotFoundException;
import puntos_de_venta.model.PuntoDeVenta;
import puntos_de_venta.repository.PuntoDeVentaRepository;

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
    @DisplayName("Given valid PDV DTO, when saving, then it should be saved successfully")
    void givenValidPdvDto_whenSaving_thenShouldBeSavedSuccessfully() {
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
    @DisplayName("Given existing PDV DTO, when saving, then it should throw an exception")
    void givenExistingPdvDto_whenSaving_thenShouldThrowException() {
        // Given
        PuntoDeVentaDTO puntoDeVentaDTO = PuntoDeVentaDTO.builder()
                .name("Existing PDV")
                .id(1L)
                .build();
        PuntoDeVenta existingPuntoDeVenta = new PuntoDeVenta(1L, "Existing PDV");

        given(puntoDeVentaRepository.findPuntoDeVentaByName("Existing PDV"))
                .willReturn(Optional.of(existingPuntoDeVenta));

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> puntoDeVentaService.savePuntoDeVenta(puntoDeVentaDTO));

        assertEquals(String.format("PVD_%d_ALREADY_EXISTS", 1L), exception.getMessage());
    }

    @Test
    @DisplayName("Given valid PDV ID, when deleting, then it should be deleted successfully")
    void givenValidPdvId_whenDeleting_thenShouldBeDeletedSuccessfully() {
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
    @DisplayName("Given invalid PDV ID, when deleting, then it should throw an exception")
    void givenInvalidPdvId_whenDeleting_thenShouldThrowException() {
        // Given
        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.empty());

        // When / Then
        PuntoDeVentaNotFoundException exception = assertThrows(PuntoDeVentaNotFoundException.class,
                () -> puntoDeVentaService.deletePuntoDeVenta(1L));

        assertEquals((String.format("PVD_%d_NOT_FOUND", 1L)), exception.getMessage());
    }

    @Test
    @DisplayName("Given valid PDV ID and DTO, when updating, then it should update successfully")
    void givenValidPdvIdAndDto_whenUpdating_thenShouldUpdateSuccessfully() {
        // Given
        PuntoDeVenta puntoDeVenta = new PuntoDeVenta(1L, "Old PDV");
        PuntoDeVentaDTO updatedPuntoDeVentaDTO = PuntoDeVentaDTO.builder().name("Updated PDV").build();

        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.of(puntoDeVenta));

        // When
        ResponseEntity<String> response = puntoDeVentaService.updatePuntoDeVenta(1L, updatedPuntoDeVentaDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SUCCESFULLY_UPDATED", response.getBody());
        assertEquals("Updated PDV", puntoDeVenta.getName());
        then(puntoDeVentaRepository).should().save(puntoDeVenta);
    }

    @Test
    @DisplayName("Given invalid PDV ID, when updating, then it should throw an exception")
    void givenInvalidPdvId_whenUpdating_thenShouldThrowException() {
        // Given
        PuntoDeVentaDTO updatedPuntoDeVentaDTO = PuntoDeVentaDTO.builder().name("Updated PDV").build();

        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.empty());

        // When / Then
        PuntoDeVentaNotFoundException exception = assertThrows(PuntoDeVentaNotFoundException.class,
                () -> puntoDeVentaService.updatePuntoDeVenta(1L, updatedPuntoDeVentaDTO));

        assertEquals((String.format("PVD_%d_NOT_FOUND", 1L)), exception.getMessage());
    }

    @Test
    @DisplayName("Given valid PDV ID, when finding by ID, then it should return the PDV")
    void givenValidPdvId_whenFindingById_thenShouldReturnPdv() {
        // Given
        PuntoDeVenta puntoDeVenta = new PuntoDeVenta(1L, "PDV Found");

        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.of(puntoDeVenta));

        // When
        ResponseEntity<PuntoDeVenta> response = puntoDeVentaService.findById(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(puntoDeVenta, response.getBody());
    }

    @Test
    @DisplayName("Given invalid PDV ID, when finding by ID, then it should throw an exception")
    void givenInvalidPdvId_whenFindingById_thenShouldThrowException() {
        // Given
        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.empty());

        // When / Then
        PuntoDeVentaNotFoundException exception = assertThrows(PuntoDeVentaNotFoundException.class,
                () -> puntoDeVentaService.findById(1L));

        assertEquals((String.format("PVD_%d_NOT_FOUND", 1L)), exception.getMessage());
    }
}
