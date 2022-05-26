package xyz.redsmarty.redcore.nbt.tag;

public class NbtDouble extends NbtNumber {

    /**
     * Constructs a new NbtDouble with the given value
     * @param value Value of the double
     */
    public NbtDouble(double value) {
        super(value);
    }

    /**
     * Get the literal value of the tag
     * */
    @Override
    public Object getValue() {
        return number.doubleValue();
    }

    /**
     * Get the type of the tag
     * @return type of the tag
     */
    @Override
    public NbtType getType() {
        return NbtType.DOUBLE;
    }
}
