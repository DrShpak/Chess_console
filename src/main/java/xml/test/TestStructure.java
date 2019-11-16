package xml.test;

import xml.XML;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("ALL")
@XML
public class TestStructure {
    @XML
    public int a = 1;
    @XML
    private float b = 1.5f;
    @XML
    private String c = "TEST";
    @XML
    private Boolean d = true;
    @XML
    private TestStructure e;
    @XML
    private TestStructure[] e1;
    @XML
    private HashMap<Integer, TestStructure> e2;

    public TestStructure() {
        e = new TestStructure(true);

        var l1 = new ArrayList<TestStructure>();
        var l2 = new HashMap<Integer, TestStructure>();

        for (int i = 0; i < 2; i++) {
            l1.add(new TestStructure(false));
            l2.put(i, new TestStructure(true));
        }

        e1 = l1.toArray(TestStructure[]::new);
        e2 = l2;
    }

    TestStructure(boolean bool) {
        d = bool;
    }
}
