package java_challenge.acreditaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcreditacionDTO {
    private Double cost;

    private Long puntoDeVentaId;

    private String namePuntoDeVenta;

    private LocalDateTime date;
}
