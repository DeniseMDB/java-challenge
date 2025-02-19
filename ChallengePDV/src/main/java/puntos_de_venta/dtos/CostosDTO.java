package puntos_de_venta.dtos;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Min(value = 1, message = "Id should not be less than 0")
    @Max(value = 1000, message = "Id should not be greater than 1000")
    private Long originId;

    @NotBlank(message = "Destination id cannot be blank")
    @Min(value = 1, message = "Id should not be less than 0")
    @Max(value = 1000, message = "Id should not be greater than 1000")
    private Long destinationId;

    @NotBlank(message = "Must provide price")
    @Min(value = 1, message = "Price should not be less than 0")
    @Max(value = 1000000, message = "Price should not be greater than 1000000.0")
    private Double price;
}
