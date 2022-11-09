package ds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class DisjointSetsTest {


    @Test
    public void unionAndFindSet() {
        DisjointSets<String> sets = new DisjointSets<>();
        sets.add("A");
        sets.add("B");
        sets.add("C");
        sets.add("D");
        sets.add("E");

        sets.add("F");
        sets.add("G");

        assertEquals(7, sets.size());

        sets.union("A", "B");
        assertEquals(6, sets.size());

        sets.union("C", "D");
        assertEquals(5, sets.size());
        sets.union("C", "E");
        assertEquals(4, sets.size());

        assertEquals("A", sets.findSet("A"));
        assertEquals("A", sets.findSet("B"));

        assertEquals("C", sets.findSet("C"));
        assertEquals("C", sets.findSet("D"));
        assertEquals("C", sets.findSet("E"));

        assertEquals("F", sets.findSet("F"));
        assertEquals("G", sets.findSet("G"));
        assertEquals(4, sets.size());

        assertEquals(2, sets.sizeOfSet("A"));
        assertEquals(2, sets.sizeOfSet("B"));

        assertEquals(3, sets.sizeOfSet("C"));
        assertEquals(3, sets.sizeOfSet("D"));
        assertEquals(3, sets.sizeOfSet("E"));

        assertEquals(1, sets.sizeOfSet("F"));
        assertEquals(1, sets.sizeOfSet("G"));
    }


}
