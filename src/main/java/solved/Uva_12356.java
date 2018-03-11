package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * 12356 - Army Buddies
 */
public class Uva_12356 {


    private Uva_12356() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {
                String[] data = rd.readLine().split(" ");

                final int soldiers = Integer.parseInt(data[0]);
                final int reports = Integer.parseInt(data[1]);

                if (soldiers == 0 && reports == 0) {
                    break;
                }

                Node list = Node.create(soldiers);

                Node[] nodesArr = new Node[soldiers];

                Node cur = list.next;
                int index = 0;

                while (cur != list) {
                    nodesArr[index] = cur;
                    cur = cur.next;
                    ++index;
                }

                StringBuilder res = new StringBuilder();

                for (int i = 0; i < reports; ++i) {
                    String[] losesData = rd.readLine().split("\\s+");

                    final Node leftNode = nodesArr[Integer.parseInt(losesData[0]) - 1];
                    final Node rightNode = nodesArr[Integer.parseInt(losesData[1]) - 1];

                    String leftCh = leftNode.prev == list ? "*" : String.valueOf(leftNode.prev.value);
                    String rightCh = rightNode.next == list ? "*" : String.valueOf(rightNode.next.value);

                    res.append(leftCh).append(" ").append(rightCh).append("\n");

                    Node.deleteNodes(leftNode, rightNode);
                }

                res.append("-");

                out.println(res);

            }

            diff();
        }
    }

    private static final class Node {
        Node prev;
        Node next;
        final int value;

        Node(int value) {
            this.value = value;
        }

        static void deleteNodes(Node left, Node right) {

            Node prevLeft = left.prev;
            Node nextRight = right.next;

            prevLeft.next = nextRight;
            nextRight.prev = prevLeft;

            left.prev = null;
            right.next = null;
        }

        static Node create(int nodesCount) {
            Node sentinel = new Node(-1);
            sentinel.next = sentinel;
            sentinel.prev = sentinel;

            for (int i = 1; i <= nodesCount; ++i) {

                Node tail = sentinel.prev;

                Node node = new Node(i);
                node.next = sentinel;
                node.prev = tail;

                tail.next = node;
                sentinel.prev = node;

            }

            return sentinel;
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
            new Uva_12356();
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
