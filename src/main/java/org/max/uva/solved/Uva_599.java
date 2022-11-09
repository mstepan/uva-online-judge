package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 599 - The Forrest for the Trees
 */
public class Uva_599 {

    private Uva_599() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int testCases = Integer.parseInt(rd.readLine().trim());

            for (int test = 0; test < testCases; ++test) {

                DisjointSetsStrings sets = new DisjointSetsStrings();

                Set<String> vertexesInTree = new HashSet<>();

                while (true) {

                    String line = rd.readLine().trim();

                    if (line.charAt(0) == '*') {
                        break;
                    }

                    String v1 = String.valueOf(line.charAt(1));
                    String v2 = String.valueOf(line.charAt(3));

                    if (!v1.equals(v2)) {
                        vertexesInTree.add(v1);
                        vertexesInTree.add(v2);

                        sets.add(v1);
                        sets.add(v2);
                        sets.union(v1, v2);
                    }
                }

                String[] vertexes = rd.readLine().trim().split(",");

                int acornsCnt = 0;

                for (int i = 0; i < vertexes.length; ++i) {
                    if (!vertexesInTree.contains(vertexes[i])) {
                        ++acornsCnt;
                    }
                }

                out.printf("There are %d tree(s) and %d acorn(s).%n", sets.setsCount(), acornsCnt);
            }
            diff();
        }
    }

    private static final class DisjointSetsStrings {
        private final Map<String, NodeString> nodes = new HashMap<>();

        private int setsCount;

        int setsCount() {
            return setsCount;
        }

        boolean union(String v1, String v2) {
            NodeString main1 = findMainNode(v1);
            checkNotNull(main1, "null 'node' detected");

            NodeString main2 = findMainNode(v2);
            checkNotNull(main2, "null 'node' detected");

            if (main1 == main2) {
                return false;
            }

            if (main1.rank >= main2.rank) {
                main2.next = main1;
                main1.rank += main2.rank;
            }
            else {
                main1.next = main2;
                main2.rank += main1.rank;
            }

            --setsCount;

            return true;
        }

        void add(String value) {
            if (nodes.containsKey(value)) {
                return;
            }

            nodes.put(value, new NodeString(value));

            ++setsCount;
        }

        private NodeString findMainNode(String value) {
            NodeString singleNode = nodes.get(value);

            if (singleNode == null) {
                return null;
            }

            NodeString cur = singleNode;

            while (cur.next != null) {
                cur = cur.next;
            }

            compressPath(singleNode, cur);

            return cur;
        }

        private static void compressPath(NodeString node, NodeString representative) {
            NodeString cur = node;
            while (cur.next != null) {
                NodeString temp = cur.next;
                cur.next = representative;
                cur = temp;
            }

        }

        private static void checkNotNull(NodeString node, String msg) {
            if (node == null) {
                throw new IllegalStateException(msg);
            }
        }

        private static class NodeString {
            final String value;
            NodeString next;

            int rank;

            NodeString(String value) {
                this.value = value;
                this.rank = 1;
            }

            @Override
            public String toString() {
                return String.valueOf(value);
            }
        }
    }

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
            new Uva_599();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static class StreamGobbler implements Runnable {

        private InputStream inputStream;

        private Consumer<String> consumer;


        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
        }
    }

}
