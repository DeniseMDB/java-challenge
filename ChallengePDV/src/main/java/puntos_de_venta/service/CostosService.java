package puntos_de_venta.service;

import org.springframework.stereotype.Service;
import puntos_de_venta.repository.CostosRepository;

@Service
public class CostosService {

    private CostosRepository costosRepository;

    public CostosService(CostosRepository costosRepository) {
        this.costosRepository = costosRepository;
    }


}
