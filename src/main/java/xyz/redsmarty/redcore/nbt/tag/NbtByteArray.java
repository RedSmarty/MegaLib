package xyz.redsmarty.redcore.nbt.tag;

import com.google.common.primitives.Bytes;

public class NbtByteArray extends NbtArray<Byte> implements NbtBase {

    /**
     * Constructs a new NbtByteArray with the given value
     * @param value Value of the byte array
     */
    public NbtByteArray(byte[] value) {
        for (byte b : value) {
            data.add(b);
        }
    }

    /**
     * Get the literal value of the tag
     * */
    @Override
    public Object getValue() {
        return Bytes.toArray(data);
    }

    /**
     * Get the type of the tag
     * @return type of the tag
     * */
    @Override
    public NbtType getType() {
        return NbtType.BYTE_ARRAY;
    }
}
