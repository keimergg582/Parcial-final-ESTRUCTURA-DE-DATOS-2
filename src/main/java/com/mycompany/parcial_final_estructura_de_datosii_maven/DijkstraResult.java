package com.mycompany.parcial_final_estructura_de_datosii_maven;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class DijkstraResult {
    private final String origin;
    private final String destination;
    private final int cost;
    private final List<String> path;
    private final Map<String, Integer> distances;
    private final Map<String, String> predecessors;

    public DijkstraResult(String origin, String destination, int cost,
                          List<String> path, Map<String, Integer> distances,
                          Map<String, String> predecessors) {
        this.origin = origin;
        this.destination = destination;
        this.cost = cost;
        this.path = List.copyOf(path);
        this.distances = Collections.unmodifiableMap(distances);
        this.predecessors = Collections.unmodifiableMap(predecessors);
    }

    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public int getCost() { return cost; }
    public List<String> getPath() { return path; }
    public Map<String, Integer> getDistances() { return distances; }
    public Map<String, String> getPredecessors() { return predecessors; }

    public boolean reachable() { return cost != Integer.MAX_VALUE; }

    @Override public String toString() {
        if (!reachable()) return "Sin ruta entre " + origin + " y " + destination;
        return String.join(" -> ", path) + " | costo = " + cost + " min";
    }
}
