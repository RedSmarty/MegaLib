package xyz.redsmarty.redcore.gui.components.input;

import xyz.redsmarty.redcore.gui.components.output.PercentageBar;
import xyz.redsmarty.redcore.gui.item.GuiItem;

public class Slider extends PercentageBar {

    public Slider(int x, int y, int length, int height) {
        super(x, y, length, height);
    }

    @Override
    public void setFillItem(GuiItem fillItem) {
        GuiItem item = new GuiItem(fillItem.getItem(), inventoryClickEvent -> {
//            setPercentage();
        });
        super.setFillItem(fillItem);
    }
}
