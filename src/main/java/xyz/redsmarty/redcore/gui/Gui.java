package xyz.redsmarty.redcore.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import xyz.redsmarty.redcore.gui.components.GuiComponent;
import xyz.redsmarty.redcore.gui.item.GuiItem;

import java.util.function.Consumer;

public interface Gui extends InventoryHolder {
    void setItem(int i, GuiItem item);
    void setItem(int row, int column, GuiItem item);
    void addComponent(GuiComponent component);
    void fillOutline(GuiItem item);
    void fillBackground(GuiItem item);
    void fromPattern(Pattern pattern);
    int getRows();
    int getSize();
    void show(Player player);
    void click(InventoryClickEvent e);
    void onClose(InventoryCloseEvent e);
    String getTitle();
    Inventory getInventory();
    void setCloseCallback(Consumer<InventoryCloseEvent> callback);

    static void register(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new GuiListener(), plugin);
    }
}
