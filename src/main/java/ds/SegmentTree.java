package ds;

import java.util.Objects;

public final class SegmentTree {

    private final int[] arr;
    private final int[] arrIndexInTree;

    private final int[] tree;

    public SegmentTree(int[] arr) {
        this.arr = Objects.requireNonNull(arr);
        this.arrIndexInTree = new int[arr.length];

        if (arr.length == 0) {
            this.tree = new int[0];
        }
        else {
            this.tree = new int[calculateHeapSize(arr.length)];
            buildTreeRecursively(0, 0, arr.length - 1);
        }
    }

    public int rangeMinQuery(int from, int to) {
        checkIndexes(from, to);

        if (from < 0 || from >= arr.length || to < 0 || to >= arr.length) {
            throw new IndexOutOfBoundsException("from = " + from + ", to = " + to);
        }

        return rmqRec(0, 0, arr.length - 1, from, to);
    }

    private int rmqRec(int treeIndex, int curFrom, int curTo, int from, int to) {

        if (from == curFrom && to == curTo) {
            return tree[treeIndex];
        }

        final int mid = (curFrom + curTo) / 2;

        int minValue = Integer.MAX_VALUE;

        // check left branch
        if (from <= mid) {
            minValue = rmqRec(leftChildIndex(treeIndex), curFrom, mid,
                    from, Math.min(to, mid));
        }

        final int rightBoundary = mid + 1;

        // check right branch
        if (to >= rightBoundary) {
            minValue = Math.min(minValue, rmqRec(rightChildIndex(treeIndex), rightBoundary, curTo,
                    Math.max(from, rightBoundary), to));
        }

        return minValue;
    }

    public void change(int index, int value) {
        if (index < 0 || index >= arr.length) {
            throw new ArrayIndexOutOfBoundsException("index: " + index + ", array length: " + arr.length);
        }

        arr[index] = value;

        int indexInTree = arrIndexInTree[index];
        tree[indexInTree] = value;

        while (indexInTree != 0) {
            int parentIndex = parentIndex(indexInTree);

            int leftChild = leftChildIndex(parentIndex);
            int rightChild = rightChildIndex(parentIndex);

            tree[parentIndex] = Math.min(tree[leftChild], tree[rightChild]);
            indexInTree = parentIndex;
        }
    }

    private void buildTreeRecursively(int index, int from, int to) {

        assert from <= to;

        if (from == to) {
            tree[index] = arr[from];
            arrIndexInTree[from] = index;
            return;
        }

        final int mid = (from + to) / 2;

        int leftChild = leftChildIndex(index);
        buildTreeRecursively(leftChild, from, mid);

        int rightChild = rightChildIndex(index);
        buildTreeRecursively(rightChild, mid + 1, to);

        tree[index] = Math.min(tree[leftChild], tree[rightChild]);
    }

    private static int calculateHeapSize(int arrLength) {
        int h = (int) (Math.ceil(Math.log10(arrLength) / Math.log10(2.0)));
        return (1 << (h + 1)) - 1;
    }

    private static int leftChildIndex(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    private static int rightChildIndex(int parentIndex) {
        return 2 * parentIndex + 2;
    }

    private static int parentIndex(int childIndex) {
        return (childIndex - 1) / 2;
    }

    private static void checkIndexes(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("from > to: from = " + from + ", to = " + to);
        }

    }
}
