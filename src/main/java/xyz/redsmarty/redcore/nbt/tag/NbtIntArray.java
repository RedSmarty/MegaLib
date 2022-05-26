package xyz.redsmarty.redcore.nbt.tag;

import com.google.common.primitives.Ints;

public class NbtIntArray extends NbtArray<Integer> {

    /**
     * Constructs a new NbtIntArray with the given value
     * @param value Value of the int array
     */
    public NbtIntArray(int[] value) {
        for (int i : value) {
            data.add(i);
        }
    }

    /**
     * Get the literal value of the tag
     * */
    @Override
    public Object getValue() {
        return Ints.toArray(data);
    }

    /**
     * Get the type of the tag
     * @return type of the tag
     */
    @Override
    public NbtType getType() {
        return NbtType.INT_ARRAY;
    }
}
