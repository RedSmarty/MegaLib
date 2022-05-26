package xyz.redsmarty.redcore.nbt.tag;

import com.google.common.primitives.Longs;

public class NbtLongArray extends NbtArray<Long> {

    /**
     * Constructs a new NbtLongArray with the given value
     * @param value Value of the long array
     */
    public NbtLongArray(long[] value) {
        for (long l : value) {
            data.add(l);
        }
    }

    /**
     * Get the literal value of the tag
     * */
    @Override
    public Object getValue() {
        return Longs.toArray(data);
    }

    /**
     * Get the type of the tag
     * @return type of the tag
     */
    @Override
    public NbtType getType() {
        return NbtType.LONG_ARRAY;
    }
}
