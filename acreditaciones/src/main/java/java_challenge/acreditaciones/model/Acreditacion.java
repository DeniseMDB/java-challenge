package java_challenge.acreditaciones.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "acreditacion")
public class Acreditacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double cost;

    private Long puntoDeVentaId;

    private String namePuntoDeVenta;

    private LocalDateTime date;
}
