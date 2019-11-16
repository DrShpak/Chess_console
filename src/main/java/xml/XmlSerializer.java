package xml;

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class XmlSerializer {
    public static void saveXml(Object object) {
        var xmlObject = new XmlNode("root");
        saveObject(object, xmlObject);

        var xmlWriter = new XmlNodeWriter("test.xml");
        xmlWriter.save(xmlObject);
    }

    private static void saveObject(Object object, XmlNode xmlDescription) {
        var clazz = object.getClass();
        if (!clazz.isAnnotationPresent(XML.class)) {
            throw new IllegalStateException(object.getClass() + " isn`t annotated with @xml.XML");
        }
        xmlDescription.appendAttribute("class", clazz.getCanonicalName());
        var savableFields = Arrays.stream(collectFields(clazz)).
                filter(x -> x.isAnnotationPresent(XML.class)).
                collect(Collectors.toList());
        savableFields.forEach(x -> saveField(object, x, xmlDescription));
    }

    private static void saveField(Object target, Field field, XmlNode parent) {
        var xmlDescription = new XmlNode(field.getName(), parent);
        var fieldValue = getFieldValue(target, field);
        saveAtomic(fieldValue, xmlDescription);
    }

    private static void saveAtomic(Object target, XmlNode xmlDescription) {
        if (target == null) {
            savePrimitive("", xmlDescription);
            return;
        }
        var objectClazz = target.getClass();
        if (ClassUtils.isPrimitiveOrWrapper(objectClazz) || objectClazz == String.class) {
            savePrimitive(target, xmlDescription);
        } else if (objectClazz.isEnum()) {
            saveEnum(target, xmlDescription);
        } else if (objectClazz.isArray()) {
            saveArray(target, xmlDescription);
        } else if (Collection.class.isAssignableFrom(objectClazz)) {
            saveCollection(target, xmlDescription);
        } else if (Map.class.isAssignableFrom(objectClazz)) {
            saveMap(target, xmlDescription);
        } else {
            saveComplexObject(target, xmlDescription);
        }
    }

    private static void savePrimitive(Object target, XmlNode xmlDescription) {
        xmlDescription.setValue(target.toString());
    }

    private static void saveEnum(Object target, XmlNode xmlDescription) {
        xmlDescription.setValue(((Enum<?>)target).name());
    }

    private static void saveArray(Object target, XmlNode xmlDescription) {
        for (Object o : ((Object[]) target)) {
            saveAtomic(o, new XmlNode(
                    "item",
                    "class",
                    o.getClass().getCanonicalName(),
                    xmlDescription
            ));
        }
    }

    private static void saveCollection(Object target, XmlNode xmlDescription) {
        ((Collection<?>)target).
                forEach(x -> saveAtomic(x, new XmlNode(
                            "item",
                            "class",
                            x.getClass().getCanonicalName(),
                            xmlDescription
                            )
                        )
                );
    }

    private static void saveMap(Object target, XmlNode xmlDescription) {
        ((Map<?, ?>)target).forEach((k, v) -> {
                    var itemXmlDescription = new XmlNode("item", xmlDescription);
                    var keyXmlDescription = new XmlNode(
                            "key",
                            "class",
                            k.getClass().getCanonicalName(),
                            itemXmlDescription
                    );
                    var valueXmlDescription = new XmlNode(
                            "value",
                            "class",
                            v.getClass().getCanonicalName(),
                            itemXmlDescription
                    );
                    saveAtomic(k, keyXmlDescription);
                    saveAtomic(v, valueXmlDescription);
                }
        );
    }

    private static void saveComplexObject(Object target, XmlNode xmlDescription) {
        saveObject(target, xmlDescription);
    }

    private static synchronized Object getFieldValue(Object target, Field field) {
        var oldAccessibleState = field.canAccess(target);
        field.setAccessible(true);
        Object value;
        try {
            value = field.get(target);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("wtf");
        }
        field.setAccessible(oldAccessibleState);
        return value;
    }

    static Field[] collectFields(Class<?> clazz) {
        var fields = new ArrayList<Field>();
        collectFields(clazz, fields);
        return fields.toArray(Field[]::new);
    }

    private static void collectFields(Class<?> clazz, ArrayList<Field> fields) {
        if (clazz == Object.class) {
            return;
        }
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        collectFields(clazz.getSuperclass(), fields);
    }
}
