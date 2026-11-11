package com.mycompany.parcial_final_estructura_de_datosii_maven;

import java.util.Locale;

public final class Medicion {
    private static final int[] TAMANOS = {1_000, 10_000, 100_000};
    private static final int REPETICIONES = 5;

    private Medicion() {}

    public static void main(String[] args) {
        System.out.println("n | ABB ms | AVL ms | Dijkstra ms");
        for (int n : TAMANOS) {
            String abbMs;
            try {
                abbMs = formato(promedioMs(() -> insertarEnAbb(n)));
            } catch (StackOverflowError ex) {
                // BinarySearchTree inserta recursivamente y una secuencia ordenada lo degrada a lista.
                abbMs = "desborde de pila";
            }

            double avl = promedioMs(() -> insertarEnAvl(n));
            double dijkstra = promedioMs(() -> ejecutarDijkstra(n));
            System.out.printf(Locale.ROOT, "%d | %s | %.3f | %.3f%n", n, abbMs, avl, dijkstra);
        }
    }

    private static void insertarEnAbb(int n) {
        BinarySearchTree<String> tree = new BinarySearchTree<>();
        for (int i = 0; i < n; i++) tree.insert(etiqueta(i));
    }

    private static void insertarEnAvl(int n) {
        AvlTree<String> tree = RedSeguraCore.createAvlTree();
        for (int i = 0; i < n; i++) tree.insert(etiqueta(i));
    }

    /** Crea un grafo dirigido en cadena y mide su construcción junto con Dijkstra. */
    private static void ejecutarDijkstra(int n) {
        MixedGraph graph = new MixedGraph();
        for (int i = 0; i < n; i++) {
            String id = idVertice(i);
            graph.addVertex(new Vertex(id, etiqueta(i), "Medicion"));
        }
        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(new Edge("E" + idVertice(i), graph.getVertex(idVertice(i)),
                    graph.getVertex(idVertice(i + 1)), 1, "Conexion de prueba", true));
        }
        DijkstraSolver.solve(graph, idVertice(0), idVertice(n - 1));
    }

    private static String etiqueta(int i) {
        return String.format(Locale.ROOT, "Etiqueta-%06d", i);
    }

    private static String idVertice(int i) {
        return String.format(Locale.ROOT, "V%06d", i);
    }

    /** Ejecuta la tarea una vez sin medir (calentamiento) y luego REPETICIONES veces midiendo. */
    private static double promedioMs(Runnable tarea) {
        tarea.run();
        long total = 0;
        for (int i = 0; i < REPETICIONES; i++) {
            long inicio = System.nanoTime();
            tarea.run();
            total += System.nanoTime() - inicio;
        }
        return total / (REPETICIONES * 1_000_000.0);
    }

    private static String formato(double milisegundos) {
        return String.format(Locale.ROOT, "%.3f", milisegundos);
    }
}
