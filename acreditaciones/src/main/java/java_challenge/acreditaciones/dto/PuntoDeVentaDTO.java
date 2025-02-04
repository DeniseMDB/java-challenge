package java_challenge.acreditaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class PuntoDeVentaDTO {
    private Long id;
    private String name;
}
