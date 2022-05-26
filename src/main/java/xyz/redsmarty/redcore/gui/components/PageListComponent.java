package xyz.redsmarty.redcore.gui.components;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.redsmarty.redcore.gui.PaginatedGui;
import xyz.redsmarty.redcore.gui.item.GuiItem;

import java.util.function.Function;

public class PageListComponent extends PaginationComponent {

    private ItemStack previousItem;
    private ItemStack nextItem;
    private ItemStack pageItem;
    private Function<Integer, GuiItem> pageItemCallback = i -> {
        ItemStack item = pageItem.clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Page " + i);
        item.setItemMeta(meta);
        return new GuiItem(item);
    };

    public PageListComponent(int x, int y, int length, int height) {
        super(x, y, length, height);
    }

    @Override
    public void render(PaginatedGui gui) {
        GuiItem previousItem = new GuiItem(this.previousItem, click -> gui.previous());
        GuiItem nextItem = new GuiItem(this.nextItem, click -> gui.next());
        for (int h = 0; h < height; h++) {
            gui.setItem(y + h, x, previousItem);
            gui.setItem(y + h, x + length - 1, nextItem);
        }
        int x = this.x + 1;
        int y = this.y;
        for (int i = 0; i < gui.getPages().size(); i++) {
            if (x > (length - 1)) {
                y++;
                x = this.x + 1;
            }
            gui.setItem(y, x, pageItemCallback.apply(i));
            x++;
        }
    }

    public ItemStack getPreviousItem() {
        return previousItem;
    }

    public void setPreviousItem(ItemStack previousItem) {
        this.previousItem = previousItem;
    }

    public ItemStack getNextItem() {
        return nextItem;
    }

    public void setNextItem(ItemStack nextItem) {
        this.nextItem = nextItem;
    }

    public ItemStack getPageItem() {
        return pageItem;
    }

    public void setPageItem(ItemStack pageItem) {
        this.pageItem = pageItem;
    }

    public void setPageItemCallback(Function<Integer, GuiItem> pageItemCallback) {
        this.pageItemCallback = pageItemCallback;
    }
}
