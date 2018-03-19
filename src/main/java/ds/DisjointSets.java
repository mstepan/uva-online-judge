package ds;

import java.util.HashMap;
import java.util.Map;

/**
 * Disjoint set data structure with path compression heuristic.
 */
public final class DisjointSets<T> {

    private final Map<T, Node<T>> nodes = new HashMap<>();
    private final Map<T, Integer> setSize = new HashMap<>();

    private int size;

    /**
     * time: O(1) amortized
     */
    public T findSet(T value) {
        Node<T> mainNode = findMainNode(value);
        return mainNode == null ? null : mainNode.value;
    }

    /**
     * time: O(1) amortized
     */
    public boolean isSameSet(T v1, T v2) {
        return findMainNode(v1) == findMainNode(v2);
    }

    /**
     * time: O(1) amortized
     */
    public boolean union(T v1, T v2) {
        Node<T> main1 = findMainNode(v1);
        checkNotNull(main1, "null 'node' detected");

        Node<T> main2 = findMainNode(v2);
        checkNotNull(main2, "null 'node' detected");

        if (main1 == main2) {
            return false;
        }

        if (main1.rank >= main2.rank) {
            main2.next = main1;
            main1.rank += main2.rank;
            updateSetSize(main1, main2);
        }
        else {
            main1.next = main2;
            main2.rank += main1.rank;
            updateSetSize(main2, main1);
        }

        --size;

        return true;
    }

    /**
     * time: O(1)
     */
    public void add(T value) {
        if (nodes.containsKey(value)) {
            return;
        }

        nodes.put(value, new Node<>(value));
        setSize.put(value, 1);

        ++size;
    }

    /**
     * time: O(1)
     *
     * @return total number of disjoint sets.
     */
    public int size() {
        return size;
    }

    /**
     * time: O(1)
     */
    public int sizeOfSet(T value) {

        Node<T> mainNode = findMainNode(value);
        checkNotNull(mainNode, "null 'main' node detected for value = " + value);

        Integer curSetSize = setSize.get(mainNode.value);
        checkNotNull(curSetSize, "setSize is null");
        return curSetSize;
    }

    private void updateSetSize(Node<T> base, Node<T> other) {
        int baseSize = setSize.get(base.value) + setSize.get(other.value);
        setSize.put(base.value, baseSize);
    }

    private Node<T> findMainNode(T value) {
        Node<T> singleNode = nodes.get(value);

        if (singleNode == null) {
            return null;
        }

        Node<T> cur = singleNode;

        while (cur.next != null) {
            cur = cur.next;
        }

        compressPath(singleNode, cur);

        return cur;
    }

    private static <U> void compressPath(Node<U> node, Node<U> representative) {
        Node<U> cur = node;
        while (cur.next != null) {
            Node<U> temp = cur.next;
            cur.next = representative;
            cur = temp;
        }

    }

    private static <U> void checkNotNull(U value, String msg) {
        if (value == null) {
            throw new IllegalStateException(msg);
        }
    }

    private static class Node<U> {
        final U value;
        Node<U> next;

        /* represents upper bound, will be inaccurate( aka smaller) after path compression */
        int rank;

        Node(U value) {
            this.value = value;
            this.rank = 1;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
