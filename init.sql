-- 1. Configuración inicial
ALTER SESSION SET CONTAINER = XEPDB1;
CREATE USER shop_app IDENTIFIED BY shop_password;
GRANT ALL PRIVILEGES TO shop_app;
GRANT UNLIMITED TABLESPACE TO shop_app;

-- 2. Crear tablas
CREATE TABLE roles (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL UNIQUE
);

CREATE TABLE usuarios (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) NOT NULL UNIQUE,
    password VARCHAR2(100) NOT NULL,
    rol_id NUMBER,
    CONSTRAINT fk_usuario_rol FOREIGN KEY (rol_id) REFERENCES roles(id)
);

CREATE TABLE productos (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    descripcion VARCHAR2(500),
    precio NUMBER(10,2) NOT NULL CHECK (precio > 0),
    stock NUMBER DEFAULT 0 CHECK (stock >= 0),
    imagen VARCHAR2(255),
    cantidad NUMBER DEFAULT 0
);

CREATE TABLE compras (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id NUMBER NOT NULL,
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR2(20) DEFAULT 'PENDIENTE',
    total NUMBER(10,2) NOT NULL,
    CONSTRAINT fk_compra_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE detalle_compra (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    compra_id NUMBER NOT NULL,
    producto_id NUMBER NOT NULL,
    cantidad NUMBER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMBER(10,2) NOT NULL,
    subtotal NUMBER(10,2) NOT NULL,
    CONSTRAINT fk_detalle_compra FOREIGN KEY (compra_id) REFERENCES compras(id),
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- 3. Insertar roles (primero)
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');
INSERT INTO roles (nombre) VALUES ('ROLE_USER');

-- 4. Insertar usuarios
INSERT INTO usuarios (nombre, email, password, rol_id)
VALUES ('Admin', 'admin@example.com',
        '$2a$10$xn3LI/AjqicFYZFruSwve.277W8YUH7svIJ4I9.1UworXh/p7UYS.',
        (SELECT id FROM roles WHERE nombre = 'ROLE_ADMIN'));

INSERT INTO usuarios (nombre, email, password, rol_id)
VALUES ('User', 'user@example.com',
        '$2a$10$xn3LI/AjqicFYZFruSwve.277W8YUH7svIJ4I9.1UworXh/p7UYS.',
        (SELECT id FROM roles WHERE nombre = 'ROLE_USER'));

-- 5. Insertar productos
INSERT INTO productos (nombre, descripcion, precio, stock, imagen)
VALUES ('Laptop HP', 'Laptop HP Pavilion 15.6" con Intel i5', 899.99, 10, '/assets/images/products/laptop-hp.jpg');

INSERT INTO productos (nombre, descripcion, precio, stock, imagen)
VALUES ('iPhone 13', 'Apple iPhone 13 128GB', 999.99, 15, '/assets/images/products/iphone-13.jpg');

INSERT INTO productos (nombre, descripcion, precio, stock, imagen)
VALUES ('Samsung TV', 'Samsung 55" 4K Smart TV', 699.99, 8, '/assets/images/products/samsung-tv.jpg');

INSERT INTO productos (nombre, descripcion, precio, stock, imagen)
VALUES ('AirPods Pro', 'Apple AirPods Pro 2nd Generation', 249.99, 20, '/assets/images/products/airpods.jpg');

INSERT INTO productos (nombre, descripcion, precio, stock, imagen)
VALUES ('PS5', 'PlayStation 5 Console', 499.99, 5, '/assets/images/products/ps5.jpg');

-- 6. Actualizar descripciones
UPDATE productos
SET descripcion = descripcion || '. Stock: ' || stock || ' unidades. Envío gratis.'
WHERE id > 0;

-- 7. Crear índices
CREATE INDEX idx_usuario_email ON usuarios(email);
CREATE INDEX idx_producto_nombre ON productos(nombre);
CREATE INDEX idx_compra_usuario ON compras(usuario_id);
CREATE INDEX idx_compra_fecha ON compras(fecha_compra);

COMMIT;