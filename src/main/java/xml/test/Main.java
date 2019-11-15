package xml.test;

import xml.XmlNodeReader;
import xml.XmlSerializer;

class Main {
    public static void main(String[] args) {
        /*var testStructure = new TestStructure();
        XmlSerializer.saveXml(testStructure);*/
        var testLoader = new XmlNodeReader("test.xml");
        var load = testLoader.load();
    }
}
