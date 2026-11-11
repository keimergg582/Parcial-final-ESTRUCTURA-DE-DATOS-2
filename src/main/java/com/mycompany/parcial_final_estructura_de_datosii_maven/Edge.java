package com.mycompany.parcial_final_estructura_de_datosii_maven;

public final class Edge {
    private final String id;
    private final Vertex origin;
    private final Vertex destination;
    private final int weight;
    private final String label;
    private final boolean directed;

    public Edge(String id, Vertex origin, Vertex destination, int weight,
                String label, boolean directed) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID de conexion invalido");
        if (origin == null || destination == null) throw new IllegalArgumentException("Vertices invalidos");
        if (weight <= 0) throw new IllegalArgumentException("El peso debe ser mayor que cero");
        if (label == null || label.isBlank()) throw new IllegalArgumentException("Etiqueta de conexion invalida");
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
        this.label = label;
        this.directed = directed;
    }

    public String getId() { return id; }
    public Vertex getOrigin() { return origin; }
    public Vertex getDestination() { return destination; }
    public int getWeight() { return weight; }
    public String getLabel() { return label; }
    public boolean isDirected() { return directed; }

    public Vertex other(Vertex v) {
        if (directed) {
            if (origin.equals(v)) return destination;
            throw new IllegalArgumentException("Una arista dirigida no puede recorrerse desde este vertice");
        }
        if (origin.equals(v)) return destination;
        if (destination.equals(v)) return origin;
        throw new IllegalArgumentException("El vertice no pertenece a la arista");
    }

    @Override public String toString() {
        return id + ": " + origin.getId() + (directed ? " -> " : " -- ")
                + destination.getId() + " [" + weight + " min] " + label;
    }
}
