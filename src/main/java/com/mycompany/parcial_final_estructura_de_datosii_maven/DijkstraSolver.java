package com.mycompany.parcial_final_estructura_de_datosii_maven;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public final class DijkstraSolver {
    private DijkstraSolver() {}
    private record State(String vertexId, int distance) {}

    public static DijkstraResult solve(MixedGraph graph, String origin, String destination) {
        if (graph == null) throw new IllegalArgumentException("El grafo no puede ser null");
        if (graph.getVertex(origin) == null || graph.getVertex(destination) == null)
            throw new IllegalArgumentException("Origen o destino inexistente");

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();
        for (Vertex vertex : graph.getVertices()) distances.put(vertex.getId(), Integer.MAX_VALUE);
        distances.put(origin, 0);

        Comparator<State> stateOrder = Comparator.comparingInt(State::distance)
                .thenComparing(State::vertexId);
        PriorityQueue<State> queue = new PriorityQueue<>(stateOrder);
        queue.offer(new State(origin, 0));

        while (!queue.isEmpty()) {
            State state = queue.poll();
            if (state.distance() != distances.get(state.vertexId())) continue;

            List<Edge> outgoing = new ArrayList<>(graph.edgesFrom(state.vertexId()));
            outgoing.sort(Comparator.comparing(e -> graph.neighbor(e, state.vertexId()).getId()));
            for (Edge edge : outgoing) {
                String next = graph.neighbor(edge, state.vertexId()).getId();
                long candidateLong = (long) state.distance() + edge.getWeight();
                if (candidateLong > Integer.MAX_VALUE) continue;
                int candidate = (int) candidateLong;
                int known = distances.get(next);

                if (candidate < known) {
                    distances.put(next, candidate);
                    predecessors.put(next, state.vertexId());
                    queue.offer(new State(next, candidate));
                } else if (candidate == known && !next.equals(origin)) {
                    String oldParent = predecessors.get(next);
                    if (oldParent == null || state.vertexId().compareTo(oldParent) < 0) {
                        predecessors.put(next, state.vertexId());
                    }
                }
            }
        }

        int cost = distances.get(destination);
        List<String> path = reconstructPath(origin, destination, predecessors, cost);
        return new DijkstraResult(origin, destination, cost, path, distances, predecessors);
    }

    private static List<String> reconstructPath(String origin, String destination,
                                                Map<String, String> predecessors, int cost) {
        if (cost == Integer.MAX_VALUE) return List.of();
        List<String> reversed = new ArrayList<>();
        String current = destination;
        while (current != null) {
            reversed.add(current);
            if (current.equals(origin)) break;
            current = predecessors.get(current);
        }
        if (!reversed.get(reversed.size() - 1).equals(origin)) return List.of();
        Collections.reverse(reversed);
        return List.copyOf(reversed);
    }
}
