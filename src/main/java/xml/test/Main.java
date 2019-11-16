package xml.test;

import xml.XmlDeserializer;
import xml.XmlSerializer;

class Main {
    public static void main(String[] args) {
        var load = true;
        TestStructure testStructure;
        if (load) {
            testStructure = (TestStructure)XmlDeserializer.loadXml();
            testStructure.notify();
        } else {
            testStructure = new TestStructure();
            XmlSerializer.saveXml(testStructure);
        }
    }
}
