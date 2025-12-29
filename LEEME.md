# Documentación de API - FitMentor

## Índice
1. [Autenticación](#autenticación)
2. [Gestión de Usuarios](#gestión-de-usuarios)
3. [Gestión de Clientes](#gestión-de-clientes)
4. [Gestión de Entrenadores (Coaches)](#gestión-de-entrenadores-coaches)
5. [Gestión de Planes de Entrenamiento](#gestión-de-planes-de-entrenamiento)
6. [Gestión de Nutrición](#gestión-de-nutrición)
   - [Alimentos (Foods)](#alimentos-foods)
   - [Planes de Nutrición](#planes-de-nutrición)
   - [Comidas (Meals)](#comidas-meals)

---

## Autenticación

### POST `/api/auth/login`
**Descripción:** Autentica un usuario y devuelve un token JWT.

**Request Body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Campos obligatorios:**
- `username`: Nombre de usuario (string, no nulo)
- `password`: Contraseña (string, no nulo)

**Response exitosa (200 OK):**
```json
{
  "token": "string"
}
```

**Errores posibles:**
- `400 Bad Request`: Credenciales inválidas o campos faltantes
- `401 Unauthorized`: Usuario o contraseña incorrectos

---

## Gestión de Usuarios

### PUT `/api/user`
**Descripción:** Crea un nuevo usuario en el sistema.

**Request Body:**
```json
{
  "id": "uuid",
  "username": "string",
  "password": "string",
  "email": "string",
  "role": "string"
}
```

**Campos obligatorios:**
- `id`: UUID del usuario (UUID, no nulo)
- `username`: Nombre de usuario (string, no nulo)
- `password`: Contraseña (string, no nulo)
- `email`: Email del usuario (string, no nulo)
- `role`: Rol del usuario (string, no nulo - valores válidos dependen del enum Role)

**Response exitosa (200 OK):**
```json
"Created"
```

**Errores posibles:**
- `400 Bad Request`: Campos faltantes o inválidos
- `409 Conflict`: Usuario o email ya existe

---

## Gestión de Clientes

### GET `/api/user/clients`
**Descripción:** Obtiene la información del cliente autenticado.

**Headers requeridos:**
- `Authorization`: Bearer token

**Response exitosa (200 OK):**
```json
{
  "id": "uuid",
  "goals": "string | null",
  "age": "integer | null",
  "injuries": "string | null",
  "weight": "integer | null",
  "equipmentAccess": "integer | null",
  "phonenumber": "string | null",
  "user": "uuid"
}
```

**Errores posibles:**
- `401 Unauthorized`: Token no válido o ausente
- `404 Not Found`: Cliente no encontrado

---

### POST `/api/user/clients`
**Descripción:** Crea un nuevo cliente.

**Autenticación:** No requiere autenticación (endpoint público)

**Request Body:**
```json
{
  "id": {
    "value": "uuid"
  },
  "goals": "string | null",
  "age": "integer | null",
  "injuries": "string | null",
  "weight": "integer | null",
  "equipmentAccess": "integer | null",
  "phonenumber": "string | null",
  "user": {
    "value": "uuid"
  },
  "email": "string",
  "username": "string"
}
```

**Campos obligatorios:**
- `id`: ID del cliente como objeto ClientId
- `user`: ID del usuario asociado como objeto UserId
- `email`: Email (string)
- `username`: Nombre de usuario (string)

**Campos opcionales:**
- `goals`: Objetivos del cliente
- `age`: Edad
- `injuries`: Lesiones
- `weight`: Peso
- `equipmentAccess`: Acceso a equipamiento
- `phonenumber`: Número de teléfono

**Response exitosa (200 OK):**
```json
""
```

**Errores posibles:**
- `400 Bad Request`: Campos obligatorios faltantes o inválidos

---

### PUT `/api/user/clients`
**Descripción:** Actualiza la información de un cliente existente.

**Autenticación:** No requiere autenticación (endpoint público)

**Request Body:**
```json
{
  "id": {
    "value": "uuid"
  },
  "goals": "string | null",
  "age": "integer | null",
  "injuries": "string | null",
  "weight": "integer | null",
  "equipmentAccess": "integer | null",
  "phonenumber": "string | null",
  "user": {
    "value": "uuid"
  },
  "email": "string",
  "username": "string"
}
```

**Campos obligatorios:**
- `id`: ID del cliente como objeto ClientId
- `user`: ID del usuario asociado como objeto UserId
- `email`: Email (string)
- `username`: Nombre de usuario (string)

**Campos opcionales:**
- Todos los demás campos pueden ser nulos para actualización parcial

**Response exitosa (200 OK):**
```json
""
```

**Errores posibles:**
- `400 Bad Request`: Campos obligatorios faltantes o inválidos
- `404 Not Found`: Cliente no encontrado

---

## Gestión de Entrenadores (Coaches)

### GET `/api/user/coaches/{id}`
**Descripción:** Obtiene la información de un entrenador por su ID.

**Path Parameters:**
- `id`: UUID del entrenador (UUID, no nulo)

**Response exitosa (200 OK):**
```json
{
  "id": "uuid",
  "phonenumber": "string | null",
  "presentation": "string | null",
  "photo": "string | null",
  "user": "uuid",
  "username": "string",
  "email": "string"
}
```

**Errores posibles:**
- `404 Not Found`: Entrenador no encontrado

---

### POST `/api/user/coaches`
**Descripción:** Crea o modifica un entrenador.

**Autenticación:** No requiere autenticación (endpoint público)

**Request Body:**
```json
{
  "id": "string",
  "phonenumber": "string | null",
  "presentation": "string | null",
  "photo": "string | null",
  "user": "string",
  "username": "string",
  "email": "string"
}
```

**Campos obligatorios:**
- `id`: UUID del entrenador como string (no nulo)
- `user`: UUID del usuario asociado como string (no nulo)
- `username`: Nombre de usuario (string, no nulo)
- `email`: Email (string, no nulo)

**Campos opcionales:**
- `phonenumber`: Número de teléfono
- `presentation`: Presentación del entrenador
- `photo`: URL o ruta de la foto

**Response exitosa (200 OK):**
```json
""
```

**Errores posibles:**
- `400 Bad Request`: Campos obligatorios faltantes o UUID inválido

---

## Gestión de Planes de Entrenamiento

### GET `/api/user/plans/client/{clientId}`
**Descripción:** Obtiene todos los planes de entrenamiento de un cliente.

**Path Parameters:**
- `clientId`: UUID del cliente (UUID, no nulo)

**Headers requeridos:**
- `Authorization`: Bearer token (debe contener coachId)

**Response exitosa (200 OK):**
```json
[
  {
    "id": "uuid",
    "active": "boolean",
    "client": "uuid",
    "coach": "uuid",
    "description": "string",
    "type": "string",
    "goals": "string",
    "equipment": "string",
    "startDate": "ISO8601 timestamp",
    "endDate": "ISO8601 timestamp"
  }
]
```

**Errores posibles:**
- `401 Unauthorized`: Token no válido o ausente
- `404 Not Found`: No se encontraron planes para el cliente

---

### GET `/api/user/plans/client/{clientId}/plan/{planId}`
**Descripción:** Obtiene un plan de entrenamiento específico.

**Path Parameters:**
- `clientId`: UUID del cliente (UUID, no nulo)
- `planId`: UUID del plan (UUID, no nulo)

**Headers requeridos:**
- `Authorization`: Bearer token (debe contener coachId)

**Response exitosa (200 OK):**
```json
{
  "id": "uuid",
  "active": "boolean",
  "client": "uuid",
  "coach": "uuid",
  "description": "string",
  "type": "string",
  "goals": "string",
  "equipment": "string",
  "startDate": "ISO8601 timestamp",
  "endDate": "ISO8601 timestamp"
}
```

**Errores posibles:**
- `401 Unauthorized`: Token no válido o ausente
- `404 Not Found`: Plan no encontrado

---

### PUT `/api/user/plans`
**Descripción:** Actualiza un plan de entrenamiento existente.

**Request Body:**
```json
{
  "id": "uuid",
  "active": "boolean | null",
  "client": "uuid",
  "coach": "uuid",
  "description": "string | null",
  "type": "string | null",
  "goals": "string | null",
  "equipment": "string | null",
  "startDate": "ISO8601 timestamp | null",
  "endDate": "ISO8601 timestamp | null"
}
```

**Campos obligatorios:**
- `id`: UUID del plan (no nulo)
- `client`: UUID del cliente (no nulo)
- `coach`: UUID del entrenador (no nulo)

**Campos opcionales:**
- `active`: Estado activo (por defecto: true)
- `description`: Descripción
- `type`: Tipo de plan
- `goals`: Objetivos
- `equipment`: Equipamiento requerido
- `startDate`: Fecha de inicio (por defecto: fecha actual)
- `endDate`: Fecha de fin (por defecto: 30 días desde inicio)

**Response exitosa (200 OK):**
```json
""
```

**Errores posibles:**
- `400 Bad Request`: Campos obligatorios faltantes o inválidos

---

## Gestión de Nutrición

### Alimentos (Foods)

#### GET `/api/nutrition/foods`
**Descripción:** Obtiene la lista completa de alimentos disponibles.

**Response exitosa (200 OK):**
```json
[
  {
    "id": "uuid",
    "name": "string",
    "calPer100g": "double",
    "proteinPer100g": "double",
    "carbohydratePer100g": "double",
    "fatPer100g": "double"
  }
]
```

**Campos de respuesta:**
- `id`: UUID del alimento
- `name`: Nombre del alimento
- `calPer100g`: Calorías por 100g
- `proteinPer100g`: Proteínas por 100g
- `carbohydratePer100g`: Carbohidratos por 100g
- `fatPer100g`: Grasas por 100g

---

### Planes de Nutrición

#### POST `/api/nutrition/plans`
**Descripción:** Crea un nuevo plan de nutrición.

**Request Body:**
```json
{
  "clientId": "uuid",
  "coachId": "uuid",
  "description": "string",
  "startDate": "ISO8601 timestamp",
  "endDate": "ISO8601 timestamp"
}
```

**Campos obligatorios:**
- `clientId`: UUID del cliente
- `coachId`: UUID del entrenador
- `description`: Descripción del plan
- `startDate`: Fecha de inicio
- `endDate`: Fecha de fin

**Response exitosa (200 OK):**
```
Código de estado HTTP 200 sin cuerpo
```

**Errores posibles:**
- `400 Bad Request`: Campos obligatorios faltantes o inválidos

---

### Comidas (Meals)

#### POST `/api/nutrition/plans/{planId}/meals`
**Descripción:** Añade una comida a un plan de nutrición.

**Path Parameters:**
- `planId`: UUID del plan de nutrición

**Headers requeridos:**
- `Authorization`: Bearer token (debe contener coachId y clientId)

**Request Body:**
```json
{
  "id": "uuid",
  "name": "string",
  "components": [
    {
      "id": "uuid",
      "mealId": "uuid",
      "planId": "uuid",
      "foodId": "uuid",
      "quantity": "double",
      "unit": "string"
    }
  ],
  "supplements": [
    {
      "id": "uuid",
      "mealId": "uuid",
      "planId": "uuid",
      "supplementId": "uuid",
      "quantity": "double",
      "unit": "string"
    }
  ]
}
```

**Campos obligatorios:**
- `id`: UUID de la comida
- `name`: Nombre de la comida
- `components`: Array de componentes de la comida
  - `id`: UUID del componente
  - `mealId`: UUID de la comida
  - `planId`: UUID del plan
  - `foodId`: UUID del alimento
  - `quantity`: Cantidad
  - `unit`: Unidad de medida (string - debe ser un valor válido de UnitType)
- `supplements`: Array de suplementos
  - `id`: UUID del suplemento intake
  - `mealId`: UUID de la comida
  - `planId`: UUID del plan
  - `supplementId`: UUID del suplemento
  - `quantity`: Cantidad
  - `unit`: Unidad de medida (string - debe ser un valor válido de UnitType)

**Response exitosa (200 OK):**
```
Código de estado HTTP 200 sin cuerpo
```

**Errores posibles:**
- `400 Bad Request`: Campos obligatorios faltantes o inválidos
- `401 Unauthorized`: Token no válido o ausente
- `404 Not Found`: Plan de nutrición no encontrado

---

## Notas Generales

### Autenticación
La mayoría de los endpoints requieren autenticación mediante un token JWT que debe ser enviado en el header `Authorization` con el formato:
```
Authorization: Bearer <token>
```

### Formato de Fechas
Todas las fechas siguen el formato ISO8601 (por ejemplo: `2023-12-28T10:30:00Z`)

### UUIDs
Todos los identificadores son UUIDs en formato estándar (por ejemplo: `550e8400-e29b-41d4-a716-446655440000`)

### Códigos de Estado HTTP
- `200 OK`: Operación exitosa
- `400 Bad Request`: Datos inválidos o campos obligatorios faltantes
- `401 Unauthorized`: Token inválido o ausente
- `404 Not Found`: Recurso no encontrado
- `409 Conflict`: Conflicto con datos existentes

### Value Objects
Algunos campos se envían como objetos value objects:
- `ClientId`: `{ "value": "uuid" }`
- `UserId`: `{ "value": "uuid" }`
- `CoachId`: `{ "value": "uuid" }`
- `PlanId`: `{ "value": "uuid" }`

---

## Estructura del Proyecto

Este es un proyecto backend basado en:
- **Spring Boot** (Framework web)
- **JWT** (Autenticación)
- **JPA** (Persistencia)
- **Flyway** (Migraciones de BD)
- **Docker** (Contenedorización)
- **Kotlin** (Lenguaje principal)

Para más información sobre cómo ejecutar el proyecto, consulta el archivo `README.md`.

