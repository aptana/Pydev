package org.python.pydev.shared_core.structure;

import junit.framework.TestCase;

public class StringToIntCounterSmallSetTest extends TestCase {

    public void testStringToIntMap() throws Exception {
        StringToIntCounterSmallSet map = new StringToIntCounterSmallSet();
        int i = map.get("test");
        assertEquals(0, i);
        map.put("test", 2);
        assertEquals(2, map.get("test"));
        assertEquals(0, map.get("test_not_there"));
        map.put("test2", 4);
        map.put("test", 3);
        assertEquals(3, map.get("test"));
        assertEquals(4, map.get("test2"));
        map.clear();
        assertEquals(0, map.get("test"));
        assertEquals(0, map.get("test2"));
        assertEquals(0, map.get("test2"));

        assertEquals(1, map.increment("test2"));
        assertEquals(2, map.increment("test2"));
        assertEquals(2, map.get("test2"));
    }
}
