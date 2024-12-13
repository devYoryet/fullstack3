# Documentación APIs - Sistema de Gestión de Pedidos

## Base URL
```
http://localhost:8080/api
```

## Autenticación

### Login
- **Endpoint**: `/auth/login`
- **Método**: POST
- **Body**:
```json
{
  "email": "string",
  "password": "string"
}
```
- **Respuesta exitosa**:
```json
{
  "id": number,
  "nombre": "string",
  "email": "string",
  "rol": {
    "id": number,
    "nombre": "string"
  }
}
```

### Registro
- **Endpoint**: `/auth/registro`
- **Método**: POST
- **Body**:
```json
{
  "nombre": "string",
  "email": "string",
  "password": "string"
}
```

## Productos

### Obtener todos los productos
- **Endpoint**: `/productos`
- **Método**: GET
- **Headers**: Authorization requerido
- **Respuesta**:
```json
[
  {
    "id": number,
    "nombre": "string",
    "descripcion": "string",
    "precio": number,
    "stock": number,
    "cantidad": number,
    "imagen": "string"
  }
]
```

### Crear producto (Solo Admin)
- **Endpoint**: `/productos`
- **Método**: POST
- **Headers**: Authorization requerido
- **Body**:
```json
{
  "nombre": "string",
  "descripcion": "string",
  "precio": number,
  "stock": number,
  "imagen": "string"
}
```

### Actualizar producto (Solo Admin)
- **Endpoint**: `/productos/{id}`
- **Método**: PUT
- **Headers**: Authorization requerido
- **Body**: Igual que en crear producto

### Eliminar producto (Solo Admin)
- **Endpoint**: `/productos/{id}`
- **Método**: DELETE
- **Headers**: Authorization requerido

## Compras

### Crear compra
- **Endpoint**: `/compras`
- **Método**: POST
- **Headers**: Authorization requerido
- **Body**:
```json
{
  "productos": [
    {
      "id": number,
      "cantidad": number
    }
  ]
}
```

### Obtener compras del usuario
- **Endpoint**: `/compras`
- **Método**: GET
- **Headers**: Authorization requerido

## Códigos de Estado

- 200: Operación exitosa
- 201: Recurso creado exitosamente
- 400: Error en la solicitud
- 401: No autorizado
- 403: Prohibido
- 404: Recurso no encontrado
- 500: Error interno del servidor

## Consideraciones de Seguridad

1. Todas las peticiones deben incluir el header de autenticación básica
2. Las contraseñas deben cumplir con los requisitos mínimos:
   - Mínimo 8 caracteres
   - Al menos una mayúscula
   - Al menos un número
   - Al menos un carácter especial

## Límites y Restricciones

1. Rate limiting: 10 peticiones por segundo
2. Tamaño máximo de archivos: 5MB
3. Tiempo máximo de respuesta: 30 segundos
