package com.mycompany.parcial_final_estructura_de_datosii_maven;

import java.util.Objects;

public final class Vertex {
    private final String id;
    private final String label;
    private final String type;

    public Vertex(String id, String label, String type) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID invalido");
        if (label == null || label.isBlank()) throw new IllegalArgumentException("Etiqueta invalida");
        if (type == null || type.isBlank()) throw new IllegalArgumentException("Tipo invalido");
        this.id = id;
        this.label = label;
        this.type = type;
    }

    public String getId() { return id; }
    public String getLabel() { return label; }
    public String getType() { return type; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex v)) return false;
        return id.equals(v.id);
    }

    @Override public int hashCode() { return Objects.hash(id); }

    @Override public String toString() { return id + " - " + label; }
}
