# Endpoints del Sistema

## 1. Autenticación y Usuarios
### Login
```http
POST /api/auth/login
Content-Type: application/json

{
    "email": "admin@example.com",
    "password": "Admin123#"
}
```

### Registro (Usuario Normal)
```http
POST /api/auth/registro
Content-Type: application/json

{
    "nombre": "Usuario Normal",
    "email": "usuario@example.com",
    "password": "Pass123#"
}
```

### Registro (Solo Admin puede crear otros admin)
```http
POST /api/auth/registro/admin
Authorization: Bearer {token}
Content-Type: application/json

{
    "nombre": "Nuevo Admin",
    "email": "nuevoadmin@example.com",
    "password": "Admin123#"
}
```

## 2. Productos

### Obtener todos los productos
```http
GET /api/productos
Authorization: Bearer {token}
```

### Obtener un producto específico
```http
GET /api/productos/{id}
Authorization: Bearer {token}
```

### Crear producto (solo admin)
```http
POST /api/productos
Authorization: Bearer {token}
Content-Type: application/json

{
    "nombre": "Nuevo Producto",
    "descripcion": "Descripción del producto",
    "precio": 99.99,
    "stock": 10
}
```

### Actualizar producto (solo admin)
```http
PUT /api/productos/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
    "nombre": "Producto Actualizado",
    "descripcion": "Nueva descripción",
    "precio": 129.99,
    "stock": 15
}
```

### Eliminar producto (solo admin)
```http
DELETE /api/productos/{id}
Authorization: Bearer {token}
```

### Actualizar stock (solo admin)
```http
PUT /api/productos/{id}/stock?cantidad=20
Authorization: Bearer {token}
```

## 3. Compras

### Obtener todas las compras
```http
GET /api/compras
Authorization: Bearer {token}
```

### Obtener una compra específica
```http
GET /api/compras/{id}
Authorization: Bearer {token}
```

### Crear nueva compra
```http
POST /api/compras
Authorization: Bearer {token}
Content-Type: application/json

{
    "productos": [
        {
            "id": 1,
            "cantidad": 2
        }
    ],
    "estadoCompra": "PENDIENTE"
}
```

### Actualizar estado de compra (solo admin)
```http
PUT /api/compras/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
    "estadoCompra": "COMPLETADA"
}
```

## 4. Productos en una Compra

### Obtener productos de una compra
```http
GET /api/compras/{compraId}/productos
Authorization: Bearer {token}
```

### Agregar producto a una compra
```http
POST /api/compras/{compraId}/productos
Authorization: Bearer {token}
Content-Type: application/json

{
    "productoId": 1,
    "cantidad": 2
}
```

## 5. Perfil de Usuario

### Obtener perfil
```http
GET /api/usuarios/perfil
Authorization: Bearer {token}
```

### Actualizar perfil
```http
PUT /api/usuarios/perfil
Authorization: Bearer {token}
Content-Type: application/json

{
    "nombre": "Nuevo Nombre",
    "email": "nuevo@email.com"
}
```