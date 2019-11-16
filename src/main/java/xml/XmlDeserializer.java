package xml;

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class XmlDeserializer {
    public static Object loadXml() {
        var xmlReader = new XmlNodeReader("test.xml");
        var xmlObject = xmlReader.load();
        return loadObject(xmlObject);
    }

    private static Object loadObject(XmlNode xmlDescription) {
        var clazzCanonicalName = xmlDescription.getAttribute("class");
        if (clazzCanonicalName == null) {
            return null;
        }
        Class<?> clazz;
        try {
            clazz = Class.forName(clazzCanonicalName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        if (!clazz.isAnnotationPresent(XML.class)) {
            throw new IllegalStateException(clazz + " isn`t annotated with @xml.XML");
        }

        Object object;
        try {
            object = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        var loadableFields = Arrays.stream(XmlSerializer.collectFields(clazz)).
                filter(x -> x.isAnnotationPresent(XML.class)).
                collect(Collectors.toList());
        loadableFields.forEach(x -> loadField(object, x, xmlDescription));
        return object;
    }

    private static void loadField(Object target, Field field, XmlNode parent) {
        var xmlDescription = parent.getChildNode(field.getName());
        var fieldValue = loadAtomic(field.getType(), xmlDescription);
        setFieldValue(target, fieldValue, field);
    }

    private static Object loadAtomic(Class<?> clazz, XmlNode xmlDescription) {
        if (xmlDescription.getNodeName() == null) {
            return null;
        }
        if (ClassUtils.isPrimitiveOrWrapper(clazz) || clazz == String.class) {
            return loadPrimitive(clazz, xmlDescription);
        } else if (clazz.isEnum()) {
            return loadEnum(clazz, xmlDescription);
        } else if (clazz.isArray()) {
            return loadArray(clazz, xmlDescription);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            return loadCollection(clazz, xmlDescription);
        } else if (Map.class.isAssignableFrom(clazz)) {
            return loadMap(clazz, xmlDescription);
        } else {
            return loadComplexObject(xmlDescription);
        }
    }

    private static Object loadPrimitive(Class<?> clazz, XmlNode xmlDescription) {
        var value = xmlDescription.getNodeValue();
        if( Boolean.class == clazz || Boolean.TYPE == clazz ) return Boolean.parseBoolean( value );
        if( Byte.class == clazz || Byte.TYPE == clazz ) return Byte.parseByte( value );
        if( Short.class == clazz || Short.TYPE == clazz ) return Short.parseShort( value );
        if( Integer.class == clazz || Integer.TYPE == clazz ) return Integer.parseInt( value );
        if( Long.class == clazz || Long.TYPE == clazz ) return Long.parseLong( value );
        if( Float.class == clazz || Float.TYPE == clazz ) return Float.parseFloat( value );
        if( Double.class == clazz || Double.TYPE == clazz ) return Double.parseDouble( value );
        return value;
    }

    private static Object loadEnum(Class<?> clazz, XmlNode xmlDescription) {
        try {
            return clazz.
                    getMethod("valueOf", String.class).
                    invoke(null, xmlDescription.getNodeValue());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static Object loadArray(Class<?> clazz, XmlNode xmlDescription) {
        var c_type = clazz.getComponentType();
        var items = xmlDescription.getChildNodes("item");
        var value = (Object[])Array.newInstance(c_type, items.length);
        for (int i = 0; i < items.length; i++) {
            value[i] = loadAtomic(getClassInformation(items[i]), items[i]);
        }
        return value;
    }

    private static Object loadCollection(Class<?> clazz, XmlNode xmlDescription) {
        try {
            var value = (Collection)clazz.getConstructor().newInstance();
            Arrays.stream(xmlDescription.getChildNodes("item")).
                    forEach(x -> value.add(loadAtomic(getClassInformation(x), x)));
            return value;
        } catch (Exception e) {
           throw new IllegalStateException(e);
        }
    }

    private static Object loadMap(Class<?> clazz, XmlNode xmlDescription) {
        try {
            var value = (Map)clazz.getConstructor().newInstance();
            Arrays.stream(xmlDescription.getChildNodes("item")).
                    forEach(x -> {
                        var key = x.getChildNode("key");
                        var val = x.getChildNode("value");
                        value.put(
                                loadAtomic(getClassInformation(key), key),
                                loadAtomic(getClassInformation(val), val)
                        );
                    });
            return value;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static Object loadComplexObject(XmlNode xmlDescription) {
        return loadObject(xmlDescription);
    }

    private static Class<?> getClassInformation(XmlNode xmlDescription) {
        try {
            return Class.forName(xmlDescription.getAttribute("class"));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private static synchronized void setFieldValue(Object target, Object value, Field field) {
        try {
            var oldAccessibleState = field.canAccess(target);
            var oldModifiers = field.getModifiers();
            var modifiers = Field.class.getDeclaredField("modifiers");

            modifiers.setAccessible(true);
            modifiers.setInt(field, oldModifiers & ~Modifier.FINAL);
            field.setAccessible(true);
            field.set(target, value);
            field.setAccessible(oldAccessibleState);
            modifiers.setInt(field, oldModifiers);
            modifiers.setAccessible(false);
        } catch (Exception e) {
            throw new IllegalStateException("wtf");
        }
    }
}
