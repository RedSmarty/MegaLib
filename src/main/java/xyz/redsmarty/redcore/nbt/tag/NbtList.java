package xyz.redsmarty.redcore.nbt.tag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class NbtList implements NbtBase, Iterable<NbtBase>, Cloneable {
    private final List<NbtBase> value;
    private NbtType containedType;

    /**
     * Constructs a new empty NbtList
     */
    public NbtList() {
        this.value = new ArrayList<>();
        this.containedType = NbtType.END;
    }

    /**
     * Constructs a new NbtList with the given value, where the type is explicitly given
     * @param value Value of the list, must only contain one type of nbt tags
     * @param type Type of the contents
     * @throws IllegalArgumentException if types are not equal
     */
    public NbtList(List<NbtBase> value, NbtType type) {
        this.containedType = type;
        for (int i = 0; i < value.size(); i++) {
            if (value.get(i).getType() != type) {
                throw new IllegalArgumentException("A nbt list can't mix different types of nbt tags");
            }
        }
        this.value = value;
    }

    /**
     * Constructs a new NbtList with the given value
     * @param value Value of the list, must only contain one type of nbt tags
     * @throws IllegalArgumentException if types are not equal
     */
    public NbtList(List<NbtBase> value) {
        if (value.isEmpty()) {
            this.containedType = NbtType.END;
        } else {
            this.containedType = value.get(0).getType();
        }
        for (int i = 0; i < value.size(); i++) {
            if (value.get(i).getType() != containedType) {
                throw new IllegalArgumentException("A nbt list can't mix different types of nbt tags");
            }
        }
        this.value = value;
    }

    /**
     * Get the literal value of the tag
     * */
    @Override
    public Object getValue() {
        return value;
    }

    /**
     * Get the type of the tag
     * @return type of the tag
     */
    @Override
    public NbtType getType() {
        return NbtType.LIST;
    }

    /**
     * Get the type contained within this list
     * @return the type contained, or NbtType.END if the list is empty
     */
    public NbtType getContainedType() {
        return containedType;
    }

    /**
     * Adds the given nbt base to the list. Types can't be mixed
     * @param nbtBase NbtTag to add
     */
    public void add(NbtBase nbtBase) {
        if (value.size() == 0) {
            value.add(nbtBase);
            containedType = nbtBase.getType();
            return;
        }
        if (value.get(0).getType() != nbtBase.getType()) {
            throw new IllegalArgumentException("A list can't mix different types of nbt tags");
        }
        value.add(nbtBase);
    }

    /**
     * Adds the given nbt base to the list. Types can't be mixed
     * @param nbtBase NbtTag to add
     */
    public void add(int index, NbtBase nbtBase) {
        if (value.size() == 0) {
            value.add(nbtBase);
            containedType = nbtBase.getType();
            return;
        }
        if (value.get(0).getType() != nbtBase.getType()) {
            throw new IllegalArgumentException("A list can't mix different types of nbt tags");
        }
        value.add(index, nbtBase);
    }

    /**
     * Adds the given nbt base list to the list. Types can't be mixed
     * @param nbtBase NbtTag to add
     */
    public void addAll(Collection<NbtBase> nbtBase) {
        for (NbtBase base : nbtBase) {
            add(base);
        }
    }

    /**
     * Adds the given nbt base list to the list. Types can't be mixed
     * @param nbtBase NbtTag to add
     */
    public void addAll(int index, Collection<NbtBase> nbtBase) {
        int i = index;
        for (NbtBase base : nbtBase) {
            add(i, base);
            i++;
        }
    }

    /**
     * Removes the given nbt base from the list.
     * @param nbtBase NbtTag to add
     */
    public void remove(NbtBase nbtBase) {
        value.remove(nbtBase);
    }

    /**
     * Gets the size of the list
     * @return The size of the list
     */
    public int size() {
        return value.size();
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public void clear() {
        value.clear();
    }

    @SafeVarargs
    public static <T> NbtList of(T... values) {
        NbtList list = new NbtList();
        for (T value : values) {
            list.add(NbtBase.of(value));
        }
        return list;
    }

    @Override
    public JsonArray toJson() {
        JsonArray array = new JsonArray();
        for (NbtBase entry : this.value) {
            array.add(entry.toJson());
        }
        return array;
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

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<NbtBase> iterator() {
        return value.iterator();
    }

    @Override
    public NbtList clone() {
        NbtList list = new NbtList();
        for (NbtBase base : value) {
            switch (base.getType()) {
                case LIST:
                    list.add(((NbtList) base).clone());
                    break;
                case COMPOUND:
                    list.add(((NbtCompound) base).clone());
                    break;
                default:
                    list.add(base);
                    break;
            }
        }
        return list;
    }
}
