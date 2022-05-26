package xyz.redsmarty.redcore.nbt.tag;

public class NbtShort extends NbtNumber {

    /**
     * Constructs a new NbtShort with the given value
     * @param value Value of the short
     */
    public NbtShort(short value) {
        super(value);
    }

    /**
     * Get the literal value of the tag
     * */
    @Override
    public Object getValue() {
        return number.shortValue();
    }

    /**
     * Get the type of the tag
     * @return type of the tag
     */
    @Override
    public NbtType getType() {
        return NbtType.SHORT;
    }
}
