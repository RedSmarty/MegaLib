package xyz.redsmarty.redcore.schematic;

import org.bukkit.util.Vector;
import xyz.redsmarty.redcore.nbt.tag.NbtCompound;

public class SchematicBlockEntity {
    private final Vector pos;
    private final String type;
    private final NbtCompound data;

    public SchematicBlockEntity(Vector pos, String type, NbtCompound data) {
        this.pos = pos;
        this.type = type;
        this.data = data;
    }

    public Vector getPos() {
        return pos;
    }

    public String getType() {
        return type;
    }

    public NbtCompound getData() {
        return data;
    }
}
