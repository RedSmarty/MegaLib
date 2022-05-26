package xyz.redsmarty.redcore.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import java.util.function.Consumer;

public class InventoryGui extends Page implements Gui {

    private final String title;
    private final Inventory inventory;
    private Consumer<InventoryCloseEvent> closeCallback = close -> {};

    public InventoryGui(int rows, String title) {
        super(rows);
        this.title = title;
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
    }

    public void show(Player player) {
        player.openInventory(inventory);
    }

    public void onClose(InventoryCloseEvent e) {
        // Update inventory in case of any desync (For example player used swap item while in GUI)
        ((Player) e.getPlayer()).updateInventory();
        closeCallback.accept(e);
    }

    public String getTitle() {
        return title;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setCloseCallback(Consumer<InventoryCloseEvent> closeCallback) {
        this.closeCallback = closeCallback;
    }

    @Override
    public void update() {
        for (int i = 0; i < contents.length; i++) {
            inventory.setItem(i, contents[i] == null ? null : contents[i].getItem());
        }
    }
}
