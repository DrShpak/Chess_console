package xml.example;

import com.google.common.collect.Streams;
import xml.XmlDeserializer;
import xml.XmlSerializer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

class Main {
    public static void main(String[] args) {
        var load = true;
        TestStructure testStructure;
        //noinspection ConstantConditions
        if (load) {
            testStructure = (TestStructure)XmlDeserializer.loadXml("test.xml");
            XmlSerializer.saveXml(testStructure, "compare.xml");
            try {
                List<String> list1 = Files.readAllLines(Paths.get("compare.xml"));
                //noinspection UnstableApiUsage
                System.out.println(Streams.mapWithIndex(Files.readAllLines(Paths.get("test.xml")).stream(),
                    (x, i) -> Streams.mapWithIndex(list1.stream(),
                        (y, j) -> x.equals(y) && i == j).anyMatch(z -> z)).allMatch(z -> z));
                System.out.println(Objects.deepEquals(
                    Files.readAllLines(Paths.get("compare.xml")).toArray(),
                    Files.readAllLines(Paths.get("test.xml")).toArray()
                ));
            } catch (Exception e) {
                System.out.println("Something went wrong :(");
            }
        } else {
            testStructure = new TestStructure();
            XmlSerializer.saveXml(testStructure, "test.xml");
        }
    }
}
