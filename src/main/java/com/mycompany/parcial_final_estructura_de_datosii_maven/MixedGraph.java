package com.mycompany.parcial_final_estructura_de_datosii_maven;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MixedGraph {
    private final Map<String, Vertex> vertices = new HashMap<>();
    private final Map<String, Edge> edges = new HashMap<>();
    private final Map<String, List<Edge>> outgoing = new HashMap<>();
    private final Map<String, Boolean> blocked = new HashMap<>();

    public void addVertex(Vertex v) {
        if (vertices.containsKey(v.getId()))
            throw new IllegalArgumentException("Vertice duplicado: " + v.getId());
        vertices.put(v.getId(), v);
        outgoing.put(v.getId(), new ArrayList<>());
    }

    public void addEdge(Edge e) {
        if (!vertices.containsKey(e.getOrigin().getId()) || !vertices.containsKey(e.getDestination().getId()))
            throw new IllegalArgumentException("La conexion usa vertices inexistentes");
        if (edges.containsKey(e.getId()))
            throw new IllegalArgumentException("Conexion duplicada: " + e.getId());

        edges.put(e.getId(), e);
        outgoing.get(e.getOrigin().getId()).add(e);

        if (!e.isDirected()) {
            // Se conserva una sola Edge; la lista de salida permite recorrerla en ambos sentidos.
            outgoing.get(e.getDestination().getId()).add(e);
        }
        blocked.put(e.getId(), false);
    }

    public Vertex getVertex(String id) { return vertices.get(id); }

    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(new ArrayList<>(vertices.values()));
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(new ArrayList<>(edges.values()));
    }

    public List<Edge> outgoingEdges(String vertexId) {
        List<Edge> result = outgoing.get(vertexId);
        if (result == null) throw new IllegalArgumentException("Vertice inexistente: " + vertexId);
        return Collections.unmodifiableList(result);
    }

    public Vertex neighbor(Edge e, String currentId) {
        Vertex current = getVertex(currentId);
        if (current == null) throw new IllegalArgumentException("Vertice inexistente: " + currentId);
        return e.other(current);
    }

    public boolean isBlocked(String edgeId) {
        if (!blocked.containsKey(edgeId)) throw new IllegalArgumentException("Conexion inexistente: " + edgeId);
        return blocked.get(edgeId);
    }

    public void setBlocked(String edgeId, boolean value) {
        if (!blocked.containsKey(edgeId)) throw new IllegalArgumentException("Conexion inexistente: " + edgeId);
        blocked.put(edgeId, value);
    }

    public void block(String edgeId) { setBlocked(edgeId, true); }
    public void unblock(String edgeId) { setBlocked(edgeId, false); }

    public List<Edge> edgesFrom(String vertexId) {
        List<Edge> result = new ArrayList<>();
        for (Edge e : outgoingEdges(vertexId)) {
            if (!isBlocked(e.getId())) result.add(e);
        }
        return result;
    }
}
