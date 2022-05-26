package xyz.redsmarty.redcore.gui.components.input;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.redsmarty.redcore.gui.Page;
import xyz.redsmarty.redcore.gui.components.GuiComponent;
import xyz.redsmarty.redcore.gui.item.GuiItem;
import xyz.redsmarty.redcore.gui.ItemBuilder;

public class Toggle extends GuiComponent {
    private boolean toggled = false;
    private GuiItem onItem;
    private GuiItem offItem;

    public Toggle(int x, int y, int length, int height) {
        super(x, y, length, height);
        setOnItem(ItemBuilder.createGuiItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GOLD + "Toggle " + ChatColor.GREEN + "on"));
        setOffItem(ItemBuilder.createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.GOLD + "Toggle " + ChatColor.RED + "off"));
    }

    @Override
    public void render(Page page) {
        super.render(page);
        GuiItem itemToFill = toggled ? onItem : offItem;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j++) {
                page.setItem(y + j, x + i, itemToFill);
            }
        }
    }

    public boolean isToggled() {
        return toggled;
    }

    public void toggle() {
        this.toggled = !toggled;
        update();
    }

    public ItemStack getOnItem() {
        return onItem.getItem();
    }

    public void setOnItem(ItemStack item) {
        this.onItem = new GuiItem(item, inventoryClickEvent -> toggle());
        update();
    }

    public ItemStack getOffItem() {
        return offItem.getItem();
    }

    public void setOffItem(ItemStack item) {
        this.offItem = new GuiItem(item, inventoryClickEvent -> toggle());
        update();
    }

}
