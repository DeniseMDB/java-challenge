package puntos_de_venta.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CostosDTO {
    private Long originId;
    private Long destinationId;
    private Double price;
}
