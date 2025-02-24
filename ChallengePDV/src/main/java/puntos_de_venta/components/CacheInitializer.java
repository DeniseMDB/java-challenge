package puntos_de_venta.components;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import puntos_de_venta.model.Costos;
import puntos_de_venta.model.PuntoDeVenta;
import puntos_de_venta.service.CostosService;
import puntos_de_venta.service.PuntoDeVentaService;

import java.util.List;

@Slf4j
@Component
public class CacheInitializer implements CommandLineRunner {

    private final CacheManager cacheManager;
    private final PuntoDeVentaService puntoDeVentaService;
    private final CostosService costosService;

    @Autowired
    public CacheInitializer(CacheManager cacheManager, PuntoDeVentaService puntoDeVentaService, CostosService costosService) {
        this.cacheManager = cacheManager;
        this.puntoDeVentaService = puntoDeVentaService;
        this.costosService = costosService;
    }

    public CacheStats getCoffeeCacheStats(String cacheName) {
        org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
        Cache nativeCoffeeCache = (Cache) cache.getNativeCache();
        return nativeCoffeeCache.stats();
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing cache with default data...");

        List<PuntoDeVenta> puntos = puntoDeVentaService.findAll();
        cacheManager.getCache("puntosCache").put("'all'", puntos);
        log.info("Puntos cache initialized with {} entries", puntos.size());

        List<Costos> costos = costosService.findAll().getBody();
        cacheManager.getCache("costosCache").put("'all'", costos);
        log.info("Costos cache initialized with {} entries", costos.size());

        log.info(getCoffeeCacheStats("puntosCache").toString());
        log.info(getCoffeeCacheStats("costosCache").toString());

    }
}
