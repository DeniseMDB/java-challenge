package puntos_de_venta.exceptions;

public class PuntoDeVentaAlreadyExistsException extends RuntimeException {
    public PuntoDeVentaAlreadyExistsException(String message) {
        super(message);
    }
}
