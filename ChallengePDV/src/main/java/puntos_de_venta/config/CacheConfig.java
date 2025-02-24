package puntos_de_venta.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("costosCache","puntosCache");
        cacheManager.setAllowNullValues(false);
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(150)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .weakKeys()
                .removalListener(new CustomRemovalListener())
                .recordStats();
    }

    static class CustomRemovalListener implements RemovalListener<Object, Object> {

        @Override
        public void onRemoval(@Nullable Object key, @Nullable Object value, RemovalCause removalCause) {
            System.out.format("Removal listener called with key [%s], cause [%s], evicted[%S]\n",
                    key, removalCause.toString(), removalCause.wasEvicted());
        }
    }
}
