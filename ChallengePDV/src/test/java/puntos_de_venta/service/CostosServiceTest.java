package puntos_de_venta.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import puntos_de_venta.dtos.CostosDTO;
import puntos_de_venta.dtos.PathDTO;
import puntos_de_venta.exceptions.PuntoDeVentaNotFoundException;
import puntos_de_venta.model.Costos;
import puntos_de_venta.model.PuntoDeVenta;
import puntos_de_venta.repository.CostosRepository;
import puntos_de_venta.repository.PuntoDeVentaRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CostosServiceTest {

    @Mock
    private CostosRepository costosRepository;

    @Mock
    private PuntoDeVentaRepository puntoDeVentaRepository;

    @InjectMocks
    private CostosService costosService;

    @Test
    void addCost_shouldAddCostSuccessfully() {
        // Given
        CostosDTO costosDTO = new CostosDTO(1L, 2L, 100.0);
        PuntoDeVenta origin = new PuntoDeVenta(1L, "Origin");
        PuntoDeVenta destination = new PuntoDeVenta(2L, "Destination");

        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.of(origin));
        given(puntoDeVentaRepository.findById(2L)).willReturn(Optional.of(destination));
        given(costosRepository.existsByPointOfOriginIdAndPointOfDestinationId(1L, 2L)).willReturn(false);

        // When
        ResponseEntity<String> response = costosService.addCost(costosDTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("SUCCESFULLY_CREATED", response.getBody());
    }

    @Test
    void addCost_shouldThrowExceptionWhenPriceIsNegative() {
        // Given
        CostosDTO costosDTO = new CostosDTO(1L, 2L, -100.0);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> costosService.addCost(costosDTO));
    }

    @Test
    void findAll_shouldReturnAllCosts() {
        // Given
        List<Costos> costosList = Arrays.asList(new Costos(), new Costos());
        given(costosRepository.findAll()).willReturn(costosList);

        // When
        ResponseEntity<List<Costos>> result = costosService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.getBody().size());
    }

    @Test
    void removeCost_shouldRemoveCostSuccessfully() {
        // Given
        PuntoDeVenta origin = new PuntoDeVenta(1L, "Origin");
        PuntoDeVenta destination = new PuntoDeVenta(2L, "Destination");
        Costos cost = new Costos(1L, origin, destination, 100.0);

        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.of(origin));
        given(puntoDeVentaRepository.findById(2L)).willReturn(Optional.of(destination));
        given(costosRepository.findByPointOfOriginAndPointOfDestination(origin, destination)).willReturn(Optional.of(cost));

        // When
        ResponseEntity<String> response = costosService.removeCost(1L, 2L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SUCCESFULLY_DELETED", response.getBody());
    }

    @Test
    void removeCost_shouldThrowExceptionWhenCostNotFound() {
        // Given
        PuntoDeVenta origin = new PuntoDeVenta(1L, "Origin");
        PuntoDeVenta destination = new PuntoDeVenta(2L, "Destination");

        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.of(origin));
        given(puntoDeVentaRepository.findById(2L)).willReturn(Optional.of(destination));
        given(costosRepository.findByPointOfOriginAndPointOfDestination(origin, destination)).willReturn(Optional.empty());

        // When / Then
        assertThrows(NoSuchElementException.class, () -> costosService.removeCost(1L, 2L));
    }

    @Test
    void getDirectConnections_shouldReturnConnections() {
        // Given
        PuntoDeVenta origin = new PuntoDeVenta(1L, "Origin");
        PuntoDeVenta destination = new PuntoDeVenta(2L, "Destination");
        Costos cost = new Costos(1L, origin, destination, 50.0);

        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.of(origin));
        given(costosRepository.findByPointOfOrigin(origin)).willReturn(Collections.singletonList(cost));

        // When
        Map<String, Double> result = costosService.getDirectConnections(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(50.0, result.get("Destination"));
    }

    @Test
    void getDirectConnections_shouldThrowExceptionWhenOriginNotFound() {
        // Given
        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.empty());

        // When / Then
        assertThrows(PuntoDeVentaNotFoundException.class, () -> costosService.getDirectConnections(1L));
    }

    @Test
    void validateExistence_shouldReturnPuntoDeVenta() {
        // Given
        PuntoDeVenta puntoDeVenta = new PuntoDeVenta(1L, "Origin");
        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.of(puntoDeVenta));

        // When
        PuntoDeVenta result = costosService.validateExistence(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void validateExistence_shouldThrowExceptionWhenNotFound() {
        // Given
        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.empty());

        // When / Then
        assertThrows(PuntoDeVentaNotFoundException.class, () -> costosService.validateExistence(1L));
    }

    @Test
    void getShortestPath_shouldReturnShortestPath() {
        // Given
        PuntoDeVenta origin = new PuntoDeVenta(1L, "Origin");
        PuntoDeVenta destination = new PuntoDeVenta(2L, "Destination");
        List<PuntoDeVenta> puntosDeVenta= new ArrayList<>();
        puntosDeVenta.add(origin);
        puntosDeVenta.add(destination);
        Costos edge = new Costos(1L, origin, destination, 50.0);

        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.of(origin));
        given(puntoDeVentaRepository.findById(2L)).willReturn(Optional.of(destination));
        given(costosRepository.findByPointOfOrigin(origin)).willReturn(Collections.singletonList(edge));
        given(puntoDeVentaRepository.findAll()).willReturn(puntosDeVenta);

        // When
        PathDTO pathDTO = costosService.getShortestPath(1L, 2L);

        // Then
        assertNotNull(pathDTO);
        assertEquals(Arrays.asList("Origin", "Destination"), pathDTO.getPath());
        assertEquals(50.0, pathDTO.getTotalCost());
    }

    @Test
    void getShortestPath_shouldThrowExceptionWhenPathDoesNotExist() {
        // Given
        PuntoDeVenta origin = new PuntoDeVenta(1L, "Origin");
        PuntoDeVenta destination = new PuntoDeVenta(2L, "Destination");

        given(puntoDeVentaRepository.findById(1L)).willReturn(Optional.of(origin));
        given(puntoDeVentaRepository.findById(2L)).willReturn(Optional.of(destination));
        given(costosRepository.findByPointOfOrigin(origin)).willReturn(Collections.emptyList());

        // When / Then
        assertThrows(NoSuchElementException.class, () -> costosService.getShortestPath(1L, 2L));
    }
}

