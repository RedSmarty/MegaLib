package xyz.redsmarty.redcore.nbt.tag;

public class NbtInt extends NbtNumber {

    /**
     * Constructs a new NbtInt with the given value
     * @param value Value of the int
     */
    public NbtInt(int value) {
        super(value);
    }

    /**
     * Get the literal value of the tag
     * */
    @Override
    public Object getValue() {
        return number.intValue();
    }

    /**
     * Get the type of the tag
     * @return type of the tag
     */
    @Override
    public NbtType getType() {
        return NbtType.INT;
    }
}
