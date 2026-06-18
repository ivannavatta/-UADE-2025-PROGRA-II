### Trabajo Práctico I — Algoritmos II / Programación II
**Universidad Argentina de la Empresa (UADE)**

---

## Integrantes del Grupo

| Nombre y Apellido | Legajo    |
|-------------------|-----------|
| [Ivan Navatta]    | [1111111] |


---



## TDAs Utilizados y Justificación

### Grafo (`DynamicGraphADT`) — Módulo 1
Utilizado para representar el mapa de terminales y sus conexiones. Cada terminal es un vértice y cada ruta directa es una arista con peso (distancia en km). Esta estructura es la más adecuada para modelar redes de transporte, ya que permite realizar búsquedas de caminos mediante DFS, verificar si existe una conexión directa entre dos terminales con `existEdge`, e identificar terminales aisladas (sin aristas entrantes ni salientes).

### Diccionario Simple (`DynamicSimpleDictionaryADT`) — Módulo 2
Mapea `idMicro → contadorAsignaciones`. El enunciado indica explícitamente "utilizar una estructura que permita, dados los identificadores de los micros, identificar qué micros fueron asignados". Un diccionario simple es exactamente eso: dada una clave (el ID del micro), devuelve un valor asociado (la cantidad de asignaciones). Se usa para obtener los micros asignados, encontrar los de mayor cantidad de asignaciones y calcular el promedio de utilización en los reportes.

### Diccionario Múltiple (`DynamicMultipleDictionaryADT`) — Módulo 2
Mapea `idMicro → [idViaje1, idViaje2, ...]`. El enunciado especifica que "un micro puede tener asignado más de un viaje". Una clave con múltiples valores asociados es la definición exacta de un diccionario múltiple. Permite recuperar todos los viajes históricos de un micro dado su identificador, lo cual es imposible con un diccionario simple.

### Cola con Prioridad (`DynamicPriorityQueueADT`) — Módulo 3
Gestiona el orden de despacho de los viajes según su nivel de prioridad (1=Alta, 2=Media, 3=Baja). El enunciado requiere "gestionar los viajes teniendo en cuenta la prioridad" y "modificar la prioridad por cuestiones meteorológicas o de visibilidad". La cola con prioridad mantiene los viajes ordenados automáticamente al insertar, y cuando se modifica la prioridad de un viaje, la cola se reconstruye reflejando el nuevo orden.

### Lista Enlazada (`DynamicLinkedListADT`) — Módulos 4 y 5
Se utilizan dos instancias: una para los IDs de terminales (`listaIdTerminales`) y otra para los IDs de rutas (`listaIdRutas`). El enunciado requiere "listar las terminales operadas" y generar reportes iterando rutas y terminales. La lista enlazada es la estructura natural para colecciones ordenadas e iterables, y permite recorrer los elementos en orden de registro sin depender de los índices del array interno.

---

## Consideraciones de Implementación

**Separación entre almacenamiento y lógica**
Los objetos del dominio (`Micro`, `Terminal`, `Ruta`, `Viaje`) se almacenan en arrays nativos. Los TDAs no guardan objetos sino IDs enteros, y operan sobre esos IDs como claves o referencias. Esto permite usar los TDAs con sus firmas originales (basadas en `int`) sin modificarlas, mientras el controller traduce entre IDs y objetos según sea necesario.

**Prioridad por defecto**
Todo viaje se planifica con prioridad 2 (Media) al ser creado. Esto es consistente con la inserción en la cola con prioridad, que ordena los viajes desde el momento de su registro.

**Reconstrucción de la cola al cambiar prioridad**
La `DynamicPriorityQueueADT` no expone un método de modificación interna, ya que una cola con prioridad por definición inserta en orden. Cuando se cambia la prioridad de un viaje, el controller drena la cola completa, reinserta cada elemento con su prioridad correspondiente (actualizando el modificado), y reemplaza la referencia. Esto mantiene la integridad del TDA sin violarlo.

**Validaciones en los constructores**
Cada clase del dominio valida sus propios invariantes en el constructor (IDs negativos, strings vacíos, tipos inválidos, origen igual a destino). Las excepciones personalizadas (`InvalidMicroException`, `InvalidTerminalException`, `InvalidRuteException`, `InvalididTravelException`) permiten identificar con precisión el origen del error.
