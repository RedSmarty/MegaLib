package xyz.redsmarty.redcore.nbt.tag;

public class NbtLong extends NbtNumber {

    /**
     * Constructs a new NbtLong with the given value
     * @param value Value of the long
     */
    public NbtLong(long value) {
        super(value);
    }

    /**
     * Get the literal value of the tag
     * */
    @Override
    public Object getValue() {
        return number.longValue();
    }

    /**
     * Get the type of the tag
     * @return type of the tag
     */
    @Override
    public NbtType getType() {
    return NbtType.LONG;
    }
}
