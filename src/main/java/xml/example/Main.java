package xml.example;

import xml.XmlDeserializer;
import xml.XmlSerializer;

class Main {
    public static void main(String[] args) {
        var load = true;
        TestStructure testStructure;
        //noinspection ConstantConditions
        if (load) {
            testStructure = (TestStructure)XmlDeserializer.loadXml("test.xml");
            XmlSerializer.saveXml(testStructure, "compare.xml");
        } else {
            testStructure = new TestStructure();
            XmlSerializer.saveXml(testStructure, "test.xml");
        }
    }
}
