package xyz.redsmarty.redcore.utils;

import org.bukkit.block.Block;

public class BlockUtils {
    public static boolean isTileEntity(Block block) {
        return !block.getState().getClass().getName().endsWith("CraftBlockState");
    }
}
