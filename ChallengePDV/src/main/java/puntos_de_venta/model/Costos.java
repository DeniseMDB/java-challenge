package puntos_de_venta.model;

import jakarta.persistence.*;

@Entity
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
