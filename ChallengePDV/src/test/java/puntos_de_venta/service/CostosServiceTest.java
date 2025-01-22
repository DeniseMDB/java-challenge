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
import puntos_de_venta.model.Costos;
import puntos_de_venta.model.PuntoDeVenta;
import puntos_de_venta.repository.CostosRepository;
import puntos_de_venta.repository.PuntoDeVentaRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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
