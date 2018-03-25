package ds;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

public class SegmentTreeTest {

    @Test
    public void rangeMinimumQueryWithUpdates() {
        int[] arr = {10, 2, 4, 7, 3, 7, 9, 1, 98, 21};
        SegmentTree tree = new SegmentTree(arr);
        assertAllMinRangeQueriesValid(arr, tree);
        tree.change(8, -5);
        assertAllMinRangeQueriesValid(arr, tree);
    }

    @Test
    public void rangeMinimumQueryWithUpdatesRandomArrays() {

        final Random rand = ThreadLocalRandom.current();

        for (int it = 0; it < 10; ++it) {

            int[] arr = new int[17 + rand.nextInt(100)];

            for (int i = 0; i < arr.length; ++i) {
                arr[i] = rand.nextInt(100);
            }

            SegmentTree tree = new SegmentTree(arr);
            assertAllMinRangeQueriesValid(arr, tree);

            // do random updates
            for (int updates = 0; updates < 100; ++updates) {
                int index = rand.nextInt(arr.length);
                int newValue = rand.nextInt(100);

                arr[index] = newValue;
                tree.change(index, newValue);

                assertAllMinRangeQueriesValid(arr, tree);
            }
        }
    }

    private static void assertAllMinRangeQueriesValid(int[] arr, SegmentTree tree) {
        for (int i = 0; i < arr.length; ++i) {
            for (int j = i; j < arr.length; ++j) {
                assertEquals("RMQ failed for range: [" + i + "; " + j + "]",
                        findMinValue(arr, i, j),
                        tree.rangeMinQuery(i, j));
            }
        }
    }

    private static int findMinValue(int[] arr, int from, int to) {

        int minValue = arr[from];

        for (int i = from + 1; i <= to; ++i) {
            minValue = Math.min(minValue, arr[i]);
        }

        return minValue;
    }

}
