package puntos_de_venta.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import puntos_de_venta.dtos.CostosDTO;
import puntos_de_venta.dtos.PathDTO;
import puntos_de_venta.model.Costos;
import puntos_de_venta.model.PuntoDeVenta;
import puntos_de_venta.repository.CostosRepository;
import puntos_de_venta.repository.PuntoDeVentaRepository;

import java.util.*;
import java.util.stream.Collectors;

import static puntos_de_venta.utils.Common.*;

@Service
@Transactional
public class CostosService {

    private final CostosRepository costosRepository;
    private final PuntoDeVentaRepository puntoDeVentaRepository;

    public CostosService(CostosRepository costosRepository, PuntoDeVentaRepository puntoDeVentaRepository) {
        this.costosRepository = costosRepository;
        this.puntoDeVentaRepository = puntoDeVentaRepository;
    }

    public List<Costos> findAll(){
        return costosRepository.findAll();
    }

    public ResponseEntity<String> addCost(CostosDTO costosDTO) {
        if (costosDTO.price < 0) {
            throw new IllegalArgumentException("Price cannot under 0");
        }

        PuntoDeVenta origin = validateExistence(costosDTO.originId);
        PuntoDeVenta destination = validateExistence(costosDTO.destinationId);

        if(costosRepository.existsByPointOfOriginIdAndPointOfDestinationId(costosDTO.originId, costosDTO.destinationId)){
            throw new IllegalArgumentException("The cost already exists");
        }

        Costos costos = new Costos();
        costos.setPointOfOrigin(origin);
        costos.setPointOfDestination(destination);
        costos.setPrice(costosDTO.price);
        costosRepository.save(costos);

        return ResponseEntity.status(HttpStatus.CREATED).body(SUCCESFULLY_CREATED);
    }

    public PuntoDeVenta validateExistence(Long puntoDeVentaId) {
        return puntoDeVentaRepository.findById(puntoDeVentaId)
                .orElseThrow(() -> new NoSuchElementException(String.format(PVD_NOT_FOUND, puntoDeVentaId)));
    }

    public ResponseEntity<String> removeCost(Long originId, Long destinationId) {
        PuntoDeVenta origin = validateExistence(originId);
        PuntoDeVenta destination = validateExistence(destinationId);

        Optional<Costos> cost = Optional.ofNullable(costosRepository.findByPointOfOriginAndPointOfDestination(origin, destination)
                .orElseThrow(() -> new NoSuchElementException("Cost not found")));

        costosRepository.deleteById(cost.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(SUCCESFULLY_DELETED);
    }

    public Map<String, Double> getDirectConnections(Long originId) {
        PuntoDeVenta origin = validateExistence(originId);
        List<Costos> directConnections = costosRepository.findByPointOfOrigin(origin);

        return directConnections.stream().collect(Collectors.toMap(
                costos -> costos.getPointOfDestination().getName(),
                Costos::getPrice
        ));
    }
    
    public PathDTO getShortestPath(Long originId, Long destinationId) {
        PuntoDeVenta origin = validateExistence(originId);
        PuntoDeVenta destination = validateExistence(destinationId);
        Map<PuntoDeVenta, Double> prices = new HashMap<>();
        Map<PuntoDeVenta, PuntoDeVenta> previousNodes = new HashMap<>();
        PriorityQueue<PuntoDeVenta> queue = new PriorityQueue<>(Comparator.comparing(prices::get));

        puntoDeVentaRepository.findAll().forEach(pv -> prices.put(pv, Double.MAX_VALUE));
        prices.put(origin, 0.0);
        queue.add(origin);

        while (!queue.isEmpty()) {
            PuntoDeVenta current = queue.poll();

            if (current.equals(destination)) break;

            costosRepository.findByPointOfOrigin(current).forEach(edge -> {
                PuntoDeVenta neighbor = edge.getPointOfDestination();
                double newPrice = prices.get(current) + edge.getPrice();

                if (newPrice < prices.get(neighbor)) {
                    prices.put(neighbor, newPrice);
                    previousNodes.put(neighbor, current);
                    queue.add(neighbor);
                }
            });
        }

        if (!prices.containsKey(destination) || prices.get(destination) == Double.MAX_VALUE) {
            throw new NoSuchElementException("There is no path available for destination");
        }

        List<String> path = new ArrayList<>();
        for (PuntoDeVenta at = destination; at != null; at = previousNodes.get(at)) {
            path.add(at.getName());
        }
        Collections.reverse(path);

        return new PathDTO(path, prices.get(destination));
    }
}



