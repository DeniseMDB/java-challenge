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
eureka.client.service-url.defaultZone=http://discovery-server:8010/eureka

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

*Swagger*

- Accede a la documentacion mediante sus respectivos archivos yml

### Ejecución del Proyecto ###

*Clona el repositorio:*

```
git clone https://github.com/DeniseMDB/java-challenge
```
*Setea la variable de entorno:*
- Importante! Dentro de la carpeta encontraras un archivo .env.
- Para editarlo: click izquierdo sobre '.env', seleccionar Editar/Abrir con Bloc de Notas
- Ingresar tu ip address para que corra correctamente. Si no sabes cual es podes ingresar a:
https://www.cualesmiip.com/ y podras copiar su valor.
```
IP_ADDR: "ingresa tu ip address"
```

*Levanta los servicios:*

*Mediante el uso de docker podras buildear cada imagen y correr sus contenedores*.
```
docker-compose up -d
``` 

*Accede al Api Gateway en el puerto 8082, utiliza Postman para probar los endpoints.*

- Por ejemplo para testear el servicio de Challenge-PVD creando una solicitud `GET` a la ruta:
```
localhost:8082/challenge-pdv/v1/costos
``` 
- O el servicio de Acreditaciones enviando un `POST` a la ruta:

```
localhost:8082/acreditaciones/v1/acreditacion?cost=50.0&id=1
``` 



## Coverage Report 
**Servicio Challenge-PVD**
![coverage report](https://github.com/user-attachments/assets/1dfbb07f-c5cd-472e-84da-33d7fcf744a1)

## Diagrama
![diagramaf](https://github.com/user-attachments/assets/3b1179c6-5a30-4775-afcb-2edabaf228cb)

## Clases
![acreditacion](https://github.com/user-attachments/assets/72b7fca3-5d94-4f60-b46b-822591d90f45)

![costos](https://github.com/user-attachments/assets/95fe6dd2-f6c5-424d-a695-5c49e9aa983a)
![puntosdeventa](https://github.com/user-attachments/assets/12f9f4fe-9405-4bfa-bae8-b43d792300ab)

