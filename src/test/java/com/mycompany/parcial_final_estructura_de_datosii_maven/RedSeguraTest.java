package com.mycompany.parcial_final_estructura_de_datosii_maven;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RedSeguraTest {
    private MixedGraph graph;

    @BeforeEach
    void crearGrafo() {
        graph = RedSeguraCore.createGraph();
    }

    @Test
    void verticeS08TieneNombreCorrecto() {
        Vertex v = graph.getVertex("S08");
        assertEquals("Terminal de Transporte", v.getLabel());
    }

    @Test
    void rutaNormalDeControl() {
        DijkstraResult r = DijkstraSolver.solve(graph, "S02", "S08");
        assertEquals(24, r.getCost());
    }

    @Test
    void verticeInexistenteDevuelveNull() {
        assertNull(graph.getVertex("ZZ"));
    }

    @Test
    void pesoCeroLanzaExcepcion() {
        Vertex a = new Vertex("A", "Nodo A", "Prueba");
        Vertex b = new Vertex("B", "Nodo B", "Prueba");
        assertThrows(IllegalArgumentException.class,
                () -> new Edge("E1", a, b, 0, "prueba", false));
    }

    @Test
    void sinRutaCuandoS02QuedaAislado() {
        graph.setBlocked("R04", true);
        graph.setBlocked("R05", true);
        DijkstraResult r = DijkstraSolver.solve(graph, "S02", "S08");
        assertFalse(r.reachable());
    }

    @Test
    void pesoNegativoLanzaExcepcion() {
        Vertex a = new Vertex("A", "Nodo A", "Prueba");
        Vertex b = new Vertex("B", "Nodo B", "Prueba");
        assertThrows(IllegalArgumentException.class,
                () -> new Edge("E2", a, b, -5, "prueba", false));
    }

    @Test
    void trianguloNoDirigidoTieneCiclo() {
        MixedGraph t = new MixedGraph();
        t.addVertex(new Vertex("A", "Nodo A", "Prueba"));
        t.addVertex(new Vertex("B", "Nodo B", "Prueba"));
        t.addVertex(new Vertex("C", "Nodo C", "Prueba"));
        t.addEdge(new Edge("E1", t.getVertex("A"), t.getVertex("B"), 1, "ab", false));
        t.addEdge(new Edge("E2", t.getVertex("B"), t.getVertex("C"), 1, "bc", false));
        t.addEdge(new Edge("E3", t.getVertex("C"), t.getVertex("A"), 1, "ca", false));
        assertFalse(DepthFirstSearch.cycleFrom(t, "A").isEmpty());
    }

    @Test
    void arbolNoDirigidoNoTieneCiclo() {
        MixedGraph t = new MixedGraph();
        t.addVertex(new Vertex("A", "Nodo A", "Prueba"));
        t.addVertex(new Vertex("B", "Nodo B", "Prueba"));
        t.addVertex(new Vertex("C", "Nodo C", "Prueba"));
        t.addEdge(new Edge("E1", t.getVertex("A"), t.getVertex("B"), 1, "ab", false));
        t.addEdge(new Edge("E2", t.getVertex("B"), t.getVertex("C"), 1, "bc", false));
        assertTrue(DepthFirstSearch.cycleFrom(t, "A").isEmpty());
    }
}