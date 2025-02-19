package puntos_de_venta.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class PuntoDeVentaDTO {

    @NotBlank(message = "Name cannot be blank")
    private String name;
}
