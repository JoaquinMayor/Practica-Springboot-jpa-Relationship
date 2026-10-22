# Práctica Spring Boot JPA Relationship

### El siguiente código cuenta con ejemplos y comentarios acerca de uso de Relationship entre los diferentes Entitys en Spring Boot.

## Relationship

`@OnetoOne`
`@OneToMany`
`@ManytoOne`
`@ManytoMany`

La aplicación cuenta con las siguientes clases Address, Client, ClientDetail, Course, Invoice y Student. 
El código no cuenta con servicios, ni con controladores con el fín de centrarse y facilitar la comprención de estos conceptos. 
Todos los datos son manejados desde la clase principal (SpringbootJpaRelationshipApplication.java) que implementa el CommandLineRunner.


<p align="center">
  <img src="https://miro.medium.com/v2/resize:fit:1358/1*IoZm9qFKEh2_n5PnCi79BA.png" alt="Spring Relationship" width="500">
</p>

---

## Objetivo

Proyecto de práctica del curso de Spring Boot 3 para comprender las **relaciones entre entidades JPA/Hibernate** (`@OneToOne`, `@OneToMany`, `@ManyToOne` y `@ManyToMany`), tanto en su forma **unidireccional** como **bidireccional**, incluyendo la creación, actualización y eliminación de elementos asociados, el uso de cascadas, `orphanRemoval`, tablas intermedias personalizadas y consultas con `join fetch`.

## Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.2.1 |
| Spring Data JPA (`spring-boot-starter-data-jpa`) / Hibernate | gestionada por Spring Boot |
| MySQL Connector/J (`mysql-connector-j`) | gestionada por Spring Boot |
| Spring Boot DevTools | gestionada por Spring Boot |
| Base de datos | MySQL |
| Maven (wrapper `mvnw`) | - |

## Estructura del proyecto

```
src/main/java/com/joaquin/curso/springboot/jpa/springbootjparelationship
├── SpringbootJpaRelationshipApplication.java  # CommandLineRunner con todos los ejemplos
├── Entities
│   ├── Client.java          # Cliente: OneToMany con Address e Invoice, OneToOne con ClientDetail
│   ├── Address.java         # Dirección (lado "muchos" unidireccional de Client)
│   ├── Invoice.java         # Factura: ManyToOne con Client (dueño de la FK id_client)
│   ├── ClientDetail.java    # Detalle del cliente: OneToOne con Client (dueño de la FK id_cliente)
│   ├── Student.java         # Alumno: ManyToMany con Course (dueño de la tabla intermedia)
│   └── Course.java          # Curso: ManyToMany inverso (mappedBy = "courses")
└── repository
    ├── IClientRepository.java         # Consultas con left join fetch de direcciones/facturas
    ├── IInvoiceRepository.java
    ├── IClientDetailsRepository.java
    ├── IStrudentRepository.java       # findOneWithCourses con left join fetch
    └── ICourseRepository.java

src/main/resources
├── application.properties
└── import.sql   # Datos iniciales: 2 clientes, 2 alumnos y 2 cursos
```

### Modelo de relaciones

| Relación | Entidades | Detalle |
|---|---|---|
| `@ManyToOne` | `Invoice` -> `Client` | FK `id_client` en `invoices` (`@JoinColumn`). |
| `@OneToMany` bidireccional | `Client` -> `Invoice` | `mappedBy = "client"`, `cascade = ALL`, `orphanRemoval = true`. |
| `@OneToMany` unidireccional | `Client` -> `Address` | Tabla intermedia `tbl_clientes_to_direcciones` (`id_cliente`, `id_direccion`) definida con `@JoinTable`. |
| `@OneToOne` bidireccional | `Client` <-> `ClientDetail` | FK `id_cliente` en `clients_details`; en `Client` se usa `mappedBy = "client"`. |
| `@ManyToMany` bidireccional | `Student` <-> `Course` | Tabla intermedia `tbl_alumnos_cursos` (`alumno_id`, `curso_id`) con `@UniqueConstraint` sobre la combinación; cascada `PERSIST` y `MERGE`. |

## Conceptos aplicados

- **`@JoinColumn`** para nombrar la llave foránea (si no se indica, se usa el nombre del atributo) y evitar tablas intermedias; en relaciones bidireccionales va en el lado que tiene la FK (el `@ManyToOne` / dueño del `@OneToOne`).
- **`@JoinTable`** con `joinColumns`, `inverseJoinColumns` y `uniqueConstraints` para controlar el nombre y las columnas de las tablas intermedias.
- **`mappedBy`** para mapear la relación desde ambos lados.
- **`CascadeType.ALL` y `orphanRemoval`**: al guardar o eliminar el cliente se propagan los cambios a sus direcciones/facturas/detalle, y se eliminan los registros que quedan sin asociar. Por eso basta con guardar solo el `Client`.
- **Mantener la consistencia bidireccional**: la factura debe conocer al cliente y el cliente a la factura; en `Student` se agregan los métodos auxiliares `addCourse()` y `removeCourse()`.
- **Colecciones `Set` inicializadas** en el constructor, y `equals()`/`hashCode()` implementados para poder eliminar elementos de las colecciones correctamente.
- **`toString()` sin ciclos**: solo uno de los lados de una relación bidireccional imprime al otro para evitar recursión infinita.
- **Carga perezosa (lazy) y `left join fetch`**: consultas como `findOnWthitAddresses`, `findOneWithInvoices`, `findOne` y `findOneWithCourses` traen las colecciones en la misma consulta. Hacer `join fetch` de varias colecciones a la vez funciona porque se usan `Set` en lugar de `List`.
- **`spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true`**: permite la carga perezosa fuera de transacción al trabajar desde consola.
- **`@Transactional`** en cada ejemplo y `saveAll()` para guardar varias entidades a la vez.
- **`import.sql`**: Hibernate lo ejecuta al iniciar (con `ddl-auto=create`) para poblar datos de prueba.

## Métodos de ejemplo en `SpringbootJpaRelationshipApplication`

| Sección | Métodos |
|---|---|
| ManyToOne / OneToMany | `manyToOneCreateClient()`, `manyToOneFindByIdClient()`, `oneToManyCreateClient()`, `oneToManyFindByIdClient()`, `removeAddres()`, `removeAddressFindById()`, `oneToManyInvoiceBidireccional()`, `oneToManyInvoiceBidireccionalFindById()`, `removeInvoiceBidireccionalFindById()` |
| OneToOne | `oneToOne()`, `oneToOneFindById()`, `oneToOneBidireccional()` |
| ManyToMany | `manyToMany()`, `manyToManyFindById()`, `manyToManyRemoveFindById()`, `manyToManyBidireccional()`, `manyToManyRemoveFindByIdBidireccional()` |

Por defecto `run()` ejecuta `manyToManyRemoveFindByIdBidireccional()`; para probar otro ejemplo cambia la llamada dentro de `run()`.

## Configuración

Se requiere un servidor **MySQL** en `localhost:3306` con la base de datos `db_jpa_relationship`:

```sql
CREATE DATABASE db_jpa_relationship;
```

`src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_jpa_relationship
spring.datasource.username=root
spring.datasource.password=<tu_password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=create
spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true
```

Con `ddl-auto=create` las tablas se recrean en cada arranque y se cargan los datos de `import.sql`. Ajusta usuario y contraseña a tu entorno.

## Cómo ejecutarlo

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

Los resultados y las sentencias SQL generadas por Hibernate se muestran en la consola.
