package xyz.redsmarty.redcore.schematic;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import xyz.redsmarty.redcore.utils.Constants;
import xyz.redsmarty.redcore.utils.BlockUtils;
import xyz.redsmarty.redcore.nbt.NbtIO;
import xyz.redsmarty.redcore.nbt.tag.NbtBase;
import xyz.redsmarty.redcore.nbt.tag.NbtCompound;
import xyz.redsmarty.redcore.nbt.tag.NbtList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class SchematicIO {
    public static final BiConsumer<Location, BlockData> PASTE_BLOCK = (location, data) -> {
        location.getBlock().setType(data.getMaterial());
        location.getBlock().setBlockData(data);
    };

    public static Schematic load(InputStream is) throws IOException {
        NbtCompound root = NbtIO.fromStream(is);

        int version = root.getInt("Version", 3);
        int dataVersion = root.getInt("DataVersion", Constants.SERVER_DATA_VERSION);
        short width = root.getShort("Width", (short) 0);
        short length = root.getShort("Length", (short) 0);
        short height = root.getShort("Height", (short) 0);
        int[] offsetArr = root.getIntArray("Offset", new int[] {0, 0, 0});
        Vector offset = new Vector(offsetArr[0], offsetArr[1], offsetArr[2]);
        Vector worldEditOffset = new Vector();
        Map<Integer, BlockData> blockPalette = new HashMap<>();
        byte[] blocks = new byte[0];
        List<SchematicBlockEntity> blockEntities = new ArrayList<>();
        List<SchematicEntity> entities = new ArrayList<>();

        NbtCompound metaData = root.getCompound("Metadata");
        for (Map.Entry<String, NbtBase> entry : metaData.entries()) {
            switch (entry.getKey()) {
                case "WEOffsetX":
                    worldEditOffset.setX((double) entry.getValue().getValue());
                    break;
                case "WEOffsetY":
                    worldEditOffset.setY((double) entry.getValue().getValue());
                    break;
                case "WEOffsetZ":
                    worldEditOffset.setZ((double) entry.getValue().getValue());
                    break;
            }
        }

        NbtCompound blocksContainer = version == 3 ? root.getCompound("Blocks") : root;
        if (!blocksContainer.isEmpty()) {
            NbtCompound paletteContainer = blocksContainer.getCompound("Palette");
            NbtList blockEntitiesContainer = blocksContainer.getList("BlockEntities");
            blocks = version == 3 ? root.getByteArray("Data") : root.getByteArray("BlockData");

            for (Map.Entry<String, NbtBase> entry : paletteContainer.entries()) {
                int index = (int) entry.getValue().getValue();
                String blockName = entry.getKey().split("\\[")[0];
                String blockState = entry.getKey().contains("[") ? entry.getKey().substring(entry.getKey().indexOf("[")) : "";
                Material blockMaterial = Material.matchMaterial(blockName);
                if (blockMaterial == null) {
                    Bukkit.getLogger().severe("Block " + blockName + " given in schematic is not valid.");
                    continue;
                }
                BlockData blockData = blockMaterial.createBlockData(blockState);
                blockPalette.put(index, blockData);
            }
            if (!blockEntitiesContainer.isEmpty()) {
                for (NbtBase nbtBase : blockEntitiesContainer) {
                    NbtCompound blockEntityObject = (NbtCompound) nbtBase;
                    int[] pos = blockEntityObject.getIntArray("Pos");
                    String id = blockEntityObject.getString("Id");
                    NbtCompound data;
                    if (version == 3) {
                        data = blockEntityObject.getCompound("Data");
                    } else {
                        blockEntityObject.remove("Pos");
                        blockEntityObject.remove("Id");
                        data = blockEntityObject;
                    }
                    SchematicBlockEntity blockEntity = new SchematicBlockEntity(new Vector(pos[0], pos[1], pos[2]), id, data);
                    blockEntities.add(blockEntity);
                }
            }
        }

        List<NbtBase> entitiesContainer = root.getRawList("Entities");
        if (!entitiesContainer.isEmpty()) {
            for (NbtBase nbtBase : entitiesContainer) {
                NbtCompound entityObject = (NbtCompound) nbtBase;
                List<Double> posList = entityObject.getListPrimitive("Pos", Double.class);
                Vector pos = new Vector(posList.get(0), posList.get(1), posList.get(2));

                String id = entityObject.getString("Id").replace("minecraft:", "");
                NbtCompound data;
                if (version == 3) {
                    data = entityObject.getCompound("Data");
                } else {
                    entityObject.remove("Pos");
                    entityObject.remove("Id");
                    data = entityObject;
                }
                entities.add(new SchematicEntity(offset.subtract(pos), EntityType.fromName(id), data));
            }
        }
        return new Schematic(version, dataVersion, width, height, length, offset, worldEditOffset, blocks, blockPalette, blockEntities, entities);
    }

    public static void save(Schematic schematic, OutputStream out) {
        NbtCompound root = NbtCompound.builder()
                .putInt("Version", schematic.getVersion())
                .putInt("DataVersion", schematic.getDataVersion())
                .putCompound("Metadata", NbtCompound.builder()
                        .putInt("WEOffsetX", schematic.getWorldEditOffset().getBlockX())
                        .putInt("WEOffsetY", schematic.getWorldEditOffset().getBlockY())
                        .putInt("WEOffsetZ", schematic.getWorldEditOffset().getBlockZ()).build())
                .putShort("Width", schematic.getWidth())
                .putShort("Height", schematic.getHeight())
                .putShort("Length", schematic.getLength())
                .putIntArray("Offset", schematic.getOffset().getBlockX(), schematic.getOffset().getBlockY(), schematic.getOffset().getBlockZ())
                .putInt("PaletteMax", schematic.getPalette().size())
                .putByteArray("BlockData", schematic.getBlocks()).build();

        NbtCompound blockPalette = new NbtCompound();
        for (Map.Entry<Integer, BlockData> entry : schematic.getPalette().entrySet()) {
            blockPalette.putInt(entry.getValue().getAsString(true), entry.getKey());
        }
        root.putCompound("Palette", blockPalette);

        NbtList blockEntities = new NbtList();
        for (SchematicBlockEntity blockEntity : schematic.getBlockEntities()) {
            blockEntities.add(NbtCompound.builder()
                    .putIntArray("Pos", blockEntity.getPos().getBlockX(), blockEntity.getPos().getBlockY(), blockEntity.getPos().getBlockZ())
                    .putString("Id", blockEntity.getType()).build().merge(blockEntity.getData(), true)
            );
        }
        root.putList("BlockEntities", blockEntities);

        NbtList entities = new NbtList();
        for (SchematicEntity entity : schematic.getEntities()) {
            entities.add(NbtCompound.builder()
                    .putList("Pos", entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ())
                    .putString("Id", "minecraft:" + entity.getType().getName())
                    .build().merge(entity.getData(), true)
            );
        }
        root.putList("Entities", entities);
        NbtIO.toStream(root, out);
    }

    public static Schematic copy(Location start, Location end, Location origin) {
        if (start.getWorld() == null || end.getWorld() == null || start.getWorld() != end.getWorld() || start.getWorld() != origin.getWorld()) {
            throw new IllegalArgumentException("Start and end locations must have the same world.");
        }

        int startX = Math.min(start.getBlockX(), end.getBlockX());
        int startY = Math.min(start.getBlockY(), end.getBlockY());
        int startZ = Math.min(start.getBlockZ(), end.getBlockZ());

        short length = (short) (Math.abs(start.getBlockZ() - end.getBlockZ()) + 1);
        short width = (short) (Math.abs(start.getBlockX() - end.getBlockX()) + 1);
        short height = (short) (Math.abs(start.getBlockY() - end.getBlockY()) + 1);
        Vector offset = new Vector(startX, startY, startZ);
        Vector worldEditOffset = new Vector(origin.getBlockX() - startX, origin.getBlockY() - startY, origin.getBlockZ() - startZ);
        BiMap<Integer, BlockData> blockPalette = HashBiMap.create();
        byte[] blocks = new byte[length * height * width];
        List<SchematicBlockEntity> blockEntities = new ArrayList<>();
        List<SchematicEntity> entities = new ArrayList<>();

        int blockIndex = 0;
        for (int h = 0; h < height; h++) {
            for (int l = 0; l < length; l++) {
                for (int w = 0; w < width; w++) {
                    Block block = start.getWorld().getBlockAt(new Location(start.getWorld(), (double) startX + w, (double) startY + h, (double) startZ + l));
                    BlockData data = block.getBlockData();
                    if (blockPalette.containsValue(data)) {
                        blocks[blockIndex] = blockPalette.inverse().get(data).byteValue();
                    } else {
                        blockPalette.put(blockPalette.size(), data);
                        blocks[blockIndex] = (byte) (blockPalette.size() - 1);
                    }
                    if (BlockUtils.isTileEntity(block)) {
                        blockEntities.add(new SchematicBlockEntity(block.getLocation().subtract(startX, startY, startZ).toVector(), "minecraft:" + block.getType().name().toLowerCase(), NbtIO.fromTileEntity(block)));
                    }
                    blockIndex++;
                }
            }
        }

        int endX = Math.max(start.getBlockX(), end.getBlockX());
        int endY = Math.max(start.getBlockY(), end.getBlockY());
        int endZ = Math.max(start.getBlockZ(), end.getBlockZ());
        BoundingBox boundingBox = new BoundingBox(startX, startY, startZ, endX, endY, endZ);

        for (Entity entity : start.getWorld().getNearbyEntities(boundingBox)) {
            entities.add(new SchematicEntity(entity.getLocation().toVector(), entity.getType(), NbtIO.fromEntity(entity)));
        }

        return new Schematic(2, Constants.SERVER_DATA_VERSION, width, height, length, offset, worldEditOffset, blocks, blockPalette, blockEntities, entities);
    }

    public static void paste(Location loc, Schematic schematic, boolean center, BiConsumer<Location, BlockData> callback) {
        loc = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        if (center) {
            loc = loc.subtract(schematic.getWidth() / 2f, schematic.getHeight() / 2f, schematic.getLength() / 2f);
        } else {
            loc = loc.add(schematic.getWorldEditOffset());
        }
        if (loc.getWorld() == null) return;
        int blockIndex = 0;
        for (int h = 0; h < schematic.getHeight(); h++) {
            for (int l = 0; l < schematic.getLength(); l++) {
                for (int w = 0; w < schematic.getWidth(); w++) {
                    BlockData data = schematic.getPalette().get((int) schematic.getBlocks()[blockIndex]);
                    callback.accept(loc.clone().add(w, h, l), data);
                    blockIndex++;
                }
            }
        }
        for (SchematicBlockEntity block : schematic.getBlockEntities()) {
            Location blockLocation = loc.clone().add(block.getPos());
            if (!(blockLocation.getBlock().getState() instanceof TileState)) {
                continue;
            }
            NbtCompound nbt = block.getData().clone();
            nbt.putInt("x", blockLocation.getBlockX());
            nbt.putInt("y", blockLocation.getBlockY());
            nbt.putInt("z", blockLocation.getBlockZ());
            NbtIO.toTileEntity(blockLocation, nbt);
        }

        for (SchematicEntity schematicEntity : schematic.getEntities()) {
            Location entityLocation = loc.clone().subtract(schematicEntity.getPos());
            Entity entity = loc.getWorld().spawnEntity(entityLocation, schematicEntity.getType());
            NbtCompound nbt = NbtIO.fromEntity(entity);
            NbtIO.toEntity(entity, nbt.merge(schematicEntity.getData(), true));
        }
    }

    public static void paste(Location loc, Schematic schematic) {
        paste(loc, schematic, true, PASTE_BLOCK);
    }
}
