package xml;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Supplier;

public class XmlSerializerRegistry {
    private HashMap<Class, XmlSerializationStrategy> classes = new HashMap<>();

    public void addClass(Class clazz, Supplier generator, Field... fields) {
        if (!classes.containsKey(clazz)) {
            classes.put(clazz, new XmlSerializationStrategy(generator, fields));
        }
    }

    XmlSerializationStrategy getClassStrategy(Class clazz) {
        return classes.get(clazz);
    }

    static class XmlSerializationStrategy {
        private ArrayList<Field> fields = new ArrayList<>();
        private Supplier generator;

        XmlSerializationStrategy(Supplier generator, Field... fields) {
            this.fields.addAll(Arrays.asList(fields));
            this.generator = generator;
        }

        Field[] getFields() {
            return fields.toArray(Field[]::new);
        }

        Supplier getGenerator() {
            return generator;
        }
    }
}
