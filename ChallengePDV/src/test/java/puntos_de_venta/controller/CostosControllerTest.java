package puntos_de_venta.controller;

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
import puntos_de_venta.service.CostosService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CostosControllerTest {

    @Mock
    private CostosService costosService;

    @InjectMocks
    private CostosController costosController;

    @Test
    void findAll_shouldReturnListOfCosts() {
        // Given
        List<Costos> costosList = Arrays.asList(new Costos(), new Costos());
        given(costosService.findAll()).willReturn(costosList);

        // When
        ResponseEntity<List<Costos>> response = costosController.findAll();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void addCost_shouldAddCostSuccessfully() {
        // Given
        CostosDTO costosDTO = new CostosDTO();
        given(costosService.addCost(costosDTO)).willReturn(ResponseEntity.ok("Cost added successfully"));

        // When
        ResponseEntity<String> response = costosController.addCost(costosDTO);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cost added successfully", response.getBody());
    }

    @Test
    void removeCost_shouldRemoveCostSuccessfully() {
        // Given
        given(costosService.removeCost(1L, 2L)).willReturn(ResponseEntity.ok("Cost removed successfully"));

        // When
        ResponseEntity<String> response = costosController.removeCost(1L, 2L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cost removed successfully", response.getBody());
    }

    @Test
    void getDirectConnections_shouldReturnConnections() {
        // Given
        Map<String, Double> connections = Map.of("Destination", 50.0);
        given(costosService.getDirectConnections(1L)).willReturn(connections);

        // When
        ResponseEntity<Map<String, Double>> response = costosController.getDirectConnections(1L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(50.0, response.getBody().get("Destination"));
    }

    @Test
    void getShortestPath_shouldReturnPath() {
        // Given
        PathDTO pathDTO = new PathDTO();
        given(costosService.getShortestPath(1L, 2L)).willReturn(pathDTO);

        // When
        ResponseEntity<PathDTO> response = costosController.getShortestPath(1L, 2L);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pathDTO, response.getBody());
    }
}