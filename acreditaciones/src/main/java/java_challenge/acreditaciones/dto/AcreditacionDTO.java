package java_challenge.acreditaciones.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AcreditacionDTO {
    private Double cost;

    private Long puntoDeVentaId;

    private String namePuntoDeVenta;

    private LocalDateTime date;
}
