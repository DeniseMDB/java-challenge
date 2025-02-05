# Proyecto de Microservicios para Puntos de Venta y Acreditaciones

**Este repositorio contiene una arquitectura de microservicios implementada en Java 17 para la gestión de puntos de venta (PDV), costos y acreditaciones. La aplicación utiliza una base de datos en memoria interna H2 para los puntos de venta y costos, mientras que el servicio de acreditaciones emplea una base de datos MySQL.**

### Tecnologías Utilizadas

*Java 17: Lenguaje principal de desarrollo.*

*Spring Boot: Framework para la creación de microservicios.*

*Spring Cloud Gateway: Para la gestión de enrutamiento y balanceo de carga entre microservicios.*

*Eureka Server: Para el descubrimiento de servicios.*

**Bases de Datos:**

*H2 (en memoria) para los servicios de costos y puntos de venta.*

*MySQL para el servicio de acreditaciones.*

*JUnit y Mockito: Para la realización de pruebas unitarias.*

*Postman: Para la validación y pruebas de los endpoints expuestos.*

**Arquitectura del Proyecto**

La aplicación se estructura en varios microservicios interconectados a través del **_API Gateway_**. Cada microservicio es responsable de una funcionalidad específica:

**Microservicio de Puntos de Venta (PDV):**

- Gestión de puntos de venta.

- Cálculo de costos.

- Base de datos H2 para almacenamiento en memoria.

**Microservicio de Acreditaciones:**

- Gestión de acreditaciones.

- Base de datos MySQL para almacenamiento persistente.

**Gateway:**

- Configuración para enrutamiento dinámico entre servicios usando Spring Cloud Gateway.

- Descubrimiento de servicios habilitado mediante *_Eureka_*.

*Configuración del Gateway*

A continuación se presenta el archivo application.properties configurado para el API Gateway:

```
spring.application.name=api-gateway
server.port=8082
eureka.client.service-url.defaultZone=http://localhost:8010/eureka

spring.cloud.gateway.discovery.locator.enabled=true
spring.cloud.gateway.discovery.locator.lower-case-service-id=true
```

### Funcionalidades Principales ###  

*Gestión de Puntos de Venta y Costos:*

- CRUD de puntos de venta.

- Cálculo de costos y reportes.

- Excepciones personalizadas para manejo de errores.

*Gestión de Acreditaciones:*

- Registro y consulta de acreditaciones.

- Integración con MySQL para almacenamiento.

*Gateway y Descubrimiento de Servicios:*

- Enrutamiento dinámico.

- Integración con Eureka para el descubrimiento de servicios.

*Pruebas Unitarias*

- Las pruebas unitarias se han implementado utilizando JUnit y Mockito.
### Ejecución del Proyecto ###

*Clona el repositorio:*

```
git clone https://github.com/DeniseMDB/java-challenge
```

*Levanta los servicios:*

*Configura las bases de datos (sera necesario crear una Base de Datos llamada "acreditaciones")*.

*Ejecuta cada servicio mediante tu IDE o usando:*
```
mvn spring-boot:run
```

*Accede al gateway en el puerto 8082 y utiliza Postman para probar los endpoints.*

## Coverage Report
![coverage report](https://github.com/user-attachments/assets/b884377e-0c91-4a80-910d-170400e3e07d)


