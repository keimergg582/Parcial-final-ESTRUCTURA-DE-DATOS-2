package com.mycompany.parcial_final_estructura_de_datosii_maven;

import java.util.List;
import java.util.Scanner;

public class RedSeguraApplication {
    private final MixedGraph graph = RedSeguraCore.createGraph();
    private final BinarySearchTree<String> abb = RedSeguraCore.createBinarySearchTree(graph);
    private final AvlTree<String> avl = RedSeguraCore.createAvlTree(graph);
    private static final String HOSPITAL = "Hospital Universitario";

    public static void main(String[] args) { new RedSeguraApplication().run(); }

    private void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            int option;
            do {
                printMenu();
                option = readInt(scanner, "Opcion: ");
                switch (option) {
                    case 1 -> searchHospital();
                    case 2 -> showRotations();
                    case 3 -> showRepresentations();
                    case 4 -> showBfs();
                    case 5 -> System.out.println("DFS: " + DepthFirstSearch.traverse(graph, "S02"));
                    case 6 -> dijkstra();
                    case 7 -> toggleR16();
                    case 8 -> queryVertex(scanner);
                    case 9 -> manualAvl(scanner);
                    case 10 -> showCycle(scanner);
                    case 0 -> System.out.println("Fin del programa.");
                    default -> System.out.println("Opcion invalida.");
                }
            } while (option != 0);
        }
    }

    private void printMenu() {
        System.out.println("\n========== REDSEGURA ==========");
        System.out.println("1. Buscar Hospital Universitario en ABB y AVL");
        System.out.println("2. Mostrar las cuatro rotaciones AVL");
        System.out.println("3. Mostrar representaciones del grafo");
        System.out.println("4. BFS desde S02 (arbol, padres y niveles)");
        System.out.println("5. DFS desde S02");
        System.out.println("6. Dijkstra S02 -> S08");
        System.out.println("7. Bloquear/desbloquear R16");
        System.out.println("8. Consultar vertice");
        System.out.println("9. Probar AVL con valores propios");
        System.out.println("10. Buscar ciclo desde un vertice");
        System.out.println("0. Salir");
    }

    private int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try { return Integer.parseInt(line); }
            catch (NumberFormatException ex) { System.out.println("Ingresa un numero entero valido."); }
        }
    }

    private void searchHospital() {
        int abbSteps = abb.searchSteps(HOSPITAL);
        int avlSteps = avl.searchSteps(HOSPITAL);
        System.out.println("Clave buscada: " + HOSPITAL);
        System.out.println("ABB: " + (abbSteps > 0 ? "encontrado" : "no encontrado")
                + " | pasos=" + (Math.abs(abbSteps) - (abbSteps < 0 ? 1 : 0)) + " | altura=" + abb.height());
        System.out.println("AVL: " + (avlSteps > 0 ? "encontrado" : "no encontrado")
                + " | pasos=" + (Math.abs(avlSteps) - (avlSteps < 0 ? 1 : 0)) + " | altura=" + avl.height());
    }

    private static final class DemoNode {
        int value;
        DemoNode left, right;
        DemoNode(int value) { this.value = value; }
    }

    private void showRotations() {
        int[][] cases = {{30, 20, 10}, {10, 20, 30}, {30, 10, 20}, {10, 30, 20}};
        String[] names = {"LL", "RR", "LR", "RL"};
        for (int i = 0; i < cases.length; i++) {
            DemoNode rawRoot = null;
            for (int value : cases[i]) rawRoot = insertDemo(rawRoot, value);
            System.out.println("\nRotacion " + names[i] + " | insercion sin balancear: "
                    + java.util.Arrays.toString(cases[i]));
            printDemo(rawRoot, "", true);
            System.out.println("Factor de balance de la raiz antes de corregir: " + demoBalance(rawRoot));
            if (names[i].equals("LL") || names[i].equals("LR")) {
                System.out.println("Factor del hijo izquierdo: " + demoBalance(rawRoot.left));
            } else {
                System.out.println("Factor del hijo derecho: " + demoBalance(rawRoot.right));
            }

            AvlTree<Integer> tree = RedSeguraCore.createAvlTree();
            for (int value : cases[i]) tree.insert(value);
            System.out.println("Despues de aplicar la rotacion " + names[i] + ":");
            System.out.print(tree.structure());
            System.out.println("PreOrder final: " + tree.preOrder() + " | altura=" + tree.height());
        }
    }

    private DemoNode insertDemo(DemoNode node, int value) {
        if (node == null) return new DemoNode(value);
        if (value < node.value) node.left = insertDemo(node.left, value);
        else if (value > node.value) node.right = insertDemo(node.right, value);
        return node;
    }

    private int demoHeight(DemoNode node) {
        return node == null ? 0 : 1 + Math.max(demoHeight(node.left), demoHeight(node.right));
    }

    private int demoBalance(DemoNode node) {
        return node == null ? 0 : demoHeight(node.left) - demoHeight(node.right);
    }

    private void printDemo(DemoNode node, String prefix, boolean last) {
        if (node == null) return;
        System.out.println(prefix + (last ? "+-- " : "+-- ") + node.value);
        String next = prefix + (last ? "    " : "|   ");
        if (node.left != null || node.right != null) {
            printDemo(node.left, next, node.right == null);
            printDemo(node.right, next, true);
        }
    }

    private void showRepresentations() {
        System.out.println("\nLISTA DE ADYACENCIA");
        System.out.print(GraphRepresentations.adjacencyList(graph));
        List<Vertex> vertices = GraphRepresentations.sortedVertices(graph);
        System.out.println("\nMATRIZ DE ADYACENCIA");
        GraphRepresentations.printMatrix(GraphRepresentations.adjacencyMatrix(graph), vertices);
        System.out.println("\nMATRIZ DE INCIDENCIA (-1 origen, +1 destino; no dirigida +1/+1)");
        GraphRepresentations.printIncidenceMatrix(GraphRepresentations.incidenceMatrix(graph), vertices, graph);
    }

    private void showBfs() {
        BreadthFirstSearch.BFSTree tree = BreadthFirstSearch.buildTree(graph, "S02");
        System.out.println("BFS: " + tree);
        System.out.println("Padres: " + tree.getParents());
        System.out.println("Niveles: " + tree.getLevels());
        System.out.println("BFS usa una cola: visita primero todos los vecinos cercanos y avanza por niveles.");
        System.out.println("Por eso su orden puede diferir de DFS, que profundiza por una rama antes de retroceder.");
    }

    private void dijkstra() {
        DijkstraResult result = DijkstraSolver.solve(graph, "S02", "S08");
        System.out.println("Resultado actual de Dijkstra:");
        printDijkstraResult(result);
        System.out.println("Estado de R16: " + (graph.isBlocked("R16") ? "BLOQUEADA" : "ACTIVA"));
    }

    private void toggleR16() {
        boolean willBlock = !graph.isBlocked("R16");
        graph.setBlocked("R16", willBlock);
        System.out.println("R16 ahora esta " + (willBlock ? "BLOQUEADA" : "ACTIVA"));
        DijkstraResult result = DijkstraSolver.solve(graph, "S02", "S08");
        System.out.println("Resultado recalculado de Dijkstra:");
        printDijkstraResult(result);
    }

    /** Imprime exclusivamente los datos calculados por Dijkstra, sin asumir rutas ni pesos. */
    private void printDijkstraResult(DijkstraResult result) {
        if (!result.reachable()) {
            System.out.println("No existe una ruta disponible entre "
                    + result.getOrigin() + " y " + result.getDestination() + ".");
            return;
        }
        System.out.println("Ruta minima: " + String.join(" -> ", result.getPath()));
        System.out.println("Costo total: " + result.getCost() + " min");
    }

    private void queryVertex(Scanner scanner) {
        System.out.print("ID del vertice: ");
        String id = scanner.nextLine().trim().toUpperCase();
        Vertex vertex = graph.getVertex(id);
        if (vertex == null) System.out.println("Vertice inexistente. El programa continua.");
        else System.out.println(vertex.getId() + " | " + vertex.getLabel() + " | " + vertex.getType());
    }

    private void manualAvl(Scanner scanner) {
        AvlTree<Integer> tree = RedSeguraCore.createAvlTree();
        int count = readInt(scanner, "Cantidad de valores: ");
        if (count < 0) { System.out.println("La cantidad no puede ser negativa."); return; }
        for (int i = 0; i < count; i++) tree.insert(readInt(scanner, "Valor " + (i + 1) + ": "));
        System.out.println("InOrder: " + tree.inOrder());
        System.out.println("PreOrder: " + tree.preOrder());
        System.out.println("PostOrder: " + tree.postOrder());
        System.out.println("Altura: " + tree.height());
        System.out.println("FB raiz: " + tree.balanceFactor());
        System.out.print(tree.structure());
        int value = readInt(scanner, "Valor a eliminar (-1 para omitir): ");
        if (value != -1) {
            System.out.println("Eliminado: " + tree.delete(value));
            System.out.println("InOrder: " + tree.inOrder());
            System.out.print(tree.structure());
        }
    }

    private void showCycle(Scanner scanner) {
        System.out.print("ID inicial (ej. S02): ");
        String start = scanner.nextLine().trim().toUpperCase();
        if (graph.getVertex(start) == null) {
            System.out.println("Vertice inexistente: " + start + ". Usa un ID entre S01 y S12.");
            return;
        }
        try {
            List<String> cycle = DepthFirstSearch.cycleFrom(graph, start);
            System.out.println(cycle.isEmpty() ? "No se encontro ciclo desde ese vertice." : "Ciclo: " + cycle);
        } catch (IllegalArgumentException ex) {
            System.out.println("No se pudo buscar el ciclo: " + ex.getMessage());
        }
    }
}
