package xyz.redsmarty.redcore.nbt.tag;

public class NbtByte extends NbtNumber {

    /**
     * Constructs a new NbtByte with the given value
     * @param value Value of the byte
     */
    public NbtByte(byte value) {
        super(value);
    }

    /**
     * Get the literal value of the tag
     * */
    @Override
    public Object getValue() {
        return number.byteValue();
    }

    /**
     * Get the type of the tag
     * @return type of the tag
     * */
    @Override
    public NbtType getType() {
        return NbtType.BYTE;
    }
}
