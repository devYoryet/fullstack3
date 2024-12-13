# Configuración Docker - Sistema de Gestión de Pedidos

## Estructura del Proyecto
```
proyecto/
├── backend/
│   ├── Dockerfile
│   └── docker-compose.yml
├── frontend/
│   ├── Dockerfile
│   └── nginx.conf
└── docker-compose.yml
```

## Archivos de Configuración

### Backend Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app
COPY target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
```

### Frontend Dockerfile
```dockerfile
FROM node:18 as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist/angular-demo /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
```

### Docker Compose Principal
```yaml
version: '3.8'

services:
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:oracle:thin:@//oracle:1521/XEPDB1
      - SPRING_DATASOURCE_USERNAME=system
      - SPRING_DATASOURCE_PASSWORD=oracle
    depends_on:
      - oracle

  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend

  oracle:
    image: container-registry.oracle.com/database/express:21.3.0-xe
    ports:
      - "1521:1521"
    environment:
      - ORACLE_PWD=oracle
    volumes:
      - oracle-data:/opt/oracle/oradata

volumes:
  oracle-data:
```

## Instrucciones de Despliegue

1. **Requisitos Previos**:
   - Docker Desktop instalado
   - Docker Compose instalado
   - Mínimo 4GB RAM disponible para Oracle DB

2. **Preparación del Backend**:
   ```bash
   cd backend
   ./mvnw clean package -DskipTests
   ```

3. **Construcción y Despliegue**:
   ```bash
   docker-compose up --build
   ```

4. **Verificación**:
   - Backend: http://localhost:8080
   - Frontend: http://localhost
   - Oracle DB: localhost:1521

## Consideraciones de Seguridad

1. Las credenciales de Oracle no deben estar en texto plano en producción
2. Usar secretos de Docker para manejar credenciales
3. Configurar redes Docker para aislar servicios
4. Implementar límites de recursos por contenedor

## Monitoreo y Logs

1. **Ver logs de contenedores**:
   ```bash
   docker-compose logs -f [servicio]
   ```

2. **Monitorear recursos**:
   ```bash
   docker stats
   ```

## Comandos Útiles

1. **Reiniciar servicios**:
   ```bash
   docker-compose restart [servicio]
   ```

2. **Detener todo**:
   ```bash
   docker-compose down
   ```

3. **Limpiar volúmenes**:
   ```bash
   docker-compose down -v
   ```

## Troubleshooting

1. **Oracle no inicia**:
   - Verificar memoria disponible
   - Revisar logs: `docker-compose logs oracle`

2. **Frontend no conecta con Backend**:
   - Verificar configuración CORS
   - Revisar nginx.conf

3. **Errores de conexión a BD**:
   - Esperar inicialización completa de Oracle (~2-3 minutos)
   - Verificar credenciales en variables de entorno
