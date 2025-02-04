package puntos_de_venta.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import puntos_de_venta.dtos.PuntoDeVentaDTO;
import puntos_de_venta.model.PuntoDeVenta;
import puntos_de_venta.service.PuntoDeVentaService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static puntos_de_venta.utils.Common.SUCCESFULLY_DELETED;


@ExtendWith(MockitoExtension .class)
class PuntoDeVentaControllerTest {

    @Mock
    private PuntoDeVentaService puntoDeVentaService;

    @InjectMocks
    private PuntoDeVentaController puntoDeVentaController;

    @Test
    void findAll_shouldReturnListOfPuntosDeVenta() {
        //given
        List<PuntoDeVenta> puntosDeVenta = Arrays.asList(new PuntoDeVenta(), new PuntoDeVenta());
        given(puntoDeVentaService.findAll()).willReturn(puntosDeVenta);

        // when
        ResponseEntity<List<PuntoDeVenta>> response = puntoDeVentaController.findAll();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void findById_shouldReturnPuntoDeVenta() {
        //Given
        PuntoDeVenta puntoDeVenta = new PuntoDeVenta();
        puntoDeVenta.setId(1L);
        given(puntoDeVentaService.findById(1L)).willReturn(ResponseEntity.ok(puntoDeVenta));

        //When
        ResponseEntity<PuntoDeVenta> response = puntoDeVentaController.findById(1L);

        //then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(puntoDeVenta, response.getBody());
    }

    @Test
    void addPuntoDeVenta_shouldSavePuntoDeVenta() {
        //given
        PuntoDeVentaDTO puntoDeVentaDTO = new PuntoDeVentaDTO();
        given(puntoDeVentaService.savePuntoDeVenta(puntoDeVentaDTO)).willReturn(ResponseEntity.status(HttpStatus.CREATED).body("SUCCESFULLY_CREATED"));

        //when
        ResponseEntity<String> response = puntoDeVentaController.addPuntoDeVenta(puntoDeVentaDTO);

        //then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("SUCCESFULLY_CREATED", response.getBody());
    }

    @Test
    void modifyPuntoDeVenta_shouldUpdatePuntoDeVenta() {
        //given
        PuntoDeVenta puntoDeVenta = new PuntoDeVenta();
        puntoDeVenta.setId(1L);
        PuntoDeVentaDTO puntoDeVentaDTO = new PuntoDeVentaDTO();
        given(puntoDeVentaService.updatePuntoDeVenta(1L, puntoDeVentaDTO)).willReturn(ResponseEntity.ok().body("SUCCESFULLY_UPDATED"));

        //when
        ResponseEntity<String> response = puntoDeVentaService.updatePuntoDeVenta(1L, puntoDeVentaDTO);

        //then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SUCCESFULLY_UPDATED", response.getBody());
    }

    @Test
    void deletePuntoDeVenta_shouldDeletePuntoDeVenta() {
        //given
        given(puntoDeVentaService.deletePuntoDeVenta(1L)).willReturn(ResponseEntity.status(HttpStatus.OK).body("SUCCESFULLY_DELETED"));

        //when
        ResponseEntity<String> response = puntoDeVentaService.deletePuntoDeVenta(1L);

        //then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SUCCESFULLY_DELETED", response.getBody());
    }

}