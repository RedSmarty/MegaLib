package xyz.redsmarty.redcore.nbt.tag;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public abstract class NbtNumber implements NbtBase {
    protected final Number number;

    protected NbtNumber(Number number) {
        this.number = number;
    }

    public byte asByte() {
        return number.byteValue();
    }

    public short asShort() {
        return number.shortValue();
    }

    public int asInt() {
        return number.intValue();
    }

    public long asLong() {
        return number.longValue();
    }

    public float asFloat() {
        return number.floatValue();
    }

    public double asDouble() {
        return number.doubleValue();
    }

    public Number asNumber() {
        return number;
    }

    public String asString() {
        return String.valueOf(number);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(number);
    }

    public String toString() {
        return asString();
    }
}
