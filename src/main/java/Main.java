import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

/**
 * https://www.hackerrank.com/challenges/cut-the-tree/problem
 */
public class Main {

    private Main() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int vertexesCnt = Integer.parseInt(rd.readLine().trim());

            final TreeNode[] treeNodes = createNodes(rd.readLine().trim().split("\\s+"));

            final int totalSum = sum(treeNodes);

            boolean[][] adjMatrix = buildAdjMatrix(rd, vertexesCnt);

            buildRootedTree(adjMatrix, treeNodes);

            final int minCutSum = findMinCutSum(treeNodes[0], totalSum);

            out.println(minCutSum);

            diff();
        }
    }

    private static boolean[][] buildAdjMatrix(BufferedReader rd, int vertexesCnt) throws IOException {
        boolean[][] adjMatrix = new boolean[vertexesCnt][vertexesCnt];

        for (int i = 0; i < vertexesCnt - 1; ++i) {
            String[] edge = rd.readLine().trim().split("\\s+");

            int index1 = Integer.parseInt(edge[0]) - 1;
            int index2 = Integer.parseInt(edge[1]) - 1;

            adjMatrix[index1][index2] = true;
            adjMatrix[index2][index1] = true;
        }

        return adjMatrix;
    }

    private static void buildRootedTree(boolean[][] adjMatrix, TreeNode[] allNodes) {

        Set<TreeNode> marked = new HashSet<>();
        marked.add(allNodes[0]);

        Queue<TreeNode> q = new ArrayDeque<>();
        q.add(allNodes[0]);

        while (!q.isEmpty()) {

            TreeNode cur = q.poll();

            boolean[] adjElems = adjMatrix[cur.index];

            for (int adjVer = 0; adjVer < adjElems.length; ++adjVer) {

                if (adjElems[adjVer]) {
                    TreeNode otherNode = allNodes[adjVer];

                    if (!marked.contains(otherNode)) {

                        cur.addChildNode(otherNode);

                        marked.add(otherNode);
                        q.add(otherNode);
                    }
                }
            }
        }
    }

    private static TreeNode[] createNodes(String[] vertexesValues) {

        TreeNode[] treeNodes = new TreeNode[vertexesValues.length];

        for (int i = 0; i < vertexesValues.length; ++i) {
            String value = vertexesValues[i];
            treeNodes[i] = new TreeNode(i, Integer.parseInt(value));
        }

        return treeNodes;
    }

    private int findMinCutSum(TreeNode treeNode, int totalSum) {

        if (treeNode.isLeaf()) {
            return Integer.MAX_VALUE;
        }

        int minCutValue = Integer.MAX_VALUE;

        for (TreeNode child : treeNode.children) {

            int curCutValue = findMinCutSum(child, totalSum);

            minCutValue = Math.min(curCutValue, minCutValue);
        }

        for (TreeNode child : treeNode.children) {
            int firstTreeValue = totalSum - child.value;
            int secondTreeValue = child.value;
            minCutValue = Math.min(minCutValue, Math.abs(firstTreeValue - secondTreeValue));
        }

        treeNode.value += sum(treeNode.children);

        return minCutValue;
    }

    private static int sum(TreeNode[] nodesArr) {
        return Arrays.stream(nodesArr).mapToInt(treeNode -> treeNode.value).sum();
    }

    private static int sum(List<TreeNode> nodesList) {
        return nodesList.stream().mapToInt(treeNode -> treeNode.value).sum();
    }

    private static final class TreeNode {

        int index;
        int value;
        final List<TreeNode> children = new ArrayList<>();

        TreeNode(int index, int value) {
            this.index = index;
            this.value = value;
        }

        boolean isLeaf() {
            return children.isEmpty();
        }

        void addChildNode(TreeNode treeNode) {
            children.add(treeNode);
        }

        @Override
        public String toString() {
            return String.valueOf(value);
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
                .exec(java.lang.String.format("/usr/bin/diff %s %s",
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
            new Main();
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
