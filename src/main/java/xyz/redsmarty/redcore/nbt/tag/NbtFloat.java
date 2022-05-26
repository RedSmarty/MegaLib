package xyz.redsmarty.redcore.nbt.tag;

public class NbtFloat extends NbtNumber {

    /**
     * Constructs a new NbtFloat with the given value
     * @param value Value of the float
     */
    public NbtFloat(float value) {
        super(value);
    }

    /**
     * Get the literal value of the tag
     * */
    @Override
    public Object getValue() {
        return number.floatValue();
    }

    /**
     * Get the type of the tag
     * @return type of the tag
     */
    @Override
    public NbtType getType() {
        return NbtType.FLOAT;
    }
}
