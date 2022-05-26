package xyz.redsmarty.redcore.nbt;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import xyz.redsmarty.redcore.utils.ReflectionUtils;
import xyz.redsmarty.redcore.utils.NMSReflectionException;
import xyz.redsmarty.redcore.nbt.tag.NbtBase;
import xyz.redsmarty.redcore.nbt.tag.NbtByte;
import xyz.redsmarty.redcore.nbt.tag.NbtByteArray;
import xyz.redsmarty.redcore.nbt.tag.NbtCompound;
import xyz.redsmarty.redcore.nbt.tag.NbtDouble;
import xyz.redsmarty.redcore.nbt.tag.NbtFloat;
import xyz.redsmarty.redcore.nbt.tag.NbtInt;
import xyz.redsmarty.redcore.nbt.tag.NbtIntArray;
import xyz.redsmarty.redcore.nbt.tag.NbtList;
import xyz.redsmarty.redcore.nbt.tag.NbtLong;
import xyz.redsmarty.redcore.nbt.tag.NbtLongArray;
import xyz.redsmarty.redcore.nbt.tag.NbtShort;
import xyz.redsmarty.redcore.nbt.tag.NbtString;
import xyz.redsmarty.redcore.nbt.tag.NbtType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NbtIO {
    public static NbtCompound fromStream(InputStream is) throws IOException {
        Class<?> compressedStreamClass;
        try {
            compressedStreamClass = ReflectionUtils.getMinecraftClass("nbt.NBTCompressedStreamTools");
            return ((NbtCompound) convertToLib(ReflectionUtils.invokeStaticMethodExplicit(compressedStreamClass, "a", new Class[] {InputStream.class}, is)));
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new NMSReflectionException("The nbt reading has thrown an exception, maybe this is not a supported server version?", e);
        } finally {
            is.close();
        }
    }

    public static NbtCompound fromFile(File file) throws IOException {
        return fromStream(new FileInputStream(file));
    }

    public static void toStream(NbtCompound nbt, OutputStream out) {
        Class<?> compressedStreamClass;
        try {
            compressedStreamClass = ReflectionUtils.getMinecraftClass("nbt.NBTCompressedStreamTools");
            ReflectionUtils.invokeStaticMethodExplicit(compressedStreamClass, "a", new Class[] {ReflectionUtils.getMinecraftClass("nbt.NBTTagCompound"), OutputStream.class}, convertToNMS(nbt), out);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new NMSReflectionException("The nbt reading has thrown an exception, maybe this is not a supported server version?", e);
        }
    }

    public static void toFile(NbtCompound nbt, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        OutputStream out = new FileOutputStream(file);
        toStream(nbt, out);
        out.flush();
        out.close();
    }

    public static NbtCompound fromEntity(Entity entity) {
        try {
            Object tagCompound = ReflectionUtils.invokeConstructor(ReflectionUtils.getMinecraftClass("nbt.NBTTagCompound"));
            Object nmsEntity = ReflectionUtils.invokeMethodExplicit(ReflectionUtils.getBukkitClass("entity.CraftEntity"), entity, "getHandle");
            ReflectionUtils.invokeMethod(ReflectionUtils.getMinecraftClass("world.entity.Entity"), nmsEntity, "save", tagCompound);
            return (NbtCompound) convertToLib(tagCompound);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            throw new NMSReflectionException("The nbt reading has thrown an exception, maybe this is not a supported server version?", e);
        }
    }

    public static void toEntity(Entity entity, NbtCompound nbt) {
        try {
            Object nmsEntity = ReflectionUtils.invokeMethodExplicit(ReflectionUtils.getBukkitClass("entity.CraftEntity"), entity, "getHandle");
            ReflectionUtils.invokeMethod(ReflectionUtils.getMinecraftClass("world.entity.Entity"), nmsEntity, "load", convertToNMS(nbt));
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new NMSReflectionException("The nbt reading has thrown an exception, maybe this is not a supported server version?", e);
        }
    }

    public static NbtCompound fromTileEntity(Block block) {
        try {
            Object nmsWorld = ReflectionUtils.invokeMethod(ReflectionUtils.getBukkitClass("CraftWorld"), block.getWorld(),"getHandle");
            Object tileEntity = ReflectionUtils.invokeMethod(ReflectionUtils.getMinecraftClass("world.level.World"), nmsWorld, "getTileEntity", ReflectionUtils.invokeConstructorExplicit(ReflectionUtils.getMinecraftClass("core.BlockPosition"), new Class[] {int.class, int.class, int.class}, block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()));
            Object tagCompound = ReflectionUtils.invokeConstructor(ReflectionUtils.getMinecraftClass("nbt.NBTTagCompound"));
            ReflectionUtils.invokeMethod(ReflectionUtils.getMinecraftClass("world.level.block.entity.TileEntity"), tileEntity, "save", tagCompound);
            return (NbtCompound) convertToLib(tagCompound);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            throw new NMSReflectionException("The nbt reading has thrown an exception, maybe this is not a supported server version?", e);
        }
    }

    public static void toTileEntity(Location loc, NbtCompound nbt) {//
        try {
            Object nmsWorld = ReflectionUtils.invokeMethod(ReflectionUtils.getBukkitClass("CraftWorld"), loc.getWorld(),"getHandle");//
            Class<?> blockPosClazz = ReflectionUtils.getMinecraftClass("core.BlockPosition");
            Object blockPos = ReflectionUtils.invokeConstructorExplicit(blockPosClazz, new Class[] {int.class, int.class, int.class}, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            Object tileEntity = ReflectionUtils.invokeMethod(ReflectionUtils.getMinecraftClass("world.level.World"), nmsWorld, "getTileEntity", blockPos);
            Object blockData = ReflectionUtils.invokeMethodExplicit(ReflectionUtils.getMinecraftClass("net.minecraft.world.level.World"), nmsWorld, "getType", new Class[]{blockPosClazz}, blockPos);
            Object tagCompound = convertToNMS(nbt);
            ReflectionUtils.invokeMethodExplicit(ReflectionUtils.getMinecraftClass("world.level.block.entity.TileEntity"), tileEntity, "load", new Class[] {ReflectionUtils.getMinecraftClass("world.level.block.state.IBlockData"), ReflectionUtils.getMinecraftClass("nbt.NBTTagCompound")}, blockData,  tagCompound);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            throw new NMSReflectionException("The nbt reading has thrown an exception, maybe this is not a supported server version?", e);
        }
    }

    public static NbtCompound fromMojangson(String data) {
        try {
            return (NbtCompound) convertToLib(ReflectionUtils.invokeMethodExplicit(ReflectionUtils.getMinecraftClass("net.minecraft.nbt.MojangsonParser"), null, "parse", new Class[]{String.class}, data));
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new NMSReflectionException("The nbt reading has thrown an exception, maybe this is not a supported server version?", e);
        }
    }

    public static String toMojangson(NbtCompound compound) {
        try {
            return (String) ReflectionUtils.invokeMethod(convertToNMS(compound), "toString");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new NMSReflectionException("The nbt writing has thrown an exception, maybe this is not a supported server version?", e);
        }
    }

    /**
     * Converts nms nbt tag types to lib internal nbt types
     * @param base NMS NBTBase to convert
     */
    @SuppressWarnings("unchecked")
    private static NbtBase convertToLib(Object base) {
        try {
            switch ((byte) ReflectionUtils.invokeMethod(base, "getTypeId")) {
                case 1:
                    return new NbtByte((byte) ReflectionUtils.invokeMethod(base, "asByte"));
                case 2:
                    return new NbtShort((short) ReflectionUtils.invokeMethod(base, "asShort"));
                case 3:
                    return new NbtInt((int) ReflectionUtils.invokeMethod(base, "asInt"));
                case 4:
                    return new NbtLong((long) ReflectionUtils.invokeMethod(base, "asLong"));
                case 5:
                    return new NbtFloat((float) ReflectionUtils.invokeMethod(base, "asFloat"));
                case 6:
                    return new NbtDouble((float) ReflectionUtils.invokeMethod(base, "asFloat"));
                case 7:
                    return new NbtByteArray((byte[]) ReflectionUtils.invokeMethod(base, "getBytes"));
                case 8:
                    return new NbtString((String) ReflectionUtils.invokeMethod(base, "asString"));
                case 9:
                    List<NbtBase> list = new ArrayList<>();
                    Iterator<?> itr = (Iterator<?>) ReflectionUtils.invokeMethod(AbstractList.class, base, "iterator");
                    itr.forEachRemaining(nbtBase -> list.add(convertToLib(nbtBase)));
                    if (list.isEmpty()) {
                        return new NbtList(list, NbtType.END);
                    }
                    return new NbtList(list, list.get(0).getType());
                case 10:
                    Map<String, NbtBase> map = new HashMap<>();
                    Set<String> keys = (Set<String>) ReflectionUtils.invokeMethod(base, "getKeys");
                    for (String key : keys) {
                        map.put(key, convertToLib(ReflectionUtils.invokeMethod(base, "get", key)));
                    }
                    return new NbtCompound(map);
                case 11:
                    return new NbtIntArray(((int[]) ReflectionUtils.invokeMethod(base, "getInts")));
                case 12:
                    return new NbtLongArray((long[]) ReflectionUtils.invokeMethod(base, "getLongs"));
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            Bukkit.getLogger().severe("The nbt reading has thrown an exception, maybe this is not a supported server version? \n" + ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    /**
     * Converts lib internal tag types to NMS nbt tags types
     * @param base NbtBase to convert
     */
    @SuppressWarnings("unchecked")
    private static Object convertToNMS(NbtBase base) {
        try {
            switch (base.getType()) {
                case BYTE:
                    return ReflectionUtils.invokeStaticMethodExplicit(ReflectionUtils.getMinecraftClass("nbt.NBTTagByte"), "a", new Class[] {byte.class}, base.getValue());
                case SHORT:
                    return ReflectionUtils.invokeStaticMethodExplicit(ReflectionUtils.getMinecraftClass("nbt.NBTTagShort"), "a", new Class[] {short.class}, base.getValue());
                case INT:
                    return ReflectionUtils.invokeStaticMethodExplicit(ReflectionUtils.getMinecraftClass("nbt.NBTTagInt"), "a", new Class[] {int.class}, base.getValue());
                case LONG:
                    return ReflectionUtils.invokeStaticMethodExplicit(ReflectionUtils.getMinecraftClass("nbt.NBTTagLong"), "a", new Class[] {long.class}, base.getValue());
                case FLOAT:
                    return ReflectionUtils.invokeStaticMethodExplicit(ReflectionUtils.getMinecraftClass("nbt.NBTTagFloat"), "a", new Class[] {float.class}, base.getValue());
                case DOUBLE:
                    return ReflectionUtils.invokeStaticMethodExplicit(ReflectionUtils.getMinecraftClass("nbt.NBTTagDouble"), "a", new Class[] {double.class}, base.getValue());
                case BYTE_ARRAY:
                    return ReflectionUtils.invokeConstructor(ReflectionUtils.getMinecraftClass("nbt.NBTTagByteArray"), base.getValue());
                case STRING:
                    return ReflectionUtils.invokeStaticMethodExplicit(ReflectionUtils.getMinecraftClass("nbt.NBTTagString"), "a", new Class[] {String.class}, base.getValue());
                case LIST:
                    Object list = ReflectionUtils.invokeConstructor(ReflectionUtils.getMinecraftClass("nbt.NBTTagList"));
                    for (NbtBase nbtBase : ((List<NbtBase>) base.getValue())) {
                        ReflectionUtils.invokeMethodExplicit(list.getClass(), list, "add", new Class[] {int.class, ReflectionUtils.getMinecraftClass("nbt.NBTBase")}, new Object[] {ReflectionUtils.invokeMethod(list, "size"), convertToNMS(nbtBase)});
                    }
                    return list;
                case COMPOUND:
                    NbtCompound nbtCompound = (NbtCompound) base;
                    Object nbtTagCompound = ReflectionUtils.invokeConstructor(ReflectionUtils.getMinecraftClass("nbt.NBTTagCompound"));
                    for (String key : nbtCompound.getKeys()) {
                        ReflectionUtils.invokeMethodExplicit(nbtTagCompound.getClass(), nbtTagCompound, "set", new Class[] {String.class, ReflectionUtils.getMinecraftClass("nbt.NBTBase")}, new Object[] {key, convertToNMS(nbtCompound.get(key))});
                    }
                    return nbtTagCompound;
                case INT_ARRAY:
                    return ReflectionUtils.invokeConstructor(ReflectionUtils.getMinecraftClass("nbt.NBTTagIntArray"), base.getValue());
                case LONG_ARRAY:
                    return ReflectionUtils.invokeConstructor(ReflectionUtils.getMinecraftClass("nbt.NBTTagLongArray"), base.getValue());
            }
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            Bukkit.getLogger().severe("The nbt writing has thrown an exception, maybe this is not a supported server version? \n" + ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
}
