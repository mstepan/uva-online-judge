package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

/**
 * https://vjudge.net/problem/UVA-10243
 * <p>
 * Vertex coverage problem on a tree. Solve using DP technique.
 *
 * <p>
 * https://vjudge.net/problem
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_10243 {

    private Uva_10243() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {
                final int vertexesCount = Integer.parseInt(rd.readLine().trim());

                if (vertexesCount == 0) {
                    break;
                }

                Map<Integer, Set<Integer>> adjList = new HashMap<>();

                for (int verIndex = 0; verIndex < vertexesCount; ++verIndex) {
                    String[] singleAdjList = rd.readLine().trim().split("\\s+");
                    int srcVertex = verIndex + 1;

                    Set<Integer> adjValues = new HashSet<>();
                    adjList.put(srcVertex, adjValues);

                    for (int i = 1; i < singleAdjList.length; ++i) {
                        int destVertex = Integer.parseInt(singleAdjList[i]);
                        adjValues.add(destVertex);
                    }
                }

                GenericTree tree = GenericTree.build(adjList);

                out.println(minVertexCoverage(tree));
            }

            diff();
        }
    }

    private static int minVertexCoverage(GenericTree tree) {

        // single vertex min coverage equals '1'
        if (tree.root.children.isEmpty()) {
            return 1;
        }

        calculateMinCoverageCoverageRec(tree.root);
        return Math.min(tree.root.with, tree.root.without);
    }

    private static void calculateMinCoverageCoverageRec(GenericTree.Node cur) {

        // leaf node
        if (cur.children.isEmpty()) {
            cur.with = 1;
            cur.without = 0;
            return;
        }

        cur.children.forEach(Uva_10243::calculateMinCoverageCoverageRec);

        cur.with = 1 + cur.children.stream().mapToInt(node -> Math.min(node.with, node.without)).sum();

        cur.without = cur.children.stream().mapToInt(node -> node.with).sum();
    }

    private static final class GenericTree {

        private final Node root;

        GenericTree(Node root) {
            this.root = root;
        }

        static GenericTree build(Map<Integer, Set<Integer>> adjList) {

            Set<Integer> alreadyProcessed = new HashSet<>();
            Node root = new Node(findRoot(adjList), new ArrayList<>());
            alreadyProcessed.add(root.value);

            Queue<Node> q = new ArrayDeque<>();
            q.add(root);

            while (!q.isEmpty()) {

                Node cur = q.poll();

                Set<Integer> children = adjList.get(cur.value);

                for (int singleChild : children) {

                    if (alreadyProcessed.contains(singleChild)) {
                        continue;
                    }

                    Node childNode = new Node(singleChild, new ArrayList<>());
                    alreadyProcessed.add(singleChild);

                    cur.children.add(childNode);

                    q.add(childNode);
                }
            }


            return new GenericTree(root);
        }

        private static int findRoot(Map<Integer, Set<Integer>> adjList) {

            if (adjList.size() == 1) {
                return adjList.entrySet().iterator().next().getKey();
            }

            for (Map.Entry<Integer, Set<Integer>> entry : adjList.entrySet()) {
                if (entry.getValue().size() == 1) {
                    return entry.getKey();
                }
            }

            throw new IllegalStateException("Can't build tree");
        }

        private static class Node {
            final int value;
            int with;
            int without;

            final List<Node> children;

            Node(int value, List<Node> children) {
                this.value = value;
                this.children = children;
            }

            @Override
            public String toString() {
                return String.valueOf(value) + ", [" + children.size() + "]";
            }
        }

    }

    /*
    10243 Fire! Fire!! Fire!!!
    10717 Mint
    11512 GATTACA
    10065 Useless Tile Packers
     */


    //------------------------------------------------------------------------------------------------------------------
    // DEBUG part
    //------------------------------------------------------------------------------------------------------------------

    private static boolean DEBUG;

    private static InputStream createInput() throws IOException {
        if (DEBUG) {
            return Files.newInputStream(Paths.get("/Users/mstepan/repo/uva-online-judge/src/main/java/in.txt"));
        }
        return System.in;
    }

    private static PrintStream createOutput() throws IOException {
        if (DEBUG) {
            return new PrintStream(Files.newOutputStream(
                    Paths.get("/Users/mstepan/repo/uva-online-judge/src/main/java/out-actual.txt")));
        }
        return System.out;
    }

    private static void diff() throws IOException, InterruptedException {

        if (!DEBUG) {
            return;
        }

        Process process = Runtime.getRuntime()
                .exec(String.format("/usr/bin/diff %s %s",
                                    "/Users/mstepan/repo/uva-online-judge/src/main/java/out.txt",
                                    "/Users/mstepan/repo/uva-online-judge/src/main/java/out-actual.txt"));

        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);

        Thread th = new Thread(streamGobbler);
        th.start();
        th.join();

        process.waitFor();
    }

    public static void main(String[] args) {
        try {
            DEBUG = (args.length == 1);
            new Uva_10243();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static class StreamGobbler implements Runnable {

        private InputStream inputStream;

        private Consumer<String> consumer;


        StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
        }
    }

}
