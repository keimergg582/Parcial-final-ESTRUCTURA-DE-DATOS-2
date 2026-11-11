# RedSegura

## Descripción

RedSegura es un programa de consola en Java 17 que modela la movilidad entre universidades, centros de salud, nodos de transporte y puntos de encuentro de Sincelejo. Los lugares son vértices de un grafo mixto, con conexiones dirigidas (arcos) y no dirigidas (aristas). Cada conexión tiene un peso que representa minutos estimados de desplazamiento.

El programa tiene dos árboles: un árbol binario de búsqueda (ABB) y un árbol AVL. Ambos buscan lugares por nombre y permiten comparar la cantidad de pasos de cada búsqueda. El AVL se rebalancea después de cada inserción y eliminación, para evitar que el árbol se comporte como una lista.

Sobre el grafo se implementan BFS (búsqueda en anchura) y DFS (búsqueda en profundidad), que recorren los lugares desde un vértice de origen, y Dijkstra, que calcula la ruta de menor costo entre dos lugares.

Los tiempos son datos de simulación didáctica y no representan información oficial de tránsito ni de respuesta a emergencias.

## Requisitos

- Java 17
- Maven 3.9 o superior

## Ejecución

Se recomienda tener Maven instalado y su carpeta bin en el PATH: https://maven.apache.org/download.cgi

Windows (cmd y PowerShell):

    mvn clean test
    mvn compile exec:java

Linux y macOS (bash o zsh):

    mvn clean test
    mvn compile exec:java

Si Maven no está en el PATH, usa la ruta completa entre comillas:

- cmd: "C:\ruta\del\maven\bin\mvn.cmd" clean test
- PowerShell: & "C:\ruta\del\maven\bin\mvn.cmd" clean test
- Linux y macOS: /ruta/del/maven/bin/mvn clean test

La clase principal está configurada en el pom.xml (propiedad exec.mainClass), por eso el comando no necesita indicarla.

## Arquitectura

Todas las clases están en el paquete com.mycompany.parcial_final_estructura_de_datosii_maven, en src/main/java. Las pruebas están en src/test/java.

| Clase                | Responsabilidad | Uso |

|----------------------|-----------------------------------------------------------------------------------------------------------|-------------------------------------------------|

| Vertex               | Lugar del grafo: ID, etiqueta y tipo.                                                                     | Consulta de vértice (8)                         |

| Edge 		           | Conexión entre dos vértices: ID, peso, etiqueta y orientación. Valida peso mayor que cero.                | Representaciones (3), Dijkstra (6)              |

| MixedGraph           | Grafo con aristas y arcos. Valida IDs duplicados e inexistentes, y permite bloquear conexiones.           | Todas las consultas                             |

| BinarySearchTree     | Árbol binario de búsqueda genérico: insertar, buscar, contar pasos, eliminar y recorridos.                | Búsqueda (1)                                    |

| AvlTree              | Árbol AVL genérico: insertar y eliminar con rotaciones LL, RR, LR y RL, y contar pasos de búsqueda.       | Búsqueda (1), rotaciones (2), prueba propia (9) |

| GraphRepresentations | Construye lista de adyacencia, matriz de adyacencia y matriz de incidencia.                               | Representaciones (3)                            |

| BreadthFirstSearch   | BFS con cola y conjunto de visitados. Devuelve el árbol de padres y los niveles.                          | BFS (4)                                         |

| DepthFirstSearch     | DFS y detección de ciclos. El ciclo se reconstruye con las aristas que lo forman.                         | DFS (5), ciclos (10)                            |

| DijkstraSolver       | Ruta mínima con cola de prioridad, relajación y mapa de predecesores.                                     | Dijkstra (6), bloqueo de R16 (7)                |

| DijkstraResult       | Resultado de Dijkstra: ruta, costo, origen, destino y si hay ruta.                                        | Dijkstra (6 y 7)                                |

| RedSeguraCore        | Carga los datos del parcial y crea las estructuras (grafo, ABB, AVL). Es el punto de entrada a los datos. | Todas las consultas                             |

| RedSeguraApplication | Menú de consola: lee la entrada, llama a los módulos y muestra resultados.                                | Menú completo  

### Dependencias

    RedSeguraApplication
     ├── RedSeguraCore ──────┬── MixedGraph ── Edge, Vertex
     │                       ├── BinarySearchTree
     │                       └── AvlTree
     ├── GraphRepresentations ── MixedGraph
     ├── BreadthFirstSearch ──── MixedGraph
     ├── DepthFirstSearch ────── MixedGraph
     └── DijkstraSolver ──────── MixedGraph, DijkstraResult

## Decisiones de diseño

- Edge con campos finales. Los datos de una arista (ID, extremos, peso, etiqueta y orientación) se asignan una sola vez, en el constructor. Así una arista no puede quedar inconsistente después de agregarla al grafo. Lo único que cambia es el bloqueo, que se maneja en MixedGraph y no dentro de Edge.

- Peso mayor que cero. Edge rechaza pesos 0 y negativos. Dijkstra solo funciona con pesos no negativos, y un peso 0 no se distingue de "sin conexión" en la matriz de adyacencia. Por eso el peso mínimo válido es 1.

- Árboles genéricos. BinarySearchTree y AvlTree usan T extends Comparable<T> en lugar de int. Así pueden guardar etiquetas de texto, como "Hospital Universitario", y el mismo código sirve para las pruebas con enteros de la opción 9.

- Conteo de pasos separado de la búsqueda. searchSteps devuelve la cantidad de nodos visitados cuando encuentra la clave, y un número negativo cuando no la encuentra. Search es un método booleano aparte, para no mezclar la respuesta con el conteo.

- Inserción del AVL con bandera. Al insertar, el AVL marca en un campo si el valor fue agregado o si ya existía. Así la inserción no necesita recorrer el árbol para contar nodos, y se mantiene en O(log n).

- Matriz de incidencia con ±1. En la fila de cada vértice, una arista dirigida tiene -1 en el origen y +1 en el destino. Una no dirigida tiene +1 en ambos extremos. Los pesos quedan en la matriz de adyacencia, que es donde corresponde.

- Representaciones con el mismo orden. Lista, matriz de adyacencia e incidencia ordenan los vértices por ID y las aristas por su ID. Así las filas y columnas coinciden entre las tres.

- BFS con árbol de padres y niveles. BFS guarda, para cada vértice visitado, su padre y su nivel. Con eso se puede reconstruir el árbol BFS, y no solo el orden de visita.

- DFS que ignora la arista de entrada. Para detectar ciclos, DFS salta la arista por la que llegó a cada vértice, usando su ID y no el vértice. Así una arista no dirigida no se cuenta como ciclo al volver por ella, y las aristas paralelas sí se detectan. Un arco dirigido solo cuenta como ciclo si su destino sigue en la pila de recursión.

- Dijkstra con cola de prioridad y predecesores. La cola PriorityQueue extrae siempre el vértice con menor distancia conocida. Un mapa de predecesores permite reconstruir la ruta al final. Se descarta una entrada de la cola si ya existe una distancia menor.

- Desempate de rutas por ID. S02 → S08 tiene dos rutas de 24 minutos. Al expandir los vértices, Dijkstra ordena sus vecinos por ID. La cola de prioridad compara solo por distancia, así que el resultado de S02 → S08 depende de ese orden de expansión y no del orden en que se agregaron las aristas.

- Fábricas en RedSeguraCore. La aplicación crea el grafo, el ABB y el AVL a través de RedSeguraCore, no con new directamente. Así la carga de datos vive en un solo lugar, y se puede cambiar un peso en un único archivo.

- Bloqueo de conexiones en el grafo. Bloquear R16 no borra la arista; solo la marca como no disponible. Así puede desbloquearse después sin volver a cargar los datos, y Dijkstra, BFS y DFS respetan el estado en todos sus recorridos.

## Complejidad

V = número de vértices (12), E = número de conexiones (18), h = altura del ABB.

| Operación                       | Tiempo                                                    | Espacio  |

|---------------------------------|-----------------------------------------------------------|----------|

| ABB: insertar, buscar, eliminar | O(h). En el peor caso O(n), si los datos llegan ordenados | O(n)     |

| AVL: insertar, buscar, eliminar | O(log n)                                                  | O(n)     |

| Lista de adyacencia             | O(V + E)                                                  | O(V + E) |

| Matriz de adyacencia            | O(V²)                                                     | O(V²)    |

| Matriz de incidencia            | O(V · E)                                                  | O(V · E) |

| BFS                             | O(V + E)                                                  | O(V)     |

| DFS y detección de ciclos       | O(V + E)                                                  | O(V)     |

| Dijkstra con cola de prioridad  | O((V + E) log V)                                          | O(V + E) |

El ABB no se rebalancea. Si las etiquetas llegan en orden, el árbol se vuelve una lista y cada búsqueda cuesta O(n). El AVL evita ese caso porque se rebalancea después de cada inserción.

## Resultados de control

Estos resultados salen de ejecutar el programa con los datos originales del parcial.

Q1. Búsqueda del Hospital Universitario. El ABB lo encuentra en 3 pasos y el AVL en 1 paso.

Q2. Rotaciones AVL. Cada caso inserta tres claves en un árbol sin balanceo y luego aplica la rotación correspondiente:

| Caso | Inserción  | Antes de rotar                                                       | Rotación                                      | Después                                    |           

|------|------------|----------------------------------------------------------------------|-----------------------------------------------|--------------------------------------------|

| LL   | 30, 20, 10 | 30 con hijo izquierdo 20, y 20 con hijo izquierdo 10 (FB de 30 = +2) | Rotación simple a la derecha en 30            | 20 con 10 a la izquierda y 30 a la derecha |

| RR   | 10, 20, 30 | 10 con hijo derecho 20, y 20 con hijo derecho 30 (FB de 10 = -2)     | Rotación simple a la izquierda en 10          | 20 con 10 a la izquierda y 30 a la derecha |

| LR   | 30, 10, 20 | 30 con hijo izquierdo 10, que tiene hijo derecho 20 (FB de 30 = +2)  | Rotación izquierda en 10, luego derecha en 30 | 20 con 10 a la izquierda y 30 a la derecha |

| RL   | 10, 30, 20 | 10 con hijo derecho 30, que tiene hijo izquierdo 20 (FB de 10 = -2)  | Rotación derecha en 30, luego izquierda en 10 | 20 con 10 a la izquierda y 30 a la derecha |

El árbol final es el mismo en los cuatro casos, porque con tres claves solo hay una forma balanceada posible.

Q3. Representaciones. Se imprimen la lista de adyacencia, la matriz de adyacencia y la matriz de incidencia, todas con el mismo orden de vértices y conexiones.

Q4. BFS y DFS desde S02. BFS visita por niveles: S02, S01, S06, S07, S09, S05, S08, S10, S12, S03, S04, S11. DFS profundiza por ramas: S02, S01, S07, S09, S05, S10, S08, S12, S03, S04, S11, S06. Por eso sus órdenes difieren aunque alcancen los mismos vértices.

Q5. Ruta mínima S02 → S08. El costo es 24 minutos por S02 → S01 → S07 → S08 (8 + 4 + 12).

Q6. Bloqueo de R16. Al bloquearla, la ruta cambia a S02 → S01 → S09 → S10 → S08, que también cuesta 24 minutos (8 + 5 + 3 + 8). El costo se mantiene porque hay dos rutas de igual costo.

Q7. Vértice inexistente. El programa muestra "Vértice inexistente" y sigue ejecutándose.

## Pruebas

El proyecto tiene 8 pruebas JUnit 5 en src/test/java/com/mycompany/parcial_final_estructura_de_datosii_maven/RedSeguraTest.java. Se ejecutan con mvn clean test y el resultado esperado es Tests run: 8, Failures: 0, Errors: 0, Skipped: 0.

| Prueba                         | Tipo                | Qué comprueba                                              |

|--------------------------------|---------------------|------------------------------------------------------------|

| verticeS08TieneNombreCorrecto  | Caso normal         | El vértice S08 tiene la etiqueta "Terminal de Transporte". |

| rutaNormalDeControl            | Caso normal         | Dijkstra de S02 a S08 cuesta 24 minutos.                   |

| verticeInexistenteDevuelveNull | Vértice inexistente | Pedir "ZZ" al grafo devuelve null.                         |

| sinRutaCuandoS02QuedaAislado   | Grafo desconectado  | Con R04 y R05 bloqueadas, S08 no es alcanzable desde S02.  |

| pesoCeroLanzaExcepcion         | Peso inválido       | Edge con peso 0 lanza IllegalArgumentException.            |

| pesoNegativoLanzaExcepcion     | Peso inválido       | Edge con peso -5 lanza IllegalArgumentException.           |

| trianguloNoDirigidoTieneCiclo  | Ciclos              | Un triángulo de aristas no dirigidas tiene ciclo.          |

| arbolNoDirigidoNoTieneCiclo    | Ciclos              | Un árbol de dos aristas no dirigidas no tiene ciclo.       |

## Limitaciones

Los tiempos son datos simulados y no representan tránsito real. El menú valida los números de entrada, pero los campos de texto, como el ID de la opción 10, solo se validan contra los vértices existentes.

## Origen del código y herramientas

- El pom.xml y la configuración de JUnit 5 se basan en la documentación oficial de Maven y JUnit.
- Parte de la estructura de las clases se generó con asistencia de ChatGPT. El autor la revisó, la probó y la adaptó, incluyendo las correcciones de DFS (detección de ciclos), AVL (bandera de inserción) y la validación de entradas.
- Las pruebas JUnit fueron escritas por el autor a partir de los casos del enunciado.



