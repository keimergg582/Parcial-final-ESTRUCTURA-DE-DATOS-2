package com.mycompany.parcial_final_estructura_de_datosii_maven;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class BreadthFirstSearch {
    private BreadthFirstSearch() {}

    public static List<String> traverse(MixedGraph graph, String startId) {
        return buildTree(graph, startId).getOrder();
    }

    public static BFSTree buildTree(MixedGraph graph, String startId) {
        if (graph == null) throw new IllegalArgumentException("El grafo no puede ser null");
        if (graph.getVertex(startId) == null)
            throw new IllegalArgumentException("Vertice inexistente: " + startId);

        List<String> order = new ArrayList<>();
        Map<String, String> parents = new LinkedHashMap<>();
        Map<String, Integer> levels = new LinkedHashMap<>();
        Set<String> visited = new LinkedHashSet<>();
        Queue<String> queue = new ArrayDeque<>();

        visited.add(startId);
        parents.put(startId, null);
        levels.put(startId, 0);
        queue.offer(startId);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            order.add(current);
            for (Edge edge : graph.edgesFrom(current)) {
                String next = graph.neighbor(edge, current).getId();
                if (visited.add(next)) {
                    parents.put(next, current);
                    levels.put(next, levels.get(current) + 1);
                    queue.offer(next);
                }
            }
        }
        return new BFSTree(startId, order, parents, levels);
    }

    public static final class BFSTree {
        private final String root;
        private final List<String> order;
        private final Map<String, String> parents;
        private final Map<String, Integer> levels;

        private BFSTree(String root, List<String> order, Map<String, String> parents,
                        Map<String, Integer> levels) {
            this.root = root;
            this.order = List.copyOf(order);
            this.parents = Collections.unmodifiableMap(new LinkedHashMap<>(parents));
            this.levels = Collections.unmodifiableMap(new LinkedHashMap<>(levels));
        }
        public String getRoot() { return root; }
        public List<String> getOrder() { return order; }
        public Map<String, String> getParents() { return parents; }
        public Map<String, Integer> getLevels() { return levels; }
        public String getParent(String vertexId) { return parents.get(vertexId); }
        public Integer getLevel(String vertexId) { return levels.get(vertexId); }

        public List<String> pathTo(String vertexId) {
            if (!parents.containsKey(vertexId)) return List.of();
            List<String> path = new ArrayList<>();
            String current = vertexId;
            while (current != null) {
                path.add(current);
                current = parents.get(current);
            }
            Collections.reverse(path);
            return List.copyOf(path);
        }

        @Override public String toString() {
            StringBuilder sb = new StringBuilder("Arbol BFS desde ").append(root)
                    .append(System.lineSeparator());
            for (String id : order) {
                sb.append(id).append(" | padre=").append(parents.get(id))
                  .append(" | nivel=").append(levels.get(id)).append(System.lineSeparator());
            }
            return sb.toString();
        }
    }
}
