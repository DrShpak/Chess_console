package xml.example;

import xml.XML;

import java.util.*;

@SuppressWarnings("ALL")
@XML
public class TestStructure {
    @XML
    public Object objEnum = E1.CC;
    @XML
    public Object primitive = 5f;
    @XML
    public Object arrobj = new Object[]{
            8
    };
    @XML
    public Object[] lol = {
            12,
            "String",
            new float[]{3.14f, 2.18f}
    };
    @XML
    public E1 ENUM = E1.BB;
    @XML
    public E1[] EARR = new E1[]{E1.AA, E1.BB, E1.CC};
    @XML
    public int[][] w = {{0, 0}, {0, 0}};
    @XML
    public Integer[][] n = {{1, 2}, {3, 4}};
    @XML
    public List<List<Integer>> test = new ArrayList<>();
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
    @XML
    private TestStructure self = this;

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

        var t = new ArrayList<Integer>();
        t.add(1);
        t.add(2);
        var t1 = new ArrayList<Integer>();
        t1.add(3);
        t1.add(4);
        test.add(t1);
        test.add(t);
    }

    TestStructure(boolean bool) {
        d = bool;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestStructure that = (TestStructure) o;
        return a == that.a &&
                Float.compare(that.b, b) == 0 &&
                Objects.deepEquals(objEnum, that.objEnum) &&
                Objects.deepEquals(primitive, that.primitive) &&
                Objects.deepEquals(arrobj, that.arrobj) &&
                Arrays.deepEquals(lol, that.lol) &&
                ENUM == that.ENUM &&
                Arrays.deepEquals(EARR, that.EARR) &&
                Arrays.deepEquals(w, that.w) &&
                Arrays.deepEquals(n, that.n) &&
                Objects.deepEquals(test, that.test) &&
                Objects.deepEquals(c, that.c) &&
                Objects.deepEquals(d, that.d) &&
                Objects.deepEquals(e, that.e) &&
                Arrays.deepEquals(e1, that.e1) &&
                Objects.deepEquals(e2, that.e2);
    }
}
