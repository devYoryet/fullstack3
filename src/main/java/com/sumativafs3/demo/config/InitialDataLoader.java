package com.sumativafs3.demo.config;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sumativafs3.demo.models.Compra;
import com.sumativafs3.demo.models.Producto;
import com.sumativafs3.demo.models.Rol;
import com.sumativafs3.demo.models.Usuario;
import com.sumativafs3.demo.repositories.CompraRepository;
import com.sumativafs3.demo.repositories.ProductoRepository;
import com.sumativafs3.demo.repositories.RolRepository;
import com.sumativafs3.demo.repositories.UsuarioRepository;
import com.sumativafs3.demo.utils.PasswordValidator;

@Component
public class InitialDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitialDataLoader.class);

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private void updateExistingRoles() {
        logger.info("Actualizando roles existentes...");
        rolRepository.findAll().forEach(rol -> {
            if (!rol.getNombre().startsWith("ROLE_")) {
                rol.setNombre("ROLE_" + rol.getNombre());
                rolRepository.save(rol);
                logger.info("Rol actualizado: {}", rol.getNombre());
            }
        });
    }

    private Producto createProducto(String nombre, String descripcion, double precio, int stock) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setCantidad(0); // Cantidad inicial en 0
        return producto;
    }

    @Override
    public void run(String... args) {
        logger.info("Iniciando carga de datos inicial...");
        
        updateExistingRoles();
        
        // Verificar si ya existen roles
        if (rolRepository.count() == 0) {
            try {
                logger.info("Creando roles iniciales...");
                
                // Crear rol ADMIN
                Rol adminRol = new Rol();
                adminRol.setNombre("ROLE_ADMIN");
                adminRol = rolRepository.save(adminRol);
                logger.info("Rol admin creado: {}", adminRol.getNombre());

                // Crear rol USER
                Rol userRol = new Rol();
                userRol.setNombre("ROLE_USER");
                userRol = rolRepository.save(userRol);
                logger.info("Rol usuario creado: {}", userRol.getNombre());

                // Crear usuario administrador
                logger.info("Creando usuario administrador...");
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setEmail("admin@example.com");
                String adminPassword = "Admin123#"; // Contraseña que cumple con las validaciones
                PasswordValidator.validatePassword(adminPassword);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRol(adminRol);
                admin = usuarioRepository.save(admin);
                logger.info("Usuario administrador creado: {}", admin.getEmail());

                // Crear usuario normal
                logger.info("Creando usuario normal...");
                Usuario user = new Usuario();
                user.setNombre("User");
                user.setEmail("user@example.com");
                String userPassword = "User123#"; // Contraseña que cumple con las validaciones
                PasswordValidator.validatePassword(userPassword);
                user.setPassword(passwordEncoder.encode(userPassword));
                user.setRol(userRol);
                user = usuarioRepository.save(user);
                logger.info("Usuario normal creado: {}", user.getEmail());

                logger.info("Credenciales creadas:");
                logger.info("Admin - Email: admin@example.com, Password: Admin123#");
                logger.info("User  - Email: user@example.com, Password: User123#");

                // Crear productos de ejemplo
                logger.info("Creando productos de ejemplo...");
                Producto[] productos = {
                    createProducto("Laptop HP", "Laptop HP Pavilion 15.6\" con Intel i5", 899.99, 10),
                    createProducto("iPhone 13", "Apple iPhone 13 128GB", 999.99, 15),
                    createProducto("Samsung TV", "Samsung 55\" 4K Smart TV", 699.99, 8),
                    createProducto("AirPods Pro", "Apple AirPods Pro 2nd Gen", 249.99, 20),
                    createProducto("PS5", "PlayStation 5 Console", 499.99, 5)
                };

                for (Producto producto : productos) {
                    try {
                        Producto savedProducto = productoRepository.save(producto);
                        logger.info("Producto creado: {} con ID: {}", savedProducto.getNombre(), savedProducto.getId());
                    } catch (Exception e) {
                        logger.error("Error al crear producto {}: {}", producto.getNombre(), e.getMessage());
                    }
                }

                // Crear compras de ejemplo
                logger.info("Creando compras de ejemplo...");
                
                // Primera compra
                Compra compra1 = new Compra();
                compra1.setUsuario(user);
                compra1.setFechaCompra(LocalDateTime.now());
                compra1.setEstadoCompra("COMPLETADA");
                
                Producto productoCompra1 = new Producto();
                productoCompra1.setNombre(productos[0].getNombre());
                productoCompra1.setDescripcion(productos[0].getDescripcion());
                productoCompra1.setPrecio(productos[0].getPrecio());
                productoCompra1.setCantidad(1);
                productoCompra1.setCompra(compra1);

                compra1.getProductos().add(productoCompra1);
                compra1.calcularTotal();
                compra1 = compraRepository.save(compra1);
                logger.info("Compra 1 creada con ID: {}", compra1.getId());

                // Segunda compra
                Compra compra2 = new Compra();
                compra2.setUsuario(admin);
                compra2.setFechaCompra(LocalDateTime.now().minusDays(1));
                compra2.setEstadoCompra("COMPLETADA");

                Producto productoCompra2 = new Producto();
                productoCompra2.setNombre(productos[1].getNombre());
                productoCompra2.setDescripcion(productos[1].getDescripcion());
                productoCompra2.setPrecio(productos[1].getPrecio());
                productoCompra2.setCantidad(2);
                productoCompra2.setCompra(compra2);

                compra2.getProductos().add(productoCompra2);
                compra2.calcularTotal();
                compra2 = compraRepository.save(compra2);
                logger.info("Compra 2 creada con ID: {}", compra2.getId());

            } catch (IllegalArgumentException e) {
                logger.error("Error en la validación de contraseñas: {}", e.getMessage());
            } catch (Exception e) {
                logger.error("Error al crear datos iniciales: {}", e.getMessage());
            }
        } else {
            logger.info("Ya existen datos en la base de datos, saltando inicialización");
        }
        
        logger.info("Carga de datos inicial completada");
    }
}