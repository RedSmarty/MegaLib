package xyz.redsmarty.redcore.nbt.tag;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class NbtString implements NbtBase {
    private final String value;

    /**
     * Constructs a new NbtString with the given value
     * @param value Value of the string
     */
    public NbtString(String value) {
        this.value = value;
    }

    /**
     * Get the literal value of the tag
     * */
    @Override
    public Object getValue() {
        return value;
    }

    /**
     * Get the type of the tag
     * @return type of the tag
     */
    @Override
    public NbtType getType() {
        return NbtType.STRING;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(value);
    }
}
