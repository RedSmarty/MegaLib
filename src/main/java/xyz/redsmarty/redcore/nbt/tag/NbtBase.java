package xyz.redsmarty.redcore.nbt.tag;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface NbtBase {
    /**
     * Get the literal value of the tag
     * @return The literal value
     * */
    Object getValue();

    /**
     * Get the type of the tag
     * @return type of the tag
     * */
    NbtType getType();

    JsonElement toJson();

    static NbtType getNbtType(Object value) {
        for (NbtType nbtType : NbtType.values()) {
            if (nbtType.isClass(value.getClass())) {
                return nbtType;
            }
        }
//        if (value instanceof Byte) {
//            return NbtType.BYTE;
//        } else if (value instanceof byte[]) {
//            return NbtType.BYTE_ARRAY;
//        } else if (value instanceof Short) {
//            return NbtType.SHORT;
//        } else if (value instanceof Integer) {
//            return NbtType.INT;
//        } else if (value instanceof int[]) {
//            return NbtType.INT_ARRAY;
//        } else if (value instanceof Long) {
//            return NbtType.LONG;
//        } else if (value instanceof long[]) {
//            return NbtType.LONG_ARRAY;
//        } else if (value instanceof Float) {
//            return NbtType.FLOAT;
//        } else if (value instanceof Double) {
//            return NbtType.DOUBLE;
//        } else if (value instanceof String) {
//            return NbtType.STRING;
//        } else if (value instanceof List) {
//            return NbtType.LIST;
//        } else if (value instanceof Map) {
//            return NbtType.COMPOUND;
//        }
        return null;
    }

    static NbtBase of(Object value) {
        NbtType type = getNbtType(value);
        if (type == null) {
            throw new IllegalArgumentException("Object is not valid NbtType");
        }
        switch (type) {
            case BYTE:
                return new NbtByte((byte) value);
            case SHORT:
                return new NbtShort((short) value);
            case INT:
                return new NbtInt((int) value);
            case LONG:
                return new NbtLong((long) value);
            case FLOAT:
                return new NbtFloat((float) value);
            case DOUBLE:
                return new NbtDouble((double) value);
            case BYTE_ARRAY:
                return new NbtByteArray((byte[]) value);
            case STRING:
                return new NbtString((String) value);
            case LIST:
                List<Object> list = (List<Object>) value;
                List<NbtBase> nbtBaseList = new ArrayList<>();
                for (Object entry : list) {
                    nbtBaseList.add(of(entry));
                }
                return new NbtList(nbtBaseList);
            case COMPOUND:
                Map<String, Object> map = (Map<String, Object>) value;
                Map<String, NbtBase> nbtBaseMap = new HashMap<>();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    nbtBaseMap.put(entry.getKey(), of(entry.getValue()));
                }
                return new NbtCompound(nbtBaseMap);
            case INT_ARRAY:
                return new NbtIntArray((int[]) value);
            case LONG_ARRAY:
                return new NbtLongArray((long[]) value);
            default:
                throw new IllegalArgumentException("Object is not valid NbtType");
        }
    }
}
