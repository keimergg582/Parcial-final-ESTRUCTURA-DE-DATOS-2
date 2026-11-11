package com.mycompany.parcial_final_estructura_de_datosii_maven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DepthFirstSearch {
    private DepthFirstSearch() {}

    public static List<String> traverse(MixedGraph graph, String startId) {
        if (graph == null) throw new IllegalArgumentException("El grafo no puede ser null");
        if (graph.getVertex(startId) == null)
            throw new IllegalArgumentException("Vertice inexistente: " + startId);
        List<String> order = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        dfs(graph, startId, visited, order);
        return order;
    }

    private static void dfs(MixedGraph graph, String current, Set<String> visited,
                            List<String> order) {
        visited.add(current);
        order.add(current);
        for (Edge edge : graph.edgesFrom(current)) {
            String next = graph.neighbor(edge, current).getId();
            if (!visited.contains(next)) dfs(graph, next, visited, order);
        }
    }

    /** Devuelve un ciclo alcanzable desde startId; lista vacía si no encuentra uno. */
    public static List<String> cycleFrom(MixedGraph graph, String startId) {
        if (graph == null) throw new IllegalArgumentException("El grafo no puede ser null");
        if (graph.getVertex(startId) == null)
            throw new IllegalArgumentException("Vertice inexistente: " + startId);
        Set<String> visited = new HashSet<>();
        Set<String> active = new HashSet<>();
        Map<String, String> parent = new HashMap<>();
        List<String> cycle = new ArrayList<>();
        findCycle(graph, startId, null, visited, active, parent, cycle);
        return List.copyOf(cycle);
    }

    private static boolean findCycle(MixedGraph graph, String current, String parentEdgeId,
                                     Set<String> visited, Set<String> active,
                                     Map<String, String> parent, List<String> cycle) {
        visited.add(current);
        active.add(current);
        for (Edge edge : graph.edgesFrom(current)) {
            if (!edge.isDirected() && edge.getId().equals(parentEdgeId)) continue;
            String next = graph.neighbor(edge, current).getId();
            if (!visited.contains(next)) {
                parent.put(next, current);
                if (findCycle(graph, next, edge.getId(), visited, active, parent, cycle)) return true;
            } else if (active.contains(next)) {
                cycle.add(next);
                String cursor = current;
                while (cursor != null && !cursor.equals(next)) {
                    cycle.add(cursor);
                    cursor = parent.get(cursor);
                }
                cycle.add(next);
                java.util.Collections.reverse(cycle);
                return true;
            }
        }
        active.remove(current);
        return false;
    }
}
