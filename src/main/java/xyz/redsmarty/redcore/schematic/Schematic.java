package xyz.redsmarty.redcore.schematic;

import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

public class Schematic {
    private int version;
    private int dataVersion;
    private short width;
    private short height;
    private short length;
    private Vector offset;
    private Vector worldEditOffset;
    private byte[] blocks;
    private final Map<Integer, BlockData> palette;
    private final List<SchematicBlockEntity> blockEntities;
    private final List<SchematicEntity> entities;

    public int getVersion() {
        return version;
    }

    public short getWidth() {
        return width;
    }

    public short getHeight() {
        return height;
    }

    public short getLength() {
        return length;
    }

    public byte[] getBlocks() {
        return blocks;
    }

    public Map<Integer, BlockData> getPalette() {
        return palette;
    }

    public List<SchematicEntity> getEntities() {
        return entities;
    }

    public List<SchematicBlockEntity> getBlockEntities() {
        return blockEntities;
    }


    public int getDataVersion() {
        return dataVersion;
    }

    public Vector getOffset() {
        return offset;
    }

    public Vector getWorldEditOffset() {
        return worldEditOffset;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setDataVersion(int dataVersion) {
        this.dataVersion = dataVersion;
    }

    public void setWidth(short width) {
        this.width = width;
    }

    public void setHeight(short height) {
        this.height = height;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public void setOffset(Vector offset) {
        this.offset = offset;
    }

    public void setWorldEditOffset(Vector worldEditOffset) {
        this.worldEditOffset = worldEditOffset;
    }

    public void setBlocks(byte[] blocks) {
        this.blocks = blocks;
    }

    public Schematic(int version, int dataVersion, short width, short height, short length, Vector offset, Vector worldEditOffset, byte[] blocks, Map<Integer, BlockData> palette, List<SchematicBlockEntity> blockEntities, List<SchematicEntity> entities) {
        this.version = version;
        this.dataVersion = dataVersion;
        this.width = width;
        this.height = height;
        this.length = length;
        this.offset = offset;
        this.worldEditOffset = worldEditOffset;
        this.blocks = blocks;
        this.palette = palette;
        this.blockEntities = blockEntities;
        this.entities = entities;
    }
}
