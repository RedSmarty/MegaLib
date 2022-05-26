package xyz.redsmarty.redcore.gui;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.redsmarty.redcore.gui.components.GuiComponent;
import xyz.redsmarty.redcore.gui.item.GuiItem;

public abstract class Page {
    protected final int rows;
    protected final int size;
    protected final GuiItem[] contents;

    public Page(int rows) {
        this.rows = rows;
        this.size = rows * 9;
        this.contents = new GuiItem[size];
    }

    public void setItem(int i, GuiItem item) {
        Validate.isTrue(i > 0 || i < size, "Index must be greater than 0 and less than the inventory size");
        contents[i] = item;
        if (item != null) {
            item.setUpdateCallback(itemStack -> setItem(i, item));
        }
        update();
    }

    public void setItem(int row, int column, GuiItem item) {
        Validate.isTrue(row < rows, "Row must be less than inventory row size");
        Validate.isTrue(column < 9, "Column must be less than 9");
        setItem((row * 9) + column, item);
    }

    public void addComponent(GuiComponent component) {
        component.render(this);
        component.setUpdateCallback(() -> component.render(this));
    }

    public void fillOutline(GuiItem item) {
        for(int i = 0; i < size; i++) {
            int row = i / 9;
            int column = (i % 9) + 1;

            if(row == 0 || row == rows - 1 || column == 1 || column == 9)
                setItem(i, item);
        }
    }

    public void fillBackground(GuiItem item) {
        for (int i = 0; i < size; i++) {
            if (contents[i] == null || contents[i].getItem() == null || contents[i].getItem().getType() == Material.AIR) {
                setItem(i, item);
            }
        }
    }

    public void fromPattern(Pattern pattern) {
        Validate.isTrue(pattern.getPattern().length == rows, "Number of rows in the pattern does not match the number of rows in Gui");
        int i = 0;//
        for (String s : pattern.getPattern()) {
            for (char c : s.toCharArray()) {
                setItem(i, pattern.getItem(c));
                i++;
            }
        }
    }

    public abstract void update();

    public void click(InventoryClickEvent e) {
        if (contents[e.getSlot()] == null) return;
        contents[e.getSlot()].click(e);
    }

    public int getRows() {
        return rows;
    }

    public int getSize() {
        return size;
    }

    public GuiItem[] getContents() {
        return contents;
    }
}
