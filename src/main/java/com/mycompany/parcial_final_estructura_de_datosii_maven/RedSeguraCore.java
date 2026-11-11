package com.mycompany.parcial_final_estructura_de_datosii_maven;

public final class RedSeguraCore {
    private RedSeguraCore() {}

    public static BinarySearchTree<String> createBinarySearchTree(MixedGraph graph) {
        BinarySearchTree<String> tree = new BinarySearchTree<>();
        graph.getVertices().stream().sorted(java.util.Comparator.comparing(Vertex::getId))
                .forEach(vertex -> tree.insert(vertex.getLabel()));
        return tree;
    }

    /** Crea un AVL vacio para que la capa de aplicacion no instancie estructuras directamente. */
    public static <T extends Comparable<T>> AvlTree<T> createAvlTree() {
        return new AvlTree<>();
    }

    public static AvlTree<String> createAvlTree(MixedGraph graph) {
        AvlTree<String> tree = createAvlTree();
        graph.getVertices().stream().sorted(java.util.Comparator.comparing(Vertex::getId))
                .forEach(vertex -> tree.insert(vertex.getLabel()));
        return tree;
    }

    public static MixedGraph createGraph() {
        MixedGraph g = new MixedGraph();

        g.addVertex(new Vertex("S01", "UAJS Sede C", "Universidad"));
        g.addVertex(new Vertex("S02", "UAJS Sede E", "Universidad"));
        g.addVertex(new Vertex("S03", "Universidad de Sucre", "Universidad"));
        g.addVertex(new Vertex("S04", "CECAR", "Universidad"));
        g.addVertex(new Vertex("S05", "Hospital Universitario", "Salud"));
        g.addVertex(new Vertex("S06", "Clinica Las Americas", "Salud"));
        g.addVertex(new Vertex("S07", "Estacion Central SETP", "Transporte"));
        g.addVertex(new Vertex("S08", "Terminal de Transporte", "Transporte"));
        g.addVertex(new Vertex("S09", "Parque Santander", "Punto de encuentro"));
        g.addVertex(new Vertex("S10", "Mercado de Sincelejo", "Comercio"));
        g.addVertex(new Vertex("S11", "PEP Argelia", "Paradero"));
        g.addVertex(new Vertex("S12", "Glorieta Sincelejito", "Movilidad"));

        g.addEdge(new Edge("R01", g.getVertex("S01"), g.getVertex("S07"), 4, "Corredor universitario", true));
        g.addEdge(new Edge("R02", g.getVertex("S07"), g.getVertex("S01"), 6, "Retorno por via alterna", true));
        g.addEdge(new Edge("R03", g.getVertex("S01"), g.getVertex("S09"), 5, "Corredor peatonal", false));
        g.addEdge(new Edge("R04", g.getVertex("S02"), g.getVertex("S01"), 8, "Conexion entre sedes", false));
        g.addEdge(new Edge("R05", g.getVertex("S02"), g.getVertex("S06"), 10, "Ruta de atencion", true));
        g.addEdge(new Edge("R06", g.getVertex("S06"), g.getVertex("S05"), 4, "Corredor clinico", true));
        g.addEdge(new Edge("R07", g.getVertex("S05"), g.getVertex("S09"), 6, "Via hospitalaria", false));
        g.addEdge(new Edge("R08", g.getVertex("S09"), g.getVertex("S10"), 3, "Centro historico", false));
        g.addEdge(new Edge("R09", g.getVertex("S10"), g.getVertex("S08"), 8, "Salida al terminal", true));
        g.addEdge(new Edge("R10", g.getVertex("S08"), g.getVertex("S10"), 9, "Ingreso desde terminal", true));
        g.addEdge(new Edge("R11", g.getVertex("S03"), g.getVertex("S12"), 4, "Corredor Puerta Roja", false));
        g.addEdge(new Edge("R12", g.getVertex("S12"), g.getVertex("S09"), 7, "Troncal al centro", false));
        g.addEdge(new Edge("R13", g.getVertex("S04"), g.getVertex("S11"), 6, "Conexion universitaria", false));
        g.addEdge(new Edge("R14", g.getVertex("S11"), g.getVertex("S07"), 8, "Ruta SETP al centro", true));
        g.addEdge(new Edge("R15", g.getVertex("S07"), g.getVertex("S09"), 3, "Transferencia central", false));
        g.addEdge(new Edge("R16", g.getVertex("S07"), g.getVertex("S08"), 12, "Ruta SETP al terminal", true));
        g.addEdge(new Edge("R17", g.getVertex("S09"), g.getVertex("S03"), 7, "Ruta hacia universidad", true));
        g.addEdge(new Edge("R18", g.getVertex("S12"), g.getVertex("S04"), 9, "Ruta hacia CECAR", true));

        return g;
    }
}
