package xyz.redsmarty.redcore.nbt.tag;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Represents the possible types of nbt tags
 */
public enum NbtType {
    END(void.class, Void.class),
    BYTE(byte.class, Byte.class),
    SHORT(short.class, Short.class),
    INT(int.class, Integer.class),
    LONG(long.class, Long.class),
    FLOAT(float.class, Float.class),
    DOUBLE(double.class, Double.class),
    BYTE_ARRAY(byte[].class),
    STRING(String.class),
    LIST(List.class),
    COMPOUND(Map.class),
    INT_ARRAY(int[].class),
    LONG_ARRAY(long[].class);

    private final Class<?>[] classes;
    NbtType(Class<?>... classes) {
        this.classes = classes;
    }

    public Class<?>[] getClasses() {
        return classes;
    }

    public boolean isClass(Class<?> clazz) {
        return Arrays.asList(classes).contains(clazz);
    }
}
