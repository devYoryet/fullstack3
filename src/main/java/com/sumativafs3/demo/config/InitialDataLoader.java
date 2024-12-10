package com.sumativafs3.demo.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sumativafs3.demo.models.*;
import com.sumativafs3.demo.repositories.*;
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
    private DetalleCompraRepository detalleCompraRepository;

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

    private Producto createProducto(String nombre, String descripcion, double precio, int stock, String imagen) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setImagen(imagen);
        return producto;
    }

    @Override
    public void run(String... args) {
        logger.info("Iniciando carga de datos inicial...");

        if (rolRepository.count() == 0) {
            try {
                // 1. Crear roles
                Rol adminRol = new Rol();
                adminRol.setNombre("ROLE_ADMIN");
                adminRol = rolRepository.save(adminRol);

                Rol userRol = new Rol();
                userRol.setNombre("ROLE_USER");
                userRol = rolRepository.save(userRol);

                // 2. Crear usuarios
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("Admin123#"));
                admin.setRol(adminRol);
                admin = usuarioRepository.save(admin);

                Usuario user = new Usuario();
                user.setNombre("User");
                user.setEmail("user@example.com");
                user.setPassword(passwordEncoder.encode("User123#"));
                user.setRol(userRol);
                user = usuarioRepository.save(user);

                logger.info("Usuarios creados con credenciales:");
                logger.info("Admin - Email: admin@example.com, Password: Admin123#");
                logger.info("User  - Email: user@example.com, Password: User123#");

                // 3. Crear productos
                List<Producto> productos = new ArrayList<>();
                productos.add(productoRepository.save(
                        createProducto("Laptop HP",
                                "Laptop HP Pavilion 15.6\" con Intel i5",
                                899.99,
                                10,
                                "/assets/images/products/laptop-hp.jpg")));

                productos.add(productoRepository.save(
                        createProducto("iPhone 13",
                                "Apple iPhone 13 128GB",
                                999.99,
                                15,
                                "/assets/images/products/iphone-13.jpg")));

                productos.add(productoRepository.save(
                        createProducto("Samsung TV",
                                "Samsung 55\" 4K Smart TV",
                                699.99,
                                8,
                                "/assets/images/products/samsung-tv.jpg")));

                productos.add(productoRepository.save(
                        createProducto("AirPods Pro",
                                "Apple AirPods Pro 2nd Generation",
                                249.99,
                                20,
                                "/assets/images/products/airpods.jpg")));

                productos.add(productoRepository.save(
                        createProducto("PS5",
                                "PlayStation 5 Console",
                                499.99,
                                5,
                                "/assets/images/products/ps5.jpg")));

                // 4. Crear compras de ejemplo
                // Primera compra
                Compra compra1 = new Compra();
                compra1.setUsuario(user);
                compra1.setFechaCompra(LocalDateTime.now());
                compra1.setEstadoCompra("COMPLETADA");
                compra1.setTotal(899.99);
                compra1 = compraRepository.save(compra1);

                DetalleCompra detalle1 = new DetalleCompra();
                detalle1.setCompra(compra1);
                detalle1.setProducto(productos.get(0));
                detalle1.setCantidad(1);
                detalle1.setPrecioUnitario(productos.get(0).getPrecio());
                detalle1.setSubtotal(productos.get(0).getPrecio());
                detalleCompraRepository.save(detalle1);

                // Segunda compra
                Compra compra2 = new Compra();
                compra2.setUsuario(admin);
                compra2.setFechaCompra(LocalDateTime.now().minusDays(1));
                compra2.setEstadoCompra("COMPLETADA");
                compra2.setTotal(1999.98); // 2 iPhones
                compra2 = compraRepository.save(compra2);

                DetalleCompra detalle2 = new DetalleCompra();
                detalle2.setCompra(compra2);
                detalle2.setProducto(productos.get(1));
                detalle2.setCantidad(2);
                detalle2.setPrecioUnitario(productos.get(1).getPrecio());
                detalle2.setSubtotal(productos.get(1).getPrecio() * 2);
                detalleCompraRepository.save(detalle2);

                logger.info("Datos iniciales creados exitosamente");

            } catch (Exception e) {
                logger.error("Error al crear datos iniciales: {}", e.getMessage());
                e.printStackTrace();
            }
        } else {
            logger.info("Ya existen datos en la base de datos");
        }
    }
}