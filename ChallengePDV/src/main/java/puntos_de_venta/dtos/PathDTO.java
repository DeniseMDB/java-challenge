package puntos_de_venta.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class PathDTO {
    public List<String> path;
    public Double totalCost;
}
