package xyz.redsmarty.redcore.nbt.tag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import xyz.redsmarty.redcore.nbt.NbtOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NbtCompound implements NbtBase, Cloneable {

    private final Map<String, NbtBase> data;

    /**
     * Constructs a new NbtCompound with the given value
     * @param data Value of the NbtCompound
     */
    public NbtCompound(Map<String, NbtBase> data) {
        this.data = data;
    }

    /**
     * Constructs a new empty NbtCompound with the given value
     */
    public NbtCompound() {
        this.data = new HashMap<>();
    }


    /**
     * Get the literal value of the tag
     * */
    @Override
    public Object getValue() {
        return data;
    }

    /**
     * Get the type of the tag
     * @return type of the tag
     */
    @Override
    public NbtType getType() {
        return NbtType.COMPOUND;
    }

    /**
     * Get nbt tag from the supplied path
     * @param path Path of the tag
     * @return Requested tag or null if not found
     * */
    public NbtBase get(String path) {
        return get(path, null);
    }

    /**
     * Get nbt tag from the supplied path
     * @param path Path of the tag
     * @param defaultValue Value to return if not found
     * @return Requested byte or default value if not found
     * */
    public NbtBase get(String path, NbtBase defaultValue) {
        NbtCompound compound = this;
        if (path.contains(Character.toString(NbtOptions.getPathSeparator()))) {
            for (String splitPath : StringUtils.split(path, NbtOptions.getPathSeparator())) {
                compound = getCompound(splitPath);
            }
        }
        if (compound.data.containsKey(path)) {
            return compound.data.get(path);
        }
        return defaultValue;
    }

    /**
     * Get byte from the supplied path
     * @param path Path of the byte
     * @return Requested byte or 0 if not found
     * */
    public byte getByte(String path) {
        return (byte) get(path, new NbtByte((byte) 0)).getValue();
    }

    /**
     * Get byte from the supplied path
     * @param path Path of the byte
     * @param defaultValue Value to return if not found
     * @return Requested byte or default value if not found
     * */
    public byte getByte(String path, byte defaultValue) {
        return (byte) get(path, new NbtByte(defaultValue)).getValue();
    }

    /**
     * Get short from the supplied path
     * @param path Path of the short
     * @return Requested short or 0 if not found
     * */
    public short getShort(String path) {
        return (short) get(path, new NbtShort((short) 0)).getValue();
    }

    /**
     * Get short from the supplied path
     * @param path Path of the short
     * @param defaultValue Value to return if not found
     * @return Requested byte or default value if not found
     * */
    public short getShort(String path, short defaultValue) {
        return (short) get(path, new NbtShort(defaultValue)).getValue();
    }

    /**
     * Get int from the supplied path
     * @param path Path of the int
     * @return Requested int or 0 if not found
     * */
    public int getInt(String path) {
        return (int) get(path, new NbtInt(0)).getValue();
    }

    /**
     * Get int from the supplied path
     * @param path Path of the int
     * @param defaultValue Value to return if not found
     * @return Requested int or default value if not found
     * */
    public int getInt(String path, int defaultValue) {
        return (int) get(path, new NbtInt(defaultValue)).getValue();
    }

    /**
     * Get long from the supplied path
     * @param path Path of the long
     * @return Requested long or 0 if not found
     * */
    public long getLong(String path) {
        return (long) get(path, new NbtLong(0)).getValue();
    }

    /**
     * Get long from the supplied path
     * @param path Path of the long
     * @param defaultValue Value to return if not found
     * @return Requested long or default value if not found
     * */
    public long getLong(String path, long defaultValue) {
        return (long) get(path, new NbtLong(defaultValue)).getValue();
    }

    /**
     * Get float from the supplied path
     * @param path Path of the float
     * @return Requested float or 0 if not found
     * */
    public float getFloat(String path) {
        return (float) get(path, new NbtFloat(0)).getValue();
    }

    /**
     * Get float from the supplied path
     * @param path Path of the float
     * @param defaultValue Value to return if not found
     * @return Requested float or default value if not found
     * */
    public float getFloat(String path, float defaultValue) {
        return (float) get(path, new NbtFloat(defaultValue)).getValue();
    }

    /**
     * Get double from the supplied path
     * @param path Path of the double
     * @return Requested double or 0 if not found
     * */
    public double getDouble(String path) {
        return (double) get(path, new NbtDouble(0)).getValue();
    }

    /**
     * Get double from the supplied path
     * @param path Path of the double
     * @param defaultValue Value to return if not found
     * @return Requested double or default value if not found
     * */
    public double getDouble(String path, double defaultValue) {
        return (double) get(path, new NbtDouble(defaultValue)).getValue();
    }


    /**
     * Get byte array from the supplied path
     * @param path Path of the byte array
     * @return Requested byte array or empty array if not found
     * */
    public byte[] getByteArray(String path) {
        return (byte[]) get(path, new NbtByteArray(new byte[0])).getValue();
    }

    /**
     * Get byte array from the supplied path
     * @param path Path of the byte array
     * @param defaultValue Value to return if not found
     * @return Requested byte array or default value if not found
     * */
    public byte[] getByteArray(String path, byte[] defaultValue) {
        return (byte[]) get(path, new NbtByteArray(defaultValue)).getValue();
    }


    /**
     * Get string from the supplied path
     * @param path Path of the string
     * @return Requested string or null if not found
     * */
    public String getString(String path) {
        return (String) get(path).getValue();
    }

    /**
     * Get string from the supplied path
     * @param path Path of the string
     * @param defaultValue Value to return if not found
     * @return Requested string or default value if not found
     * */
    public String getString(String path, String defaultValue) {
        return (String) get(path, new NbtString(defaultValue)).getValue();
    }

    /**
     * Get raw list from the supplied path
     * @param path Path of the list
     * @return Requested list or empty list if not found
     * */
    public List<NbtBase> getRawList(String path) {
        return (List<NbtBase>) get(path, new NbtList(new ArrayList<>())).getValue();
    }

    /**
     * Get raw list from the supplied path
     * @param path Path of the list
     * @param defaultValue Value to return if not found
     * @return Requested list or default value if not found
     * */
    public List<NbtBase> getRawList(String path, List<NbtBase> defaultValue) {
        return (List<NbtBase>) get(path, new NbtList(defaultValue)).getValue();
    }

    /**
     * Get list from the supplied path
     * @param path Path of the list
     * @return Requested list or empty list if not found
     * */
    public NbtList getList(String path) {
        return (NbtList) get(path, new NbtList());
    }

    /**
     * Get list from the supplied path
     * @param path Path of the list
     * @return Requested list or empty list if not found
     * */
    public NbtList getList(String path, NbtList defaultValue) {
        return (NbtList) get(path, defaultValue);
    }

    /**
     * Get list from the supplied path. This will return the literal value of any nbt tag except NbtCompound and NbtList
     * @param path Path of the list
     * @return Requested list or empty list if not found
     * */
    public <T> List<T> getListPrimitive(String path, Class<T> type) {
        List<T> list = new ArrayList<>();
        for (NbtBase nbtBase: ((List<NbtBase>) get(path, new NbtList(new ArrayList<>())).getValue())) {
            if (!(nbtBase instanceof NbtCompound || nbtBase instanceof NbtList)) {
//                if (nbtBase.getType().getClazz() == type) {
                    list.add(type.cast(nbtBase.getValue()));
//                }
            }
        }

        return list;
    }

    /**
     * Get list from the supplied path. This will return the literal value of any nbt tag
     * @param path Path of the list
     * @return Requested list or empty list if not found
     * */
    public List<Object> getListPrimitive(String path, List<Object> defaultValue) {
        List<Object> list = new ArrayList<>();
        NbtBase value = get(path);
        if (value == null) {
            return defaultValue;
        }
        for (NbtBase nbtBase: ((List<NbtBase>) value.getValue())) {
            list.add(nbtBase.getValue());
        }
        return list;
    }

    /**
     * Get compound tag from the supplied path
     * @param path Path of the compound tag
     * @return Requested compound tag or 0 if not found
     * */
    public NbtCompound getCompound(String path) {
        return (NbtCompound) get(path, new NbtCompound(new HashMap<>()));
    }

    /**
     * Get compound tag from the supplied path
     * @param path Path of the compound tag
     * @param defaultValue Value to return if not found
     * @return Requested compound tag or default value if not found
     * */
    public NbtCompound getCompound(String path, Map<String, NbtBase> defaultValue) {
        return (NbtCompound) get(path, new NbtCompound(defaultValue));
    }

    /**
     * Get int array from the supplied path
     * @param path Path of the int array
     * @return Requested int array or 0 if not found
     * */
    public int[] getIntArray(String path) {
        return (int[]) get(path, new NbtIntArray(new int[0])).getValue();
    }

    /**
     * Get int array from the supplied path
     * @param path Path of the int array
     * @param defaultValue Value to return if not found
     * @return Requested int array or default value if not found
     * */
    public int[] getIntArray(String path, int[] defaultValue) {
        return (int[]) get(path, new NbtIntArray(defaultValue)).getValue();
    }

    /**
     * Get long array from the supplied path
     * @param path Path of the long array
     * @return Requested long array or 0 if not found
     * */
    public long[] getLongArray(String path) {
        return (long[]) get(path, new NbtLongArray(new long[0])).getValue();
    }

    /**
     * Get long array from the supplied path
     * @param path Path of the long array
     * @param defaultValue Value to return if not found
     * @return Requested long array or default value if not found
     * */
    public long[] getLongArray(String path, long[] defaultValue) {
        return (long[]) get(path, new NbtLongArray(defaultValue)).getValue();
    }

    /**
     * Get boolean from the supplied path
     * @param path Path of the boolean
     * @return Requested boolean or false if not found
     * */
    public boolean getBoolean(String path) {
        return ((byte) get(path, new NbtByte((byte) 0)).getValue() != 0);
    }

    /**
     * Get boolean from the supplied path
     * @param path Path of the boolean
     * @param defaultValue Value to return if not found
     * @return Requested boolean or default value if not found
     * */
    public boolean getBoolean(String path, boolean defaultValue) {
        return ((byte) get(path, new NbtByte((byte) (defaultValue ? 1 : 0))).getValue() != 0);
    }

    public boolean hasKey(String key) {
        return data.containsKey(key);
    }

    /**
     * Get keys of this compound tag
     * @return A put of keys present in this tag
     * */
    public Set<String> getKeys() {
        return Collections.unmodifiableSet(data.keySet());
    }

    public Collection<NbtBase> getValues() {
        return data.values();
    }

    public Set<Map.Entry<String, NbtBase>> entries() {
        return data.entrySet();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public void clear() {
        data.clear();
    }

    public void remove(String key) {
        data.remove(key);
    }

    /**
     * Sets the specified path to the given value. If value is null, the entry will be removed. Any existing entry will be replaced, regardless of what the new value is.
     * @param path Path to put the value on
     * @param nbtBase Value to put on the path
     * @return The instance of this NbtCompound, allowing you to chain calls
     * */
    public NbtBase put(String path, NbtBase nbtBase) {
        if (nbtBase == null) {
            data.remove(path);
            return null;
        }
        return this.data.put(path, nbtBase);
    }

    public NbtBase putByte(String path, byte value) {
        return put(path, new NbtByte(value));
    }

    public NbtBase putShort(String path, short value) {
        return put(path, new NbtShort(value));
    }

    public NbtBase putInt(String path, int value) {
        return put(path, new NbtInt(value));
    }

    public NbtBase putLong(String path, long value) {
        return put(path, new NbtLong(value));
    }

    public NbtBase putFloat(String path, float value) {
        return put(path, new NbtFloat(value));
    }

    public NbtBase putDouble(String path, double value) {
        return put(path, new NbtDouble(value));
    }

    public NbtBase putByteArray(String path, byte[] value) {
        return put(path, new NbtByteArray(value));
    }

    public NbtBase putString(String path, String value) {
        return put(path, new NbtString(value));
    }

    public NbtBase putList(String path, NbtList value) {
        return put(path, value);
    }

    public NbtBase putCompound(String path, NbtCompound value) {
        return put(path, value);
    }

    public NbtBase putIntArray(String path, int[] value) {
        return put(path, new NbtIntArray(value));
    }

    public NbtBase putLongArray(String path, long[] value) {
        return put(path, new NbtLongArray(value));
    }

    public NbtCompound merge(NbtCompound nbt, boolean overwrite) {
        for (Map.Entry<String, NbtBase> entry : nbt.entries()) {
            if (hasKey(entry.getKey()) && !overwrite) continue;
            put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        for (Map.Entry<String, NbtBase> entry : this.data.entrySet()) {
            object.add(entry.getKey(), entry.getValue().toJson());
        }
        return object;
    }


    public String toJsonString(boolean pretty) {
        GsonBuilder builder = new GsonBuilder();
        if (pretty) {
            builder.setPrettyPrinting();
        }
        Gson gson = builder.create();
        return gson.toJson(toJson());
    }

    public String toString() {
        return toJsonString(false);
    }

    @Override
    public NbtCompound clone() {
        NbtCompound nbt = new NbtCompound();
        for (Map.Entry<String, NbtBase> entry : entries()) {
            switch (entry.getValue().getType()) {
                case LIST:
                    nbt.put(entry.getKey(), ((NbtList) entry.getValue()).clone());
                    break;
                case COMPOUND:
                    nbt.put(entry.getKey(), ((NbtCompound) entry.getValue()).clone());
                    break;
                default:
                    nbt.put(entry.getKey(), entry.getValue());
                    break;
            }
        }
        return nbt;
    }

    public static NbtCompoundBuilder builder() {
        return new NbtCompoundBuilder();
    }
}
