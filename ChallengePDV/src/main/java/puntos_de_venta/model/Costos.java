package puntos_de_venta.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Costos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "origin_id", nullable = false)
    private PuntoDeVenta pointOfOrigin;

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private PuntoDeVenta pointOfDestination;

    private Double price;
}
