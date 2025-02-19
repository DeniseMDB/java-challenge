package puntos_de_venta.service;

import static puntos_de_venta.utils.Common.PVD_NOT_FOUND;
import static puntos_de_venta.utils.Common.SUCCESFULLY_CREATED;
import static puntos_de_venta.utils.Common.SUCCESFULLY_DELETED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import puntos_de_venta.dtos.CostosDTO;
import puntos_de_venta.dtos.PathDTO;
import puntos_de_venta.exceptions.PuntoDeVentaNotFoundException;
import puntos_de_venta.model.Costos;
import puntos_de_venta.model.PuntoDeVenta;
import puntos_de_venta.repository.CostosRepository;
import puntos_de_venta.repository.PuntoDeVentaRepository;

@Service
@Transactional
public class CostosService {

    private final CostosRepository costosRepository;
    private final PuntoDeVentaRepository puntoDeVentaRepository;
    private static final Logger log = LoggerFactory.getLogger(CostosService.class);

    public CostosService(CostosRepository costosRepository, PuntoDeVentaRepository puntoDeVentaRepository) {
        this.costosRepository = costosRepository;
        this.puntoDeVentaRepository = puntoDeVentaRepository;
    }

    /**
     * Retrieves all the costs stored in the database.
     *
     * @return a list of all costs.
     */
    public ResponseEntity<List<Costos>> findAll(){
        log.info("Retrieving all costs");
        List<Costos> costos = costosRepository.findAll();
        if(costos.isEmpty()){
            log.error("No costs found in the database.");
            throw new NoSuchElementException("NO COSTOS FOUND");
        }
        return ResponseEntity.ok(costos);
    }

    /**
     * Adds a new cost between two points of sale.
     *
     * @param costosDTO the data transfer object containing the cost information.
     * @return a ResponseEntity indicating the result of the operation.
     * @throws IllegalArgumentException if the price is below 0 or the cost already exists.
     */
    public ResponseEntity<String> addCost(CostosDTO costosDTO) {
        log.info("Adding new cost: Origin ID = {}, Destination ID = {}, Cost = {}",
                costosDTO.getOriginId(), costosDTO.getDestinationId(), costosDTO.getPrice());
        if (costosDTO.getPrice() <= 0) {
            log.error("Invalid price: Price cannot be under 0. Provided value: {}", costosDTO.getPrice());    
            throw new IllegalArgumentException("Price cannot be under 0");
        }

        PuntoDeVenta origin = validateExistence(costosDTO.getOriginId());
        PuntoDeVenta destination = validateExistence(costosDTO.getDestinationId());

        if(costosRepository.existsByPointOfOriginIdAndPointOfDestinationId(costosDTO.getOriginId(), costosDTO.getDestinationId())){
            log.error("Cost already exists between Origin ID = {} and Destination ID = {}",
                    costosDTO.getOriginId(), costosDTO.getDestinationId());
            throw new IllegalArgumentException("The cost already exists");
        }

        Costos costos = new Costos();
        costos.setPointOfOrigin(origin);
        costos.setPointOfDestination(destination);
        costos.setPrice(costosDTO.getPrice());
        costosRepository.save(costos);

        return ResponseEntity.status(HttpStatus.CREATED).body(SUCCESFULLY_CREATED);
    }

    /**
     * Validates the existence of a point of sale by its ID.
     *
     * @param puntoDeVentaId the ID of the point of sale.
     * @return the {@link PuntoDeVenta} entity if found.
     * @throws NoSuchElementException if the point of sale does not exist.
     */
    public PuntoDeVenta validateExistence(Long puntoDeVentaId) {
        return puntoDeVentaRepository.findById(puntoDeVentaId)
                .orElseThrow(() -> new PuntoDeVentaNotFoundException(String.format(PVD_NOT_FOUND,puntoDeVentaId)));
    }

    /**
     * Removes an existing cost between two points of sale.
     *
     * @param originId      the ID of the origin point of sale.
     * @param destinationId the ID of the destination point of sale.
     * @return a ResponseEntity indicating the result of the operation.
     * @throws NoSuchElementException if the cost or points of sale do not exist.
     */
    public ResponseEntity<String> removeCost(Long originId, Long destinationId) {
        log.info("Removing cost: Origin ID = {}, Destination ID = {}", originId, destinationId);
        PuntoDeVenta origin = validateExistence(originId);
        PuntoDeVenta destination = validateExistence(destinationId);

        Optional<Costos> cost = Optional.ofNullable(costosRepository.findByPointOfOriginAndPointOfDestination(origin, destination)
                .orElseThrow(() -> new NoSuchElementException("COST NOT FOUND")));

        costosRepository.deleteById(cost.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(SUCCESFULLY_DELETED);
    }

    /**
     * Retrieves all direct connections from a given point of sale.
     *
     * @param originId the ID of the origin point of sale.
     * @return a map containing the destination names as keys and their costs as values.
     */
    public Map<String, Double> getDirectConnections(Long originId) {
        log.info("Retrieving direct connections for Origin ID = {}", originId);
        PuntoDeVenta origin = validateExistence(originId);
        List<Costos> directConnections = costosRepository.findByPointOfOrigin(origin);

        return directConnections.stream().collect(Collectors.toMap(
                costos -> costos.getPointOfDestination().getName(),
                Costos::getPrice
        ));
    }

    /**
     * Finds the shortest path between two points of sale using Dijkstra's algorithm.
     *
     * @param originId      the ID of the origin point of sale.
     * @param destinationId the ID of the destination point of sale.
     * @return a {@link PathDTO} containing the shortest path and its total cost.
     * @throws NoSuchElementException if no path is available.
     */
    public PathDTO getShortestPath(Long originId, Long destinationId) {
        log.info("Calculating shortest path: Origin ID = {}, Destination ID = {}", originId, destinationId);
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
                }});
        }

        if (!prices.containsKey(destination) || prices.get(destination) == Double.MAX_VALUE) {
            log.error("No path available from Origin ID = {} to Destination ID = {}", originId, destinationId);
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



