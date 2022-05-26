package xyz.redsmarty.redcore.schematic;

import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import xyz.redsmarty.redcore.nbt.tag.NbtCompound;

public class SchematicEntity {
    private final Vector pos;
    private final EntityType type;
    private final NbtCompound data;

    public SchematicEntity(Vector pos, EntityType type, NbtCompound data) {
        this.pos = pos;
        this.type = type;
        this.data = data;
    }

    public Vector getPos() {
        return pos;
    }

    public EntityType getType() {
        return type;
    }

    public NbtCompound getData() {
        return data;
    }
}
