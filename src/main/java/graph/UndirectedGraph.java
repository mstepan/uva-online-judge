package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class UndirectedGraph {

    private final Map<String, Set<String>> representation = new HashMap<>();

    /**
     * Parse graph representation from String.
     * Example for 'graphRepr' format: "1-3,1-2,2-3,2-4"
     */
    public static UndirectedGraph parse(String graphRepr) {
        UndirectedGraph graph = new UndirectedGraph();

        String[] edges = graphRepr.trim().split(",");

        for (String singleEdge : edges) {
            String[] vertexesOfEdge = singleEdge.split("-");
            graph.addEdge(vertexesOfEdge[0].trim(), vertexesOfEdge[1].trim());
        }

        return graph;
    }

    public Set<String> adjacent(String ver) {
        if (!representation.containsKey(ver)) {
            throw new IllegalStateException("Can't find vertex '" + ver + "' in graph.");
        }
        return representation.get(ver);
    }

    public Set<String> vertexes() {
        return representation.keySet();
    }

    private void addEdge(String ver1, String ver2) {
        addEdgeSafely(ver1, ver2);
        addEdgeSafely(ver2, ver1);
    }


    private void addEdgeSafely(String ver1, String ver2) {
        representation.compute(ver1, (notUsed, neighbours) -> neighbours == null ? new HashSet<>() : neighbours).add(ver2);
    }
}
