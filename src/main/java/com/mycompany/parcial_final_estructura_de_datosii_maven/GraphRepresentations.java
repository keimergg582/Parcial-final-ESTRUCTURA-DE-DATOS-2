package com.mycompany.parcial_final_estructura_de_datosii_maven;

import java.util.ArrayList;
import java.util.List;

public final class GraphRepresentations {
    private GraphRepresentations() {}

    public static String adjacencyList(MixedGraph g) {
        StringBuilder sb = new StringBuilder();
        List<Vertex> vs = new ArrayList<>(g.getVertices());
        vs.sort((a,b) -> a.getId().compareTo(b.getId()));

        for (Vertex v : vs) {
            sb.append(v.getId()).append(" -> ");
            List<String> parts = new ArrayList<>();
            for (Edge e : g.outgoingEdges(v.getId())) {
                Vertex n = g.neighbor(e, v.getId());
                String arrow = e.isDirected() ? "->" : "--";
                parts.add(n.getId() + " (" + arrow + ", " + e.getWeight() + ")");
            }
            sb.append(String.join(", ", parts)).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public static int[][] adjacencyMatrix(MixedGraph g) {
        List<Vertex> vs = sortedVertices(g);
        int n = vs.size();
        int[][] m = new int[n][n];

        for (Edge e : g.getEdges()) {
            int i = indexOf(vs, e.getOrigin().getId());
            int j = indexOf(vs, e.getDestination().getId());
            m[i][j] = e.getWeight();
            if (!e.isDirected()) m[j][i] = e.getWeight();
        }
        return m;
    }

    public static int[][] incidenceMatrix(MixedGraph g) {
        List<Vertex> vs = sortedVertices(g);
        List<Edge> es = new ArrayList<>(g.getEdges());
        es.sort((a,b) -> a.getId().compareTo(b.getId()));

        int[][] m = new int[vs.size()][es.size()];

        for (int j = 0; j < es.size(); j++) {
            Edge e = es.get(j);
            int iOrigin = indexOf(vs, e.getOrigin().getId());
            int iDest = indexOf(vs, e.getDestination().getId());

            if (e.isDirected()) {
                m[iOrigin][j] = -1;
                m[iDest][j] = 1;
            } else {
                m[iOrigin][j] = 1;
                m[iDest][j] = 1;
            }
        }
        return m;
    }

    public static List<Vertex> sortedVertices(MixedGraph g) {
        List<Vertex> vs = new ArrayList<>(g.getVertices());
        vs.sort((a,b) -> a.getId().compareTo(b.getId()));
        return vs;
    }

    private static int indexOf(List<Vertex> vs, String id) {
        for (int i = 0; i < vs.size(); i++)
            if (vs.get(i).getId().equals(id)) return i;
        throw new IllegalArgumentException("Vertice inexistente: " + id);
    }

    /** Imprime la matriz de incidencia con una columna identificada por cada arista. */
    public static void printIncidenceMatrix(int[][] matrix, List<Vertex> vertices, MixedGraph graph) {
        List<Edge> edges = new ArrayList<>(graph.getEdges());
        edges.sort((a, b) -> a.getId().compareTo(b.getId()));
        System.out.printf("%6s", " ");
        for (Edge edge : edges) System.out.printf("%6s", edge.getId());
        System.out.println();

        for (int i = 0; i < matrix.length; i++) {
            System.out.printf("%6s", vertices.get(i).getId());
            for (int value : matrix[i]) System.out.printf("%6d", value);
            System.out.println();
        }
    }

    public static void printMatrix(int[][] matrix, List<Vertex> vertices) {
        System.out.print("       ");
        for (Vertex v : vertices) System.out.printf("%6s", v.getId());
        System.out.println();

        for (int i = 0; i < matrix.length; i++) {
            System.out.printf("%6s", vertices.get(i).getId());
            for (int value : matrix[i]) System.out.printf("%6d", value);
            System.out.println();
        }
    }
}
