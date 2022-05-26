package xyz.redsmarty.redcore.nbt.tag;

public class NbtCompoundBuilder {
    private final NbtCompound compound = new NbtCompound();

    public NbtCompoundBuilder putByte(String path, byte value) {
        compound.putByte(path, value);
        return this;
    }

    public NbtCompoundBuilder putShort(String path, short value) {
        compound.putShort(path, value);
        return this;
    }

    public NbtCompoundBuilder putInt(String path, int value) {
        compound.putInt(path, value);
        return this;
    }

    public NbtCompoundBuilder NbtLong(String path, long value) {
        compound.putLong(path, value);
        return this;
    }

    public NbtCompoundBuilder putFloat(String path, float value) {
        compound.putFloat(path, value);
        return this;
    }

    public NbtCompoundBuilder putDouble(String path, double value) {
        compound.putDouble(path, value);
        return this;
    }

    public NbtCompoundBuilder putByteArray(String path, byte... value) {
        compound.putByteArray(path, value);
        return this;
    }

    public NbtCompoundBuilder putString(String path, String value) {
        compound.putString(path, value);
        return this;
    }

    public <T> NbtCompoundBuilder putList(String path, T... values) {
        compound.putList(path, NbtList.of(values));
        return this;
    }

    public NbtCompoundBuilder putCompound(String path, NbtCompound value) {
        compound.putCompound(path, value);
        return this;
    }

    public NbtCompoundBuilder putIntArray(String path, int... value) {
        compound.putIntArray(path, value);
        return this;
    }

    public NbtCompoundBuilder putLongArray(String path, long... value) {
        compound.putLongArray(path, value);
        return this;
    }

    public NbtCompound build() {
        return compound;
    }

    NbtCompoundBuilder() {}
}
