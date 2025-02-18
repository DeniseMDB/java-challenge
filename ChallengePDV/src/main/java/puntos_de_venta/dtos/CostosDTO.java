package puntos_de_venta.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CostosDTO {

    @NotBlank(message = "Origin id cannot be blank")
    private Long originId;

    @NotBlank(message = "Destination id cannot be blank")
    private Long destinationId;

    @NotBlank(message = "pricecannot be blank")
    private Double price;
}
