package xyz.redsmarty.redcore.gui.item;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.redsmarty.redcore.gui.ItemBuilder;

import java.util.function.Consumer;

public class GuiItem {
    public static final GuiItem BACKGROUND_ITEM = new GuiItem(ItemBuilder.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, ""));

    private ItemStack item;
    private Consumer<InventoryClickEvent> clickCallBack;
    private Consumer<ItemStack> updateCallback;

    public GuiItem(ItemStack item) {
        this(item, click -> {});
    }

    public GuiItem(ItemStack item, Consumer<InventoryClickEvent> clickCallBack) {
        this.item = item;
        this.clickCallBack = clickCallBack;
    }

    public void click(InventoryClickEvent e) {
        e.setCancelled(true);
        if (clickCallBack != null) {
            clickCallBack.accept(e);
        }
    }

    public ItemStack getItem() {
        return item;
    }

    public Consumer<InventoryClickEvent> getClickCallBack() {
        return clickCallBack;
    }

    public Consumer<ItemStack> getUpdateCallback() {
        return updateCallback;
    }

    public void setItem(ItemStack item) {
        this.item = item;
        if (updateCallback != null) {
            updateCallback.accept(item);
        }
    }

    public void setClickCallBack(Consumer<InventoryClickEvent> clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public void setUpdateCallback(Consumer<ItemStack> updateCallback) {
        this.updateCallback = updateCallback;
    }

}

